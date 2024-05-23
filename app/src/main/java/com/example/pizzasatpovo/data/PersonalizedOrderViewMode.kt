package com.example.pizzasatpovo.data

import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.snapshots.Snapshot
import androidx.compose.runtime.snapshots.SnapshotMutableState
import androidx.compose.runtime.snapshots.SnapshotStateObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class PersonalizedOrderViewMode: ViewModel() {
    private val _toppings = MutableStateFlow<ArrayList<Topping>>(arrayListOf())
    val topping = _toppings.asStateFlow()
    val _isEmpty= MutableLiveData<Boolean>(true)


    fun addTopping(topping: Topping){
        _toppings.update {
            val newTopping= it
            newTopping.add(topping)
            newTopping
        }
        if (!_toppings.value.isEmpty()){
            println(_toppings.value)
            _isEmpty.postValue(false)
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
        if (_toppings.value.isEmpty()){
            val y= true

            _isEmpty.postValue(y)
        }
    }





    fun getRetrievedPizza():RetrievedPizza{
        return RetrievedPizza(name= "La tua pizza",
            toppings = _toppings.value,
            image = "https://www.wanmpizza.com/wp-content/uploads/2022/09/pizza.png")
    }


}