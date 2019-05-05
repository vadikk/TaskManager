package com.example.managingtask.data.model

import com.google.gson.annotations.SerializedName

data class TaskModel (
    @SerializedName("id") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("dueBy") val dueBy: String,
    @SerializedName("priority") val priority: String
)