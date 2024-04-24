package com.example.pizzasatpovo.presentation.sign_in

import androidx.lifecycle.ViewModel
import com.example.pizzasatpovo.data.RetrievedPizza
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SignInViewModel: ViewModel() {

    private val _state = MutableStateFlow(SignInState())
    val state = _state.asStateFlow()

    private val _selectedPizza= MutableStateFlow(RetrievedPizza())
    val selectedPizza = _selectedPizza.asStateFlow()

    fun onSignInResult(result: SignInResult) {
        _state.update { it.copy(
            isSignInSuccessful = result.data != null,
            signInError = result.errorMessage
        ) }
    }

    fun setPizza(pizza: RetrievedPizza) {
        _selectedPizza.value= pizza
        println(pizza)
    }

    fun getPizza(): RetrievedPizza{
        return selectedPizza.value.copy()
    }



    fun resetState() {
        _state.update { SignInState() }
    }
}