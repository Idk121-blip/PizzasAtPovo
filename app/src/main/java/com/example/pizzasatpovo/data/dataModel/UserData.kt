package com.example.pizzasatpovo.data.dataModel

import com.google.firebase.firestore.DocumentReference


data class UserData(val name: String?= "",
                    val role: String= "User",
                    var credit: Double = 0.0,
                    var favourites: ArrayList<DocumentReference>?= null,
                    val image: String?= "",
                    var orders: ArrayList<DocumentReference>?= null)


