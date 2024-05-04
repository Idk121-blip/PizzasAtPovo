package com.example.pizzasatpovo.data

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


class PizzaViewModel: ViewModel() {
    private val _pizzas = MutableStateFlow<ArrayList<Pizza>>(arrayListOf())
    val pizza = _pizzas.asStateFlow()
    private val _pizzaToppingsList=  MutableStateFlow<ArrayList<ArrayList<Topping>>>(ArrayList())
    val pizzaToppingsList = _pizzaToppingsList.asStateFlow()
    private val _toppings= MutableStateFlow<ArrayList<Topping>>(ArrayList())
    val toppings = _toppings.asStateFlow()
    private val _selectedPizza= MutableStateFlow(RetrievedPizza())
    val selectedPizza= _selectedPizza.asStateFlow()
    private val _favourites= MutableStateFlow<ArrayList<RetrievedPizza>>(arrayListOf())
    val favourites= _favourites.asStateFlow()
    private val _numberOfPizzaToOrder= MutableStateFlow<Int>(1)
    val numberOfPizzaToOrder= _numberOfPizzaToOrder.asStateFlow()




    fun addPizzas(pizzaArrayList: ArrayList<Pizza>) {
        _pizzas.update {
            pizzaArrayList
        }
    }

    fun setPizzasToppings(toppings: ArrayList<ArrayList<Topping>>) {
        _pizzaToppingsList.update {
            toppings
        }
    }

    fun setToppings(toppings: ArrayList<Topping>){
        _toppings.update {
            toppings
        }
    }

    fun setPizza(pizza: RetrievedPizza) {
        _selectedPizza.update {
            pizza
        }
    }

    fun setFavourites(favourites: ArrayList<RetrievedPizza>){
        _favourites.update {
            favourites
        }
    }

    fun addToFavourites(pizza: RetrievedPizza){
        _favourites.update {
            val temp = it
            temp.add(pizza)
            temp

        }
    }

    fun removeFromFavourites(pizza: RetrievedPizza){
        _favourites.update {
            val temp = it
            temp.remove(pizza)
            temp
        }
    }


    fun increaseNumberOfPizza(){
        _numberOfPizzaToOrder.update {
            val number=
            if (it>=2){
                3
            }else{
                it+1
            }
            number
        }
    }

    fun decreaseNumberOfPizza(){
        _numberOfPizzaToOrder.update {
            val number=
                if (it<=1){
                    1
                }else{
                    it-1
                }
            number
        }
    }



}