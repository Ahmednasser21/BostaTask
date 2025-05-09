package com.task.bosta.presentation.area

import com.task.bosta.presentation.area.dto.CityUI
import com.task.bosta.presentation.area.dto.DistrictUI

sealed class DeliveryAreaItem {
    data class CityItem(val city: CityUI) : DeliveryAreaItem()
    data class DistrictItem(val district: DistrictUI) : DeliveryAreaItem()
}