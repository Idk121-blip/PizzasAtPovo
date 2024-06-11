package com.example.pizzasatpovo.ui.screens.addpizza

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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.pizzasatpovo.ui.components.Bars
import com.example.pizzasatpovo.data.viewmodel.NavigationViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pizzasatpovo.R
import com.example.pizzasatpovo.data.viewmodel.TimeOrderViewModel
import com.example.pizzasatpovo.data.viewmodel.PersonalizedOrderViewModel
import com.example.pizzasatpovo.data.viewmodel.PizzaViewModel
import com.example.pizzasatpovo.data.model.RetrievedPizza
import com.example.pizzasatpovo.data.model.Topping
import com.example.pizzasatpovo.ui.components.BackgroundImage
import com.example.pizzasatpovo.ui.screens.detailspizza.DetailsPizzaScreen

class AddPizzaScreen {
    @Composable
    fun AddPizzaPage(
        message:String,
        navViewModel: NavigationViewModel,
        viewModel: PizzaViewModel,
        modifier: Modifier = Modifier,
        timeOrderViewModel: TimeOrderViewModel,
        onOrderButtonClicked: (RetrievedPizza) -> Unit = {},
    ){
        val personalizedOrderViewModel = viewModel<PersonalizedOrderViewModel>()
        val toppings by viewModel.toppings.collectAsStateWithLifecycle()
        val isToppingEmpty by personalizedOrderViewModel.isEmpty.observeAsState(true)
        Box(modifier = modifier
            .fillMaxSize()
        ){
            BackgroundImage()
            Column {
                Bars().AppBarWithBackBtn(
                    pizzasName = stringResource(R.string.crea_la_tua_pizza),
                    navViewModel = navViewModel
                )
                IngredientList(
                    toppings = toppings,
                    personalizedOrderViewModel= personalizedOrderViewModel)
                Column (
                    verticalArrangement = Arrangement.Bottom,
                    modifier = modifier
                        .fillMaxSize()
                ){
                    viewModel.resetNumberOfPizza()
                    DetailsPizzaScreen().OrderDetails(
                        message= message,
                        pizzaName = stringResource(R.string.la_tua_pizza),
                        viewModel = viewModel,
                        timeOrderViewModel=timeOrderViewModel,
                        onOrderButtonClicked = {
                            onOrderButtonClicked(personalizedOrderViewModel.getRetrievedPizza())
                        },
                        toppingsEmpty = isToppingEmpty,
                        navViewModel = navViewModel
                    )
                }
            }
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterEnd)
            ){
                AsyncImage(model = stringResource(R.string.pizza_for_add_pizza), contentDescription = "pizza image", modifier= modifier
                    .size(250.dp)
                    .align(Alignment.CenterHorizontally)
                )
            }
        }
    }

    @Composable
    fun IngredientList(
        toppings: ArrayList<Topping>,
        personalizedOrderViewModel: PersonalizedOrderViewModel,
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
                    if (topping.name == stringResource(R.string.mozzarella)
                        ||topping.name == stringResource(R.string.pomodoro)
                    ){
                        IngredientCard(
                            topping = topping,
                            personalizedOrderViewModel= personalizedOrderViewModel,

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
                    if (topping.availability && topping.name!=stringResource(R.string.mozzarella)
                        && topping.name!=stringResource(R.string.pomodoro)){
                        IngredientCard(topping = topping,
                            personalizedOrderViewModel= personalizedOrderViewModel)
                    }
                }
            }
        }
    }

   @OptIn(ExperimentalMaterial3Api::class)
   @Composable
   fun IngredientCard(
       topping: Topping,
       personalizedOrderViewModel: PersonalizedOrderViewModel,
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
                       personalizedOrderViewModel.addTopping(topping)
                   } else {
                       personalizedOrderViewModel.removeTopping(topping)
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
                       contentDescription = stringResource(R.string.selected),
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