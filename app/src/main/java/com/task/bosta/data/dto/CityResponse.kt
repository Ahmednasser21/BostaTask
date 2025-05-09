package com.task.bosta.data.dto

data class CityResponse(
    val success: Boolean,
    val message: String,
    val data: List<City>
)
