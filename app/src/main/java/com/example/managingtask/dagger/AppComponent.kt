package com.example.managingtask.dagger

import com.example.managingtask.activities.createTaskActivity.CreateTaskActivity
import com.example.managingtask.activities.loginActivity.LoginActivity
import com.example.managingtask.activities.mainActivity.MainActivity
import com.example.managingtask.activities.taskDetailsActivity.TaskDetailActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(AppModule::class,
    NetworkModule::class, PresenterModule::class))
interface AppComponent {

    fun inject(loginActivity: LoginActivity)
    fun inject(mainActivity: MainActivity)
    fun inject(createTaskActivity: CreateTaskActivity)
    fun inject(taskDetailActivity: TaskDetailActivity)
}