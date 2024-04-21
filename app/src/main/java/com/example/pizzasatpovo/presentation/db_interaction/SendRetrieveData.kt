package com.example.pizzasatpovo.presentation.db_interaction

import com.example.pizzasatpovo.data.Order
import com.example.pizzasatpovo.data.Pizza
import com.example.pizzasatpovo.data.PizzaPrice
import com.example.pizzasatpovo.data.ResponseData
import com.example.pizzasatpovo.data.Topping
import com.example.pizzasatpovo.data.UserData
import com.example.pizzasatpovo.presentation.sign_in.GoogleAuthUiClient
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class SendRetrieveData (private val googleAuthUiClient: GoogleAuthUiClient) {
    private val auth = Firebase.auth
    suspend fun sendOrder(pizza: Pizza, pickupTime: Timestamp, pizzaNumber: Int): ResponseData<Order>? = auth.currentUser?.run {
        val db = Firebase.firestore
        val snapPrice= db.collection("menuPizzaPrice")
            .document("StandardMenuPrice")
            .get()
            .await()
        val price= snapPrice.toObject(PizzaPrice::class.java)

        if (pizzaNumber<1){
            return ResponseData(false, message = "Numero di pizze non valido")
        }
        if (price == null){
            return ResponseData(false, message = "Errore nella richiesta del prezzo al Database")
        }

        val order= Order(topping=pizza.toppings!!,
                        price= price.price*pizzaNumber,
                        image= pizza.image,
                        uid=uid,
                        date=pickupTime,
                        pizzaNumber= pizzaNumber)
        val user= googleAuthUiClient.retrieveUserData() ?: return ResponseData(message = "Utente non trovato")
        if (user.credit <order.price){
            return ResponseData(false, message = "Credito non sufficiente")
        }

        val userRef = db.collection("users").document(uid)
        db.collection("orders")
            .add(order)
            .addOnSuccessListener { documentReference->
                if (user.orders == null){
                    user.orders= arrayListOf(documentReference)
                }else{
                    user.orders!!.add(0, documentReference)
                }
                userRef.set(user)
            }
        return ResponseData(true, "Ordine completato con successo", order)
    }
    suspend fun getPizza(name:String): ResponseData<Pizza>? =auth.currentUser?.run {
        val db = Firebase.firestore
        val pizza= (db.collection("pizzas").document(name).get().await().toObject(Pizza::class.java))
            ?: return ResponseData(false, "Pizza non trovata, riprova")
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


    suspend fun getToppings(): ResponseData<ArrayList<Topping>>? =auth.currentUser?.run{
        val db = Firebase.firestore
        val pizzasArray:ArrayList<Topping> = arrayListOf()
        val pizzasQuery= db.collection("toppings")
            .get()
            .await()
        for (pizzaSnapshot in pizzasQuery){
            pizzasArray.add(pizzaSnapshot.toObject(Topping::class.java))
        }

        return ResponseData(true, "Fetchd successfully", pizzasArray)
    }

    suspend fun getToppingByRef(toppingReference: DocumentReference): ResponseData<Topping>? =auth.currentUser?.run{
        val topping= (toppingReference.get().await()).toObject(Topping::class.java)
        return ResponseData(true, "Fetchd successfully", topping)
    }

    suspend fun addFavourite(pizzaName: String, googleAuthUiClient: GoogleAuthUiClient? = null): ResponseData<Boolean>? = auth.currentUser?.run{
        val db= Firebase.firestore
        val userRef= db.collection("users").document(uid)
        var user= if (googleAuthUiClient == null){
            userRef.get().await().toObject(UserData::class.java)
        }else{
            googleAuthUiClient.retrieveUserData()
        }

        if (user==null){
            return ResponseData()
        }
        val pizzaRef= db.collection("pizzas").document(pizzaName)
        val pizzaExist = pizzaRef.get().await().exists()
        if (!pizzaExist){
            return ResponseData(false, "Pizza doesn't exists")
        }

        var favourites = user.favourites

        if (favourites.isNullOrEmpty()){
            favourites = arrayListOf(pizzaRef)
        }else{
            favourites.add(pizzaRef)
        }

        user.favourites=favourites
        userRef.set(user).await()

        return ResponseData(true, "Pizza added successfully")
    }

    suspend fun retrieveFavourites(googleAuthUiClient: GoogleAuthUiClient): ResponseData<ArrayList<Pizza>>? = auth.currentUser?.run {
        val user = googleAuthUiClient.retrieveUserData() ?: return ResponseData(message= "Error fetching the user")
        var favourites: ArrayList<Pizza>? = null
        if (user.favourites==null){
            return ResponseData(message= "No favourites")
        }

        for (pizzaDocument in user.favourites!!){
            val pizza= pizzaDocument.get().await().toObject(Pizza::class.java)
            if (favourites==null){
                favourites = arrayListOf(pizza!!)
            }else{
                favourites.add(pizza!!)
            }
        }

        return ResponseData(true, "Pizza retrieved successfully", favourites)
    }
}