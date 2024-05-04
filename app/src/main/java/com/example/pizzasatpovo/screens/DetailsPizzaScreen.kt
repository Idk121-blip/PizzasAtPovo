package com.example.pizzasatpovo.screens

import PickerExample
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
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
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.pizzasatpovo.R
import com.example.pizzasatpovo.data.Pizza
import com.example.pizzasatpovo.data.PizzaViewModel
import com.example.pizzasatpovo.data.RetrievedPizza
import com.example.pizzasatpovo.ui.components.Allergen
import com.example.pizzasatpovo.ui.components.Bars

class DetailsPizzaScreen {
    @Composable
    fun DetailsPizzaPage(
        pizza: RetrievedPizza,
        onBackButtonClicked: () -> Unit = {},
        onOrderButtonClicked: () -> Unit={},
        viewModel: PizzaViewModel,
        modifier: Modifier = Modifier
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
                    onBackButtonClicked = onBackButtonClicked
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
                        Allergen()
                        Allergen()
                        Allergen()
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
                orderDetails(
                    onOrderButtonClicked= onOrderButtonClicked,
                    pizza_name = pizza.name,
                    viewModel = viewModel
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
    fun orderDetails(
        title: String = "",//Sembra inutile
        onOrderButtonClicked: () -> Unit={},
        pizza_name:String,
        viewModel: PizzaViewModel,
        modifier: Modifier = Modifier
    ){

        val pizze by viewModel.numberOfPizzaToOrder.collectAsStateWithLifecycle()
        //val nPizzas by remember { mutableIntStateOf(pizze) }

        val customOrderSentDialog =  remember { mutableStateOf(false) }

        if(customOrderSentDialog.value) {
            CustomDialogDatabaseResponse(setShowDialog = {
                customOrderSentDialog.value = it
            })
        }



        val showDialog =  remember { mutableStateOf(false) }

        if(showDialog.value) {
            CustomDialog(setShowDialog = {
                showDialog.value = it
            }, sendOrder = {
                onOrderButtonClicked()
                customOrderSentDialog.value = true
            }, pizze, pizza_name )
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

//                if(title != ""){
//                    Text(
//                        text = title,
//                        fontWeight = FontWeight.Bold,
//                        modifier = modifier
//                            .padding(bottom = 20.dp)
//                            //.fillMaxWidth()
//                            .align(Alignment.CenterHorizontally)
//                    )
//                }else{
                    Spacer(modifier = modifier.height(20.dp))
//                }
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
                                onClick = { viewModel.decreaseNumberOfPizza() },
                                shape = RoundedCornerShape(5.dp),
                                content = {
                                    Icon(
                                        imageVector = Icons.Default.ArrowDropDown,
                                        contentDescription = "Add pizza",
                                        modifier = modifier
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
                            PickerExample()
                        }
                        val cost = 4.40F * pizze
                        Text(
                            text = "€ $cost",
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


    fun addAnimation(
        duration: Int = 800,
        modifier: Modifier = Modifier
    ): ContentTransform {
        return slideInVertically(animationSpec = tween(durationMillis = duration)){
            height -> height
        } + fadeIn(animationSpec = tween(durationMillis = duration)
        ) togetherWith slideOutVertically(animationSpec = tween(durationMillis = duration)){
                height -> -height
        } + fadeOut(animationSpec = tween(durationMillis = duration))
    }
}