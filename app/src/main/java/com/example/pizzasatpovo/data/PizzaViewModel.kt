package com.example.pizzasatpovo.data

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow


class PizzaViewModel: ViewModel() {
    private val _pizzas = MutableStateFlow<ArrayList<Pizza>>(arrayListOf())
    val pizza = _pizzas.asStateFlow()
    private val _pizzaToppingsList=  MutableStateFlow<ArrayList<ArrayList<Topping>>>(ArrayList())
    val pizzaToppingsList = _pizzaToppingsList.asStateFlow()
    private val _toppings= MutableStateFlow<ArrayList<Topping>>(ArrayList())
    val toppings = _toppings.asStateFlow()

    fun addPizzas(pizzaArrayList: ArrayList<Pizza>) {
        _pizzas.value = pizzaArrayList
    }

    fun setPizzasToppings(toppings: ArrayList<ArrayList<Topping>>) {
        _pizzaToppingsList.value = toppings
    }

    fun setToppings(toppings: ArrayList<Topping>){
        _toppings.value = toppings
    }

}