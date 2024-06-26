package com.example.pizzasatpovo.ui.screens.listofpizza

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.pizzasatpovo.R
import com.example.pizzasatpovo.data.viewmodel.NavigationViewModel
import com.example.pizzasatpovo.data.viewmodel.PizzaViewModel
import com.example.pizzasatpovo.data.model.RetrievedPizza
import com.example.pizzasatpovo.ui.components.Allergen
import com.example.pizzasatpovo.ui.components.Bars
import com.example.pizzasatpovo.ui.components.BackgroundImage
import com.example.pizzasatpovo.ui.screens.PizzaScreens

class ListOfPizzasScreen {
    @Composable
    fun ListOfPizzasPage(
        navViewModel: NavigationViewModel,
        viewModel: PizzaViewModel,
        modifier: Modifier = Modifier,
        onAddToFavouritesClicked:(String)->Unit={},
        onRemoveFromFavouritesClicked:(String)->Unit={},
    ){
        BackgroundImage()
        //Page content
        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .fillMaxSize()
        ){
            Bars().AppBar(
                text = stringResource(R.string.pizze_di_oggi),
                modifier = modifier
                    .height(35.dp)
                    .padding(2.dp)
            )
            SearchBar(
                viewModel= viewModel,
                modifier = modifier
                    .padding(0.dp, 10.dp)
            )
            ListOfPizzas(
                navViewModel = navViewModel,
                viewModel = viewModel,
                onAddToFavouritesClicked = onAddToFavouritesClicked,
                onRemoveFromFavouritesClicked = onRemoveFromFavouritesClicked
            )
        }
        Bars().BottomBar(
            screen = PizzaScreens.ListOfPizzas,
            navViewModel = navViewModel
        )
    }

    @Composable
    fun SearchBar(viewModel: PizzaViewModel, modifier: Modifier = Modifier){
        val text by viewModel.searchQuery.collectAsStateWithLifecycle()
        TextField(
            value = text,
            onValueChange = {
                viewModel.onSearchTextChanged(it)
            },
            leadingIcon = { Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = stringResource(R.string.searchicon)
            ) },
            textStyle = TextStyle(
                textAlign = TextAlign.Start,
            ),
            shape = RoundedCornerShape(15.dp),
            label = null,
            placeholder = { Text(text = stringResource(R.string.cerca)) },
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White
            ),
            singleLine = false,
            modifier = modifier
                .width(330.dp)
                .padding()
        )
    }

    @Composable
    fun ListOfPizzas(
        navViewModel: NavigationViewModel,
        viewModel: PizzaViewModel,
        modifier: Modifier = Modifier,
        onAddToFavouritesClicked:(String)->Unit = {},
        onRemoveFromFavouritesClicked:(String)->Unit = {},
    ){
        val pizzas by viewModel.searchPizza.collectAsStateWithLifecycle()
        val favourites by viewModel.favourites.collectAsStateWithLifecycle()
        val favouritesName: ArrayList<String> = arrayListOf()
        for (favourite in favourites){
            favouritesName.add(favourite.name)
        }

        val state= rememberLazyListState()

        LazyColumn (
            state= state,
            modifier = modifier
                .padding(30.dp, 10.dp)
        ){
            items(pizzas){ pizza->
                PizzaCard(
                    viewModel = viewModel,
                    navViewModel = navViewModel,
                    pizza= pizza,
                    isFavourite =  favouritesName.contains(pizza.name),
                    onAddToFavouritesClicked =
                    {
                        onAddToFavouritesClicked(it)
                        val retPizza= RetrievedPizza(name= pizza.name, toppings= pizza.toppings!!, pizza.image)
                        viewModel.addToFavourites(retPizza)
                    },
                    onRemoveFromFavouritesClicked = {
                        onRemoveFromFavouritesClicked(it)
                        val retPizza= RetrievedPizza(name= pizza.name, toppings= pizza.toppings!!, pizza.image)
                        viewModel.removeFromFavourites(retPizza)
                    }
                )
                if (pizzas.last()==pizza){
                    Spacer(
                        modifier = modifier
                            .height(60.dp)
                    )
                }

            }
        }

        Spacer(
            modifier = modifier
                .height(60.dp)
        )
    }

    @Composable
    fun PizzaCard(
        viewModel: PizzaViewModel,
        navViewModel: NavigationViewModel,
        modifier: Modifier = Modifier,
        onAddToFavouritesClicked:(String)->Unit = {},
        onRemoveFromFavouritesClicked:(String)->Unit = {},
        pizza: RetrievedPizza,
        isFavourite: Boolean= false,
    ){
        var toppingForCard=""
        for (topping in pizza.toppings!!){
            toppingForCard= toppingForCard.plus(topping.name).plus(", ")
        }
        toppingForCard= toppingForCard.removeSuffix(", ")

        val allergens:ArrayList<String> = arrayListOf()
        for (topping in pizza.toppings){
            if (!allergens.contains(topping.allergens)){
                allergens.add(topping.allergens)
            }
        }
        var favourite by remember {mutableStateOf(isFavourite)}
        favourite= isFavourite
        val interactionSource = remember { MutableInteractionSource() }

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
                            viewModel.setPizza(pizza)
                            navViewModel.goToDetails()
                        }
                ) {
                    AsyncImage(
                        model = pizza.image,
                        contentDescription = stringResource(R.string.pizza_image),
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
                            fontSize = 16.sp,
                            modifier = modifier.padding(end = 15.dp)
                        )
                        Row(
                            verticalAlignment = Alignment.Bottom,
                        ) {

                            if (allergens!= arrayListOf("")){
                                for (allergen in allergens){
                                    if (allergen!=""){
                                        Allergen(
                                            modifier = modifier.align(Alignment.Bottom),
                                            allergen = allergen
                                        )
                                    }

                                }
                            }
                        }
                    }
                }
                Image(
                    imageVector =  if(favourite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                    contentDescription = stringResource(R.string.favourite_icon),
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