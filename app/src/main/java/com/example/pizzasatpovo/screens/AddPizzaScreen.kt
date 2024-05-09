package com.example.pizzasatpovo.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.pizzasatpovo.ui.components.Bars
import com.example.pizzasatpovo.data.NavigationViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pizzasatpovo.data.PersonalizedOrderViewMode
import com.example.pizzasatpovo.data.PizzaViewModel
import com.example.pizzasatpovo.data.RetrievedPizza
import com.example.pizzasatpovo.data.Topping
import com.example.pizzasatpovo.ui.components.BackgroundImage

class AddPizzaScreen {
    @Composable
    fun AddPizzaPage(
        navViewModel: NavigationViewModel,
        viewModel: PizzaViewModel,
        modifier: Modifier = Modifier,
        onOrderButtonClicked: (RetrievedPizza) -> Unit = {},
    ){
        val personalizedOrderViewMode = viewModel<PersonalizedOrderViewMode>()
        val toppings by viewModel.toppings.collectAsStateWithLifecycle()
        Box(modifier = modifier
            .fillMaxSize()
        ){

            BackgroundImage()
            Column {
                Bars().AppBarWithBackBtn(
                    pizzasName = "Crea la tua pizza",
                    navViewModel = navViewModel
                )
                IngredientList(toppings = toppings,
                    personalizedOrderViewMode= personalizedOrderViewMode)
                //White container
                Column (
                    verticalArrangement = Arrangement.Bottom,
                    modifier = modifier
                        .fillMaxSize()
                ){
                    viewModel.resetNumberOfPizza()
                    DetailsPizzaScreen().OrderDetails(
                        pizzaName = "La tua pizza",
                        viewModel = viewModel,
                        onOrderButtonClicked = {
                            onOrderButtonClicked(personalizedOrderViewMode.getRetrievedPizza())
                        })
                }
            }
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterEnd)
            ){
                //TODO! REPLACE IMAGE OF MARGHERITA WITH THE FINAL ONE
                AsyncImage(model = "https://www.wanmpizza.com/wp-content/uploads/2022/09/pizza.png", contentDescription = "pizza image", modifier= modifier
                    .size(250.dp)
                    .align(Alignment.CenterHorizontally)
                )
            }
        }
    }

    @Composable
    fun IngredientList(
        toppings: ArrayList<Topping>,
        personalizedOrderViewMode: PersonalizedOrderViewMode,
        modifier: Modifier = Modifier
    ){
        Column(
            modifier = modifier
                .padding(50.dp, 10.dp)
        ) {
            Text(
                text = "Base:",
                fontWeight = FontWeight.Bold
            )
            Row {
                for (topping in toppings){
                    if (topping.name=="Mozzarella"||topping.name=="Pomodoro"){
                        personalizedOrderViewMode.addTopping(topping)
                        IngredientCard(
                            topping = topping,
                            personalizedOrderViewMode= personalizedOrderViewMode,
                            defaultSelected = true
                        )
                    }
                }
            }
            Text(
                text = "Altri ingredienti:",
                fontWeight = FontWeight.Bold,
                modifier = modifier
                    .padding(top = 10.dp)
            )
            Row (
                modifier = modifier
                    .horizontalScroll(rememberScrollState())
            ){
                for (topping in toppings){
                    if (topping.availability && topping.name!="Mozzarella" && topping.name!="Pomodoro"){
                        IngredientCard(topping = topping,
                            personalizedOrderViewMode= personalizedOrderViewMode)
                    }
                }
            }
        }
    }

   @OptIn(ExperimentalMaterial3Api::class)
   @Composable
   fun IngredientCard(
       topping: Topping,
       personalizedOrderViewMode: PersonalizedOrderViewMode,
       modifier: Modifier = Modifier,
       defaultSelected:Boolean= false
   ) {
       var selected by remember { mutableStateOf(defaultSelected) }


       TooltipBox(
           positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
           tooltip = {
               PlainTooltip {
                   Text(topping.name)
               }
           },
           state = rememberTooltipState()
       ) {
           Card(
               onClick =
               {
                   selected = !selected
                   if (selected) {
                       personalizedOrderViewMode.addTopping(topping)
                   } else {
                       personalizedOrderViewMode.removeTopping(topping)
                   }
               },
               shape = RoundedCornerShape(percent = 15),
               colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
               modifier = modifier
                   .padding(2.dp)
                   .size(60.dp)
           ) {
               Box(
                   modifier = modifier
                       .fillMaxSize()
                       .padding(2.dp)
               ) {
                   Image(
                       imageVector = if (selected) Icons.Default.CheckCircle else Icons.Default.AddCircle,
                       contentDescription = "Selected",
                       modifier = modifier
                           .size(20.dp)
                           .align(Alignment.TopEnd)
                   )
                   AsyncImage(
                       model = topping.image, contentDescription = topping.name, modifier = modifier
                           .padding(top = 8.dp)
                           .size(55.dp)
                           .align(Alignment.BottomCenter)
                           .padding(4.dp)
                   )
               }
           }
       }
   }
}