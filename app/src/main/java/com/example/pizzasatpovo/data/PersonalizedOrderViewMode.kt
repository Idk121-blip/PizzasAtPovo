package com.example.pizzasatpovo.data

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class PersonalizedOrderViewMode: ViewModel() {
    private val _toppings = MutableStateFlow<ArrayList<Topping>>(arrayListOf())

    fun addTopping(topping: Topping){
        _toppings.update {
            val newTopping= it
            newTopping.add(topping)
            newTopping
        }
    }

    fun removeTopping(topping: Topping){
        _toppings.update {
            val newTopping= it
            while (newTopping.contains(topping)){
                newTopping.remove(topping)
            }
            newTopping
        }
    }


    fun getRetrievedPizza():RetrievedPizza{
        return RetrievedPizza(name= "La tua pizza",
            toppings = _toppings.value,
            image = "https://www.wanmpizza.com/wp-content/uploads/2022/09/pizza.png")
    }


}