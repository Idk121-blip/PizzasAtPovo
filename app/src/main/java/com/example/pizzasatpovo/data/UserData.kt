package com.example.pizzasatpovo.data

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference

data class UserData(val name: String?= "",
                    val role: String= "User",
                    val credit: Double = 0.0,
                    var favourites: ArrayList<DocumentReference>?= null,
                    val image: String?= "",
                    var orders: ArrayList<DocumentReference>?= null
                    )
data class Pizza(val name: String= "", val toppings: ArrayList<DocumentReference>? = null, val image: String= "")

data class Topping(val name:String= "", val allergen: String="", val vegetarian: Boolean= true)

data class Order(val topping: ArrayList<DocumentReference>,
                 val price: Double,
                 val image: String,
                 val uid: String,
                 val date: Timestamp,
                 val pizzaNumber: Int)

data class PizzaPrice(val price: Double)
