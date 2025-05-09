package com.task.bosta.data.dto

data class City(
    val cityId: String,
    val cityName: String,
    val cityOtherName: String,
    val cityCode: String,
    val districts: List<District>,
    val pickupAvailability: Boolean,
    val dropOffAvailability: Boolean
)