package com.example.managingtask.data

import com.example.managingtask.data.model.responce.TaskListResponce
import com.example.managingtask.data.model.responce.TaskResponce
import io.reactivex.Completable
import io.reactivex.Observable

interface ITaskRepository {

    fun getTaskList(page: Int, sort: String): Observable<TaskListResponce>
    fun createTask(title: String, dueBy: String, priority: String): Observable<TaskResponce>
    fun getTaskDetail(taskID: Int): Observable<TaskResponce>
    fun deleteTask(taskID: Int): Completable
    fun updateTask(taskID: Int, title: String, dueBy: String, priority: String): Completable
}