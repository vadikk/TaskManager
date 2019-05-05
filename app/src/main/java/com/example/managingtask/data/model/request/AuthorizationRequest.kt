package com.example.managingtask.data.model.request

import com.google.gson.annotations.SerializedName

data class AuthorizationRequest(
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String
)