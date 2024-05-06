package com.example.pizzasatpovo.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update


class PizzaViewModel: ViewModel() {
    private val _pizzas = MutableStateFlow<ArrayList<RetrievedPizza>>(arrayListOf())
    val pizza = _pizzas.asStateFlow()
    private val _toppings= MutableStateFlow<ArrayList<Topping>>(ArrayList())
    val toppings = _toppings.asStateFlow()
    private val _selectedPizza= MutableStateFlow(RetrievedPizza())
    val selectedPizza= _selectedPizza.asStateFlow()
    private val _favourites= MutableStateFlow<ArrayList<RetrievedPizza>>(arrayListOf())
    val favourites= _favourites.asStateFlow()
    private val _numberOfPizzaToOrder= MutableStateFlow(1)
    val numberOfPizzaToOrder= _numberOfPizzaToOrder.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

//    private val _isSearching= MutableStateFlow(false)
//    val isSearching= _isSearching.asStateFlow()

    val searchPizza = searchQuery.combine(_pizzas){ text, pizzas->
        if (text.isBlank()){
            pizzas
        }else{
            pizzas.filter {
                it.matchSearch(text)
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), _pizzas.value)


    fun addPizzas(pizzaArrayList: ArrayList<RetrievedPizza>) {
        _pizzas.update {
            pizzaArrayList
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
            while (temp.contains(pizza)){
                temp.remove(pizza)
            }
            temp
        }
    }

    fun onSearchTextChanged(text: String){
        _searchQuery.update {
            text
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