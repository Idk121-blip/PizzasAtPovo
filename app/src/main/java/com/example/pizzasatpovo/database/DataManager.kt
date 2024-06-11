package com.example.pizzasatpovo.database

import com.example.pizzasatpovo.data.model.Pizza
import com.example.pizzasatpovo.data.model.ResponseData
import com.example.pizzasatpovo.data.model.RetrievedPizza
import com.example.pizzasatpovo.data.model.Topping
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

@Suppress("LABEL_NAME_CLASH")
class DataManager {

    private val auth = Firebase.auth


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
