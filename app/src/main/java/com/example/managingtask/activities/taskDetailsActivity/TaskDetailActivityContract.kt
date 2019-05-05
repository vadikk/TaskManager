package com.example.managingtask.activities.taskDetailsActivity

import com.example.managingtask.data.model.TaskModel

interface TaskDetailActivityContract {

    interface View{
        fun updateUI(taskOperation: TaskOperation,model: TaskModel)
        fun closeView()
        fun closeEditLayout()
    }

    interface Presenter{
        fun init(view: View)
        fun destroy()
        fun passTaskID(id: Int)
        fun getTaskDetail()
        fun deleteTask()
        fun editTask()
        fun updateTask(title: String, dueBy: String, priority: String)
    }
}