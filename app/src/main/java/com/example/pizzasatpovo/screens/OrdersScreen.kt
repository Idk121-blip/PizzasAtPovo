package com.example.pizzasatpovo.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.pizzasatpovo.R
import com.example.pizzasatpovo.data.viewModels.NavigationViewModel
import com.example.pizzasatpovo.data.dataModel.Order
import com.example.pizzasatpovo.data.viewModels.OrderListViewModel
import com.example.pizzasatpovo.database.OrderManager
import com.example.pizzasatpovo.ui.components.BackgroundImage
import com.example.pizzasatpovo.ui.components.Bars
import com.example.pizzasatpovo.ui.components.shimmerBrush
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date

class OrdersScreen {
    @Composable
    fun OrdersPage(
        navController: NavigationViewModel,
        modifier: Modifier = Modifier,
        lifecycleScope: LifecycleCoroutineScope,
        orderManager: OrderManager
    ) {

        val ordersViewModel = viewModel<OrderListViewModel>()
        val navController2: NavHostController = rememberNavController()

        NavHost(
            navController = navController2,
            startDestination = "LoadingPage",
            modifier = modifier
        ) {
            composable(route = "LoadingPage") {

                LaunchedEffect(key1 = Unit) {

                    lifecycleScope.launch {
                        val orders = orderManager.retrieveOrders()
                        ordersViewModel.addOrders(orders!!.retrievedObject ?: arrayListOf())
                        //println(orders)
                        navController2.navigate("OrdersPage")
                    }

                }
                Box(
                    modifier = modifier
                        .fillMaxSize()
                ) {
                    BackgroundImage()
                    Column {
                        Bars().AppBar(
                            text = "Ordini",
                            modifier = modifier
                                .height(30.dp)
                        )
                        ListOfChargingOrders(
                            modifier = modifier
                                .fillMaxHeight(),

                            )
                    }
                    Bars().BottomBar(
                        navViewModel = navController,
                        screen = PizzaScreens.RecentOrders
                    )
                }


            }
            composable(route = "OrdersPage") {
                Box(
                    modifier = modifier
                        .fillMaxSize()
                ) {
                    BackgroundImage()
                    Column {
                        Bars().AppBar(
                            text = "Ordini",
                            modifier = modifier
                                .height(30.dp)
                        )
                        ListOfOrders(
                            modifier = modifier
                                .fillMaxHeight(),
                            ordersViewModel = ordersViewModel
                        )
                    }
                    Bars().BottomBar(
                        navViewModel = navController,
                        screen = PizzaScreens.RecentOrders
                    )
                }
            }
        }
    }

    @Composable
    fun ListOfChargingOrders(modifier: Modifier) {
        val verticalArrangement: Arrangement.Vertical = Arrangement.Center
        val horizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally
        Column(
            verticalArrangement = verticalArrangement,
            horizontalAlignment = horizontalAlignment,
            modifier = modifier
                .verticalScroll(rememberScrollState())
                .padding(30.dp, 10.dp)
                .fillMaxSize()
        ) {
            var order = 0
            Text(
                text = "Recenti",
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Start,
                modifier = modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
            )

            SingleOrderChargingCard()

            Text(
                text = "Meno recenti",
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Start,
                modifier = modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
            )
            while (order < 8) {
                SingleOrderChargingCard()
                order++
            }
            Spacer(
                modifier = modifier
                    .height(60.dp)
            )

        }
    }

    @SuppressLint("SimpleDateFormat")
    @Composable
    fun ListOfOrders(
        ordersViewModel: OrderListViewModel,
        modifier: Modifier = Modifier
    ) {
        val ordersList by ordersViewModel.orders.collectAsStateWithLifecycle()
        var verticalArrangement: Arrangement.Vertical = Arrangement.Center
        val horizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally
        println("Size array list: " + ordersList.size)
        if (ordersList.size != 0) {
            verticalArrangement = Arrangement.Top
        }
        Column(
            verticalArrangement = verticalArrangement,
            horizontalAlignment = horizontalAlignment,
            modifier = modifier
                .verticalScroll(rememberScrollState())
                .padding(30.dp, 10.dp)
                .fillMaxSize()
        ) {

            if (ordersList.size == 0) {
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
                var order = 0
                Text(
                    text = "Recenti",
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Start,
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                )
                while (order < ordersList.size &&
                    (SimpleDateFormat("dd.MM.yyyy")
                        .format(
                            ordersList[order]
                                .date.toDate()
                        ) >= SimpleDateFormat("dd.MM.yyyy").format(Date()))
                ) {
                    SingleOrderCard(
                        order = ordersList[order]
                    )
                    order++
                }
                Text(
                    text = "Meno recenti",
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Start,
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                )
                while (order < ordersList.size) {
                    SingleOrderCard(
                        order = ordersList[order],
                    )
                    order++
                }

                Spacer(
                    modifier = modifier
                        .height(60.dp)
                )
            }
        }
    }

    @Composable
    fun SingleOrderChargingCard(modifier: Modifier= Modifier) {
        Card(
            shape = RoundedCornerShape(15.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White,
            ),
            modifier = modifier
                .fillMaxWidth()
                .height(140.dp)
                .padding(0.dp, 10.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                AsyncImage(
                    model = "",
                    contentDescription = "pizza image",
                    modifier = modifier
                        .padding(end = 15.dp)
                        .clip(CircleShape)
                        .size(95.dp)
                        .background(
                            shimmerBrush(
                                targetValue = 1300f,
                                showShimmer = true
                            )
                        )
                )
                Column {
                    Text(
                        text = " ",
                        fontWeight = FontWeight.Bold,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                        fontSize = 18.sp,
                        modifier = modifier.width(90.dp).height(30.dp).background(
                                shimmerBrush(
                                    targetValue = 1300f,
                                    showShimmer = true
                                )
                        )
                    )
                    Text(
                        text = "                                ",
                        fontSize = 16.sp,
                        modifier= modifier.width(80.dp).height(30.dp).background(
                            shimmerBrush(
                                targetValue = 1300f,
                                showShimmer = true
                            )
                        )
                    )
                }
                Text(
                    text = "              ",
                    textAlign = TextAlign.End,
                    fontWeight = FontWeight.Bold,
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(start= 10.dp, end= 20.dp)
                        .height(30.dp)
                        .background(
                            shimmerBrush(
                                targetValue = 1300f,
                                showShimmer = true
                            )
                        )
                )
            }
        }
    }
    @SuppressLint("SimpleDateFormat")
    @Composable
    fun SingleOrderCard(
        order: Order,
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
                    .padding(10.dp)
            ){
                AsyncImage(
                    model = order.image,
                    contentDescription = "pizza image",
                    modifier= modifier
                        .fillMaxHeight()
                        .padding(end = 15.dp)
                )
                Column {
                    Text(
                        text = order.pizzaName,
                        fontWeight = FontWeight.Bold,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                        fontSize = 18.sp,
                        modifier = modifier.width(120.dp)
                    )
                    Text(
                        text = SimpleDateFormat("dd.MM.yyyy").format(order.date.toDate()),
                        fontSize = 16.sp,
                    )
                }
                Text(
                    text = "€ " +String.format("%.2f", order.price),
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