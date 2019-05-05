package com.example.managingtask.activities.createTaskActivity

import android.widget.Button

interface CreateTaskActivityContract {

    interface View{
        fun goToNextView(dueBy:String, title: String)
    }

    interface Presenter{
        fun init(view: View)
        fun destroy()
        fun createTask(title: String, priority: String)
        fun passDateValue(year: Int, month: Int, dayOfMonth: Int)
        fun passTimeValue(hourOfDay: Int, minute: Int)
        fun passTaskText(string: String)
        fun passPriority(string: String)
    }
}