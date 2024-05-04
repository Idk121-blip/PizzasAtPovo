package com.example.pizzasatpovo.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.ColorMatrixColorFilter
import androidx.compose.ui.graphics.vector.DefaultTintColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.pizzasatpovo.R
import com.example.pizzasatpovo.data.Pizza
import com.example.pizzasatpovo.data.PizzaViewModel
import com.example.pizzasatpovo.data.RetrievedPizza
import com.example.pizzasatpovo.data.Topping
import com.example.pizzasatpovo.ui.components.Allergen
import com.example.pizzasatpovo.ui.components.Bars

class FavouritesScreen {
    @Composable
    fun FavouritesPage(
        onOrdersButtonClicked: () -> Unit = {},
        onProfileButtonClicked: () -> Unit = {},
        onDetailButtonClicked: () -> Unit = {},
        onAddPizzaButtonClicked: () -> Unit = {},
        onHomeButtonClicked: () -> Unit = {},
        onAddToFavouritesClicked:(String)->Unit={},//TODO: maybe add a screen when clicked?
        onRemoveFromFavouritesClicked:(String)->Unit={},
        pizzas: ArrayList<Pizza> = arrayListOf(),
        toppings: ArrayList<ArrayList<Topping>> = arrayListOf(arrayListOf()),
        viewModel: PizzaViewModel,
        modifier: Modifier = Modifier
    ){
        val favourites by viewModel.favourites.collectAsStateWithLifecycle()
        Box(modifier = modifier
            .fillMaxSize()
        ){
            Image(
                painter = painterResource(id = R.drawable.background),
                contentDescription = "Background image",
                contentScale = ContentScale.FillBounds,
                alpha = 0.5F,
                modifier = modifier
                    .fillMaxSize()
            )
        }

        Box(
            modifier = modifier
                .fillMaxSize()
        ) {
            Column {
                Bars().AppBar()
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = modifier
                        .fillMaxSize()
                ) {
                    ListOfPizzas(
                        pizzas = pizzas,
                        toppings = toppings,
                        viewModel = viewModel,
                        onDetailsButtonClicked = onDetailButtonClicked,
                        onAddToFavouritesClicked = onAddToFavouritesClicked,
                        onRemoveFromFavouritesClicked = onRemoveFromFavouritesClicked
                    )
                }
            }
        }
        Bars().BottomBar(
            screen = PizzaScreens.Favourites,
            onProfileButtonClicked = onProfileButtonClicked,
            onAddPizzaButtonClicked = onAddPizzaButtonClicked,
            onOrdersButtonClicked = onOrdersButtonClicked,
            onHomeButtonClicked = onHomeButtonClicked
        )
    }

    @Composable
    fun ListOfPizzas(
        onDetailsButtonClicked: () -> Unit = {},
        pizzas: ArrayList<Pizza>,
        toppings: ArrayList<ArrayList<Topping>> = arrayListOf(arrayListOf()),
        viewModel: PizzaViewModel,
        onAddToFavouritesClicked:(String)->Unit = {},
        onRemoveFromFavouritesClicked:(String)->Unit = {},
        modifier: Modifier = Modifier
    ){
        val favourites by viewModel.favourites.collectAsStateWithLifecycle()




        Column (
            modifier = modifier
                .verticalScroll(rememberScrollState())
                .padding(30.dp, 10.dp)
        ){

            for (i in 0..<favourites.size)
            {
                var x by remember {
                    mutableStateOf(true)
                }
                if (x) {
                    PizzaCard(
                        //TODO! check names
                        pizza = favourites[i],
                        onNavbarButtonClicked = {
                            viewModel.setPizza(
                                RetrievedPizza(
                                    name = favourites[i].name,
                                    image = favourites[i].image,
                                    toppings = favourites[i].toppings
                                )
                            )

                            onDetailsButtonClicked()
                        },
                        viewModel = viewModel,
                        onAddToFavouritesClicked =
                        {
                            onAddToFavouritesClicked(it)
                            val retPizza = RetrievedPizza(
                                name = favourites[i].name,
                                toppings = favourites[i].toppings,
                                favourites[i].image
                            )
                            viewModel.addToFavourites(retPizza)
                        },
                        onRemoveFromFavouritesClicked = {
                            onRemoveFromFavouritesClicked(it)
                            val retPizza = RetrievedPizza(
                                name = favourites[i].name,
                                toppings = favourites[i].toppings,
                                favourites[i].image
                            )
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

    @Composable
    fun PizzaCard(
        pizza: RetrievedPizza,
        viewModel: PizzaViewModel,
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
            var favourite by remember { mutableStateOf(true) }
            val pizzaToppings = pizza.toppings
            val interactionSource = remember { MutableInteractionSource() }
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
//            println("Available: $available")



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
                                onNavbarButtonClicked()
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
                            allergens.forEach {allergen ->
                                Allergen(
                                    modifier = modifier.align(Alignment.Bottom),
                                    allergen = allergen
                                )
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