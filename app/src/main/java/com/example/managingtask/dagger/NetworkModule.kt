package com.example.managingtask.dagger

import android.content.Context
import com.example.managingtask.api.ApiClient
import com.example.managingtask.data.IApiClient
import com.example.managingtask.data.IAuthorizationRepository
import com.example.managingtask.data.ITaskRepository
import com.example.managingtask.data.ITokenManager
import com.example.managingtask.data.manager.TokenManager
import com.example.managingtask.data.repositories.AuthorizationRepository
import com.example.managingtask.data.repositories.TaskRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class NetworkModule {

    @Provides
    @Singleton
    fun getApiClient(tokenManager: ITokenManager): IApiClient = ApiClient(tokenManager)

    @Provides
    fun getAuthRepository(apiClient: IApiClient): IAuthorizationRepository = AuthorizationRepository(apiClient)

    @Provides
    @Singleton
    fun getTokenManager(context: Context): ITokenManager = TokenManager(context)

    @Provides
    fun getTaskRepository(apiClient: IApiClient, tokenManager: ITokenManager): ITaskRepository = TaskRepository(apiClient, tokenManager)
}