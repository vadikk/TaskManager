package com.example.managingtask.dagger

import com.example.managingtask.activities.createTaskActivity.CreateTaskActivityContract
import com.example.managingtask.activities.createTaskActivity.CreateTaskPresenter
import com.example.managingtask.activities.loginActivity.LoginActivityContract
import com.example.managingtask.activities.loginActivity.LoginPresenter
import com.example.managingtask.activities.mainActivity.MainActivityContarct
import com.example.managingtask.activities.mainActivity.MainActivityPresenter
import com.example.managingtask.activities.taskDetailsActivity.TaskDetailActivityContract
import com.example.managingtask.activities.taskDetailsActivity.TaskDetailPresenter
import com.example.managingtask.data.IAuthorizationRepository
import com.example.managingtask.data.ITaskRepository
import com.example.managingtask.data.ITokenManager
import dagger.Module
import dagger.Provides

@Module
class PresenterModule {

    @Provides
    fun getLoginPresenter(authorizationRepository: IAuthorizationRepository,
                          tokenManager: ITokenManager): LoginActivityContract.Presenter =
            LoginPresenter(authorizationRepository, tokenManager)

    @Provides
    fun getMainActivityPresenter(taskRepository: ITaskRepository): MainActivityContarct.Presenter = MainActivityPresenter(taskRepository)

    @Provides
    fun getCreateTaskPresenter(taskRepository: ITaskRepository): CreateTaskActivityContract.Presenter = CreateTaskPresenter(taskRepository)

    @Provides
    fun getTaskDetailPresenter(taskRepository: ITaskRepository): TaskDetailActivityContract.Presenter = TaskDetailPresenter(taskRepository)
}