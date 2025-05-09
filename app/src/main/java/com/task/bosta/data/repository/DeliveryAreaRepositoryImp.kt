package com.task.bosta.data.repository

import com.task.bosta.data.DeliveryAreaAPIService
import com.task.bosta.data.dto.CityResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DeliveryAreaRepositoryImp @Inject constructor(val deliveryAreaAPIService: DeliveryAreaAPIService) :
    DeliveryAreaRepository {
    override fun getDeliveryArea(): Flow<CityResponse> = flow {
       emit(deliveryAreaAPIService.getDeliveryArea())
    }
}