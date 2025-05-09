package com.task.bosta.domain

import com.task.bosta.data.dto.CityResponse

sealed class DeliveryAreaDomainState {
    data class OnSuccess(val cityResponse: CityResponse ) : DeliveryAreaDomainState()
    data class OnFailed(val error: String? = null) : DeliveryAreaDomainState()

}