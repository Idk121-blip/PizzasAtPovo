package com.example.pizzasatpovo.presentation

import com.example.pizzasatpovo.data.Orders
import com.example.pizzasatpovo.data.Pizza
import com.example.pizzasatpovo.presentation.sign_in.GoogleAuthUiClient
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class OrderSender (private val googleAuthUiClient: GoogleAuthUiClient) {
    private val auth = Firebase.auth
    suspend fun sendOrder(pizza: Pizza, price: Double, pickupTime: LocalDate): Boolean? = auth.currentUser?.run {
        val order= Orders(topping=pizza.toppings!!,
                        price= price,
                        image= pizza.image,
                        uid=uid,
                        date=pickupTime)
        val user= googleAuthUiClient.retrieveUserData() ?: return false
        val db = Firebase.firestore
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
        println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA")
        return true
    }
    suspend fun getPizza(name:String): Pizza? =auth.currentUser?.run {
        println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA")
        val db = Firebase.firestore
        return (db.collection("pizze").document(name).get().await().toObject(Pizza::class.java))


    }
}