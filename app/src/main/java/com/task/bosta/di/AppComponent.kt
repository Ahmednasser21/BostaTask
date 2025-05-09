package com.task.bosta.di

import com.task.bosta.presentation.BostaApplication
import com.task.bosta.presentation.area.DeliveryAreaFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        DataModule::class,
        NetworkModule::class,
        DispatcherModule::class,
        ViewModelModule::class
    ]
)
interface AppComponent {
    fun inject(application: BostaApplication)
    fun inject(fragment: DeliveryAreaFragment)
}