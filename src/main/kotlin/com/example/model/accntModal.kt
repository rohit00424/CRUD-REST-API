package com.example.model

import kotlinx.serialization.Serializable

@Serializable

data class UserDataType(
    var id: Int,
    var username: String,
    var password: String,
    var fname: String ,
    var lname: String,
    var email: String
)

data class GlobalResponse (
    val code: Int,
    val status: String
)