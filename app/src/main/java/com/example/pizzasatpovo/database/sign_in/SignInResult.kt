package com.example.pizzasatpovo.database.sign_in

import com.example.pizzasatpovo.data.dataModel.UserData

data class SignInResult(
    val data: UserData?,
    val errorMessage: String?
)

