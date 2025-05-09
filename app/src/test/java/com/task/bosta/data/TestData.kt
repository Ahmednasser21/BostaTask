package com.task.bosta.data

import com.task.bosta.data.dto.City
import com.task.bosta.data.dto.CityResponse
import com.task.bosta.data.dto.District
import com.task.bosta.domain.DeliveryAreaDomainState
import com.task.bosta.presentation.area.dto.CityUI
import com.task.bosta.presentation.area.dto.DistrictUI

object TestData {

    val alexandriaCity = CityUI(
        cityId = "Jrb6X6ucjiYgMP4T7",
        cityName = "Alexandria",
        cityOtherName = "الاسكندريه",
        cityCode = "EG-02",
        districts = listOf(
            DistrictUI(
                zoneId = "9mih4NXL1GF",
                zoneName = "Abu Yousef",
                zoneOtherName = "ابو يوسف",
                districtId = "zoJP71_5Ca1",
                districtName = "Abu Yousef",
                districtOtherName = "ابو يوسف",
                pickupAvailability = true,
                dropOffAvailability = true,
                coverage = "BOSTA"
            ),
            DistrictUI(
                zoneId = "9mih4NXL1GF",
                zoneName = "Abu Yousef",
                zoneOtherName = "ابو يوسف",
                districtId = "Naw9Fm_UfMS",
                districtName = "Qetaa ElTarik ElSahrawi",
                districtOtherName = "قطاع الطريق الصحراوي",
                pickupAvailability = true,
                dropOffAvailability = true,
                coverage = "BOSTA"
            )
        ),
        pickupAvailability = true,
        dropOffAvailability = true,
        isExpanded = false
    )

    val testDomainSuccess = DeliveryAreaDomainState.OnSuccess(
        CityResponse(
            success = true,
            message = "Done successfully.",
            data = listOf(
                City(
                    cityId = "Jrb6X6ucjiYgMP4T7",
                    cityName = "Alexandria",
                    cityOtherName = "الاسكندريه",
                    cityCode = "EG-02",
                    districts = listOf(
                        District(
                            zoneId = "9mih4NXL1GF",
                            zoneName = "Abu Yousef",
                            zoneOtherName = "ابو يوسف",
                            districtId = "zoJP71_5Ca1",
                            districtName = "Abu Yousef",
                            districtOtherName = "ابو يوسف",
                            pickupAvailability = true,
                            dropOffAvailability = true,
                            coverage = "BOSTA"
                        ),
                        District(
                            zoneId = "9mih4NXL1GF",
                            zoneName = "Abu Yousef",
                            zoneOtherName = "ابو يوسف",
                            districtId = "Naw9Fm_UfMS",
                            districtName = "Qetaa ElTarik ElSahrawi",
                            districtOtherName = "قطاع الطريق الصحراوي",
                            pickupAvailability = true,
                            dropOffAvailability = true,
                            coverage = "BOSTA"
                        )
                    ),
                    pickupAvailability = true,
                    dropOffAvailability = true
                )
            )
        )
    )
    val testDomainError = DeliveryAreaDomainState.OnFailed("Failed to load data")
}
