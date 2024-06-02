package com.example.pizzasatpovo.data.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pizzasatpovo.data.model.RealTimeOrder
import com.example.pizzasatpovo.database.ListenerManager
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ChefViewModel : ViewModel() {

    private val _orders = MutableLiveData<SnapshotStateList<RealTimeOrder>>()
    private val orderSnapshotStateList = mutableStateListOf<RealTimeOrder>()
    val orders: LiveData<SnapshotStateList<RealTimeOrder>> get() = _orders

    private val repository: ListenerManager

    private val orderEventListener = object : ChildEventListener {
        override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
            val newOrder = snapshot.getValue(RealTimeOrder::class.java)
            if (newOrder != null) {
                addOrder(newOrder)
            }
        }

        override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            val updatedOrder = snapshot.getValue(RealTimeOrder::class.java)
            if (updatedOrder != null) {
                updateOrder(updatedOrder)
            }
        }

        override fun onChildRemoved(snapshot: DataSnapshot) {
            val removedOrder = snapshot.getValue(RealTimeOrder::class.java)
            if (removedOrder != null) {
                removeOrder(removedOrder)
            }
        }

        override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            // Handle order moved if needed
        }

        override fun onCancelled(error: DatabaseError) {
            // Handle error
        }
    }

    init {
        val database = Firebase.database("https://pizzasatpovo-default-rtdb.europe-west1.firebasedatabase.app")
        val ordersRef = database.getReference("orders")
        repository = ListenerManager(ordersRef)

        repository.addOrderEventListener(orderEventListener)
    }


    private fun addOrder(order: RealTimeOrder) {
        val index = orderSnapshotStateList.binarySearch { it.time.compareTo(order.time) }
        val insertIndex = if (index >= 0) index else -(index + 1)
        orderSnapshotStateList.add(insertIndex, order)
        _orders.value = orderSnapshotStateList
    }

    private fun updateOrder(order: RealTimeOrder) {
        val index = orderSnapshotStateList.indexOfFirst { it.id == order.id }
        if (index >= 0) {
            orderSnapshotStateList[index] = order
            _orders.value = orderSnapshotStateList
        }
    }

    private fun removeOrder(order: RealTimeOrder) {
        val index = orderSnapshotStateList.indexOfFirst { it.id == order.id }
        if (index >= 0) {
            orderSnapshotStateList.removeAt(index)
            _orders.value = orderSnapshotStateList
        }
    }

    override fun onCleared() {
        super.onCleared()
        repository.removeOrderEventListeners()
    }
}
