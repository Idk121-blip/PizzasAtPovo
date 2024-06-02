package com.example.pizzasatpovo.data.model

import com.google.firebase.firestore.DocumentReference

data class Pizza(val name: String= "", val toppings: ArrayList<DocumentReference>? = null, val image: String= "")

data class RetrievedPizza(val name: String= "", val toppings: ArrayList<Topping>? = null, val image: String= ""){
    fun matchSearch(query: String): Boolean {
        return name.contains(query, ignoreCase = true)
    }
}
data class Topping(val name:String= "",
                   val allergens: String="",
                   val vegetarian: Boolean= true,
                   val image: String= "",
                   val availability:Boolean = true)