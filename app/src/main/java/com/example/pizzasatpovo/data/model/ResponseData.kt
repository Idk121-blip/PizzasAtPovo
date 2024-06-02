package com.example.pizzasatpovo.data.model

data class ResponseData<T>(val isSuccessful: Boolean = false,
                           val message:String="Not logged",
                           val retrievedObject: T? =null)
