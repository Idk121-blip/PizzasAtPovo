package com.example.pizzasatpovo.data.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pizzasatpovo.data.model.RetrievedPizza
import com.example.pizzasatpovo.data.model.Topping
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class PersonalizedOrderViewModel: ViewModel() {
    private val _toppings = MutableStateFlow<ArrayList<Topping>>(arrayListOf())
    val isEmpty = MutableLiveData(true)


    fun addTopping(topping: Topping){
        _toppings.update {
            val newTopping= it
            newTopping.add(topping)
            newTopping
        }
        isEmpty.postValue(_toppings.value.isEmpty())
        println("Empty add? " + isEmpty.value)
    }

    fun removeTopping(topping: Topping){
        _toppings.update {
            val newTopping= it
            while (newTopping.contains(topping)){
                newTopping.remove(topping)
            }
            newTopping
        }
        isEmpty.postValue(_toppings.value.isEmpty())
        println("Empty remove? " + isEmpty.value)
    }

    fun getRetrievedPizza(): RetrievedPizza {
        return RetrievedPizza(name= "La tua pizza",
            toppings = _toppings.value,
            image = "https://www.wanmpizza.com/wp-content/uploads/2022/09/pizza.png")
    }


}