package com.example.pizzasatpovo.data

import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.DocumentReference
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class LoadingViewModel: ViewModel() {
    private val _state = MutableStateFlow(LoadingState())
    val state = _state.asStateFlow()
    private val _userData = MutableStateFlow(UserData())
    val userData = _userData.asStateFlow()

    fun onSignInResult(result: LoadingResult) {
        _state.update { it.copy(
            isFinished = result.data != null,
            loadingInError = result.errorMessage
        ) }
        if (result.data != null){
            _userData.update {it.copy(
                name= result.data.name,
                role= result.data.role,
                credit = result.data.credit,
                favourites= result.data.favourites,
                image= result.data.image,
                orders= result.data.orders
            )
        }

        }

    }

    //TODO: ADD A RESET SETTING WHEN LOGGED OUT

    fun resetState() {
        _state.update { LoadingState() }
    }
}