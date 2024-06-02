package com.example.pizzasatpovo.data.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import java.util.UUID

data class Order(val topping: ArrayList<Topping> = arrayListOf(),
                 val price: Double=0.0,
                 val image: String="",
                 val uid: String= "",
                 val date: Timestamp = Timestamp.now(),
                 val pizzaNumber: Int= 0,
                 val pizzaName:String= "")

data class DBOrder(val topping: ArrayList<DocumentReference> = arrayListOf(),
                   val price: Double=0.0,
                   val image: String="",
                   val uid: String= "",
                   val date: Timestamp= Timestamp.now(),
                   val pizzaNumber: Int= 0,
                   val pizzaName:String= "")

data class RealTimeOrder(val id: String = UUID.randomUUID().toString(),
                         val uname:String= "",
                         val image:String = "",
                         val topping: ArrayList<String> = arrayListOf(),
                         val pizzaNumber: Int =  0,
                         val time: String= "",
                         val pizzaName:String= "",
                         var completed: Boolean=false)

data class PizzaPrice(val price: Double= 4.4)
