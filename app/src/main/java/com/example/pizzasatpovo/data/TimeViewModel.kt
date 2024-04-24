package com.example.pizzasatpovo.data

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

class TimeViewModel: ViewModel() {
    val hour = (0..59)
        .asSequence()
        .asFlow()
        .map{
            if(it in (0..9)) "0$it" else it
        }
        .onEach { delay(1000) }

    val minutes = (0..59)
        .asSequence()
        .asFlow()
        .map{
            if(it in (0..9)) "0$it" else it
        }
        .onEach { delay(1000) }
}