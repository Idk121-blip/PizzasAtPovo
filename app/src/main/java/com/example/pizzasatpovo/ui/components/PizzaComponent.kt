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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.DefaultTintColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.pizzasatpovo.R

@Composable
fun Allergen(
    //allergen: Allergen,
    available: Boolean = true,
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
        Image(
            painter = painterResource(id = R.drawable.back_icon), //allergen icon
            contentDescription = "Allergen", //allergen name
            modifier = modifier
                .padding(8.dp)
                .fillMaxSize()
        )
    }
}