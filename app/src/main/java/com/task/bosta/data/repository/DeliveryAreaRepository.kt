package com.task.bosta.data.repository

import com.task.bosta.data.dto.CityResponse
import kotlinx.coroutines.flow.Flow

interface DeliveryAreaRepository{
    fun getDeliveryArea() : Flow<CityResponse>
}