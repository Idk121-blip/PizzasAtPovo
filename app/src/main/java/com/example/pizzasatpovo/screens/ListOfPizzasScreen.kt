package com.example.pizzasatpovo.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pizzasatpovo.R

class ListOfPizzasScreen() {
    private val sizeTitle: TextUnit = 50.sp
    private val sizeSubtitle: TextUnit = 35.sp
    private val sizeText: TextUnit = 40.sp
    private val weightText: FontWeight = FontWeight.Bold
    private val uniColor: Color =  Color(0xffce0e2d)

    @Composable
    @Preview(showBackground = true)
    fun ListOfPizzas(
        modifier: Modifier = Modifier,
        onBackButtonClicked: () -> Unit = {},
        onLoginButtonClicked: () -> Unit = {},

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
        ){
            Column {
                Image(
                    painter = painterResource(id = R.drawable.back_icon),
                    contentDescription = "Back button",
                    modifier = modifier
                        .size(30.dp)
                )
                Row {
                    Column {
                        LoginField()
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun LoginField(modifier: Modifier = Modifier){
        var text by remember{ mutableStateOf("Ciccio") }
        Column {
            BasicTextField(
                value = text,
                onValueChange = {
                    text = it
                },

//            leadingIcon = { Icon(
//                imageVector = Icons.Filled.Search,
//                contentDescription = "Search"
//            ) },
                //shape = RoundedCornerShape(50.dp),
                //placeholder = { Text(text = "Cerca ...") },
                //singleLine = true,

//            modifier = modifier
//                .clip(CircleShape)
//                .background(Color.White)
//                .border(1.dp, Color.Red, CircleShape)

                //.height(40.dp)
            )

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
}