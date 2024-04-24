package com.example.pizzasatpovo.screens
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.Observer
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pizzasatpovo.data.ChefViewModel
import com.example.pizzasatpovo.data.Pizza
import com.example.pizzasatpovo.data.RealTimeOrder
import com.example.pizzasatpovo.presentation.db_interaction.SendRetrieveData
import com.example.pizzasatpovo.presentation.profile.ProfileScreen
import com.google.android.gms.auth.api.identity.Identity
import com.example.pizzasatpovo.presentation.sign_in.GoogleAuthUiClient
import com.example.pizzasatpovo.presentation.sign_in.SignInScreen
import com.example.pizzasatpovo.presentation.sign_in.SignInViewModel
import com.example.pizzasatpovo.ui.theme.ComposeGoogleSignInCleanArchitectureTheme
import com.google.firebase.Timestamp
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import java.util.Calendar

class MainActivity : ComponentActivity() {

    private val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext)
        )
    }
    private val  sendRetrieveData by lazy{
        SendRetrieveData(googleAuthUiClient)
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeGoogleSignInCleanArchitectureTheme {
                // A surface container using the 'background' color from the theme
//
                //sign_in_button()

                Surface(){
                    PizzasAtPovoApp(googleAuthUiClient, sendRetrieveData, lifecycleScope, applicationContext)
                }
            }
        }
    }

    @Composable
    fun sign_in_button(){
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            val navController = rememberNavController()
            var pizza:Pizza?
            NavHost(navController = navController, startDestination = "sign_in") {
                composable("sign_in") {
                    val viewModel = viewModel<SignInViewModel>()
                    val state by viewModel.state.collectAsStateWithLifecycle()

                    LaunchedEffect(key1 = Unit) {
                        lifecycleScope.launch {
                            googleAuthUiClient.retrieveUserData()

                            if (googleAuthUiClient.getSignedInUser() != null) {
                                navController.navigate("profile")
                            }
                        }
                    }


                    val launcher = rememberLauncherForActivityResult(
                        contract = ActivityResultContracts.StartIntentSenderForResult(),
                        onResult = { result ->
                            if (result.resultCode == RESULT_OK) {
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
                            navController.navigate("profile")
                            viewModel.resetState()
                        }
                    }

                    SignInScreen(
                        state = state,
                        onSignInClick = {
                            lifecycleScope.launch {
                                val signInIntentSender = googleAuthUiClient.signIn()
                                launcher.launch(
                                    IntentSenderRequest.Builder(
                                        signInIntentSender ?: return@launch
                                    ).build()
                                )
                            }
                        },
                    )
                }
                composable("profile") {
                    val calendar = Calendar.getInstance().apply {
                        set(2024, Calendar.APRIL, 21, 12, 0, 0) // April 25, 2024, 12:00 PM
                    }
                    ProfileScreen(
                        userData = googleAuthUiClient.getSignedInUser(),
                        onSignOut = {
                            lifecycleScope.launch {

                                //SENDING ORDERS:
                                /*
                                val retrievedPizza = sendRetrieveData.getPizza("Margherita")
                                if (retrievedPizza == null){
                                    Toast.makeText(
                                        applicationContext,
                                        "Something went wrong with auth",
                                        Toast.LENGTH_LONG,
                                    ).show()
                                    return@launch
                                }
                                if (!retrievedPizza.isSuccessful){
                                    Toast.makeText(
                                        applicationContext,
                                        retrievedPizza.message,
                                        Toast.LENGTH_LONG,
                                    ).show()
                                    return@launch
                                }
                                pizza= retrievedPizza.retrievedObject
                                val orderResponse= sendRetrieveData.sendOrder(pizza= pizza!!, pickupTime = Timestamp.now(), price = 4.4)
                                if (orderResponse==null){
                                    Toast.makeText(
                                        applicationContext,
                                        "Something went wrong with google Auth",
                                        Toast.LENGTH_LONG,
                                    ).show()
                                    return@launch
                                }
                                if (orderResponse.isSuccessful){
                                    Toast.makeText(
                                        applicationContext,
                                        orderResponse.message,
                                        Toast.LENGTH_LONG,
                                    ).show()
                                }

                                */
                                val retrievedPizza = sendRetrieveData.getPizza("Margherita")
                                pizza= retrievedPizza!!.retrievedObject
                                val reqResponse= sendRetrieveData.getPizzas()

                                if (reqResponse!=null){
                                    //println(reqResponse.retrievedObject)
                                }

                                sendRetrieveData.sendRTOrderd(pizza!!, date = "22-04-2024 14:30", 2);

                                //send

                                val ResponseFavourites= sendRetrieveData.retrieveFavourites()

                                if (ResponseFavourites!=null){
                                    if (ResponseFavourites.isSuccessful){
                                        println(ResponseFavourites.retrievedObject)
                                    }
                                }




                                //googleAuthUiClient.signOut()
                                navController.popBackStack()
                            }
                        }
                    )
                }
            }

        }
    }


}





