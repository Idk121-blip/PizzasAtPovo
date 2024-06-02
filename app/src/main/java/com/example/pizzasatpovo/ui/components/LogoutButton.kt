package com.example.pizzasatpovo.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.pizzasatpovo.R
import com.example.pizzasatpovo.data.viewModels.LoadingViewModel

@Composable
fun LogoutButton(
    viewModel: LoadingViewModel,
    modifier: Modifier = Modifier,
    onLogOutButtonClicked: () -> Unit = {}
) {
    val interactionSource = remember { MutableInteractionSource() }
    val state by viewModel.state.collectAsStateWithLifecycle()
    val showShimmer = remember { mutableStateOf(true) }
    showShimmer.value = !state.isFinished
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .padding(start = 25.dp, end = 25.dp)
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
            contentDescription = "Logout icon",
            tint = MaterialTheme.colorScheme.primary,
            modifier = modifier.size(30.dp)
        )
        Text(
            text = "Log out",
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.primary,
            modifier = modifier.align(Alignment.CenterVertically)
        )
    }
}