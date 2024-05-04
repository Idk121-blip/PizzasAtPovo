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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pizzasatpovo.data.MyViewModelFactory
import com.example.pizzasatpovo.data.NavigationViewModel
import com.example.pizzasatpovo.data.PizzaViewModel
import com.example.pizzasatpovo.data.RealTimeOrder
import com.example.pizzasatpovo.data.RetrievedPizza
import com.example.pizzasatpovo.data.Topping
import kotlinx.coroutines.launch
import com.example.pizzasatpovo.presentation.db_interaction.SendRetrieveData
import com.example.pizzasatpovo.presentation.sign_in.GoogleAuthUiClient
import com.example.pizzasatpovo.presentation.sign_in.SignInViewModel
import com.google.firebase.Timestamp
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
    val pizzaViewModel = viewModel<PizzaViewModel>()
    val toppings by pizzaViewModel.toppings.collectAsStateWithLifecycle()
    val selectedPizza by pizzaViewModel.selectedPizza.collectAsStateWithLifecycle()
    val favourites by pizzaViewModel.favourites.collectAsState()

    val controller: NavigationViewModel = viewModel(factory = MyViewModelFactory(navController))
    NavHost(
        navController = navController,
        startDestination = PizzaScreens.FirstPage.name,
        modifier = modifier
    ){

        composable(
            route = PizzaScreens.FirstPage.name
        ){
            LaunchedEffect(key1 = Unit) {
                lifecycleScope.launch {
                    googleAuthUiClient.retrieveUserData()
                    if (googleAuthUiClient.getSignedInUser() == null) {
                        //TODO THIS SHOULD REDIRECT TO LOGIN THAT RN IS THIS PAGE BUT SHOULD CHANGE
                    }else if (googleAuthUiClient.getSignedInUser()!!.role!="Chef"){
                        userLogged(applicationContext, sendRetrieveData, pizzaViewModel)
                        navController.navigate(PizzaScreens.ListOfPizzas.name)
                    }else{
                        navController.navigate(PizzaScreens.ChefOrders.name)
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

                    if (listResponseData==null){
                        Toast.makeText(
                            applicationContext,
                            "Error retrieving pizzas",
                            Toast.LENGTH_LONG
                        ).show()
                        //TODO: DO SOMETHING IF NO PIZZAS ARE RETRIEVED?
                        return@LaunchedEffect
                    }else if (!listResponseData.isSuccessful){
                        Toast.makeText(
                            applicationContext,
                            "Error retrieving pizzas",
                            Toast.LENGTH_LONG
                        ).show()
                    }else{
                        userLogged(applicationContext, sendRetrieveData, pizzaViewModel)
                        navController.navigate(PizzaScreens.ListOfPizzas.name)
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

//            var context = LocalContext.current
            ListOfPizzasScreen().ListOfPizzasPage(
                navViewModel = controller,
                viewModel = pizzaViewModel,
                onAddToFavouritesClicked = {pizzaToAdd->
                    lifecycleScope.launch {
                        sendRetrieveData.addFavourite(pizzaToAdd)
                    }
                },
                onRemoveFromFavouritesClicked ={pizzaToRemove->
                    lifecycleScope.launch {
                        sendRetrieveData.removeFavourite(pizzaToRemove)
                    }
                },
            )

        }
        composable(route= PizzaScreens.Account.name){
            AccountPageScreen().AccountPage(
                navController = controller,
                googleAuthUiClient = googleAuthUiClient,
                lifecycleScope = lifecycleScope,
                modifier = modifier,
                onLogOutButtonClicked =  {
                    lifecycleScope.launch {
                        googleAuthUiClient.signOut()
                        navController.navigate(PizzaScreens.FirstPage.name)
                    }
                },
                onHomeButtonClicked = {
                    navController.navigate(PizzaScreens.ListOfPizzas.name)
                },
                onAddPizzaButtonClicked = {
                    navController.navigate(PizzaScreens.NewPizza.name)
                },
                onOrdersButtonClicked = {
                    navController.navigate(PizzaScreens.RecentOrders.name)
                }
            )
        }
        composable(route= PizzaScreens.DetailsPizza.name){
            val numberOfPizza by pizzaViewModel.numberOfPizzaToOrder.collectAsStateWithLifecycle()
            DetailsPizzaScreen().DetailsPizzaPage(
                pizza = selectedPizza,
                onBackButtonClicked = { navController.popBackStack() },
                onOrderButtonClicked = {

                    lifecycleScope.launch {
                        sendRetrieveData.sendOrderRetrievedPizza(selectedPizza, Timestamp.now(), numberOfPizza)
                        sendRetrieveData.sendRTOrderd(selectedPizza, "12.30", numberOfPizza)
                    }
                },
                viewModel = pizzaViewModel

            )
        }

        composable(route= PizzaScreens.NewPizza.name){
            val numberOfPizza by pizzaViewModel.numberOfPizzaToOrder.collectAsStateWithLifecycle()
            AddPizzaScreen().AddPizzaPage(
                navViewModel = controller,
                viewModel= pizzaViewModel,
                onOrderButtonClicked = {
                    lifecycleScope.launch {
                        sendRetrieveData.sendOrderRetrievedPizza(it, pizzaNumber = numberOfPizza, pickupTime = Timestamp.now())
                        sendRetrieveData.sendRTOrderd(it, "12.30", numberOfPizza)
                    }

                }

            )
        }

        composable(route= PizzaScreens.RecentOrders.name){
            OrdersScreen().OrdersPage(
                navController = controller,
                onHomeButtonClicked = { navController.navigate(
                    PizzaScreens.ListOfPizzas.name
                )},
                viewModel = viewModel
            )
        }

        composable(route= PizzaScreens.Favourites.name){
            FavouritesScreen().FavouritesPage(
                navViewModel = controller,
                viewModel = pizzaViewModel,
                onAddToFavouritesClicked = {pizzaToAdd->
                    lifecycleScope.launch {
                        sendRetrieveData.addFavourite(pizzaToAdd)
                    }
                },
                onRemoveFromFavouritesClicked ={pizzaToRemove->
                    lifecycleScope.launch {
                        sendRetrieveData.removeFavourite(pizzaToRemove)
                    }
                },
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


suspend fun userLogged(applicationContext: Context, sendRetrieveData: SendRetrieveData, pizzaViewModel: PizzaViewModel) {
    //TODO: try to do these as val
    var pizzas: ArrayList<RetrievedPizza> = arrayListOf()
    var toppings: ArrayList<Topping> = arrayListOf()
    val reqRespone= sendRetrieveData.getPizzas()
    val toppingResponse= sendRetrieveData.getToppings()

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



    if (reqRespone==null){
        Toast.makeText(
            applicationContext,
            "Error retrieving pizzas",
            Toast.LENGTH_LONG
        ).show()
    }else if (!reqRespone.isSuccessful) {
        Toast.makeText(
            applicationContext,
            "Error retrieving pizzas",
            Toast.LENGTH_LONG
        ).show()
    }else{
        pizzas = reqRespone.retrievedObject!!
    }

    val favouritesRespone = sendRetrieveData.retrieveFavourites()

    val favourites = if (favouritesRespone!=null){
        favouritesRespone.retrievedObject?: arrayListOf()
    }else{
        arrayListOf()
    }


    pizzaViewModel.setToppings(toppings)
    pizzaViewModel.addPizzas(pizzas)
    pizzaViewModel.setFavourites(favourites)
}



