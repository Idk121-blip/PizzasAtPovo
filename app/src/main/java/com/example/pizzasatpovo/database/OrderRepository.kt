package com.example.pizzasatpovo.database

import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class OrderRepository(private val ordersRef: DatabaseReference) {

    private val listeners = mutableListOf<ChildEventListener>()

    fun addOrderEventListener(listener: ChildEventListener) {
        ordersRef.addChildEventListener(listener)
        listeners.add(listener)
    }

    fun removeOrderEventListeners() {
        for (listener in listeners) {
            ordersRef.removeEventListener(listener)
        }
        listeners.clear()
    }
}
