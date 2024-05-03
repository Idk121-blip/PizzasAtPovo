package com.example.pizzasatpovo.screens

//import android.text.Layout.Alignment
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.pizzasatpovo.R


@Composable
fun CustomDialog(setShowDialog: (Boolean) -> Unit, sendOrder: ()->Unit, numberOfPizzas: Int, pizzaName:String) {

    Dialog(onDismissRequest = { setShowDialog(false) }) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.LightGray
        ) {
            Box(
            ) {
                Spacer(modifier = Modifier.height(20.dp))
                Column( modifier = Modifier.padding(20.dp)) {
            
                    Column (horizontalAlignment = Alignment.CenterHorizontally) {
                        Row(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Ordinare $numberOfPizzas pizza",

                                    fontSize = 24.sp,
                                    fontFamily = FontFamily.Default,

                            )
                            Text(
                                text = " $pizzaName",
                                fontSize = 24.sp,
                                fontFamily = FontFamily.Default,
                                fontWeight = FontWeight.Bold

                            )
                        }
                        Row(modifier = Modifier) {
                            Text(
                                text = "per € ",
                                fontSize = 24.sp,
                                fontFamily = FontFamily.Default,
//                            fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = (numberOfPizzas*4.4).toString(),
                                fontSize = 24.sp,
                                fontFamily = FontFamily.Default,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "?",
                                fontSize = 24.sp,
                                fontFamily = FontFamily.Default,
//                            fontWeight = FontWeight.Bold
                            )
                        }
                    }


                    Spacer(modifier = Modifier.height(20.dp))

                    Row(modifier = Modifier.padding(20.dp, 0.dp, 20.dp, 0.dp)) {
                        Button(
                            onClick = {
                                //TODO
                                setShowDialog(false)
                            },
                            colors= ButtonDefaults.buttonColors(containerColor = Color.LightGray, contentColor = Color.Black),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .padding(end = 10.dp)
                                .weight(0.5F)
                                .fillMaxWidth()
                                .height(35.dp)
                        ) {
                            Text(text = "Annulla")
                        }
                        Button(
                            onClick = {
                                //TODO

                                setShowDialog(false)
                                sendOrder()



                            },
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(0.5F)
                                .height(35.dp)
                        ) {
                            Text(text = "Ordina")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CustomDialogDatabaseResponse(setShowDialog: (Boolean) -> Unit) {
    println("Ciao")
    Dialog(onDismissRequest = { setShowDialog(false) }) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.LightGray
        ) {
            Box(
                //contentAlignment = Alignment.ALIGN_CENTER
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(Modifier.fillMaxWidth(),) {

                    Row(
                        modifier = Modifier
                            .align(Alignment.End)
                            .padding(end = 10.dp, top = 10.dp),


                    ) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "",
                            tint = colorResource(android.R.color.darker_gray),
                            modifier = Modifier
                                .width(30.dp)
                                .height(30.dp)
                                .clickable { setShowDialog(false) }
                        )
                    }


                    Spacer(modifier = Modifier.height(5.dp))

                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                        Image(painter = painterResource(id = R.drawable.order_sent), contentDescription = "Order sent icon", Modifier.width(100.dp))
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(text = "Ordine effettuato!")
                    }


                    Spacer(modifier = Modifier.height(20.dp))




                }
            }
        }
    }
}