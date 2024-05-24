package com.example.pizzasatpovo.data

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import java.util.UUID


data class UserData(val name: String?= "",
                    val role: String= "User",
                    var credit: Double = 0.0,
                    var favourites: ArrayList<DocumentReference>?= null,
                    val image: String?= "",
                    var orders: ArrayList<DocumentReference>?= null)
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

data class DBOrder(val topping: ArrayList<DocumentReference> = arrayListOf(),
                   val price: Double=0.0,
                   val image: String="",
                   val uid: String= "",
                   val date: Timestamp= Timestamp.now(),
                   val pizzaNumber: Int= 0,
                   val pizzaName:String= "")

data class Order(val topping: ArrayList<Topping> = arrayListOf(),
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


data class LoadingState(
    val isFinished: Boolean = false,
    val loadingInError: String? = null
)


data class LoadingResult(
    val data: UserData?,
    val errorMessage: String?
)