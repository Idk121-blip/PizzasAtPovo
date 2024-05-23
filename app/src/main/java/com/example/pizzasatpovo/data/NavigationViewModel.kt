package com.example.pizzasatpovo.data

import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.example.pizzasatpovo.screens.PizzaScreens
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class NavigationViewModel(controller: NavHostController): ViewModel() {
    private val _navController = MutableStateFlow(controller)
    val navController = _navController.asStateFlow()
    //TODO if not used remove


    fun goToListOfPizzas(){
        _navController.update {
            return it.navigate(PizzaScreens.ListOfPizzas.name)
        }
    }

    fun goToDetails(){
//        _navController.update {
//            println("Name: ${PizzaScreens.DetailsPizza.name}")
//            return it.navigate(PizzaScreens.DetailsPizza.name)
//        }
        _navController.update{
            return it.navigate(PizzaScreens.DetailsPizza.name)
        }
    }

    fun goToFavourites(){
        _navController.update {
            return it.navigate(PizzaScreens.Favourites.name)
        }
    }

    fun goToAddPizza(){
        _navController.update {
            return it.navigate(PizzaScreens.NewPizza.name)
        }
    }

    fun goToOrders(){
        _navController.update {
            return it.navigate(PizzaScreens.RecentOrders.name)
        }
    }

    fun goToAccount(){
        _navController.update {
            return it.navigate(PizzaScreens.Account.name)
        }
    }

    fun goBack(){
        _navController.update {
            it.popBackStack()
            it
        }
    }
}

