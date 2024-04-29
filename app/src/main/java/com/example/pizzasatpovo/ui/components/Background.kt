package com.example.pizzasatpovo.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.pizzasatpovo.R

@Composable
fun BackgroundImage(
    modifier: Modifier = Modifier
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