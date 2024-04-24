package com.example.pizzasatpovo.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text2.BasicTextField2
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material.icons.sharp.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Shapes
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
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
    private val dimIcons: Dp = 30.dp

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
                AppBar()
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
        Box(
            modifier = modifier
                .fillMaxWidth()
                .fillMaxSize()
        ){
            Box {
                Image(
                    painter = painterResource(id = R.drawable.nav_bar),
                    contentDescription = "NavBar",
                    alignment = Alignment.BottomCenter,
                    modifier = modifier
                        .fillMaxSize()
                )
            }
            Box (
                modifier = modifier
                    .fillMaxSize()
                    .align(Alignment.BottomCenter)
            ){
                Navbar(
                    modifier = modifier
                        .align(Alignment.BottomCenter)
                        .padding(0.dp, 10.dp)
                )
            }
        }
    }
    
    @Composable
    fun AppBar(modifier: Modifier = Modifier){
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
            )
            Text(
                text = "Pizza at Povo",
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


    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun SearchBar(modifier: Modifier = Modifier){
        var text by remember{ mutableStateOf("Ciccio") }

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
                modifier = modifier
                    .clip(CircleShape)
                    .background(Color.White)
                    .height(40.dp)
            )



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
                .height(140.dp)
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
                    Row {
                        Allergen()
                        Allergen()
                    }
                }
            }
        }
    }
    
    @Composable
    fun Allergen(modifier: Modifier = Modifier){
        Card (
            shape = RoundedCornerShape(5.dp),
            modifier = modifier
                .padding(top = 2.dp, end = 5.dp, bottom = 2.dp)
                .width(30.dp)
                .fillMaxHeight()
        ){
            Image(
                painter = painterResource(id = R.drawable.back_icon),
                contentDescription = "Allergen",
                modifier = modifier
                    .padding(8.dp)
                    .fillMaxSize()
            )
        }
    }

    @Composable
    fun Navbar(modifier : Modifier = Modifier){
        Row(
            //verticalAlignment = Alignment.Bottom,
            modifier = modifier
                .fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Filled.Home,
                contentDescription = "List of pizzas",
                modifier = Modifier
                    .size(dimIcons)
                    .weight(0.2F)
            )
            Icon(
                imageVector = Icons.Filled.Favorite,
                contentDescription = "Favourite pizzas",
                modifier = Modifier
                    .size(dimIcons)
                    .weight(0.2F)
            )
            Icon(
                imageVector = Icons.Filled.Menu,
                contentDescription = "Recent orders",
                modifier = Modifier
                    .size(dimIcons)
                    .weight(0.2F)
            )
            Icon(
                imageVector = Icons.Filled.AccountCircle,
                contentDescription = "Account",
                modifier = Modifier
                    .size(dimIcons)
                    .weight(0.2F)
            )
        }
        Button(
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(Color.Red),
            content = {
                Image(
                    painter = painterResource(id = R.drawable.plus_icon),
                    contentDescription = "Add pizza",
                    contentScale = ContentScale.Fit,
                    modifier = modifier

                )
            },
            onClick = { /*TODO*/ },
            contentPadding = PaddingValues(),
            modifier = modifier
                .defaultMinSize(minWidth = 1.dp, minHeight = 1.dp)
                .size(50.dp)
                .offset(0.dp, (-20).dp)

        )

    }
}









