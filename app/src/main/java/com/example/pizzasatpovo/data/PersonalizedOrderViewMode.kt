package com.example.pizzasatpovo.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class PersonalizedOrderViewMode: ViewModel() {
    private val _toppings = MutableStateFlow<ArrayList<Topping>>(arrayListOf())
    val _isEmpty = MutableLiveData(true)


    fun addTopping(topping: Topping){
        _toppings.update {
            val newTopping= it
            newTopping.add(topping)
            newTopping
        }
        _isEmpty.postValue(_toppings.value.isEmpty())
        println("Empty add? " + _isEmpty.value)

    }


    fun removeTopping(topping: Topping){
        _toppings.update {
            val newTopping= it
            while (newTopping.contains(topping)){
                newTopping.remove(topping)
            }
            newTopping
        }
        _isEmpty.postValue(_toppings.value.isEmpty())
        println("Empty remove? " + _isEmpty.value)
    }


    fun getRetrievedPizza():RetrievedPizza{
        return RetrievedPizza(name= "La tua pizza",
            toppings = _toppings.value,
            image = "https://www.wanmpizza.com/wp-content/uploads/2022/09/pizza.png")
    }


}