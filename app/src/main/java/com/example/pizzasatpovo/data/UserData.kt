package com.example.pizzasatpovo.data

import com.google.firebase.firestore.DocumentReference
import java.time.format.DateTimeFormatter

data class UserData(val name: String?= "",
                    val role: String= "User",
                    val credit: Double? = 0.0,
                    val favourites: List<DocumentReference>?= null,
                    val image: String?= "",
                    val orders: ArrayList<Orders>?= null,
                    )
data class Pizza(val name: String, val toppings: ArrayList<Topping>, val image: String)

data class Topping(val name:String, val allergen: String, val vegetarian: Boolean)

data class Orders(val topping: ArrayList<Topping>,
                  val price: Double,
                  val image: String,
                  val uid: String,
                  val date: DateTimeFormatter)
