package com.example.pizzasatpovo.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pizzasatpovo.R


class FirstPageScreen() {
    private val sizeText: TextUnit = 35.sp
    private val weightText: FontWeight = FontWeight.Bold
    private val uniColor: Color =  Color(0xffce0e2d)

    @Composable
    fun FirstPage(
        modifier: Modifier = Modifier,
        onLoginButtonClicked: () -> Unit,
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

        Box(modifier = modifier){
            Column(
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = modifier
                    .fillMaxSize()
                    .padding(30.dp, 0.dp)
            ) {
                //Row {
                    Text(
                        text = "Pizza at Povo",
                        fontSize = 20.sp,
                        fontWeight = weightText,
                        modifier = modifier
                            .padding(bottom = 75.dp)
                    )
                //}
                Column (
                    modifier = modifier
                        .align(Alignment.Start)
                        .padding(0.dp, 80.dp)
                ) {
                    DisplayText()
                }
                Row {
                    DisplayButton( onLoginButtonClicked )
                }
                Row (
                    modifier = modifier
                ){
                    Image(
                        painter = painterResource(id = R.drawable.login_image),
                        contentDescription = "Login image",
                        modifier = modifier
                            .padding(top = 70.dp)
                            .size(300.dp)
                    )
                }

            }
        }
    }
    @Composable
    fun DisplayText(modifier: Modifier = Modifier){
        Text(
            text = "PRENOTA LA TUA ",
            fontSize = sizeText,
            fontWeight = weightText
        )
        Row{
            Text(
                text = "PIZZA ",
                fontSize = sizeText,
                fontWeight = weightText,
                color = uniColor
            )
            Text(
                text = "CON UN ",
                fontSize = sizeText,
                fontWeight = weightText
            )
        }
        Row {
            Text(
                text = "CLICK",
                fontSize = sizeText,
                fontWeight = weightText,
                color = uniColor
            )
            Text(
                text = "!",
                fontSize = sizeText,
                fontWeight = weightText
            )
        }
    }

    @Composable
    fun DisplayButton(
        onLoginButtonClicked: () -> Unit,
        modifier: Modifier = Modifier
    ){
        Button(
            onClick = onLoginButtonClicked,
            colors = ButtonDefaults.buttonColors(uniColor),
            shape = RoundedCornerShape(25),
            modifier = modifier
        ) {
            Text(
                text = "LOGIN CON GOOGLE",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = modifier
                    .padding(end = 50.dp) //usare lo space between?
            )
            Spacer(modifier = Modifier.width(10.dp))

            Image(
                painter = painterResource(id = R.drawable.google_icon),
                contentDescription = "Login with Google",
                modifier = modifier
                    .size(30.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .padding(4.dp)
            )
        }
    }
}