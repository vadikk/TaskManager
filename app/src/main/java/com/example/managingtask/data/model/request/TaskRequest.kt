package com.example.managingtask.data.model.request

import com.google.gson.annotations.SerializedName

data class TaskRequest (
    @SerializedName("title") val title: String,
    @SerializedName("dueBy") val dueByTime: String,
    @SerializedName("priority") val priority: String
)