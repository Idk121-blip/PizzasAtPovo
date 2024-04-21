package com.example.pizzasatpovo.screens

import android.content.Context
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pizzasatpovo.data.Pizza
import com.example.pizzasatpovo.data.Topping
import kotlinx.coroutines.launch
import com.example.pizzasatpovo.presentation.db_interaction.SendRetrieveData
import com.example.pizzasatpovo.presentation.sign_in.GoogleAuthUiClient
import com.example.pizzasatpovo.presentation.sign_in.SignInViewModel

enum class PizzaScreens {
    FirstPage,
    LoginPage,
    ListOfPizzas,
    NewPizza,
    RecentOrders,
    Account
}

@Composable
fun PizzasAtPovoApp(
    googleAuthUiClient: GoogleAuthUiClient,
    sendRetrieveData: SendRetrieveData,
    lifecycleScope: LifecycleCoroutineScope,
    applicationContext: Context,
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier
){
    NavHost(
        navController = navController,
        startDestination = PizzaScreens.LoginPage.name,
        modifier = modifier
    ){
        var pizzas: ArrayList<Pizza> = arrayListOf()
        var toppings: ArrayList<ArrayList<Topping>> = arrayListOf()
        composable(
            route = PizzaScreens.LoginPage.name
        ){
            val viewModel = viewModel<SignInViewModel>()
            val state by viewModel.state.collectAsStateWithLifecycle()

            LaunchedEffect(key1 = Unit) {
                lifecycleScope.launch {
                    googleAuthUiClient.retrieveUserData()
                    println(googleAuthUiClient.getSignedInUser())

                    if (googleAuthUiClient.getSignedInUser() != null) {
                        val reqRespone= sendRetrieveData.getPizzas()

                        if (reqRespone!=null){


                            if (reqRespone.isSuccessful) {
                                pizzas = reqRespone.retrievedObject!!
                                println("BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB")
                                println(pizzas)
                                for (pizza in pizzas){
                                    val pizzaTopping: ArrayList<Topping> = arrayListOf()
                                    var add = false
                                    for (toppingRef in pizza.toppings!!){
                                        val topping= sendRetrieveData.getToppingByRef(toppingRef)
                                        println(toppingRef)
                                        println(topping)
                                        if (topping != null) {
                                            pizzaTopping.add(topping.retrievedObject!!)
                                            add=true
                                        }
                                    }
                                    if (add){
                                        toppings.add(pizzaTopping)
                                    }
                                }
                            println(toppings)
                            }else{
                                Toast.makeText(
                                    applicationContext,
                                    "Error retrieving pizzas",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }else{
                            Toast.makeText(
                                applicationContext,
                                "Error retrieving pizzas",
                                Toast.LENGTH_LONG
                            ).show()
                        }


                        navController.navigate(PizzaScreens.ListOfPizzas.name)
                    }
                }
            }
            val launcher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.StartIntentSenderForResult(),
                onResult = { result ->
                    if (result.resultCode == ComponentActivity.RESULT_OK) {
                        lifecycleScope.launch {
                            val signInResult = googleAuthUiClient.signInWithIntent(
                                intent = result.data ?: return@launch
                            )
                            viewModel.onSignInResult(signInResult)
                        }
                    }
                }
            )

            LaunchedEffect(key1 = state.isSignInSuccessful) {
                if (state.isSignInSuccessful) {
                    val reqRespone= sendRetrieveData.getPizzas()

                    if (reqRespone!=null){
                        if (reqRespone.isSuccessful) {
                            pizzas = reqRespone.retrievedObject!!
                            for (pizza in pizzas){
                                val pizzaTopping: ArrayList<Topping> = arrayListOf()
                                var add = false
                                for (toppingRef in pizza.toppings!!){
                                    val topping= sendRetrieveData.getToppingByRef(toppingRef)
                                    println(toppingRef)
                                    println(topping)
                                    if (topping != null) {
                                        pizzaTopping.add(topping.retrievedObject!!)
                                        add=true
                                    }
                                }
                                if (add){
                                    toppings.add(pizzaTopping)
                                }
                            }
                        }else{
                            Toast.makeText(
                                applicationContext,
                                "Error retrieving pizzas",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }else{
                        Toast.makeText(
                            applicationContext,
                            "Error retrieving pizzas",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                    Toast.makeText(
                        applicationContext,
                        "Sign in successful",
                        Toast.LENGTH_LONG
                    ).show()

                    navController.navigate(PizzaScreens.ListOfPizzas.name)
                    viewModel.resetState()
                }
            }

            FirstPageScreen().FirstPage(
                onLoginButtonClicked = {
                    lifecycleScope.launch {
                        val signInIntentSender = googleAuthUiClient.signIn()
                        launcher.launch(
                            IntentSenderRequest.Builder(
                                signInIntentSender ?: return@launch
                            ).build()
                        )
                    }
                }
            )
        }
        composable(
            route = PizzaScreens.ListOfPizzas.name
        ){
            var context = LocalContext.current
            ListOfPizzasScreen().ListOfPizzasPage(pizzas= pizzas, toppings = toppings)
        }
    }
}