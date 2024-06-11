package com.example.pizzasatpovo.ui.screens.account

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
import com.example.pizzasatpovo.data.model.LoadingResult
import com.example.pizzasatpovo.data.viewmodel.LoadingViewModel
import com.example.pizzasatpovo.data.viewmodel.NavigationViewModel
import com.example.pizzasatpovo.database.GoogleAuthUiClient
import com.example.pizzasatpovo.ui.components.BackgroundImage
import com.example.pizzasatpovo.ui.components.Bars
import com.example.pizzasatpovo.ui.components.shimmerBrush
import com.example.pizzasatpovo.ui.screens.PizzaScreens
import kotlinx.coroutines.launch


class AccountPageScreen {

    @Composable
    fun AccountPage(
        googleAuthUiClient: GoogleAuthUiClient,
        lifecycleScope: LifecycleCoroutineScope,
        modifier: Modifier= Modifier,
        navController2: NavHostController = rememberNavController(),
        navController: NavigationViewModel,
        onLogOutButtonClicked: ()->Unit,
    ){
        val viewModel = viewModel<LoadingViewModel>()

        BackgroundImage()
        NavHost(
            navController = navController2,
            startDestination = "AccountPage",
            modifier = modifier
        ) {
            composable(route= "AccountPage"){

                LaunchedEffect(key1 = Unit) {
                    lifecycleScope.launch {
                        val loadResult = LoadingResult(googleAuthUiClient.retrieveUserData(), null)
                        viewModel.onSignInResult(loadResult)
                    }
                }
                Column {
                    Bars().AppBar(
                        text = stringResource(R.string.account),
                        modifier = modifier
                            .height(35.dp)
                            .padding(2.dp)
                    )
                    Column {
                        UserInfoCard(viewModel = viewModel)

                        SectionTitle(text = stringResource(R.string.impostazioni_generali))
                        Section(
                            text = stringResource(R.string.notifiche),
                            resourceId = R.drawable.notification_icon,
                            description = "Notification icon"
                        )
                        Section(
                            text = stringResource(R.string.lingua),
                            resourceId = R.drawable.language_icon,
                            description = stringResource(R.string.language_icon)
                        )

                        SectionTitle(text = stringResource(R.string.aiuto_e_supporto))
                        Section(
                            text = stringResource(R.string.faq),
                            resourceId = R.drawable.faq_icon,
                            description = stringResource(R.string.faq_icon)
                        )
                        Section(
                            text = stringResource(R.string.contatta_il_supporto),
                            resourceId = R.drawable.email_icon,
                            description = stringResource(R.string.support_icon)
                        )
                        Section(
                            text = stringResource(R.string.lascia_un_feedback),
                            resourceId = R.drawable.review_icon,
                            description = stringResource(R.string.feedback_icon)
                        )

                        SectionTitle(text = stringResource(R.string.informazioni_legali))
                        Section(
                            text = stringResource(R.string.termini_e_condizioni),
                            resourceId = R.drawable.terms_and_conditions_icon,
                            description = stringResource(R.string.notifiction_icon)
                        )
                        Section(
                            text = stringResource(R.string.privacy_e_sicurezza),
                            resourceId = R.drawable.privacy_and_security,
                            description = stringResource(R.string.privacy_icon)
                        )

                        LogoutButton(
                            onLogOutButtonClicked = onLogOutButtonClicked,
                            viewModel= viewModel
                        )
                    }
                    Bars().BottomBar(
                        screen = PizzaScreens.Account,
                        navViewModel = navController
                    )
                }

            }
        }
    }

    @SuppressLint("DefaultLocale")
    @Composable
    fun UserInfoCard(
        viewModel: LoadingViewModel,
        modifier: Modifier = Modifier
    ){
        Card (
            shape = RoundedCornerShape(15.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White,
            ),
            modifier = modifier
                .fillMaxWidth()
                .height(160.dp)
                .padding(25.dp, 10.dp)
        ){
            val userData by viewModel.userData.collectAsStateWithLifecycle()
            val state by viewModel.state.collectAsStateWithLifecycle()
            val showShimmer = remember { mutableStateOf(true) }
            showShimmer.value= !state.isFinished
            Card (
                shape = RoundedCornerShape(15.dp, 0.dp),
                colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primary),
                modifier = modifier
                    .fillMaxWidth()
                    .height(105.dp)
                    .padding(0.dp)
            ){
                Row (
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = modifier
                        .fillMaxSize()
                ) {
                    Box(modifier = modifier.padding(start = 15.dp)){
                        AsyncImage(
                            model = userData.image,
                            contentScale = ContentScale.Crop,
                            contentDescription = stringResource(R.string.user_image),
                            onSuccess = {  },
                            modifier= modifier
                                .clip(CircleShape)
                                .background(
                                    shimmerBrush(
                                        targetValue = 1300f,
                                        showShimmer = showShimmer.value
                                    )
                                ).size(90.dp)
                        )
                    }
                    Spacer(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                    )

                    Text(
                        text = "" +if(showShimmer.value){"            "} else { "€ "+String.format("%.2f", userData.credit)},
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = modifier
                            .padding(end = 15.dp)
                            .background(
                                shimmerBrush(
                                    targetValue = 1300f,
                                    showShimmer = showShimmer.value
                                )
                            )
                    )
                }
            }
            Row(
                modifier= modifier
                    .align(Alignment.Start)
                    .fillMaxSize()
            ) {
                Text(
                    text = "" +if(showShimmer.value){"                    "} else {userData.name!!},
                    modifier = modifier
                        .padding(start = 15.dp, end = 7.dp, bottom = 7.dp, top = 7.dp)
                        .background(
                            shimmerBrush(
                                targetValue = 1300f,
                                showShimmer = showShimmer.value
                            )
                        )
                )
                Spacer(
                    Modifier
                        .weight(1f)
                        .fillMaxHeight()
                )
                Image(
                    painter = painterResource(id = R.drawable.unitn_logo),
                    contentDescription = stringResource(R.string.logo_unitn),
                    modifier = modifier.padding(end= 15.dp, bottom = 5.dp, top = 5.dp)
                )
            }
        }
    }

    @Composable
    fun SectionTitle(
        text: String,
        modifier: Modifier = Modifier
    ){
        Text(
            text = text,
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
            modifier = modifier.padding(top = 15.dp, bottom = 15.dp, start = 25.dp)
        )
    }

    @Composable
    fun Section(
        text: String,
        resourceId: Int,
        description: String,
        modifier: Modifier = Modifier
    ){
        Row(
            modifier = modifier.padding(bottom = 2.dp, start = 25.dp, end = 25.dp)) {
            Icon(
                painter = painterResource(id = resourceId),
                contentDescription = description,
                modifier = modifier.size(25.dp))
            Text(
                text = text,
                modifier
                    .align(Alignment.CenterVertically)
                    .padding(start = 10.dp))
            if(text == stringResource(R.string.notifiche)){
                Spacer(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                )
                var checked by remember { mutableStateOf(true) }
                Switch(
                    checked = checked,
                    onCheckedChange = {
                        checked = it
                    },
                    modifier = modifier
                        .scale(0.75F)
                        .height(10.dp)
                )
            }
            if(text == stringResource(R.string.lingua)){
                Spacer(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                )
                Text(
                    text = stringResource(R.string.italiano),
                    modifier = modifier
                        .align(Alignment.CenterVertically)
                        .padding(start = 10.dp)
                )
            }
        }
        HorizontalDivider(
            color = Color.Black.copy(alpha = 0.3F),
            thickness = 1.dp,
            modifier = modifier.padding(bottom = 7.dp, start = 25.dp, end = 25.dp))

    }

    @Composable
    fun LogoutButton(
        viewModel: LoadingViewModel,
        onLogOutButtonClicked: () -> Unit,
        modifier: Modifier = Modifier,
    ){
        val interactionSource = remember { MutableInteractionSource() }
        val state by viewModel.state.collectAsStateWithLifecycle()
        val showShimmer = remember { mutableStateOf(true) }
        showShimmer.value= !state.isFinished
        Row(modifier = modifier
            .padding(top = 25.dp, bottom = 2.dp, start = 25.dp, end = 25.dp)
            .clickable(
                enabled = !showShimmer.value,
                interactionSource = interactionSource,
                indication = null
            ) {
                onLogOutButtonClicked()
            }
        ) {
            Icon(
                painter = painterResource(id = R.drawable.logout_icon),
                contentDescription = stringResource(R.string.logout_icon),
                tint = MaterialTheme.colorScheme.primary,
                modifier = modifier.size(30.dp))
            Text(
                text = stringResource(R.string.log_out),
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.primary,
                modifier= modifier.align(Alignment.CenterVertically)
            )
        }
    }

}