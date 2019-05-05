package com.example.managingtask.api

import com.example.managingtask.data.model.request.AuthorizationRequest
import com.example.managingtask.data.model.request.TaskRequest
import com.example.managingtask.data.model.responce.AuthorizationResponce
import com.example.managingtask.data.model.responce.TaskResponce
import com.example.managingtask.data.model.responce.TaskListResponce
import io.reactivex.Completable
import io.reactivex.Observable
import retrofit2.http.*

interface ApiService {

    @POST("users")
    fun signUp(@Body request: AuthorizationRequest): Observable<AuthorizationResponce>

    @POST("auth")
    fun signIn(@Body request: AuthorizationRequest): Observable<AuthorizationResponce>

    @GET("tasks?")
    fun getTaskList(@Query ("page") page: Int,
                    @Query ("sort") sort: String): Observable<TaskListResponce>

    @POST("tasks")
    fun createTask(@Body request: TaskRequest): Observable<TaskResponce>

    @POST("tasks/{task}/")
    fun getTaskDetail(@Path("task") taskID: Int): Observable<TaskResponce>

    @DELETE("tasks/{task}")
    fun deleteTask(@Path("task") taskID: Int): Completable

    @PUT("tasks/{task}")
    fun updateTask(@Path("task") taskID: Int,
                   @Body request: TaskRequest): Completable
}