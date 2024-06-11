package com.example.pizzasatpovo.ui.screens

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.ActivityInfo
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
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
import com.example.pizzasatpovo.database.GoogleAuthUiClient
import com.example.pizzasatpovo.data.viewmodel.SignInViewModel
import com.example.pizzasatpovo.database.DataManager
import com.example.pizzasatpovo.ui.components.BackgroundImage
import com.example.pizzasatpovo.ui.components.userLogged
import com.example.pizzasatpovo.ui.screens.account.AccountPageScreen
import com.example.pizzasatpovo.ui.screens.addpizza.AddPizzaScreen
import com.example.pizzasatpovo.ui.screens.chef.ChefOrdersScreen
import com.example.pizzasatpovo.ui.screens.detailspizza.DetailsPizzaScreen
import com.example.pizzasatpovo.ui.screens.favourites.FavouritesScreen
import com.example.pizzasatpovo.ui.screens.listofpizza.ListOfPizzasScreen
import com.example.pizzasatpovo.ui.screens.login.FirstPageScreen
import com.example.pizzasatpovo.ui.screens.orders.OrdersScreen
import com.google.firebase.Timestamp
import java.util.Calendar

enum class PizzaScreens {
    LoadinPage,
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
        startDestination = PizzaScreens.LoadinPage.name,
        modifier = modifier
    ){
        composable(route= PizzaScreens.LoadinPage.name){
            LaunchedEffect(key1 = Unit) {
                lifecycleScope.launch {
                    googleAuthUiClient.retrieveUserData()
                    if (googleAuthUiClient.getSignedInUser() == null) {
                        navController.navigate(PizzaScreens.FirstPage.name)
                    }else if (googleAuthUiClient.getSignedInUser()!!.role!="Chef"){
                        navController.popBackStack()
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
            BackgroundImage()
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = Color.Black.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Caricamento, attendere...",
                        color = Color.White // Optional: Set text color to white for better visibility
                    )
                }
            }
        }
        composable(
            route = PizzaScreens.FirstPage.name
        ){
            val launcher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.StartIntentSenderForResult(),
                onResult = { result ->
                    if (result.resultCode == -1) {
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
                        navController.popBackStack()
                        activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                        navController.navigate(PizzaScreens.ChefOrders.name)
                    }
                    navController.popBackStack()
                    navController.navigate(PizzaScreens.LoadinPage.name)
                    userLogged(applicationContext, dataManager,favouritesManager, pizzaViewModel)

                    navController.enableOnBackPressed(enabled = false)
                    navController.navigate(PizzaScreens.ListOfPizzas.name)

                    Toast.makeText(
                        applicationContext,
                        "Sign in successful",
                        Toast.LENGTH_LONG
                    ).show()
                    navController.popBackStack()
                    navController.navigate(PizzaScreens.ListOfPizzas.name)
                    viewModel.resetState()
                }
            }
            var buttonAvailable by remember {
                mutableStateOf(true)
            }

            var lastSignInAttemptTime by remember { mutableLongStateOf(0L) }
            val signInCoolDown = 30000L // 30 seconds cooldown

            FirstPageScreen().FirstPage(
                buttonAvailable= buttonAvailable,
                onLoginButtonClicked = {
                    if (!buttonAvailable) return@FirstPage // Prevent multiple clicks

                    val currentTime = System.currentTimeMillis()
                    if (currentTime - lastSignInAttemptTime < signInCoolDown) {
                        Toast.makeText(
                            applicationContext,
                            "Please wait before attempting to sign in again.",
                            Toast.LENGTH_LONG
                        ).show()
                        return@FirstPage
                    }


                    lifecycleScope.launch {
                        buttonAvailable = false
                        lastSignInAttemptTime = currentTime
                        try {
                            val signInIntentSender = googleAuthUiClient.signIn()
                            launcher.launch(
                                IntentSenderRequest.Builder(
                                    signInIntentSender ?: return@launch
                                ).build()
                            )
                        } finally {
                            buttonAvailable = true
                        }
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
            var message by remember {
                mutableStateOf("")
            }

            DetailsPizzaScreen().DetailsPizzaPage(
                message = message,
                pizza = selectedPizza,
                onOrderButtonClicked = {
                    message= "Ordine effettuato!"
                    lifecycleScope.launch {
                        if (timestamp.seconds< Timestamp.now().seconds) {
                            message= "Orario non valido"
                            return@launch
                        }

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
                        message= (orderManager.sendOrderRetrievedPizza(selectedPizza, timestamp, numberOfPizza, time) ?: return@launch).message

                        val rtOrder= orderManager.sendRTOrder(selectedPizza,  time, numberOfPizza) ?: return@launch
                        notificationViewModel.addListenerForSpecificDocuments(
                            listOf(rtOrder))

                    }
                },
                viewModel = pizzaViewModel,
                timeOrderViewModel = timeOrderViewModel,
                navViewModel = controller
            )
        }

        composable(route= PizzaScreens.NewPizza.name){
            val numberOfPizza by pizzaViewModel.numberOfPizzaToOrder.collectAsStateWithLifecycle()
            var message by remember {
                mutableStateOf("Ordine effettuato")
            }
            AddPizzaScreen().AddPizzaPage(
                message = message,
                navViewModel = controller,
                viewModel= pizzaViewModel,
                timeOrderViewModel = timeOrderViewModel,
                onOrderButtonClicked = { newPizza ->
                    lifecycleScope.launch {
                        if (timestamp.seconds< Timestamp.now().seconds) {
                            message= "Orario non valido"
                            return@launch
                        }
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

                        message= (orderManager.sendOrderRetrievedPizza(newPizza, timestamp, numberOfPizza, time) ?: return@launch).message



                        val rtOrder= orderManager.sendRTOrder(newPizza,  time, numberOfPizza) ?: return@launch
                        notificationViewModel.addListenerForSpecificDocuments(
                            listOf(rtOrder))

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
                },
                onResetButtonClicked = {
                    orderManager.resetOrdersSlot()
                }
            )
        }
    }
}







