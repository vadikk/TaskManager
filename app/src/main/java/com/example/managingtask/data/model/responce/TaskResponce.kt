package com.example.managingtask.data.model.responce

import com.example.managingtask.data.model.TaskModel
import com.google.gson.annotations.SerializedName

data class TaskResponce (
    @SerializedName("task") val task: TaskModel
)