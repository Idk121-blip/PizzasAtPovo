package com.example.pizzasatpovo.data.viewmodel

import androidx.lifecycle.ViewModel
import com.example.pizzasatpovo.data.model.SignInResult
import com.example.pizzasatpovo.data.model.SignInState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SignInViewModel: ViewModel() {
    private val _state = MutableStateFlow(SignInState())
    val state = _state.asStateFlow()
    //private val time = MutableStateFlow()
    fun onSignInResult(result: SignInResult) {

        _state.update { it.copy(
            isSignInSuccessful = result.data != null,
            signInError = result.errorMessage
        ) }
    }

    fun resetState() {
        _state.update { SignInState() }
    }
}