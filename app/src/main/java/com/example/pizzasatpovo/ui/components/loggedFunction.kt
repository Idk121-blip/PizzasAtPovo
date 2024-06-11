package com.example.pizzasatpovo.ui.components

import android.content.Context
import android.widget.Toast
import com.example.pizzasatpovo.data.model.RetrievedPizza
import com.example.pizzasatpovo.data.model.Topping
import com.example.pizzasatpovo.data.viewmodel.PizzaViewModel
import com.example.pizzasatpovo.database.DataManager
import com.example.pizzasatpovo.database.FavouritesManager

suspend fun userLogged(applicationContext: Context, dataManager: DataManager, favouritesManager: FavouritesManager, pizzaViewModel: PizzaViewModel) {
    var pizzas: ArrayList<RetrievedPizza> = arrayListOf()
    var toppings: ArrayList<Topping> = arrayListOf()
    val reqResponse= dataManager.getPizzas()
    val toppingResponse= dataManager.getToppings()

    if (toppingResponse == null){
        Toast.makeText(
            applicationContext,
            "Error retrieving toppings",
            Toast.LENGTH_LONG
        ).show()
    }else if (!toppingResponse.isSuccessful){
        Toast.makeText(
            applicationContext,
            "Error retrieving pizzas",
            Toast.LENGTH_LONG
        ).show()
    }else if (toppingResponse.retrievedObject == null){
        Toast.makeText(
            applicationContext,
            "Error retrieving pizzas",
            Toast.LENGTH_LONG
        ).show()
    }else{
        toppings=toppingResponse.retrievedObject

    }



    if (reqResponse==null){
        Toast.makeText(
            applicationContext,
            "Error retrieving pizzas",
            Toast.LENGTH_LONG
        ).show()
    }else if (!reqResponse.isSuccessful) {
        Toast.makeText(
            applicationContext,
            "Error retrieving pizzas",
            Toast.LENGTH_LONG
        ).show()
    }else{
        pizzas = reqResponse.retrievedObject!!
    }

    val favouritesResponse = favouritesManager.retrieveFavourites()

    val favourites = if (favouritesResponse!=null){
        favouritesResponse.retrievedObject?: arrayListOf()
    }else{
        arrayListOf()
    }


    pizzaViewModel.setToppings(toppings)
    pizzaViewModel.setPizzas(pizzas)
    pizzaViewModel.setFavourites(favourites)
}
