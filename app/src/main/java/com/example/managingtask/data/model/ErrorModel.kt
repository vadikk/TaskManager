package com.example.managingtask.data.model

import com.google.gson.annotations.SerializedName

data class ErrorModel (
    @SerializedName("email") val emailError: List<String>
)