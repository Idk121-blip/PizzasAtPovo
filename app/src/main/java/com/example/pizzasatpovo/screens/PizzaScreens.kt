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
import com.example.pizzasatpovo.data.RealTimeOrder
import com.example.pizzasatpovo.data.Topping
import kotlinx.coroutines.launch
import com.example.pizzasatpovo.presentation.db_interaction.SendRetrieveData
import com.example.pizzasatpovo.presentation.sign_in.GoogleAuthUiClient
import com.example.pizzasatpovo.presentation.sign_in.SignInViewModel
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

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
                    if (googleAuthUiClient.getSignedInUser() != null) {
                        if (googleAuthUiClient.getSignedInUser()!!.role!="Chef"){
                            val (returnedPizzas, returnedToppings) = userLogged(applicationContext, sendRetrieveData)
                            pizzas= returnedPizzas
                            toppings= returnedToppings
                            navController.navigate(PizzaScreens.ListOfPizzas.name)
                        }else{
                            val database = Firebase.database("https://pizzasatpovo-default-rtdb.europe-west1.firebasedatabase.app")

                            val ordersRef = database.getReference("orders")
                            ordersRef.addChildEventListener(object : ChildEventListener {
                                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                                    // Handle new order added
                                    val newOrder = snapshot.getValue(RealTimeOrder::class.java)
                                    println(newOrder)
                                    // Process the new order
                                }

                                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                                    // Handle order changed
                                    val newOrder = snapshot.getValue(RealTimeOrder::class.java)

                                    // Update the order in your app
                                }

                                override fun onChildRemoved(snapshot: DataSnapshot) {
                                    // Handle order removed
                                    // Remove the order from your app
                                }

                                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                                    // Handle order moved
                                }

                                override fun onCancelled(error: DatabaseError) {
                                    // Handle error
                                }
                            })
                        }
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


suspend fun userLogged(applicationContext: Context, sendRetrieveData: SendRetrieveData): Pair<ArrayList<Pizza>, ArrayList<ArrayList<Topping>>> {
    var pizzas: ArrayList<Pizza> = arrayListOf()
    var toppings: ArrayList<ArrayList<Topping>> = arrayListOf()
    val reqRespone= sendRetrieveData.getPizzas()
    if (reqRespone!=null){
        if (reqRespone.isSuccessful) {
            pizzas = reqRespone.retrievedObject!!
            for (pizza in pizzas){
                val pizzaTopping: ArrayList<Topping> = arrayListOf()
                var add = false
                for (toppingRef in pizza.toppings!!){
                    val topping= sendRetrieveData.getToppingByRef(toppingRef)

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
    return Pair(pizzas, toppings)
}