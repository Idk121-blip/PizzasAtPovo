package com.example.pizzasatpovo.ui.screens

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.ActivityInfo
import android.widget.Toast
import androidx.activity.ComponentActivity
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
import com.example.pizzasatpovo.data.viewmodel.MyViewModelFactory
import com.example.pizzasatpovo.data.viewmodel.NavigationViewModel
import com.example.pizzasatpovo.data.viewmodel.PizzaViewModel
import com.example.pizzasatpovo.data.model.RetrievedPizza
import com.example.pizzasatpovo.data.model.Topping
import com.example.pizzasatpovo.data.viewmodel.NotificationViewModel
import com.example.pizzasatpovo.data.viewmodel.TimeOrderViewModel
import com.example.pizzasatpovo.database.FavouritesManager
import com.example.pizzasatpovo.database.OrderManager
import kotlinx.coroutines.launch
import com.example.pizzasatpovo.database.sign_in.GoogleAuthUiClient
import com.example.pizzasatpovo.database.sign_in.SignInViewModel
import com.example.pizzasatpovo.presentation.db_interaction.DataManager
import com.example.pizzasatpovo.ui.screens.account.AccountPageScreen
import com.example.pizzasatpovo.ui.screens.addpizza.AddPizzaScreen
import com.example.pizzasatpovo.ui.screens.chef.ChefOrdersScreen
import com.example.pizzasatpovo.ui.screens.detailspizza.DetailsPizzaScreen
import com.example.pizzasatpovo.ui.screens.favourites.FavouritesScreen
import com.example.pizzasatpovo.ui.screens.listofpizza.ListOfPizzasScreen
import com.example.pizzasatpovo.ui.screens.login.FirstPageScreen
import com.example.pizzasatpovo.ui.screens.orders.OrdersScreen
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
@SuppressLint("UnrememberedMutableState", "SourceLockedOrientationActivity")
fun PizzasAtPovoApp(
    googleAuthUiClient: GoogleAuthUiClient,
    dataManager: DataManager,
    favouritesManager: FavouritesManager,
    orderManager: OrderManager,
    lifecycleScope: LifecycleCoroutineScope,
    applicationContext: Context,
    activity: Activity,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
){
    val viewModel = viewModel<SignInViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val pizzaViewModel = viewModel<PizzaViewModel>()
    val selectedPizza by pizzaViewModel.selectedPizza.collectAsStateWithLifecycle()
    val timeOrderViewModel = viewModel<TimeOrderViewModel>()
    val notificationViewModel= viewModel<NotificationViewModel>()
    notificationViewModel.setContext(applicationContext)
    val timestamp by timeOrderViewModel.time.collectAsStateWithLifecycle()
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
                        userLogged(applicationContext, dataManager,favouritesManager, pizzaViewModel)
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

                    val listResponseData= dataManager.getPizzas()

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
                        userLogged(applicationContext, dataManager,favouritesManager, pizzaViewModel)
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
            ListOfPizzasScreen().ListOfPizzasPage(
                navViewModel = controller,
                viewModel = pizzaViewModel,
                onAddToFavouritesClicked = {pizzaToAdd->
                    lifecycleScope.launch {
                        favouritesManager.addFavourite(pizzaToAdd)
                    }
                },
                onRemoveFromFavouritesClicked ={pizzaToRemove->
                    lifecycleScope.launch {
                        favouritesManager.removeFavourite(pizzaToRemove)
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
                        orderManager.sendOrderRetrievedPizza(selectedPizza, timestamp, numberOfPizza)
                        val cal = Calendar.getInstance()
                        cal.time=timestamp.toDate()
                        var time= cal.get(Calendar.HOUR_OF_DAY).toString()+":"
                        time+= if (cal.get(Calendar.MINUTE).toString()=="0"){
                            "00"
                        }else if (cal.get(Calendar.MINUTE).toString()=="5"){
                            "05"
                        }else{
                            cal.get(Calendar.MINUTE).toString()
                        }

                        notificationViewModel.addListenerForSpecificDocuments(
                            listOf(orderManager.sendRTOrder(selectedPizza,  time, numberOfPizza)!!))
                    }
                },
                viewModel = pizzaViewModel,
                timeOrderViewModel = timeOrderViewModel,
                navViewModel = controller
            )
        }

        composable(route= PizzaScreens.NewPizza.name){
            val numberOfPizza by pizzaViewModel.numberOfPizzaToOrder.collectAsStateWithLifecycle()
            AddPizzaScreen().AddPizzaPage(
                navViewModel = controller,
                viewModel= pizzaViewModel,
                timeOrderViewModel = timeOrderViewModel,
                onOrderButtonClicked = {
                    lifecycleScope.launch {
                        orderManager.sendOrderRetrievedPizza(it, pizzaNumber = numberOfPizza, pickupTime = timestamp)
                        val cal = Calendar.getInstance()
                        cal.time=timestamp.toDate()
                        val time= cal.get(Calendar.HOUR_OF_DAY).toString()+":"
                        time+ if (cal.get(Calendar.MINUTE).toString()==""){
                            "00"
                        }else if (cal.get(Calendar.MINUTE).toString()=="5"){
                            "05"
                        }else{
                            cal.get(Calendar.MINUTE).toString()
                        }
                        notificationViewModel.addListenerForSpecificDocuments(
                            listOf(orderManager.sendRTOrder(selectedPizza,  time, numberOfPizza)!!))
                    }

                }

            )
        }

        composable(route= PizzaScreens.RecentOrders.name) {
            OrdersScreen().OrdersPage(
                navController = controller,
                lifecycleScope= lifecycleScope,
                orderManager = orderManager
            )
        }

        composable(route= PizzaScreens.Favourites.name){
            FavouritesScreen().FavouritesPage(
                navViewModel = controller,
                viewModel = pizzaViewModel,
                onAddToFavouritesClicked = {pizzaToAdd->
                    lifecycleScope.launch {
                        favouritesManager.addFavourite(pizzaToAdd)
                    }
                },
                onRemoveFromFavouritesClicked ={pizzaToRemove->
                    lifecycleScope.launch {
                        favouritesManager.removeFavourite(pizzaToRemove)
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
                    orderManager.processOrder(it)
                }

                    }
            )
        }
    }
}




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



