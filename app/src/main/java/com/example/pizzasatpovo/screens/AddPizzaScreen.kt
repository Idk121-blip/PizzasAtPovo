package com.example.pizzasatpovo.screens

import android.text.Layout
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.HoverInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.pizzasatpovo.R
import com.example.pizzasatpovo.data.RetrievedPizza
import com.example.pizzasatpovo.ui.components.Allergen
import com.example.pizzasatpovo.ui.components.Bars
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import com.example.pizzasatpovo.ui.components.BackgroundImage

class AddPizzaScreen {
    @Composable
    fun AddPizzaPage(
        onBackButtonClicked: () -> Unit = {},
        modifier: Modifier = Modifier
    ){
        Box(modifier = modifier
            .fillMaxSize()
        ){

            BackgroundImage()
            Column {
                Bars().AppBarWithBackBtn(
                    pizzasName = "Crea la tua pizza",
                    onBackButtonClicked = onBackButtonClicked
                )
                Column(
                    modifier = modifier
                        .padding(50.dp, 10.dp)
                ) {

                    Text(
                        text = "Base:",
                        fontWeight = FontWeight.Bold
                    )
                    Row {
                        IngredientCard()
                        IngredientCard()
                    }
                    Text(
                        text = "Altri ingredienti:",
                        fontWeight = FontWeight.Bold,
                        modifier = modifier
                            .padding(top = 10.dp)
                    )
                    Row (
                        modifier = modifier
                            .horizontalScroll(rememberScrollState())
                    ){
                        IngredientCard()
                        IngredientCard()
                        IngredientCard()
                        IngredientCard()
                        IngredientCard()
                        IngredientCard()
                        IngredientCard()
                        IngredientCard()
                    }
                }
                //White container
                Column (
                    verticalArrangement = Arrangement.Bottom,
                    modifier = modifier
                        .fillMaxSize()
                ){
                    DetailsPizzaScreen().orderDetails(title = "La tua creazione")
                }
            }
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterEnd)
            ){
                //TODO! REPLACE IMAGE OF MARGHERITA WITH THE FINAL ONE
                AsyncImage(model = "https://www.wanmpizza.com/wp-content/uploads/2022/09/pizza.png", contentDescription = "pizza image", modifier= modifier
                    .size(250.dp)
                    .align(Alignment.CenterHorizontally)
                )
            }
        }
    }

   @OptIn(ExperimentalMaterial3Api::class)
   @Composable
   fun IngredientCard(
       modifier: Modifier = Modifier
   ){
       var selected by remember { mutableStateOf(false) }
       TooltipBox(
           positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
           tooltip = {
               PlainTooltip {
                   Text("Ingredienti")
               }
           },
           state = rememberTooltipState()
       ) {
           Card(
               onClick = { selected = !selected },
               shape = RoundedCornerShape(percent = 15),
               colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
               modifier = modifier
                   .padding(5.dp)
                   .size(60.dp)
           ) {
               Box(
                   modifier = modifier
                       .fillMaxSize()
                       .padding(2.dp)
               ) {
                   Image(
                       imageVector = if (selected) Icons.Default.CheckCircle else Icons.Default.AddCircle,
                       contentDescription = "Selected",
                       modifier = modifier
                           .size(20.dp)
                           .align(Alignment.TopEnd)
                   )
                   Image(
                       painter = painterResource(id = R.drawable.google_icon),
                       contentDescription = "Ingredient image",
                       modifier = modifier
                           .padding(top = 10.dp)
                           .size(50.dp)
                           .align(Alignment.BottomCenter)
                           .padding(5.dp)
                   )
               }
           }
       }
   }
}