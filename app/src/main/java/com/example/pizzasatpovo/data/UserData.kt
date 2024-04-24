package com.example.pizzasatpovo.data

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference

data class UserData(val name: String?= "",
                    val role: String= "User",
                    val credit: Double = 0.0,
                    var favourites: ArrayList<DocumentReference>?= null,
                    val image: String?= "",
                    var orders: ArrayList<DocumentReference>?= null)
data class Pizza(val name: String= "", val toppings: ArrayList<DocumentReference>? = null, val image: String= "")
data class RetrievedPizza(val name: String= "", val toppings: ArrayList<Topping>? = null, val image: String= "")

data class Topping(val name:String= "", val allergen: String="", val vegetarian: Boolean= true, val image: String= "")

data class Order(val topping: ArrayList<DocumentReference>,
                 val price: Double,
                 val image: String,
                 val uid: String,
                 val date: Timestamp,
                 val pizzaNumber: Int)

data class RealTimeOrder(val uname:String= "",
                         val image:String = "",
                         val topping: ArrayList<String> = arrayListOf(),
                         val pizzaNumber: Int =  0,
                         val time: String= "")

data class PizzaPrice(val price: Double= 4.4)
