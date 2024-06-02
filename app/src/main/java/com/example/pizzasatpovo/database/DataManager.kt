package com.example.pizzasatpovo.presentation.db_interaction

import com.example.pizzasatpovo.data.dataModel.Pizza
import com.example.pizzasatpovo.data.dataModel.ResponseData
import com.example.pizzasatpovo.data.dataModel.RetrievedPizza
import com.example.pizzasatpovo.data.dataModel.Topping
import com.example.pizzasatpovo.database.sign_in.GoogleAuthUiClient
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

@Suppress("LABEL_NAME_CLASH")
class DataManager(private val googleAuthUiClient: GoogleAuthUiClient) {

    private val auth = Firebase.auth

    suspend fun getPizza(name: String): ResponseData<Pizza>? = auth.currentUser?.run {
        val pizza = Firebase.firestore.collection("pizzas").document(name).get().await().toObject(
            Pizza::class.java)
            ?: return ResponseData(false, "Pizza non trovata, riprova")
        return ResponseData(true, "Pizza trovata con successo", pizza)
    }

    suspend fun getPizzas(): ResponseData<ArrayList<RetrievedPizza>>? = auth.currentUser?.run {
        val pizzasArray: ArrayList<RetrievedPizza> = arrayListOf()
        val pizzasQuery = Firebase.firestore.collection("pizzas").get().await()
        pizzasQuery.forEach { pizzaSnapshot ->
            val pizza = pizzaSnapshot.toObject(Pizza::class.java)
            val toppings: ArrayList<Topping> = arrayListOf()
            pizza.toppings?.forEach { documentRef ->
                toppings.add(documentRef.get().await().toObject(Topping::class.java) ?: return@forEach)
            }
            pizzasArray.add(RetrievedPizza(name = pizza.name, image = pizza.image, toppings = toppings))
        }
        return ResponseData(true, "Fetched successfully", pizzasArray)
    }

    suspend fun getToppings(): ResponseData<ArrayList<Topping>>? = auth.currentUser?.run {
        val toppingsArray: ArrayList<Topping> = arrayListOf()
        val toppingsQuery = Firebase.firestore.collection("toppings").get().await()
        toppingsQuery.forEach { pizzaSnapshot ->
            toppingsArray.add(pizzaSnapshot.toObject(Topping::class.java))
        }
        return ResponseData(true, "Fetched successfully", toppingsArray)
    }
}
