package com.task.bosta.presentation.area.dto

data class CityUI(
    val cityId: String,
    val cityName: String,
    val cityOtherName: String,
    val cityCode: String,
    val districts: List<DistrictUI>,
    val pickupAvailability: Boolean,
    val dropOffAvailability: Boolean,
    val isExpanded: Boolean = false
)