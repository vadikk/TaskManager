package com.example.managingtask.data.model.responce

import com.example.managingtask.data.model.MetaModel
import com.example.managingtask.data.model.TaskModel
import com.google.gson.annotations.SerializedName

data class TaskListResponce (
    @SerializedName("tasks") val taskList: List<TaskModel>,
    @SerializedName("meta") val metaModel: MetaModel
)