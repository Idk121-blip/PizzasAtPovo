package com.example.pizzasatpovo.ui.components

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.pizzasatpovo.R
import com.example.pizzasatpovo.data.viewmodel.NavigationViewModel


@SuppressLint("DefaultLocale")
@Composable
fun CustomDialog(
    setShowDialog: (Boolean) -> Unit,
    sendOrder: () -> Unit,
    numberOfPizzas: Int,
    pizzaName: String,
    navViewModel: NavigationViewModel,
) {
    Dialog(onDismissRequest = { setShowDialog(false) }) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color(217, 217, 217, 255)
        ) {
            Box {
                Spacer(modifier = Modifier.height(20.dp))
                Column(modifier = Modifier.padding(20.dp, 25.dp, 20.dp, 0.dp)) {

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = stringResource(R.string.ordinare_pizza, numberOfPizzas),
                                fontSize = 20.sp,
                                fontFamily = FontFamily.Default,
                            )
                            Text(
                                text = " $pizzaName",
                                fontSize = 20.sp,
                                fontFamily = FontFamily.Default,
                                fontWeight = FontWeight.Bold

                            )
                        }
                        Row(modifier = Modifier) {
                            Text(
                                text = stringResource(R.string.per),
                                fontSize = 20.sp,
                                fontFamily = FontFamily.Default,
                            )
                            Text(
                                text = String.format("%.2f", numberOfPizzas * 4.4),
                                fontSize = 20.sp,
                                fontFamily = FontFamily.Default,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = stringResource(R.string.question_mark),
                                fontSize = 20.sp,
                                fontFamily = FontFamily.Default,
                            )
                        }
                    }
                    Row(modifier = Modifier.padding(20.dp)) {
                        Button(
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(217, 217, 217, 255),
                                contentColor = Color.Black
                            ),
                            onClick = {
                                setShowDialog(false)
                            },
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .padding(start = 10.dp)
                                .weight(0.5F)
                                .fillMaxWidth()
                                .height(35.dp)
                        ) {
                            Text(text = stringResource(R.string.annulla))
                        }
                        Button(
                            onClick = {
                                navViewModel.goToLoadingPage()
                                sendOrder()
                                navViewModel.popStack()
                                setShowDialog(false)

                            },
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(0.5F)
                                .height(35.dp)
                        ) {
                            Text(text = stringResource(R.string.ordina))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CustomDialogDatabaseResponse(message: String, setShowDialog: (Boolean) -> Unit) {
    Dialog(onDismissRequest = { setShowDialog(false) }) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color(217, 217, 217, 255)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
            ) {
                Column(Modifier.fillMaxWidth()) {
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

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        if (message == stringResource(R.string.ordine_effettuato)) {
                            Image(
                                painter = painterResource(id = R.drawable.order_sent),
                                contentDescription = stringResource(R.string.order_sent_icon),
                                Modifier.width(70.dp)
                            )
                        } else {
                            Image(
                                painter = painterResource(id = R.drawable.order_not_sent),
                                contentDescription = stringResource(R.string.order_not_sent_icon),
                                Modifier.width(75.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(text = message)
                    }
                }
            }
        }
    }
}