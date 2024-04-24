package com.example.pizzasatpovo.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.pizzasatpovo.R

@Composable
fun Allergen(
    //allergen: Allergen,
    modifier: Modifier = Modifier
){
    Card (
        shape = RoundedCornerShape(5.dp),
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