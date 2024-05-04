package com.example.pizzasatpovo.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.pizzasatpovo.R
import com.example.pizzasatpovo.data.NavigationViewModel
import com.example.pizzasatpovo.data.RetrievedPizza
import com.example.pizzasatpovo.presentation.sign_in.SignInViewModel
import com.example.pizzasatpovo.ui.components.BackgroundImage
import com.example.pizzasatpovo.ui.components.Bars

class OrdersScreen {
    @Composable
    fun OrdersPage(
        navController: NavigationViewModel,
        onHomeButtonClicked: () -> Unit = {},
        onAddPizzaButtonClicked: () -> Unit = {},
        viewModel: SignInViewModel,
        modifier: Modifier = Modifier
    ){
        Box(modifier = modifier
            .fillMaxSize()
        ) {
            BackgroundImage()
            Column () {
                Bars().AppBar(text = "Ordini")
                ListOfOrders(
                    viewModel = viewModel,
                    modifier = modifier
                        .fillMaxHeight()
                )
            }
            Bars().BottomBar(
                navViewModel = navController,
                screen = PizzaScreens.RecentOrders
            )
        }
    }

    @Composable
    @Preview(showBackground = true)
    fun ListOfOrders(
        pizzas: ArrayList<RetrievedPizza> = arrayListOf(),
        viewModel: SignInViewModel = SignInViewModel(),
        modifier: Modifier = Modifier
    ){
        var pizzas: ArrayList<RetrievedPizza> = arrayListOf()
        var verticalArrangement: Arrangement.Vertical = Arrangement.Center
        var horizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally
        println("Size array list: " + pizzas.size)
        if(pizzas.size == 0){
            verticalArrangement = Arrangement.Top
        }
        Column (
            verticalArrangement = verticalArrangement,
            horizontalAlignment = horizontalAlignment,
            modifier = modifier
                .verticalScroll(rememberScrollState())
                .padding(30.dp, 10.dp)
                .fillMaxSize()
        ){
//            pizzas.add(
//                RetrievedPizza(
//                    name = "Margherita",
//                    image = "https://www.wanmpizza.com/wp-content/uploads/2022/09/pizza.png",
//
//                )
//            )
            if(pizzas.size != 0){
                Text(
                    text = "Non hai ancora effettuato",
                    fontSize = 20.sp,
                    modifier = modifier
                )
                Text(
                    text = "alcun ordine",
                    fontSize = 20.sp,
                    modifier = modifier
                )
                Image(
                    painter = painterResource(id = R.drawable.margherita),
                    contentDescription = "No order found",
                    modifier = modifier
                        .size(150.dp)
                )
                return
            } else {
                var pizza = 0;
                Text(
                    text = "Recenti",
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Start,
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                )
                while (pizza<=3  /*TODO! and check date */) {
                    SingleOrderCard(
                        //TODO! check names
                        image = "https://www.wanmpizza.com/wp-content/uploads/2022/09/pizza.png",
                        name = "Margherita",
//                        image = pizzas[i].image,
//                        name = pizzas[i].name,
//                        pizza = RetrievedPizza("Margherita", image = "", toppings = arrayListOf()),
//                        viewModel = viewModel
                    )
                    pizza++;
                }
                Text(
                    text = "Meno recenti",
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Start,
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(top=10.dp)
                )
                while (pizza<=5  /*TODO! and check date */) {
                    SingleOrderCard(
                        //TODO! check names
                        image = "https://www.wanmpizza.com/wp-content/uploads/2022/09/pizza.png",
                        name = "Margherita",
//                        image = pizzas[i].image,
//                        name = pizzas[i].name,
//                        pizza = RetrievedPizza("Margherita", image = "", toppings = arrayListOf()),
//                        viewModel = viewModel
                    )
                    pizza++;
                }

                Spacer(
                    modifier = modifier
                        .height(60.dp)
                )
            }
        }
    }

    @Composable
    fun SingleOrderCard(
        image: String,
        name: String,
        modifier: Modifier = Modifier
    ){
        Card (
            shape = RoundedCornerShape(15.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White,
            ),
            modifier = modifier
                .fillMaxWidth()
                .height(140.dp)
                .padding(0.dp, 10.dp)
        ){
            Row (
                verticalAlignment = Alignment.CenterVertically,
                modifier = modifier
                    .fillMaxWidth()
                    .padding(20.dp, 10.dp)
            ){
                AsyncImage(
                    model = image,
                    contentDescription = "pizza image",
                    modifier= modifier
                        .fillMaxHeight()
                        .padding(end = 15.dp)
                )
                Column {
                    Text(
                        text = "$name",
                        fontWeight = FontWeight.Bold,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                        fontSize = 18.sp
                    )
                    Text(
                        text = "01/02/2023",
                        fontSize = 16.sp,
                    )
                }
                Text(
                    text = "$ 4.40",
                    textAlign = TextAlign.End,
                    fontWeight = FontWeight.Bold,
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(end = 20.dp)
                )
            }
        }
    }
}