package com.example.pizzasatpovo.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.sharp.KeyboardArrowLeft
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pizzasatpovo.R
import com.example.pizzasatpovo.data.Pizza
import com.example.pizzasatpovo.data.Topping

class ListOfPizzasScreen() {
    private val sizeTitle: TextUnit = 50.sp
    private val sizeSubtitle: TextUnit = 35.sp
    private val sizeText: TextUnit = 40.sp
    private val weightText: FontWeight = FontWeight.Bold
    private val uniColor: Color =  Color(0xffce0e2d)

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    @Preview(showBackground = true)
    fun ListOfPizzasPage(
        onBackButtonClicked: () -> Unit = {},
        onLoginButtonClicked: () -> Unit = {},
        pizzas: ArrayList<Pizza> = arrayListOf(),
        toppings: ArrayList<ArrayList<Topping>> = arrayListOf(arrayListOf()),
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
                Row (
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Icon(
                        modifier = Modifier.size(48.dp).fillMaxWidth().padding(end = 8.dp),
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = "Back to login",
                    )
                    Text(
                        text = "Pizza at Povo",
                        fontSize = sizeText,
                        fontWeight = weightText
                    )
                }
                Row {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = modifier
                            .fillMaxSize()
                    ) {
                        SearchBar(
                            modifier = modifier
                                .padding(10.dp)
                        )
                        ListOfPizzas(pizzas, toppings)
                    }
                }
            }
        }
    }


    @Composable
    fun SearchBar(modifier: Modifier = Modifier){
        var text by remember{ mutableStateOf("Ciccio") }
        Column {
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
                placeholder = { Text(text = "Cerca ...") },
                singleLine = true,
                //contentPadding = PaddingValues(),
                modifier = modifier
                    .clip(CircleShape)
                    .background(Color.White)
                    .height(40.dp)
            )
        }

    }

    @Composable
    fun ListOfPizzas(
        pizzas: ArrayList<Pizza>,
        toppings: ArrayList<ArrayList<Topping>>,
        modifier: Modifier = Modifier
    ){
        var array: ArrayList<Triple<Int, String, String>> = ArrayList();
        if (pizzas.size>0){
            array.add(Triple(R.drawable.ic_launcher_background, pizzas[0].name, toppings[0][0].name))
        }
        array.add(Triple(R.drawable.ic_launcher_background, "Patatoso", "Mozzarella"))
        array.add(Triple(R.drawable.ic_launcher_background, "Patatoso", "Mozzarella"))
        array.add(Triple(R.drawable.ic_launcher_background, "Patatoso", "Mozzarella"))
        array.add(Triple(R.drawable.ic_launcher_background, "Patatoso", "Mozzarella"))
        array.add(Triple(R.drawable.ic_launcher_background, "Patatoso", "Mozzarella"))
        array.add(Triple(R.drawable.ic_launcher_background, "Patatoso", "Mozzarella"))
        array.add(Triple(R.drawable.ic_launcher_background, "Patatoso", "Mozzarella"))
        array.add(Triple(R.drawable.ic_launcher_background, "Patatoso", "Mozzarella"))
        array.add(Triple(R.drawable.ic_launcher_background, "Patatoso", "Mozzarella"))
        array.add(Triple(R.drawable.ic_launcher_background, "Patatoso", "Mozzarella"))
        array.add(Triple(R.drawable.ic_launcher_background, "Patatoso", "Mozzarella"))



//        LazyColumn(){
//            items(array){ pizza ->
//                PizzaCard(
//                    image = pizza.first,
//                    name = pizza.second,
//                    toppings = pizza.third,
//                    modifier = modifier
//                )
//            }
//        }
        Column (
            modifier = modifier
                .verticalScroll(rememberScrollState())
                .padding(5.dp)
        ){
            array.forEach { pizza ->
                PizzaCard(image = pizza.first, name = pizza.second, toppings = pizza.third)
            }
        }
    }

    @Composable
    fun PizzaCard(
        image: Int,
        name: String,
        toppings: String,
        modifier: Modifier = Modifier
    ){
        Card (
            shape = RoundedCornerShape(15.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White,
            ),
            modifier = modifier
                .width(350.dp)
                .height(130.dp)
                .padding(0.dp, 10.dp)
        ){
            Row (
                modifier = modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ){
                Image(
                    painter = painterResource(id = image),
                    contentDescription = "$name image",
                    modifier = modifier
                        .fillMaxHeight()
                        .padding(end = 15.dp)
                )
                Column {
                    Text(
                        text = "$name",
                        fontWeight = weightText
                    )
                    Text(text = "$toppings")
                }
            }
        }
    }
}