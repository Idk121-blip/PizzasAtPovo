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
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel

import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import com.example.pizzasatpovo.presentation.db_interaction.SendRetrieveData
import com.example.pizzasatpovo.presentation.sign_in.GoogleAuthUiClient
import com.example.pizzasatpovo.presentation.sign_in.SignInScreen
import com.example.pizzasatpovo.presentation.sign_in.SignInViewModel
import com.google.android.gms.auth.api.identity.Identity

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
        startDestination = PizzaScreens.FirstPage.name,
        modifier = modifier
    ){
        composable(
            route = PizzaScreens.FirstPage.name
        ){
            val viewModel = viewModel<SignInViewModel>()
            val state by viewModel.state.collectAsStateWithLifecycle()

            LaunchedEffect(key1 = Unit) {
                println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA")
                lifecycleScope.launch {
                    googleAuthUiClient.retrieveUserData()
                    println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA")
                    println(googleAuthUiClient.getSignedInUser())
                    println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA")

                    if (googleAuthUiClient.getSignedInUser() != null) {
                        navController.navigate(PizzaScreens.LoginPage.name)
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
                    Toast.makeText(
                        applicationContext,
                        "Sign in successful",
                        Toast.LENGTH_LONG
                    ).show()
                    navController.navigate(PizzaScreens.LoginPage.name)
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
            route = PizzaScreens.LoginPage.name
        ){
            var context = LocalContext.current
            ListOfPizzasScreen().ListOfPizzas()
        }
    }
}