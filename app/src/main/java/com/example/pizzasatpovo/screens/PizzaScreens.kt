package com.example.pizzasatpovo.screens

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.unit.dp
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
    ListOfPizzas,
    DetailsPizza,
    Favourites,
    NewPizza,
    RecentOrders,
    Account,
    ChefOrders
}

@Composable
@SuppressLint("UnrememberedMutableState")
fun PizzasAtPovoApp(
    googleAuthUiClient: GoogleAuthUiClient,
    sendRetrieveData: SendRetrieveData,
    lifecycleScope: LifecycleCoroutineScope,
    applicationContext: Context,
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier
){
    val viewModel = viewModel<SignInViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()
    NavHost(
        navController = navController,
        startDestination = PizzaScreens.FirstPage.name,
        modifier = modifier
    ){
        var pizzas: ArrayList<Pizza> = arrayListOf()
        var pizzasToppings: ArrayList<ArrayList<Topping>> = arrayListOf()
        var toppings: ArrayList<Topping> = arrayListOf()
        composable(
            route = PizzaScreens.FirstPage.name
        ){
            LaunchedEffect(key1 = Unit) {
                lifecycleScope.launch {
                    googleAuthUiClient.retrieveUserData()
                    if (googleAuthUiClient.getSignedInUser() != null) {
                        if (googleAuthUiClient.getSignedInUser()!!.role!="Chef"){
                            val (returnedPizzas, returnedPizzasToppings,returnedToppings ) = userLogged(applicationContext, sendRetrieveData)
                            pizzas= returnedPizzas
                            pizzasToppings= returnedPizzasToppings
                            toppings= returnedToppings
                            navController.navigate(PizzaScreens.ListOfPizzas.name)
                        }else{
                            navController.navigate(PizzaScreens.ChefOrders.name)
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
                    val listResponseData= sendRetrieveData.getPizzas()

                    if (listResponseData!=null){
                        if (listResponseData.isSuccessful) {
                            pizzas = listResponseData.retrievedObject!!
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
                                    pizzasToppings.add(pizzaTopping)
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
            ListOfPizzasScreen().ListOfPizzasPage(
                pizzas= pizzas,
                toppings = pizzasToppings,
                viewModel = viewModel,
                onHomeButtonClicked = {
                    navController.navigate(PizzaScreens.DetailsPizza.name)},
                onProfileButtonClicked = {
                    navController.navigate(PizzaScreens.Account.name)
                },
                onAddPizzaButtonClicked = {
                    navController.navigate(PizzaScreens.NewPizza.name) },
                onOrdersButtonClicked = {
                    navController.navigate(PizzaScreens.RecentOrders.name) }
            )

        }
        composable(route= PizzaScreens.Account.name){
            AccountPageScreen().AccountPage(googleAuthUiClient = googleAuthUiClient, lifecycleScope = lifecycleScope,modifier,
                onLogOutButtonClicked =  {
                    lifecycleScope.launch {
                        googleAuthUiClient.signOut()
                        navController.navigate(PizzaScreens.FirstPage.name)
                    }
                },
                onHomeButtonClicked = {
                    println("aaaaaaaaaaaaaaaaaaaaaaaaa")
                    navController.navigate(PizzaScreens.ListOfPizzas.name)
                },
            )
        }
        composable(route= PizzaScreens.DetailsPizza.name){
            DetailsPizzaScreen().DetailsPizzaPage(
                pizza = viewModel.getPizza(),
                onBackButtonClicked = { navController.popBackStack() }
            )
        }

        composable(route= PizzaScreens.NewPizza.name){
            println("Add")
            AddPizzaScreen().AddPizzaPage(
                onBackButtonClicked = { navController.popBackStack() }
            )
        }

        composable(route= PizzaScreens.RecentOrders.name){
            OrdersScreen().OrdersPage(
                onHomeButtonClicked = { navController.navigate(
                    PizzaScreens.ListOfPizzas.name
                )},
                viewModel = viewModel
            )
        }

        composable(route= PizzaScreens.ChefOrders.name){
            val database = Firebase.database("https://pizzasatpovo-default-rtdb.europe-west1.firebasedatabase.app")
            val ordersRef = database.getReference("orders")
            val orders= mutableStateListOf<RealTimeOrder?>()
            val childEventListener = object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    val newOrder = snapshot.getValue(RealTimeOrder::class.java)
                    newOrder?.let { order ->
                        orders.add(order)

                    }
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                    // Handle order changed
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {
                    // Handle order removed
                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                    // Handle order moved
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle error
                }
            }

            ordersRef.addChildEventListener(childEventListener)
            LazyColumn {
                items(orders){
                    Box(modifier = modifier.height(50.dp)){
                        Text(text = it!!.uname)
                    }
                }
            }
        }
    }
}


suspend fun userLogged(applicationContext: Context, sendRetrieveData: SendRetrieveData): Triple<ArrayList<Pizza>, ArrayList<ArrayList<Topping>>, ArrayList<Topping>> {
    var pizzas: ArrayList<Pizza> = arrayListOf()
    val pizzasToppings: ArrayList<ArrayList<Topping>> = arrayListOf()
    var toppings: ArrayList<Topping> = arrayListOf()
    val reqRespone= sendRetrieveData.getPizzas()
    val toppingResponse= sendRetrieveData.getToppings()

    if (toppingResponse != null){
        if (toppingResponse.isSuccessful){
            if (toppingResponse.retrievedObject != null){
                toppings=toppingResponse.retrievedObject
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

    }else{
        Toast.makeText(
            applicationContext,
            "Error retrieving toppings",
            Toast.LENGTH_LONG
        ).show()
    }



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
                    pizzasToppings.add(pizzaTopping)
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

    return Triple(pizzas, pizzasToppings, toppings)
}



