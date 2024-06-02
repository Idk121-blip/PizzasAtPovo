package com.example.pizzasatpovo.database

import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DatabaseReference

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
