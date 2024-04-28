package com.example.pizzasatpovo.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pizzasatpovo.R
import com.example.pizzasatpovo.screens.PizzaScreens

class Bars() {
    private val weightText: FontWeight = FontWeight.Bold
    private val dimIcons: Dp = 25.dp
    @Composable
    fun AppBar(text:String= "Pizza at Povo", modifier: Modifier = Modifier){
        Column (
            modifier = modifier
                .padding(0.dp, 15.dp, 0.dp, 5.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = text,
                fontSize = 20.sp,
                fontWeight = weightText,
                textAlign = TextAlign.Center,
                modifier = modifier
                    .align(Alignment.CenterHorizontally)
            )
        }
    }

    @Composable
    fun AppBarWithBackBtn(
        pizzasName: String,
        onBackButtonClicked: () -> Unit = {},
        modifier: Modifier = Modifier
    ) {
        Column(
            modifier = modifier
                .padding(0.dp, 15.dp, 0.dp, 5.dp)
                .fillMaxWidth()
        ) {
            Row (
                verticalAlignment = Alignment.CenterVertically,
                modifier = modifier
                    .padding(0.dp, 5.dp)
            ){
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = "Back to login",
                    modifier = Modifier
                        .height(48.dp)
                        .weight(0.15F)
                        .fillMaxWidth()
                        .clickable { onBackButtonClicked() }
                )
                Text(
                    text = pizzasName,
                    fontSize = 20.sp,
                    fontWeight = weightText,
                    textAlign = TextAlign.Center,
                    modifier = modifier
                        .weight(0.7F)
                )
                Spacer(
                    modifier = modifier
                        .weight(0.15F)
                )
            }
        }
    }

    @Composable
    fun BottomBar(
        screen: PizzaScreens,
        onHomeButtonClicked: () -> Unit = {},
        onProfileButtonClicked: () -> Unit={},
        onFavouritesButtonClicked:()->Unit = {},
        onAddPizzaButtonClicked:()->Unit={},
        onOrdersButtonClicked:()->Unit={},
        modifier: Modifier = Modifier
    ){
        Box(
            modifier = modifier
                .fillMaxWidth()
                .fillMaxSize()
        ) {
            Box {
                Image(
                    painter = painterResource(id = R.drawable.nav_bar),
                    contentDescription = "NavBar",
                    alignment = Alignment.BottomCenter,
                    modifier = modifier
                        .fillMaxSize()
                )
            }
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .padding(5.dp)
            ) {
                Navbar(
                    screen = screen,
                    onHomeButtonClicked = onHomeButtonClicked,
                    onProfileButtonClicked= onProfileButtonClicked,
                    onFavouritesButtonClicked=onFavouritesButtonClicked,
                    onAddPizzaButtonClicked=onAddPizzaButtonClicked,
                    onOrdersButtonClicked=onOrdersButtonClicked,
                    modifier = modifier
                        .align(Alignment.BottomCenter)
                        .padding(0.dp, 10.dp)
                )
            }
        }
    }

    @Composable
    fun Navbar(
        screen: PizzaScreens,
        onHomeButtonClicked: () -> Unit = {},
        onProfileButtonClicked: () -> Unit= {},
        onFavouritesButtonClicked:()->Unit = {},
        onAddPizzaButtonClicked:()->Unit={},
        onOrdersButtonClicked:()->Unit={},
        modifier : Modifier = Modifier
    ){
        Row(
            //verticalAlignment = Alignment.Bottom,
            modifier = modifier
                .fillMaxWidth()
                .padding(1.dp, 0.dp)
        ) {
            Image(
                painter = painterResource(
                    id = if(screen == PizzaScreens.ListOfPizzas) {
                        R.drawable.pizzas_selected
                    }else{ R.drawable.pizzas}
                ),
                contentDescription = "List of pizzas",
                modifier = Modifier
                    .size(dimIcons)
                    .weight(0.1F)
                    .clickable {
                        onHomeButtonClicked()
                    }
            )
            Image(
                painter = painterResource(
                    id = if(screen == PizzaScreens.Favourites) {
                        R.drawable.favourites_selected
                    }else{ R.drawable.favourites}
                ),
                contentDescription = "Favourites",
                modifier = Modifier
                    .size(dimIcons)
                    .weight(0.1F)
                    .clickable { onFavouritesButtonClicked() }
            )
            Spacer(
                modifier = modifier
                    .width(50.dp)
            )
            Image(
                painter = painterResource(
                    id = if(screen == PizzaScreens.RecentOrders) {
                        R.drawable.orders_selected
                    }else{ R.drawable.orders}
                ),
                contentDescription = "Recent orders",
                modifier = Modifier
                    .size(dimIcons)
                    .weight(0.1F)
                    .clickable { onOrdersButtonClicked() }
            )
            Image(
                painter = painterResource(
                    id = if(screen == PizzaScreens.Account) {
                        R.drawable.account_selected
                    }else{ R.drawable.account}
                ),
                contentDescription = "Account",
                modifier = Modifier
                    .size(dimIcons)
                    .weight(0.1F)
                    .clickable {
                        onProfileButtonClicked()
                    }
            )

        }
        Button(
            shape = CircleShape,
            content = {
                Image(
                    painter = painterResource(id = R.drawable.plus_icon),
                    contentDescription = "Add pizza",
                    contentScale = ContentScale.Fit,
                    modifier = modifier
                )
            },
            onClick = { onAddPizzaButtonClicked() },
            contentPadding = PaddingValues(),
            modifier = modifier
                .size(50.dp)
                .offset(0.dp, (-15).dp)

        )

    }
}
