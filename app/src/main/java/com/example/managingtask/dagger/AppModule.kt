package com.example.managingtask.dagger

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(var applications: Application) {

    @Provides
    @Singleton
    fun getApplication(): Application = applications

    @Provides
    @Singleton
    fun getContext(): Context = applications
}