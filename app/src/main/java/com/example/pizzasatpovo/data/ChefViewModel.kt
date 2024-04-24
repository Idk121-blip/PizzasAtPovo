package com.example.pizzasatpovo.data

import androidx.lifecycle.LiveData

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ChefViewModel : ViewModel(){
    private val _orders = MutableLiveData<ArrayList<RealTimeOrder>>()
    val orders: LiveData<ArrayList<RealTimeOrder>> = _orders

    val database = Firebase.database("https://pizzasatpovo-default-rtdb.europe-west1.firebasedatabase.app")
    private val ordersRef = database.getReference("orders")

    private val childEventListener = object : ChildEventListener {
        override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
            val newOrder = snapshot.getValue(RealTimeOrder::class.java)

            newOrder?.let { order ->
                //println(order)
                _orders.value!!.add(order)
                fetchData()
            }
        }

        override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            // Handle order changed
        }

        override fun onChildRemoved(snapshot: DataSnapshot) {
            // Handle order removed
        }

        override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
            // Handle order moved
        }

        override fun onCancelled(error: DatabaseError) {
            // Handle error
        }
    }

    init {
        ordersRef.addChildEventListener(childEventListener)
        _orders.value = arrayListOf()
    }

    fun fetchData(): ArrayList<RealTimeOrder>{
        return orders.value!!
    }


    override fun onCleared() {
        super.onCleared()
        ordersRef.removeEventListener(childEventListener)
    }
}




