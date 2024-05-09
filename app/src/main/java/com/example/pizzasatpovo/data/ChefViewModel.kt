package com.example.pizzasatpovo.data

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ChefViewModel : ViewModel() {
//    private val _orders = MutableStateFlow<ArrayList<RealTimeOrder>>(arrayListOf())
//    val orders = _orders.asStateFlow()

    private val _orders = MutableLiveData<SnapshotStateList<RealTimeOrder>>()
    private val orderSnapshotStateList= mutableStateListOf<RealTimeOrder>()


    val orders: LiveData<SnapshotStateList<RealTimeOrder>>
        get() = _orders
    fun addOrder(order: RealTimeOrder) {
        var low = 0
        var high = orderSnapshotStateList.size
        while (low<high){
            val mid= (low+high)/2
            if (order.time<orderSnapshotStateList[mid].time){
                high= mid
            }else{
                low= mid+1
            }
        }
        orderSnapshotStateList.add(low,order)
        _orders.value = orderSnapshotStateList
    }

    init {
        val database = Firebase.database("https://pizzasatpovo-default-rtdb.europe-west1.firebasedatabase.app")
        val ordersRef = database.getReference("orders")
        ordersRef.addChildEventListener( object: ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val newOrder = snapshot.getValue(RealTimeOrder::class.java)
                if (newOrder!=null){
                    addOrder(newOrder)
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
        })
    }



}




