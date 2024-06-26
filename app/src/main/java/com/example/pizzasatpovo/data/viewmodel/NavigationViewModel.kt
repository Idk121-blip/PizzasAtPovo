package com.example.pizzasatpovo.data.viewmodel

import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.example.pizzasatpovo.ui.screens.PizzaScreens
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class NavigationViewModel(controller: NavHostController): ViewModel() {
    private val _navController = MutableStateFlow(controller)

    fun goToListOfPizzas(){
        _navController.update {
            return it.navigate(PizzaScreens.ListOfPizzas.name)
        }
    }

    fun popStack(){
        _navController.update {
            it.popBackStack()
            it
        }
    }

    fun goToLoadingPage(){
        _navController.update {
            return it.navigate(PizzaScreens.LoadinPage.name)
        }
    }

    fun goToDetails(){
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

