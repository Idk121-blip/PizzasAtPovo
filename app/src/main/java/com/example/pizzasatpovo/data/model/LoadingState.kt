package com.example.pizzasatpovo.data.model

data class LoadingState(
    val isFinished: Boolean = false,
    val loadingInError: String? = null
)

data class LoadingResult(
    val data: UserData?,
    val errorMessage: String?
)