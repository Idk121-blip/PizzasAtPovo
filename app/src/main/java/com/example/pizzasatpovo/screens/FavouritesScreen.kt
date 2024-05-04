package com.example.pizzasatpovo.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.pizzasatpovo.data.NavigationViewModel
import com.example.pizzasatpovo.data.PizzaViewModel
import com.example.pizzasatpovo.data.RetrievedPizza
import com.example.pizzasatpovo.ui.components.Allergen
import com.example.pizzasatpovo.ui.components.BackgroundImage
import com.example.pizzasatpovo.ui.components.Bars

class FavouritesScreen {
    @Composable
    fun FavouritesPage(
        navViewModel: NavigationViewModel,
        onAddToFavouritesClicked:(String)->Unit={},//TODO: maybe add a screen when clicked?
        onRemoveFromFavouritesClicked:(String)->Unit={},
        viewModel: PizzaViewModel,
        modifier: Modifier = Modifier
    ){
        BackgroundImage()

        Box(
            modifier = modifier
                .fillMaxSize()
        ) {
            Column {
                Bars().AppBar()
                ListOfPizzas(
                    viewModel = viewModel,
                    navViewModel = navViewModel,
                    onAddToFavouritesClicked = onAddToFavouritesClicked,
                    onRemoveFromFavouritesClicked = onRemoveFromFavouritesClicked
                )
            }
        }
        Bars().BottomBar(
            screen = PizzaScreens.Favourites,
            navViewModel = navViewModel,
        )
    }

    @Composable
    fun ListOfPizzas(
        viewModel: PizzaViewModel,
        navViewModel: NavigationViewModel,
        onAddToFavouritesClicked:(String)->Unit = {},
        onRemoveFromFavouritesClicked:(String)->Unit = {},
        modifier: Modifier = Modifier
    ){
        val favourites by viewModel.favourites.collectAsStateWithLifecycle()
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .fillMaxSize()
        ) {
            Column(
                modifier = modifier
                    .verticalScroll(rememberScrollState())
                    .padding(30.dp, 10.dp)
            ) {

                for (i in 0..<favourites.size) {
                    var x by remember {
                        mutableStateOf(true)
                    }
                    if (x) {
                        val retPizza = RetrievedPizza(
                            name = favourites[i].name,
                            toppings = favourites[i].toppings,
                            favourites[i].image
                        )
                        PizzaCard(
                            navViewModel = navViewModel,
                            pizza = favourites[i],
                            onNavbarButtonClicked = {
                                viewModel.setPizza(retPizza)
                                navViewModel.goToDetails()
                            },
                            onAddToFavouritesClicked =
                            {
                                onAddToFavouritesClicked(it)
                                viewModel.addToFavourites(retPizza)
                            },
                            onRemoveFromFavouritesClicked = {
                                onRemoveFromFavouritesClicked(it)
                                viewModel.removeFromFavourites(retPizza)
                                x = !x
                            }
                        )
                    }
                }
                Spacer(
                    modifier = modifier
                        .height(60.dp)
                )
            }
        }
    }

    @Composable
    fun PizzaCard(
        navViewModel: NavigationViewModel,
        pizza: RetrievedPizza,
        onNavbarButtonClicked: () -> Unit,
        onAddToFavouritesClicked:(String)->Unit = {},
        onRemoveFromFavouritesClicked:(String)->Unit = {},
        modifier: Modifier = Modifier
    ){
        var toppingForCard=""
        for (topping in pizza.toppings!!){
            toppingForCard= toppingForCard.plus(topping.name).plus(", ")
        }
        toppingForCard= toppingForCard.removeSuffix(", ")

        var favourite by remember { mutableStateOf(true) }

        val pizzaToppings = pizza.toppings

        var available = true
        var i = 0
        while(available && i < pizzaToppings.size){
            if(!pizzaToppings[i].availability){
                available = false
            }
            i++;
        }

        val allergens:ArrayList<String> = arrayListOf()
        for (topping in pizzaToppings){
            if (!allergens.contains(topping.allergens)){
                allergens.add(topping.allergens)
            }
        }

        val interactionSource = remember { MutableInteractionSource() }
        val context = LocalContext.current
        Card(
            shape = RoundedCornerShape(15.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White,
            ),
            modifier = modifier
                .width(350.dp)
                .height(140.dp)
                .padding(0.dp, 10.dp)
        ) {
            Box (
                modifier = modifier
                    .fillMaxWidth()
                    .padding(10.dp, 15.dp)
            ){
                Row(
                    modifier = modifier
                        .fillMaxWidth()
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) {
                            if (available) {
                                navViewModel.goToDetails()
                            } else {
                                Toast
                                    .makeText(
                                        context,
                                        "Pizza non disponibile",
                                        Toast.LENGTH_LONG
                                    )
                                    .show()
                            }

                        }
                ) {
                    AsyncImage(
                        model = pizza.image,
                        contentDescription = "pizza image",
                        colorFilter = if(available) {
                            ColorFilter
                                .colorMatrix(ColorMatrix().apply { setToSaturation(1f) })
                        }else {
                            ColorFilter
                                .colorMatrix(ColorMatrix().apply { setToSaturation(0f) })
                        },
                        modifier = modifier
                            .fillMaxHeight()
                            .padding(end = 15.dp)
                    )
                    Column {
                        Text(
                            text = pizza.name,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                        Text(
                            text = toppingForCard,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1,
                            fontSize = 16.sp
                        )
                        Row(
                            verticalAlignment = Alignment.Bottom,
                        ) {
                            if (allergens!= arrayListOf("")) {
                                for (allergen in allergens) {
                                    Allergen(
                                        modifier = modifier.align(Alignment.Bottom),
                                        allergen = allergen
                                    )
                                }
                            }
                            if(!available){
                                Text(
                                    text = "TERMINATO",
                                    color = Color.DarkGray,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.End,
                                    maxLines = 2,
                                    overflow = TextOverflow.Clip,
                                    modifier = modifier
                                        .align(Alignment.Bottom)
                                        .fillMaxWidth()
                                        .padding(end = 10.dp)
                                )
                            }
                        }
                    }
                }
                Image(
                    imageVector =  if(favourite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                    contentDescription = "Favourite icon",
                    modifier = modifier
                        .align(Alignment.TopEnd)
                        .clickable(
                            interactionSource = interactionSource,
                            indication = null
                        ) {
                            if (!favourite) {
                                onAddToFavouritesClicked(pizza.name)
                            } else {
                                onRemoveFromFavouritesClicked(pizza.name)
                            }
                            favourite = !favourite
                        }
                )
            }
        }
    }
}