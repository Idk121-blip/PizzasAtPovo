package com.example.pizzasatpovo.data

import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class OrderViewModel:ViewModel() {
    private val _time = MutableStateFlow(Timestamp.now())
    val time= _time.asStateFlow()
    fun setTime(newTime: Timestamp){
        _time.update {
            newTime
        }
    }

}