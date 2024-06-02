package com.example.pizzasatpovo.database

import com.example.pizzasatpovo.data.dataModel.DBOrder
import com.example.pizzasatpovo.data.dataModel.Order
import com.example.pizzasatpovo.data.dataModel.Pizza
import com.example.pizzasatpovo.data.dataModel.PizzaPrice
import com.example.pizzasatpovo.data.dataModel.RealTimeOrder
import com.example.pizzasatpovo.data.dataModel.ResponseData
import com.example.pizzasatpovo.data.dataModel.RetrievedPizza
import com.example.pizzasatpovo.data.dataModel.Topping
import com.example.pizzasatpovo.database.sign_in.GoogleAuthUiClient
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

@Suppress("LABEL_NAME_CLASH")
class OrderManager(private val googleAuthUiClient: GoogleAuthUiClient) {

    private val auth = Firebase.auth
    private val dbString = "dbstring"

    private suspend fun sendOrder(pizza: Pizza, pickupTime: Timestamp, pizzaNumber: Int): ResponseData<DBOrder>? = auth.currentUser?.run {
        val db = Firebase.firestore
        val snapPrice = db.collection("menuPizzaPrice")
            .document("StandardMenuPrice")
            .get()
            .await()
        val price = snapPrice.toObject(PizzaPrice::class.java)

        if (pizzaNumber < 1) return ResponseData(false, message = "Numero di pizze non valido")
        if (price == null) return ResponseData(false, message = "Errore nella richiesta del prezzo al Database")

        val dbOrder = DBOrder(
            topping = pizza.toppings!!,
            price = price.price * pizzaNumber,
            image = pizza.image,
            uid = uid,
            date = pickupTime,
            pizzaNumber = pizzaNumber,
            pizzaName = pizza.name
        )

        val user = googleAuthUiClient.retrieveUserData() ?: return ResponseData(message = "Utente non trovato")
        if (user.credit < dbOrder.price) return ResponseData(false, message = "Credito non sufficiente")

        val userRef = db.collection("users").document(uid)
        db.collection("orders")
            .add(dbOrder)
            .addOnSuccessListener { documentReference ->
                user.orders = (user.orders ?: arrayListOf()).apply { add(0, documentReference) }
                user.credit = Math.round((user.credit - dbOrder.price) * 100.0) / 100.0
                userRef.set(user)
            }
        return ResponseData(true, "Ordine completato con successo", dbOrder)
    }

    suspend fun sendOrderRetrievedPizza(retrievedPizza: RetrievedPizza, pickupTime: Timestamp, pizzaNumber: Int): ResponseData<DBOrder>? = auth.currentUser?.run {
        if (retrievedPizza.toppings == null) return ResponseData(false, "Number of toppings not valid (null)")

        val pizza = Pizza(
            name = retrievedPizza.name,
            image = retrievedPizza.image,
            toppings = arrayListOf<DocumentReference>().apply {
                retrievedPizza.toppings.forEach { topping ->
                    add(Firebase.firestore.collection("toppings").document(topping.name))
                }
            }
        )

        return sendOrder(pizza, pickupTime, pizzaNumber)
    }

    suspend fun retrieveOrders(): ResponseData<ArrayList<Order>>? = auth.currentUser?.run {
        val userData = googleAuthUiClient.retrieveUserData() ?: return ResponseData()
        val ordersToReturn: ArrayList<Order> = arrayListOf()
        userData.orders?.forEach { order ->
            val dbOrders = order.get().await().toObject(DBOrder::class.java) ?: return@forEach
            val toppings: ArrayList<Topping> = arrayListOf()
            dbOrders.topping.forEach { documentRef ->
                toppings.add(documentRef.get().await().toObject(Topping::class.java) ?: return@forEach)
            }
            ordersToReturn.add(
                Order(
                topping = toppings,
                pizzaNumber = dbOrders.pizzaNumber,
                image = dbOrders.image,
                date = dbOrders.date,
                uid = dbOrders.uid,
                price = dbOrders.price,
                pizzaName = dbOrders.pizzaName
            )
            )
        }
        return ResponseData(true, "Retrieved successfully", ordersToReturn)
    }

    suspend fun sendRTOrder(pizza: RetrievedPizza, date: String, pizzaNumber: Int): DatabaseReference? = auth.currentUser?.run {
        val db = Firebase.firestore
        val snapPrice = db.collection("menuPizzaPrice").document("StandardMenuPrice").get().await()
        val price = snapPrice.toObject(PizzaPrice::class.java) ?: return null

        if (pizzaNumber < 1) return null

        val user = googleAuthUiClient.retrieveUserData()!!
        if (user.credit < (price.price * pizzaNumber)) return null

        val toppingList = arrayListOf<String>().apply {
            pizza.toppings?.forEach { add(it.name) }
        }

        val database = Firebase.database(dbString).reference
        val order = RealTimeOrder(
            topping = toppingList,
            pizzaNumber = pizzaNumber,
            time = date,
            image = pizza.image,
            uname = user.name!!,
            pizzaName = pizza.name
        )

        val orderRef = database.child("orders").child(order.id)
        orderRef.setValue(order).await()
        return orderRef
    }

    suspend fun processOrder(documentName: String) {
        val dbRef = Firebase.database(dbString).reference.child("orders").child(documentName)
        val order = dbRef.get().await().getValue(RealTimeOrder::class.java) ?: return
        order.completed = true
        dbRef.setValue(order)
    }

    suspend fun isThisSlotFree(time: String): Boolean {
        val spots: Int = Firebase.database(dbString).reference.child("timeslots").child(time).get().await().getValue(Int::class.java) ?: return false
        return spots > 0
    }
}
