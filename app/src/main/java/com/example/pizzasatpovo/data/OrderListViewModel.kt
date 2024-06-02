package com.example.pizzasatpovo.data

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class OrderListViewModel : ViewModel() {
    private val _orders = MutableStateFlow<ArrayList<Order>>(arrayListOf())
    val orders = _orders.asStateFlow()
    fun addOrders(orderArrayList: ArrayList<Order>) {
        _orders.update {
            orderArrayList
        }
    }
}