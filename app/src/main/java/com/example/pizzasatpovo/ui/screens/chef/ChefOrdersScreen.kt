package com.example.pizzasatpovo.ui.screens.chef
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pizzasatpovo.R
import com.example.pizzasatpovo.data.model.RealTimeOrder
import com.example.pizzasatpovo.data.viewmodel.ChefViewModel
import com.example.pizzasatpovo.data.viewmodel.LoadingViewModel
import com.example.pizzasatpovo.ui.components.BackgroundImage
import com.example.pizzasatpovo.ui.components.Bars
import com.example.pizzasatpovo.ui.components.LogoutButton
import kotlinx.coroutines.delay
import java.util.Calendar

class ChefOrdersScreen {
    @Composable
    fun ChefOrdersPage(
        processOrder: (String)->Unit,
        onLogOutButtonClicked: () -> Unit,
        onResetButtonClicked: () -> Unit,
        modifier:Modifier = Modifier,
    ) {
        val viewModel = viewModel<ChefViewModel>()
        val chefViewModel = viewModel<LoadingViewModel>()
        val orders by viewModel.orders.observeAsState(initial = arrayListOf())

        Box {
            BackgroundImage()
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = modifier
                    .padding(40.dp, 10.dp)
            ) {
                Box(
                    modifier = modifier
                        .height(40.dp)
                ) {
                    Bars().AppBar(
                        fontSize = 22.sp,
                        modifier = modifier
                    )
                    LogoutButton(
                        onLogOutButtonClicked = onLogOutButtonClicked,
                        viewModel = chefViewModel,
                        modifier = modifier
                            .align(Alignment.CenterEnd)
                    )
                }
                var i = 0
                while (i<orders.size&&orders[i].completed) {
                    i++
                }
                val calendar = Calendar.getInstance()
                val groupedOrders = groupOrdersByTime(orders)

                var hourNow by remember {
                    mutableStateOf(calendar.get(Calendar.HOUR_OF_DAY))
                }
                var minutesNow by remember {
                    mutableStateOf(calendar.get( Calendar.MINUTE))
                }
                // Update the current time every minute
                LaunchedEffect(Unit) {
                    while (true) {
                        delay(60000L) // Delay for 1 minute
                        val newCalendar = Calendar.getInstance()
                        hourNow = newCalendar.get(Calendar.HOUR_OF_DAY)
                        minutesNow = newCalendar.get(Calendar.MINUTE)
                    }
                }


                LazyColumn {
                    groupedOrders.forEach { (time, orders) ->
                        val allOrdersCompleted = orders.all { it.completed }
                        if (!allOrdersCompleted) {
                            item {
                                Row (verticalAlignment = Alignment.CenterVertically){
                                    val t= time.split(":")
                                    Text(
                                        text = "$time - ",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 25.sp,
                                        modifier = modifier
                                            .padding(0.dp, 2.dp)
                                    )
                                    val hour = if(t.isEmpty() || t[0] == ""){
                                        0
                                    } else {
                                        (t[0].toInt() - hourNow)*60
                                    }

                                    val minutes = if(t.isEmpty() || t[0] == ""){
                                        hour
                                    } else {
                                        if (t[1].toInt()>=minutesNow){
                                            hour + t[1].toInt() - minutesNow
                                        }
                                        else{
                                            (hour - minutesNow) + t[1].toInt()
                                        }
                                    }
                                    println("Hour: $hour")
                                    println("Minutes: $minutes")

                                    Text(
                                        text = if(minutes > 0){
                                            "${minutes} minuti rimanenti"
                                        } else {
                                            "Scaduto ${-(minutes)} minuti fa"
                                        },
                                        color = Color.Red,
                                        fontSize = 20.sp,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                            items(orders) { order ->
                                println(order)
                                SingleOrderCard(order = order,
                                    processOrder = { processOrder(order.id) }
                                )
                            }

                        }

                    }
                    item {
                        ResetSlot(
                            onResetButtonClicked = { onResetButtonClicked() },
                            modifier = modifier
                                .fillMaxWidth()
                        )
                    }
                }

            }
        }
    }
    private fun groupOrdersByTime(orders: List<RealTimeOrder>): Map<String, List<RealTimeOrder>> {
        return orders.groupBy { it.time }
    }

    @Composable
    fun SingleOrderCard(
        modifier : Modifier = Modifier,
        processOrder: ()->Unit,
        order: RealTimeOrder = RealTimeOrder(uname = "NoOne", pizzaName = "Margherita", image = "", time = "07:30", pizzaNumber = 1, topping = arrayListOf("Pomodoro", "Mozzarella", "Prosciutto")),
    ){
        if (order.completed) return
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
                            text = order.pizzaName+"  -  "+ order.uname,
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
                        onClick = {processOrder() },
                        content = {
                            Icon(
                                imageVector = Icons.Rounded.Done,
                                contentDescription = stringResource(R.string.pizza_completed),
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

    @Composable
    fun ResetSlot(
        onResetButtonClicked: () -> Unit,
        modifier: Modifier = Modifier
    ){

        Button(
            onClick = onResetButtonClicked,
            modifier = modifier
                .padding(450.dp, 25.dp)
                .height(50.dp)
        ) {
            Text(text = stringResource(R.string.azzera_slot_pizze), fontSize = 18.sp)
        }
    }
}