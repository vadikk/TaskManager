package com.example.managingtask.data.model.responce

import com.example.managingtask.data.model.ErrorModel
import com.google.gson.annotations.SerializedName

data class AuthorizationResponce (
    @SerializedName("token") val token: String?,
    @SerializedName("message") val message: String?,
    @SerializedName("fields") val fields: ErrorModel?
)