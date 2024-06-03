package com.example.pizzasatpovo

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.material3.Surface
import androidx.lifecycle.lifecycleScope
import com.example.pizzasatpovo.database.FavouritesManager
import com.example.pizzasatpovo.database.OrderManager
import com.google.android.gms.auth.api.identity.Identity
import com.example.pizzasatpovo.database.sign_in.GoogleAuthUiClient
import com.example.pizzasatpovo.database.DataManager
import com.example.pizzasatpovo.ui.screens.PizzasAtPovoApp
import com.example.pizzasatpovo.ui.theme.ComposeGoogleSignInCleanArchitectureTheme

class MainActivity : ComponentActivity() {

    private val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext)
        )
    }
    private val dataManager by lazy{
        DataManager()
    }
    private val orderManager by lazy{
        OrderManager(googleAuthUiClient)
    }
    private val favouritesManager by lazy{
        FavouritesManager(googleAuthUiClient)
    }
    @RequiresApi(Build.VERSION_CODES.O) // TODO CHECK IF REMOVING IT WORKS
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeGoogleSignInCleanArchitectureTheme {
                Surface{
                    PizzasAtPovoApp(googleAuthUiClient, dataManager, favouritesManager, orderManager, lifecycleScope, applicationContext, activity = this)
                }
            }
        }
    }
}





