package com.example.pizzasatpovo.ui.components

import android.icu.util.Calendar
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.unit.dp
import com.example.pizzasatpovo.data.viewmodel.TimeOrderViewModel
import com.google.firebase.Timestamp
import java.util.Date

@Composable
fun FlipClock(
    timeOrderViewModel: TimeOrderViewModel,
    modifier: Modifier = Modifier
) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,

        ) {
            val hours = remember { (11..14).map { it.toString() } }
            val hoursPickerState = rememberPickerState()


            val minutes = remember { (0..59).step(5).map {
                if(it in 0..9) "0$it" else it.toString()
            } }

            val minutesPickerState = rememberPickerState()

            Row (modifier = modifier.width(100.dp)){
                Picker(
                    state = hoursPickerState,
                    items = hours,
                    visibleItemsCount = 1,
                    modifier = modifier.weight(0.5f),
                    textModifier = Modifier.padding(2.dp)
                )
                Text(
                    text = " : "
                )
                Picker(
                    state = minutesPickerState,
                    items = minutes,
                    visibleItemsCount = 1,
                    modifier = modifier.weight(0.5f),
                    textModifier = Modifier.padding(2.dp)
                )
            }

            val calendar = Calendar.getInstance()
            calendar.time = Date() // Set date object here
            if (hoursPickerState.selectedItem!=""){
                calendar.set(Calendar.HOUR_OF_DAY, hoursPickerState.selectedItem.toInt())
                calendar.set(Calendar.MINUTE, minutesPickerState.selectedItem.toInt())
            }
            calendar.set(Calendar.SECOND, 0)
            timeOrderViewModel.setTime(Timestamp(calendar.time))
        }
}

@Composable
fun rememberPickerState() = remember { PickerState() }

class PickerState {
    var selectedItem by mutableStateOf("")
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Picker(
    items: List<String>,
    modifier: Modifier = Modifier,
    state: PickerState = rememberPickerState(),
    startIndex: Int = 0,
    visibleItemsCount: Int,
    textModifier: Modifier = Modifier,
    textStyle: TextStyle = LocalTextStyle.current,

    ) {
    val visibleItemsMiddle = visibleItemsCount / 3
    val listScrollCount = Integer.MAX_VALUE
    val listScrollMiddle = listScrollCount / 3
    val listStartIndex = listScrollMiddle - listScrollMiddle % items.size - visibleItemsMiddle + startIndex

    fun getItem(index: Int) = items[index % items.size]

    val listState = rememberLazyListState(initialFirstVisibleItemIndex = listStartIndex)
    val flingBehavior = rememberSnapFlingBehavior(lazyListState = listState)

    val itemHeightPixels = remember { mutableIntStateOf(0) }
    val itemHeightDp = pixelsToDp(itemHeightPixels.intValue)

    val fadingEdgeGradient = remember {
        Brush.verticalGradient(
            0f to Color.Transparent,
            0.5f to Color.Black,
            1f to Color.Transparent
        )
    }

    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemIndex }
            .map { index -> getItem(index + visibleItemsMiddle) }
            .distinctUntilChanged()
            .collect { item -> state.selectedItem = item }
    }

    Box(modifier = modifier) {

        LazyColumn(
            state = listState,
            flingBehavior = flingBehavior,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .height(itemHeightDp * visibleItemsCount)
                .fadingEdge(fadingEdgeGradient)
        ) {
            items(listScrollCount) { index ->
                Text(
                    text = getItem(index),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = textStyle,
                    modifier = Modifier
                        .onSizeChanged { size -> itemHeightPixels.intValue = size.height }
                        .then(textModifier)
                )
            }
        }
    }
}

private fun Modifier.fadingEdge(brush: Brush) = this
    .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
    .drawWithContent {
        drawContent()
        drawRect(brush = brush, blendMode = BlendMode.DstIn)
    }

@Composable
private fun pixelsToDp(pixels: Int) = with(LocalDensity.current) { pixels.toDp() }