package com.example.pizzasatpovo.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pizzasatpovo.R

class FirstPageScreen() {
    @Composable
    fun FirstPage(modifier: Modifier = Modifier){
        Column(
            modifier = modifier
        ) {
            DisplayText()
            DisplayButton()
        }

    }
    @Composable
    fun DisplayText(){
        Text(
            text = "PRENOTA LA TUA",
        )
        Row{
            Text(
                text = "PIZZA",
                color = Color(0xC5E50E2D)
            )
            Text(text = "CON UN ")
        }
        Row {
            Text(
                text = "CLICK",
                color = Color(0xC5E50E2D)
            )
            Text(text = "!")
        }

    }

    @Composable
    fun DisplayButton(modifier: Modifier = Modifier){
        Button(onClick = { /*TODO*/ }) {
            Text(
                text = "PROCEDI AL LOGIN",
                fontWeight = FontWeight.Bold,
                modifier = modifier
                    .padding(end = 50.dp) //usare lo space between?
            )
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_background),
                contentDescription = "Next page",
                modifier = modifier
                    .size(25.dp)
            )
        }
    }
}