package com.example.pizzasatpovo.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pizzasatpovo.data.Pizza

enum class PizzaScreens {
    FirstPage,
    ListOfPizzas,
    DetailsPizza,
    Favourites,
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
                    navController.navigate(PizzaScreens.ListOfPizzas.name)
                }
            )
        }
        composable(
            route = PizzaScreens.ListOfPizzas.name
        ){
            ListOfPizzasScreen().ListOfPizzasPage(
                onDetailsButtonClicked = {
                    navController.navigate(PizzaScreens.DetailsPizza.name)
                }
            )
        }
        composable(
            route = PizzaScreens.DetailsPizza.name
        ){
            DetailsPizzaScreen().DetailsPizzaPage(
                //pizza = Pizza("Patatosa", ),
                onBackButtonClicked = { navController.popBackStack() }
            )
        }
    }
}