package com.task.bosta.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.task.bosta.presentation.area.viewmodel.DeliveryAreaViewModelFactory
import com.task.bosta.presentation.area.viewmodel.DeliveryAreaViewModule
import com.task.bosta.presentation.area.viewmodel.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(DeliveryAreaViewModule::class)
    abstract fun bindDeliveryAreaViewModel(viewModel: DeliveryAreaViewModule): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: DeliveryAreaViewModelFactory): ViewModelProvider.Factory
}