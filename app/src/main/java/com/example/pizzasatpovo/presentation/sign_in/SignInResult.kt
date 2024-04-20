package com.example.pizzasatpovo.presentation.sign_in

import com.example.pizzasatpovo.data.UserData

data class SignInResult(
    val data: UserData?,
    val errorMessage: String?
)

