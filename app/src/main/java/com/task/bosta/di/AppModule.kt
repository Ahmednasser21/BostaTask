package com.task.bosta.di

import android.content.Context
import com.task.bosta.presentation.BostaApplication
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class AppModule {
    @Binds
    @Singleton
    abstract fun bindApplicationContext(app: BostaApplication): Context
}