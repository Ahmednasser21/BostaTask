package com.task.bosta.data

import com.task.bosta.data.dto.CityResponse
import retrofit2.http.GET

interface DeliveryAreaAPIService {
    @GET("cities/getAllDistricts?countryId=60e4482c7cb7d4bc4849c4d5")
    suspend fun getDeliveryArea(): CityResponse
}