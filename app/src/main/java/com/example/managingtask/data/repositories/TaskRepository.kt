package com.example.managingtask.data.repositories

import com.example.managingtask.data.IApiClient
import com.example.managingtask.data.ITaskRepository
import com.example.managingtask.data.ITokenManager
import com.example.managingtask.data.model.request.TaskRequest
import com.example.managingtask.data.model.responce.TaskListResponce
import com.example.managingtask.data.model.responce.TaskResponce
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class TaskRepository @Inject constructor(
    private val apiClient: IApiClient,
    private val tokenManager: ITokenManager
) : ITaskRepository {

    override fun getTaskList(page: Int, sort: String): Observable<TaskListResponce> {
        return apiClient.service.getTaskList(page, sort)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun createTask(title: String, dueBy: String, priority: String): Observable<TaskResponce> {
        return apiClient.service.createTask(TaskRequest(title, dueBy, priority))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun getTaskDetail(taskID: Int): Observable<TaskResponce> {
        return apiClient.service.getTaskDetail(taskID)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun deleteTask(taskID: Int): Completable {
        return apiClient.service.deleteTask(taskID)
            .subscribeOn(Schedulers.io())

    }

    override fun updateTask(taskID: Int, title: String, dueBy: String, priority: String): Completable {
        return apiClient.service.updateTask(taskID, TaskRequest(title, dueBy, priority))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}