package com.example.pizzasatpovo.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ChefViewModel : ViewModel() {
//    private val _orders = MutableStateFlow<ArrayList<RealTimeOrder>>(arrayListOf())
//    val orders = _orders.asStateFlow()

    private val _orders = MutableLiveData<SnapshotStateList<RealTimeOrder>>()
    private val t= mutableStateListOf<RealTimeOrder>()
    private val _snapshotsKey = MutableStateFlow<ArrayList<String?>>(arrayListOf())
    val snapshotKey = _snapshotsKey.asStateFlow()
//    val orders: LiveData<ArrayList<RealTimeOrder>> = _orders

    val orders: LiveData<SnapshotStateList<RealTimeOrder>>
        get() = _orders
    fun addOrder(order: RealTimeOrder, snapshotKey: String?) {
        t.add(order)
        _orders.value = t

        _snapshotsKey.value.add(snapshotKey)
    }

    init {
        val database = Firebase.database("https://pizzasatpovo-default-rtdb.europe-west1.firebasedatabase.app")
        val ordersRef = database.getReference("orders")
        ordersRef.addChildEventListener( object: ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val newOrder = snapshot.getValue(RealTimeOrder::class.java)
                if (newOrder!=null){
                    addOrder(newOrder, snapshot.key)
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




