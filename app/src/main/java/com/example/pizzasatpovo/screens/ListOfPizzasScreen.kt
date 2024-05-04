package com.example.pizzasatpovo.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.pizzasatpovo.R
import com.example.pizzasatpovo.data.Pizza
import com.example.pizzasatpovo.data.PizzaViewModel
import com.example.pizzasatpovo.data.RetrievedPizza
import com.example.pizzasatpovo.ui.components.Allergen
import com.example.pizzasatpovo.ui.components.Bars
import com.example.pizzasatpovo.data.Topping

class ListOfPizzasScreen() {
//    private val sizeTitle: TextUnit = 50.sp
//    private val sizeSubtitle: TextUnit = 35.sp
//    private val sizeText: TextUnit = 40.sp
    private val weightText: FontWeight = FontWeight.Bold
//    private val uniColor: Color =  Color(0xffce0e2d)
//    private val dimIcons: Dp = 30.dp

    @Composable
    fun ListOfPizzasPage(
        onDetailButtonClicked: () -> Unit = {},
        onOrdersButtonClicked: () -> Unit = {},
        onProfileButtonClicked: () -> Unit = {},
        onAddPizzaButtonClicked: () -> Unit = {},
        onFavouritesButtonClicked: () -> Unit = {},
        onAddToFavouritesClicked:(String)->Unit={},//TODO: maybe add a screen when clicked?
        onRemoveFromFavouritesClicked:(String)->Unit={},
        pizzas: ArrayList<Pizza> = arrayListOf(),
        toppings: ArrayList<ArrayList<Topping>> = arrayListOf(arrayListOf()),
        viewModel: PizzaViewModel,
        modifier: Modifier = Modifier,

        ){

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
                    SearchBar(
                        modifier = modifier
                            .padding(10.dp)
                    )
                    ListOfPizzas(
                        onDetailButtonClicked,
                        pizzas,
                        toppings,
                        viewModel,
                        onAddToFavouritesClicked = onAddToFavouritesClicked,
                        onRemoveFromFavouritesClicked = onRemoveFromFavouritesClicked
                    )
                }
            }
        }
        Bars().BottomBar(
            screen = PizzaScreens.ListOfPizzas,
            onProfileButtonClicked = onProfileButtonClicked,
            onAddPizzaButtonClicked = onAddPizzaButtonClicked,
            onOrdersButtonClicked = onOrdersButtonClicked,
            onFavouritesButtonClicked = onFavouritesButtonClicked
        )
    }

    @Composable
    fun SearchBar(modifier: Modifier = Modifier){
        var text by remember{ mutableStateOf("") }

        OutlinedTextField(
            value = text,
            onValueChange = {
                text = it
            },
            leadingIcon = { Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = "Search"
            ) },
            textStyle = TextStyle(
                textAlign = TextAlign.Start
            ),
            shape = RoundedCornerShape(50.dp),
            label = null,
            placeholder = { Text(text = "Cerca ...") },
            singleLine = true,
            modifier = modifier
                .clip(CircleShape)
                .background(Color.White)
                //.height(40.dp)
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
        val favouritesName: ArrayList<String> = arrayListOf()
        for (favourite in favourites){
            favouritesName.add(favourite.name)
        }

        Column (
            modifier = modifier
                .verticalScroll(rememberScrollState())
                .padding(30.dp, 10.dp)
        ){

            for (i in 0..<pizzas.size)
            {
                PizzaCard(
                    //TODO! check names
                    onNavbarButtonClicked = {
                        viewModel.setPizza(RetrievedPizza(name= pizzas[i].name, image = pizzas[i].image, toppings = toppings[i]))
                        onDetailsButtonClicked()
                    },
                    image = pizzas[i].image,
                    name = pizzas[i].name,
                    toppings = toppings[i],
                    isFavourite =  favouritesName.contains(pizzas[i].name),
                    onAddToFavouritesClicked =
                    {
                        onAddToFavouritesClicked(it)
                        val retPizza= RetrievedPizza(name= pizzas[i].name, toppings= toppings[i], pizzas[i].image)
                        viewModel.addToFavourites(retPizza)
                    },
                    onRemoveFromFavouritesClicked = {
                        onRemoveFromFavouritesClicked(it)
                        val retPizza= RetrievedPizza(name= pizzas[i].name, toppings= toppings[i], pizzas[i].image)
                        viewModel.removeFromFavourites(retPizza)
                    }
                )
            }
            Spacer(
                modifier = modifier
                    .height(60.dp)
            )
        }
    }

    @Composable
    fun PizzaCard(
        onNavbarButtonClicked: () -> Unit,
        onAddToFavouritesClicked:(String)->Unit = {},
        onRemoveFromFavouritesClicked:(String)->Unit = {},
        image: String,
        name: String,
        toppings: ArrayList<Topping>,
        isFavourite: Boolean= false,
        modifier: Modifier = Modifier
    ){
        var toppingForCard=""
        for (topping in toppings){
            toppingForCard= toppingForCard.plus(topping.name).plus(", ")
        }
        toppingForCard= toppingForCard.removeSuffix(", ")
        val interactionSource = remember { MutableInteractionSource() }

        val allergens:ArrayList<String> = arrayListOf()

        for (topping in toppings){
            if (!allergens.contains(topping.allergens)){
                allergens.add(topping.allergens)
            }
        }




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
            var favourite by remember {mutableStateOf(isFavourite)}
            Box (
                modifier = modifier
                    .fillMaxWidth()
                    .padding(10.dp, 15.dp)
            ){

                Row(
                    modifier = modifier
                        .fillMaxWidth()
                        .clickable (
                            interactionSource = interactionSource,
                            indication = null
                        ) {
                            onNavbarButtonClicked()
                        }
                ) {
                    AsyncImage(
                        model = image, contentDescription = "pizza image", modifier = modifier
                            .fillMaxHeight()
                            .padding(end = 15.dp)
                    )
                    Column {
                        Text(
                            text = name,
                            fontWeight = weightText,
                            fontSize = 18.sp
                        )
                        Text(
                            text = toppingForCard,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1,
                            fontSize = 16.sp
                        )
                        Row(
                            modifier = modifier
                                .fillMaxSize()
                        ) {
                            for (allergen in allergens){
                                Allergen(
                                    modifier = modifier.align(Alignment.Bottom)
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
                                onAddToFavouritesClicked(name)
                            } else {
                                onRemoveFromFavouritesClicked(name)
                            }

                            favourite = !favourite
                        }


                )
            }
        }
    }
}