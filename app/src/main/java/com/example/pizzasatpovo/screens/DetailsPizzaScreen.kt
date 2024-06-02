package com.example.pizzasatpovo.screens

import PickerExample
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.pizzasatpovo.R
import com.example.pizzasatpovo.data.NavigationViewModel
import com.example.pizzasatpovo.data.OrderViewModel
import com.example.pizzasatpovo.data.PizzaViewModel
import com.example.pizzasatpovo.data.RetrievedPizza
import com.example.pizzasatpovo.ui.components.Allergen
import com.example.pizzasatpovo.ui.components.Bars

class DetailsPizzaScreen {
    @Composable
    fun DetailsPizzaPage(
        pizza: RetrievedPizza,
        viewModel: PizzaViewModel,
        navViewModel: NavigationViewModel,
        modifier: Modifier = Modifier,
        orderViewModel: OrderViewModel,
        onOrderButtonClicked: () -> Unit={}
    ){
        var listOfToppings = ""
        for (topping in pizza.toppings!!){
            listOfToppings= listOfToppings.plus(topping.name).plus(", ")
        }

        listOfToppings= listOfToppings.removeSuffix(", ")
        Box(modifier = modifier
            .fillMaxSize()
        ){
            Box {
                Image(
                    painter = painterResource(id = R.drawable.background),
                    contentDescription = "Background image",
                    contentScale = ContentScale.FillBounds,
                    alpha = 0.5F,
                    modifier = modifier
                        .fillMaxSize()
                )
            }
        }
        Box(
            modifier = modifier
                .fillMaxSize()
        ) {
            Column {
                Bars().AppBarWithBackBtn(
                    pizzasName = pizza.name,
                    navViewModel = navViewModel
                )
                Column (
                    modifier = modifier
                        .padding(50.dp, 10.dp)
                        .fillMaxWidth()
                ) {

                        Text(
                            text = "Ingredienti",
                            fontWeight = FontWeight.Bold,
                            modifier = modifier
                                .padding(top = 10.dp)
                        )
                        Text(

                            text = listOfToppings,
                            overflow = TextOverflow.Clip,
                            modifier = modifier
                                .padding(start = 20.dp, top = 10.dp)
                        )
                        Text(
                            text = "Allergeni",
                            fontWeight = FontWeight.Bold,
                            modifier = modifier
                                .padding(top = 10.dp)
                        )
                    }
                    Row (
                        modifier = modifier
                            .padding(start = 70.dp)
                    ){
                        //TODO
                        val allergens:ArrayList<String> = arrayListOf()
                        var vegetarian=true
                        for (topping in pizza.toppings){
                            if (!allergens.contains(topping.allergens)){
                                allergens.add(topping.allergens)
                            }
                            vegetarian= vegetarian&&topping.vegetarian
                        }

                        if (allergens!= arrayListOf("")) {
                            for (allergen in allergens) {
                                if (allergen != "") {
                                    Allergen(allergen = allergen)
                                }
                            }
                        }
                    }
            }
        }

        //White container
        Column (
            verticalArrangement = Arrangement.Bottom,
            modifier = modifier
                .fillMaxSize()

        ){
            Box(modifier = modifier){
                viewModel.resetNumberOfPizza()
                OrderDetails(
                    onOrderButtonClicked= onOrderButtonClicked,
                    pizzaName = pizza.name,
                    viewModel = viewModel,
                    orderViewModel=  orderViewModel
                )
            }

        }

        Box (
            modifier = modifier.fillMaxSize()
        ){
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterEnd)
            ){
                AsyncImage(model = pizza.image, contentDescription = "pizza image", modifier= modifier
                    .size(250.dp)
                    .align(Alignment.CenterHorizontally)
                )
            }
        }
    }

    @Composable
    fun OrderDetails(
        pizzaName:String,
        viewModel: PizzaViewModel,
        modifier: Modifier = Modifier,
        orderViewModel:OrderViewModel,
        onOrderButtonClicked: () -> Unit={}
    ){


        val pizze by viewModel.numberOfPizzaToOrder.collectAsStateWithLifecycle()
        val customOrderSentDialog =  remember { mutableStateOf(false) }

        if(customOrderSentDialog.value) {
            CustomDialogDatabaseResponse(setShowDialog = {
                customOrderSentDialog.value = it
            })
        }

        val showDialog =  remember { mutableStateOf(false) }
        var minusButtonColors: ButtonColors = ButtonDefaults.buttonColors()
        println()
        if(pizze == 1){
            minusButtonColors = ButtonDefaults.buttonColors(containerColor = Color.LightGray)
        }
        var plusButtonColors: ButtonColors = ButtonDefaults.buttonColors()
        if(pizze == 3){
            plusButtonColors = ButtonDefaults.buttonColors(containerColor = Color.LightGray)
        }
        if(showDialog.value) {
            CustomDialog(
                setShowDialog = { showDialog.value = it },
                sendOrder = {
                    onOrderButtonClicked()
                    customOrderSentDialog.value = true
                },
                pizze,
                pizzaName
            )
        }
        Column (
            modifier = modifier
                .clip(RoundedCornerShape(50.dp, 50.dp, 0.dp, 0.dp))
                .background(Color.White)
                .height(
                    LocalConfiguration.current.screenHeightDp.dp
                        .div(20)
                        .times(11)
                )
                .fillMaxWidth()
                .padding(20.dp, top = 175.dp)
        ){
            Spacer(modifier = modifier.height(20.dp))
            Row(
                modifier = modifier
            ) {
                Column(
                    modifier = modifier
                        .padding(end = 10.dp)
                ) {
                    Text(
                        text = "Quantità: ",
                        modifier = modifier
                            .padding(start = 20.dp)
                            .height(45.dp)
                    )
                    Text(
                        text = "Orario: ",
                        modifier = modifier
                            .padding(start = 20.dp)
                            .height(45.dp)
                    )
                    Text(
                        text = "Totale: ",
                        modifier = modifier
                            .padding(start = 20.dp)
                            .height(45.dp)
                    )
                }
                Column {
                    Row {
                        Button(
                            colors = minusButtonColors,
                            onClick = {
                                viewModel.decreaseNumberOfPizza()
                            },
                            shape = RoundedCornerShape(5.dp),
                            content = {
                                Image(
                                    painter = painterResource(id = R.drawable.minus_icon),
                                    contentDescription = "Add pizza",
                                    modifier = modifier.size(15.dp)
                                )
                            },
                            contentPadding = PaddingValues(),
                            modifier = modifier
                                .size(30.dp)
                        )
                        Text(
                            text = pizze.toString(),
                            fontWeight = FontWeight.Bold,
                            modifier = modifier
                                .padding(10.dp, 0.dp)
                                .align(Alignment.CenterVertically)
                        )
                        Button(
                            colors = plusButtonColors,
                            onClick = { viewModel.increaseNumberOfPizza() },
                            shape = RoundedCornerShape(5.dp),
                            content = {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Add pizza",
                                    modifier = modifier
                                )
                            },
                            contentPadding = PaddingValues(),
                            modifier = modifier
                                .size(30.dp)
                        )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = modifier
                            .padding(0.dp, 15.dp)

                    ) {
                        PickerExample(orderViewModel)
                    }
                    val cost = 4.40 * pizze
                    Text(
                        text = "€ " +String.format("%.2f", cost),
                        fontWeight = FontWeight.Bold,
                        modifier = modifier
                            .padding(10.dp, 0.dp)
                    )
                }
            }

            Button(
                content = {
                    Text(text = "ORDINA") //TODO: CHANGE THE DIMENSION
                },
                onClick = {
                    showDialog.value = true
                          //onOrderButtonClicked()
                },
                shape = RoundedCornerShape(10.dp),
                modifier = modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, end = 30.dp)
            )
        }
    }
}