package com.example.pizzasatpovo.database.sign_in

import com.example.pizzasatpovo.data.model.UserData

data class SignInResult(
    val data: UserData?,
    val errorMessage: String?
)

