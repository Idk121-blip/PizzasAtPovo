package com.example.pizzasatpovo.presentation.db_interaction

import com.example.pizzasatpovo.data.Order
import com.example.pizzasatpovo.data.Pizza
import com.example.pizzasatpovo.data.ResponseData
import com.example.pizzasatpovo.presentation.sign_in.GoogleAuthUiClient
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class SendRetrieveData (private val googleAuthUiClient: GoogleAuthUiClient) {
    private val auth = Firebase.auth
    suspend fun sendOrder(pizza: Pizza, price: Double, pickupTime: Timestamp): ResponseData<Order>? = auth.currentUser?.run {
        val order= Order(topping=pizza.toppings!!,
                        price= price,
                        image= pizza.image,
                        uid=uid,
                        date=pickupTime)
        val user= googleAuthUiClient.retrieveUserData() ?: return ResponseData(message = "Utente non trovato")
        val db = Firebase.firestore
        val userRef = db.collection("users").document(uid)
        db.collection("orders")
            .add(order)
            .addOnSuccessListener { documentReference->
                println(documentReference)
                println(user)
                if (user.orders == null){
                    user.orders= arrayListOf(documentReference)
                }else{
                    user.orders!!.add(0, documentReference)
                }
                println(user)
                userRef.set(user)
            }
        return ResponseData(true, "Ordine completato con successo", order)
    }
    suspend fun getPizza(name:String): ResponseData<Pizza>? =auth.currentUser?.run {
        val db = Firebase.firestore
        val pizza= (db.collection("pizzas").document(name).get().await().toObject(Pizza::class.java))
        if (pizza==null){
            return ResponseData(false, "Pizza non trovata, riprova")
        }
        return ResponseData(true, "Pizza trovata con successo", pizza)
    }

    suspend fun getPizzas(): ResponseData<ArrayList<Pizza>>? =auth.currentUser?.run{
        val db = Firebase.firestore
        val pizzasArray:ArrayList<Pizza> = arrayListOf()
        val pizzasQuery= db.collection("pizzas")
            .get()
            .addOnSuccessListener {

        }.await()
        for (pizzaSnapshot in pizzasQuery){
            pizzasArray.add(pizzaSnapshot.toObject(Pizza::class.java))
        }

        return ResponseData(true, "Fetchd successfully", pizzasArray)
    }
}