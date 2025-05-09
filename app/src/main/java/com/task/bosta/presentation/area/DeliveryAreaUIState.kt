package com.task.bosta.presentation.area

import com.task.bosta.presentation.area.dto.CityUI

sealed class DeliveryAreaUIState {
    object Loading : DeliveryAreaUIState()
    data class OnSuccess(val cities: List<CityUI>) : DeliveryAreaUIState()
    data class OnFailed(val msg: String) : DeliveryAreaUIState()
}