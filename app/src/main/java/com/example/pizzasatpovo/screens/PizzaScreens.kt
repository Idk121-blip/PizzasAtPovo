package com.example.pizzasatpovo.screens

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context

import android.content.pm.ActivityInfo
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pizzasatpovo.data.MyViewModelFactory
import com.example.pizzasatpovo.data.NavigationViewModel
import com.example.pizzasatpovo.data.PizzaViewModel
import com.example.pizzasatpovo.data.RetrievedPizza
import com.example.pizzasatpovo.data.Topping
import com.example.pizzasatpovo.data.UserOrderViewModel
import com.example.pizzasatpovo.data.NotificationViewModel
import com.example.pizzasatpovo.data.OrderViewModel
import kotlinx.coroutines.launch
import com.example.pizzasatpovo.presentation.db_interaction.SendRetrieveData
import com.example.pizzasatpovo.presentation.sign_in.GoogleAuthUiClient
import com.example.pizzasatpovo.presentation.sign_in.SignInViewModel
import com.google.firebase.Timestamp
import java.util.Calendar
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
    activity: Activity,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
){
    val viewModel = viewModel<SignInViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val pizzaViewModel = viewModel<PizzaViewModel>()
    //val toppings by pizzaViewModel.toppings.collectAsStateWithLifecycle()
    val selectedPizza by pizzaViewModel.selectedPizza.collectAsStateWithLifecycle()
    //val favourites by pizzaViewModel.favourites.collectAsState()
    val orderViewModel = viewModel<OrderViewModel>()
    val notificationViewModdel= viewModel<NotificationViewModel>()
    notificationViewModdel.setContext(applicationContext)
    val timestamp by orderViewModel.time.collectAsStateWithLifecycle()

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
                        activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                        userLogged(applicationContext, sendRetrieveData, pizzaViewModel)
                        navController.popBackStack()
                        navController.navigate(PizzaScreens.ListOfPizzas.name)
                    }else{
                        activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                        navController.popBackStack()
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
                    if (googleAuthUiClient.getSignedInUser()!!.role=="Chef"){
                        navController.navigate(PizzaScreens.ChefOrders.name)
                    }

                    val listResponseData= sendRetrieveData.getPizzas()

                    if (listResponseData==null){
                        Toast.makeText(
                            applicationContext,
                            "Error retrieving pizzas",
                            Toast.LENGTH_LONG
                        ).show()
                    }else if (!listResponseData.isSuccessful){
                        Toast.makeText(
                            applicationContext,
                            "Error retrieving pizzas",
                            Toast.LENGTH_LONG
                        ).show()
                    }else{
                        userLogged(applicationContext, sendRetrieveData, pizzaViewModel)
                        navController.enableOnBackPressed(enabled = false)
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
                        navController.enableOnBackPressed(enabled = false)
                        navController.navigate(PizzaScreens.FirstPage.name)
                    }
                },
            )
        }
        composable(route= PizzaScreens.DetailsPizza.name){
            val numberOfPizza by pizzaViewModel.numberOfPizzaToOrder.collectAsStateWithLifecycle()
            DetailsPizzaScreen().DetailsPizzaPage(
                pizza = selectedPizza,
                onOrderButtonClicked = {
                    lifecycleScope.launch {
                        sendRetrieveData.sendOrderRetrievedPizza(selectedPizza, timestamp, numberOfPizza)
                        val cal = Calendar.getInstance()
                        cal.time=timestamp.toDate()
                        notificationViewModdel.addListenerForSpecificDocuments(
                            listOf(sendRetrieveData.sendRTOrder(selectedPizza, cal.get(Calendar.HOUR_OF_DAY).toString()+"."+cal.get(Calendar.MINUTE).toString(), numberOfPizza)!!))
                    }
                },
                viewModel = pizzaViewModel,
                orderViewModel = orderViewModel,
                navViewModel = controller
            )
        }

        composable(route= PizzaScreens.NewPizza.name){
            val numberOfPizza by pizzaViewModel.numberOfPizzaToOrder.collectAsStateWithLifecycle()
            AddPizzaScreen().AddPizzaPage(
                navViewModel = controller,
                viewModel= pizzaViewModel,
                orderViewModel = orderViewModel,
                onOrderButtonClicked = {
                    lifecycleScope.launch {
                        sendRetrieveData.sendOrderRetrievedPizza(it, pizzaNumber = numberOfPizza, pickupTime = timestamp)
                        val cal = Calendar.getInstance()
                        cal.time=timestamp.toDate()
                        notificationViewModdel.addListenerForSpecificDocuments(
                        listOf( sendRetrieveData.sendRTOrder(it, cal.get(Calendar.HOUR_OF_DAY).toString()+"."+cal.get(Calendar.MINUTE).toString() , numberOfPizza)!!))
                    }

                }

            )
        }

        composable(route= PizzaScreens.RecentOrders.name) {
            OrdersScreen().OrdersPage(
                navController = controller,
                lifecycleScope= lifecycleScope,
                sendRetrieveData = sendRetrieveData
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
            ChefOrdersScreen().ChefOrdersPage(
                    onLogOutButtonClicked =  {
                        lifecycleScope.launch {
                            googleAuthUiClient.signOut()
                            navController.enableOnBackPressed(enabled = false)
                            navController.navigate(PizzaScreens.FirstPage.name)
                        }
                    },
                    processOrder = {
                        lifecycleScope.launch {
                            sendRetrieveData.ProcessOrder(it)
                        }

                    }
            )
        }
    }
}




suspend fun userLogged(applicationContext: Context, sendRetrieveData: SendRetrieveData, pizzaViewModel: PizzaViewModel) {
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
    pizzaViewModel.setPizzas(pizzas)
    pizzaViewModel.setFavourites(favourites)
}



