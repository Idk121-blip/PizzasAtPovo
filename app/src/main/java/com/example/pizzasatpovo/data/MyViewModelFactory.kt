package com.example.pizzasatpovo.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController

class MyViewModelFactory(private val navController: NavHostController) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T = NavigationViewModel(navController) as T
}