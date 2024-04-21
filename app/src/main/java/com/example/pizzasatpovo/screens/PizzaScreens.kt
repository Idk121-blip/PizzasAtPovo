package com.example.pizzasatpovo.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

enum class PizzaScreens {
    FirstPage,
    LoginPage,
    ListOfPizzas,
    NewPizza,
    RecentOrders,
    Account
}

@Composable
@Preview(showBackground = true)
fun PizzasAtPovoApp(
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
            FirstPageScreen().FirstPage(
                onLoginButtonClicked = {
                    println("Changing screen")
                    navController.navigate(PizzaScreens.LoginPage.name)
                }
            )
        }
        composable(
            route = PizzaScreens.LoginPage.name
        ){
            var context = LocalContext.current
            ListOfPizzasScreen().LoginPage()
        }
    }
}