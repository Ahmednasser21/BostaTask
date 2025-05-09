package com.task.bosta.di

import com.task.bosta.data.repository.DeliveryAreaRepository
import com.task.bosta.data.repository.DeliveryAreaRepositoryImp
import com.task.bosta.domain.DeliveryAreaUseCase
import com.task.bosta.domain.DeliveryAreaUseCaseImp
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class DataModule {
    @Binds
    @Singleton
    abstract fun bindDeliveryAreaRepo(
        deliveryAreaRepositoryImp: DeliveryAreaRepositoryImp
    ): DeliveryAreaRepository

    @Binds
    @Singleton
    abstract fun bindDeliveryAreaUseCase(
        deliveryAreaUseCaseIml: DeliveryAreaUseCaseImp
    ): DeliveryAreaUseCase
}