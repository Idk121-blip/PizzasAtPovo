package com.example.pizzasatpovo.data

import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.example.pizzasatpovo.screens.PizzaScreens
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class NavigationViewModel(val controller: NavHostController): ViewModel() {
    private val _navController = MutableStateFlow(controller)
    val navController = _navController.asStateFlow()


    fun GoToListOfPizzas(){
        _navController.update {
            return it.navigate(PizzaScreens.ListOfPizzas.name)
        }
    }

    fun GoToDetails(){
//        _navController.update {
//            println("Name: ${PizzaScreens.DetailsPizza.name}")
//            return it.navigate(PizzaScreens.DetailsPizza.name)
//        }
        _navController.value.navigate(PizzaScreens.DetailsPizza.name)
    }

    fun GoToFavourites(){
        _navController.update {
            return it.navigate(PizzaScreens.Favourites.name)
        }
    }

    fun GoToAddPizza(){
        _navController.update {
            return it.navigate(PizzaScreens.NewPizza.name)
        }
    }

    fun GoToOrders(){
        _navController.update {
            return it.navigate(PizzaScreens.RecentOrders.name)
        }
    }

    fun GoToAccount(){
        _navController.update {
            return it.navigate(PizzaScreens.Account.name)
        }
    }
}

