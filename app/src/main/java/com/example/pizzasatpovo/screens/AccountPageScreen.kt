package com.example.pizzasatpovo.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.HorizontalAlignmentLine
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.pizzasatpovo.R
import com.example.pizzasatpovo.data.LoadingResult
import com.example.pizzasatpovo.data.LoadingViewModel
import com.example.pizzasatpovo.data.UserData
import com.example.pizzasatpovo.presentation.sign_in.GoogleAuthUiClient
import com.example.pizzasatpovo.presentation.sign_in.SignInViewModel
import com.example.pizzasatpovo.ui.components.Bars
import kotlinx.coroutines.launch


class AccountPageScreen {

    @Composable
    fun AccountPage(googleAuthUiClient: GoogleAuthUiClient,
                    lifecycleScope: LifecycleCoroutineScope,
                    modifier: Modifier= Modifier,
                    navController: NavHostController = rememberNavController(),
                    onLogOutButtonClicked: ()->Unit,
                    onProfileButtonClicked: () -> Unit={},
                    onHomeButtonClicked:() ->Unit={},
                    onFavouritesButtonClicked:()->Unit = {},
                    onAddPizzaButtonClicked:()->Unit={},
                    onOrdersButtonClicked:()->Unit={},
    ){
        val viewModel = viewModel<LoadingViewModel>()
        val state by viewModel.state.collectAsStateWithLifecycle()
        Box(modifier = modifier
            .fillMaxSize()
        ){
            Box {
                Image(
                    painter = painterResource(id = R.drawable.background),
                    contentDescription = "Background image",
                    contentScale = ContentScale.FillBounds,
                    alpha = 0.5F,
                    modifier = modifier
                        .fillMaxSize()
                )

            }
        }
        NavHost(
            navController = navController,
            startDestination = "LoadingPage",
            modifier = modifier
        ) {

            composable(route= "LoadingPage"){

                Button(onClick = { /*TODO*/ }) {
                    Text(text = "Loading")
                }

                LaunchedEffect(key1 = Unit) {

                    lifecycleScope.launch {
                        val loadResult = LoadingResult(googleAuthUiClient.retrieveUserData(), null)
                        viewModel.onSignInResult(loadResult)
                    }

                }

                LaunchedEffect(state.isFinished) {
                    navController.navigate("AccountPage")
                }
            }
            composable(route= "AccountPage"){
                    Column {
                        Bars().AppBar(
                             text = "Account",
                        )
                        Column {
                            Card (
                                shape = RoundedCornerShape(15.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color.White,
                                ),
                                modifier = modifier
                                    .fillMaxWidth()
                                    .height(160.dp)
                                    .padding(
                                        start = 25.dp,
                                        top = 10.dp,
                                        end = 25.dp,
                                        bottom = 10.dp
                                    )
//                                    .align(Alignment.CenterHorizontally)

                            ){
                                val userData by viewModel.userData.collectAsStateWithLifecycle()
                                Card (shape = RoundedCornerShape(
                                    topEnd = 15.dp,
                                    topStart = 15.dp,
                                    bottomEnd = 0.dp,
                                    bottomStart = 0.dp
                                ),
                                colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primary),
                                modifier = modifier
                                    .fillMaxWidth()
                                    .height(105.dp)
                                    .padding(0.dp, 0.dp)

                                ){
                                Row (verticalAlignment = Alignment.CenterVertically, modifier = modifier
                                    .fillMaxSize()) {

                                    Box(modifier = modifier.padding(start = 15.dp)){
                                        AsyncImage(model = userData.image,
                                            contentScale = ContentScale.Crop,
                                            contentDescription = "user image",
                                            modifier= modifier
                                                .size(90.dp)
                                                .clip(CircleShape)
                                        )
                                    }


                                    Spacer(
                                        Modifier
                                            .weight(1f)
                                            .fillMaxHeight())
                                    Text(text = "€ " +String.format("%.2f", userData.credit),
                                        fontSize = 30.sp,
                                        fontWeight = FontWeight.Bold,
                                        textAlign = TextAlign.Center,
                                        modifier = modifier.padding(end = 15.dp)
                                    )


                                }
                            }
                                Row(modifier= modifier
                                    .align(Alignment.Start)
                                    .fillMaxSize()) {
                                    Text(text = userData.name!!,
                                        modifier
                                            .padding(start = 15.dp,end= 7.dp, bottom = 7.dp, top = 7.dp))
                                    Spacer(
                                        Modifier
                                            .weight(1f)
                                            .fillMaxHeight()
                                            )
                                    Image(painter = painterResource(id = R.drawable.unitn_logo), contentDescription = "logo unitn", modifier.padding(end= 15.dp, bottom = 5.dp, top = 5.dp))
                                }

                            }
                            Text(text = "Impostazioni generali",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.SemiBold,
                                textAlign = TextAlign.Center,
                                modifier = modifier.padding(top = 15.dp, bottom = 15.dp, start = 25.dp))
                            Row(modifier = modifier.padding(bottom = 2.dp, start = 25.dp, end = 25.dp)) {
                                Icon(painter = painterResource(id = R.drawable.notification_icon), contentDescription = "Notification icon", modifier.size(25.dp))
                                Text(text = "Notifiche",
                                    modifier
                                        .align(Alignment.CenterVertically)
                                        .padding(start = 10.dp))
                                Spacer(
                                    Modifier
                                        .weight(1f)
                                        .fillMaxWidth()
                                )
                                var checked by remember { mutableStateOf(true) }
                                Switch(
                                    checked = checked,
                                    onCheckedChange = {
                                        checked = it
                                    },
                                    modifier
                                        .scale(0.75F)
                                        .height(10.dp)
                                )

                            }
                            HorizontalDivider(color = Color.Black.copy(alpha = 0.3F), thickness = 1.dp, modifier = modifier.padding(bottom = 7.dp, start = 25.dp, end = 25.dp))
                            Row(modifier = modifier.padding(bottom = 2.dp, start = 25.dp, end = 25.dp)) {
                                Icon(painter = painterResource(id = R.drawable.language_icon), contentDescription = "Language icon", modifier.size(25.dp))
                                Text(text = "Lingua",
                                    modifier
                                        .align(Alignment.CenterVertically)
                                        .padding(start = 10.dp))
                                Spacer(
                                    Modifier
                                        .weight(1f)
                                        .fillMaxWidth()
                                )
                                Text(text = "Italiano",
                                    modifier
                                        .align(Alignment.CenterVertically)
                                        .padding(start = 10.dp))
                            }

                            HorizontalDivider(color = Color.Black.copy(alpha = 0.3F), thickness = 1.dp, modifier = modifier.padding(bottom = 7.dp, start = 25.dp, end = 25.dp))
                            Text(text = "Aiuto e supporto",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.SemiBold,
                                textAlign = TextAlign.Center,
                                modifier = modifier.padding(top = 15.dp, bottom = 15.dp, start = 25.dp))
                            Row(modifier = modifier.padding(bottom = 2.dp, start = 25.dp, end = 25.dp)) {
                                Icon(painter = painterResource(id = R.drawable.faq_icon), contentDescription = "Notification icon", modifier.size(25.dp))
                                Text(text = "FAQ",
                                    modifier
                                        .align(Alignment.CenterVertically)
                                        .padding(start = 10.dp))
                            }
                            HorizontalDivider(color = Color.Black.copy(alpha = 0.3F), thickness = 1.dp, modifier = modifier.padding(bottom = 7.dp, start = 25.dp, end = 25.dp))
                            Row(modifier = modifier.padding(bottom = 2.dp, start = 25.dp, end = 25.dp)) {
                                Icon(painter = painterResource(id = R.drawable.email_icon), contentDescription = "Language icon", modifier.size(25.dp))
                                Text(text = "Contatta il supporto",
                                    modifier
                                        .align(Alignment.CenterVertically)
                                        .padding(start = 10.dp))
                                Spacer(
                                    Modifier
                                        .weight(1f)
                                        .fillMaxWidth()
                                )

                            }
                            HorizontalDivider(color = Color.Black.copy(alpha = 0.3F), thickness = 1.dp, modifier = modifier.padding(bottom = 7.dp, start = 25.dp, end = 25.dp))
                            Row(modifier = modifier.padding(bottom = 2.dp, start = 25.dp, end = 25.dp)) {
                                Icon(painter = painterResource(id = R.drawable.review_icon), contentDescription = "Language icon", modifier.size(25.dp))
                                Text(text = "Lascia un feedback",
                                    modifier
                                        .align(Alignment.CenterVertically)
                                        .padding(start = 10.dp))
                                Spacer(
                                    Modifier
                                        .weight(1f)
                                        .fillMaxWidth()
                                )

                            }
                            HorizontalDivider(color = Color.Black.copy(alpha = 0.3F), thickness = 1.dp, modifier = modifier.padding(bottom = 7.dp, start = 25.dp, end = 25.dp))

                            Text(text = "Informazioni legali",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.SemiBold,
                                textAlign = TextAlign.Center,
                                modifier = modifier.padding(top = 15.dp, bottom = 15.dp, start = 25.dp))
                            Row(modifier = modifier.padding(bottom = 2.dp, start = 25.dp, end = 25.dp)) {
                                Icon(painter = painterResource(id = R.drawable.terms_and_conditions_icon), contentDescription = "Notification icon", modifier.size(25.dp))
                                Text(text = "Termini e condizioni",
                                    modifier
                                        .align(Alignment.CenterVertically)
                                        .padding(start = 10.dp))



                            }
                            HorizontalDivider(color = Color.Black.copy(alpha = 0.3F), thickness = 1.dp, modifier = modifier.padding(bottom = 7.dp, start = 25.dp, end = 25.dp))
                            Row(modifier = modifier.padding(bottom = 2.dp, start = 28.dp, end = 25.dp)) {
                                Icon(painter = painterResource(id = R.drawable.privacy_and_security), contentDescription = "Language icon", modifier.size(25.dp))
                                Text(text = "Privacy e sicurezza",
                                    modifier
                                        .align(Alignment.CenterVertically)
                                        .padding(start = 10.dp))
                            }
                            HorizontalDivider(color = Color.Black.copy(alpha = 0.3F), thickness = 1.dp, modifier = modifier.padding(bottom = 7.dp, start = 25.dp, end = 25.dp))

                            Row(modifier = modifier
                                .padding(top = 25.dp, bottom = 2.dp, start = 25.dp, end = 25.dp)
                                .clickable {
                                    onLogOutButtonClicked()
                                }) {
                                Icon(painter = painterResource(id = R.drawable.logout_icon), contentDescription = "Logout icon", tint = MaterialTheme.colorScheme.primary, modifier = modifier.size(30.dp))
                                Text(text = "Log out",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    textAlign = TextAlign.Center,
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier= modifier.align(Alignment.CenterVertically)
                                )
                            }

                        }
                        Bars().BottomBar(screen = PizzaScreens.Account,
                            onProfileButtonClicked= onProfileButtonClicked,
                            onHomeButtonClicked=onHomeButtonClicked,
                            onFavouritesButtonClicked=onFavouritesButtonClicked,
                            onAddPizzaButtonClicked=onAddPizzaButtonClicked,
                            onOrdersButtonClicked=onOrdersButtonClicked)
                    }

            }



        }
    }

}