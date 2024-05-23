package com.example.pizzasatpovo.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.DefaultTintColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.unit.dp
import com.example.pizzasatpovo.R

@Composable
fun Allergen(
//    allergen: Allergen,
    available: Boolean = true,
    allergen:String,
    modifier: Modifier = Modifier
){
    Card (
        shape = RoundedCornerShape(5.dp),
        colors = if(available){
            CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
        }else {
            CardDefaults.cardColors(containerColor = Color.LightGray)
        },
        modifier = modifier
            .padding(top = 2.dp, end = 5.dp, bottom = 2.dp)
            .width(30.dp)
            .height(30.dp)
    ){
        val context = LocalContext.current
        val name= allergen.plus("_allergen").lowercase()
        println(name)
        val drawableId = remember(name) {
            context.resources.getIdentifier(
                name,
                "drawable",
                context.packageName
            )
        }
        Image(
            painter = painterResource(id = drawableId), //allergen icon
            contentDescription = "Allergen", //allergen name
            modifier = modifier
                .padding(8.dp)
                .fillMaxSize()
        )
    }
}