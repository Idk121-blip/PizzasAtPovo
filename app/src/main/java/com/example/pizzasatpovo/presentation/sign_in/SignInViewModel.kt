package com.example.pizzasatpovo.presentation.sign_in

import androidx.lifecycle.ViewModel
import com.example.pizzasatpovo.data.RetrievedPizza
import com.google.firebase.Timestamp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDate

class SignInViewModel: ViewModel() {

    private val _state = MutableStateFlow(SignInState())
    val state = _state.asStateFlow()

    private val _selectedPizza= MutableStateFlow(RetrievedPizza())


    //private val time = MutableStateFlow()
    fun onSignInResult(result: SignInResult) {
        _state.update { it.copy(
            isSignInSuccessful = result.data != null,
            signInError = result.errorMessage
        ) }
    }

    fun setPizza(pizza: RetrievedPizza) {
        _selectedPizza.value= pizza
    }

    fun getPizza(): RetrievedPizza{
        return _selectedPizza.value.copy()
    }



    fun resetState() {
        _state.update { SignInState() }
    }
}