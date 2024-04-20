package com.example.pizzasatpovo.presentation

import com.example.pizzasatpovo.data.Orders
import com.example.pizzasatpovo.data.Pizza
import com.example.pizzasatpovo.presentation.sign_in.GoogleAuthUiClient
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.time.format.DateTimeFormatter

class OrderSender (val googleAuthUiClient: GoogleAuthUiClient) {
    private val auth = Firebase.auth
    suspend fun sendOrder(pizza: Pizza, price: Double, pickupTime: DateTimeFormatter): Boolean? = auth.currentUser?.run {
        val order= Orders(topping=pizza.toppings,
                        price= price,
                        image= pizza.image,
                        uid=uid,
                        date=pickupTime)
        val user= googleAuthUiClient.retrieveUserData() ?: return false


        return true
    }
}