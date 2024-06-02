package com.example.pizzasatpovo.data.viewmodel

import androidx.lifecycle.ViewModel
import com.example.pizzasatpovo.data.model.Order
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