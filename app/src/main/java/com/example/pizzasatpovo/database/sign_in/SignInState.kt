package com.example.pizzasatpovo.database.sign_in
data class SignInState(
    val isSignInSuccessful: Boolean = false,
    val signInError: String? = null
)