package com.example.pizzasatpovo.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pizzasatpovo.data.ChefViewModel
import com.example.pizzasatpovo.data.LoadingViewModel
import com.example.pizzasatpovo.data.RealTimeOrder
import com.example.pizzasatpovo.ui.components.BackgroundImage
import com.example.pizzasatpovo.ui.components.Bars
import com.example.pizzasatpovo.ui.components.LogoutButton

class ChefOrdersScreen {
    @Composable
    fun ChefOrdersPage(
        processOrder: (String)->Unit,
        onLogOutButtonClicked: ()->Unit,
        modifier:Modifier = Modifier,
    ){
        val viewModel = viewModel<LoadingViewModel>()
        Box {
            BackgroundImage()
            Column() {
                val chefViewModel = viewModel<ChefViewModel>()
                val orders by chefViewModel.orders.observeAsState(
                    initial = arrayListOf(
                        RealTimeOrder()
                    )
                )
                Box {
                    Bars().AppBar("Chef page");
                    LogoutButton(
                        onLogOutButtonClicked = onLogOutButtonClicked,
                        viewModel= viewModel
                    )
                }
                LazyColumn {
                    items(orders) { order ->
                        SingleOrderCard(order = order, processOrder = { processOrder(order.id) })
                    }
                }
            }
        }

    }

    @Composable
    fun SingleOrderCard(
        modifier : Modifier = Modifier,
        processOrder: ()->Unit,
        order: RealTimeOrder = RealTimeOrder(uname = "NoOne", pizzaName = "Margherita", image = "", time = "07:30", pizzaNumber = 1, topping = arrayListOf("Pomodoro", "Mozzarella", "Prosciutto")),
    ){
        Card (
            shape = RoundedCornerShape(15.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White,
            ),
            modifier = modifier
                .fillMaxWidth()
                .padding(30.dp, 10.dp)
        ){
            Box {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(15.dp)
                ) {
                    Column(
                        modifier = modifier
                            .weight(0.8F)
                    ) {
                        Text(
                            text = order.pizzaName,
                            fontWeight = FontWeight.Bold,
                            overflow = TextOverflow.Clip,
                            maxLines = 3,
                            fontSize = 18.sp,
                            modifier = modifier
                                .padding(5.dp)
                        )
                        Text(
                            text = order.topping.toString(),
                            fontSize = 16.sp,
                            modifier = modifier.padding(5.dp)
                        )
                    }
                    Button(
                        onClick = {
                            processOrder()
                                  },
                        content = {
                            Icon(
                                imageVector = Icons.Rounded.Done,
                                contentDescription = "Pizza completed",
                                modifier = modifier
                                    .size(40.dp)
                            )
                        },
                        shape = CircleShape,
                        contentPadding = PaddingValues(0.dp),
                        colors = ButtonDefaults
                            .buttonColors(containerColor = Color.Green),
                        modifier = modifier
                            .size(60.dp)
                    )
                }
            }
        }
    }
}