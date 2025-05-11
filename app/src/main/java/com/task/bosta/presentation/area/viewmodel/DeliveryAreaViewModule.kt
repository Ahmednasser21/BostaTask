package com.task.bosta.presentation.area.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.task.bosta.data.dto.City
import com.task.bosta.data.dto.District
import com.task.bosta.di.MainDispatcher
import com.task.bosta.domain.DeliveryAreaDomainState
import com.task.bosta.domain.DeliveryAreaUseCase
import com.task.bosta.presentation.area.DeliveryAreaUIState
import com.task.bosta.presentation.area.dto.CityUI
import com.task.bosta.presentation.area.dto.DistrictUI
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class DeliveryAreaViewModule @Inject constructor(
    private val deliveryAreaUseCase: DeliveryAreaUseCase,
    @MainDispatcher private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    init {
        getDeliveryAreas()
    }
    private val _deliveryAreaStateFlow =
        MutableStateFlow<DeliveryAreaUIState>(DeliveryAreaUIState.Loading)
    val deliveryAreaStateFlow = _deliveryAreaStateFlow.asStateFlow()

    private var allCities: List<CityUI> = emptyList()
    private var filteredCities: List<CityUI> = emptyList()

    fun getDeliveryAreas() {
        viewModelScope.launch(dispatcher) {
            deliveryAreaUseCase.invoke()
                .collect { domainState ->
                    when (domainState) {
                        is DeliveryAreaDomainState.OnSuccess -> {
                            allCities = domainState.cityResponse.data.map { it.toUI() }
                            filteredCities = allCities
                            _deliveryAreaStateFlow.emit(
                                DeliveryAreaUIState.OnSuccess(filteredCities)
                            )
                        }

                        is DeliveryAreaDomainState.OnFailed -> {
                            _deliveryAreaStateFlow.emit(
                                DeliveryAreaUIState.OnFailed(domainState.error.toString())
                            )
                        }
                    }
                }
        }
    }

    fun filterCities(query: String) {
        if (query.isEmpty()) {
            filteredCities = allCities
        } else {
            filteredCities = allCities.mapNotNull { city ->

                val matchingDistricts = city.districts.filter { district ->
                    district.districtName.contains(query, true) ||
                            district.zoneName.contains(query, true) ||
                            district.districtOtherName.contains(query, true)
                }

                val cityMatches = city.cityName.startsWith(query, true) ||
                        city.cityOtherName.startsWith(query, true)

                when {
                    cityMatches -> {
                        city.copy(isExpanded = true)
                    }

                    matchingDistricts.isNotEmpty() -> {
                        city.copy(
                            districts = matchingDistricts,
                            isExpanded = true
                        )
                    }

                    else -> null
                }
            }
        }
        _deliveryAreaStateFlow.value = DeliveryAreaUIState.OnSuccess(filteredCities)
    }

    fun toggleCityExpansion(cityId: String) {
        filteredCities = filteredCities.map { city ->
            if (city.cityId == cityId) {
                city.copy(isExpanded = !city.isExpanded)
            } else {
                city
            }
        }
        _deliveryAreaStateFlow.value = DeliveryAreaUIState.OnSuccess(filteredCities)
    }

    private fun City.toUI(): CityUI {
        return CityUI(
            cityId = cityId,
            cityName = cityName,
            cityOtherName = cityOtherName,
            cityCode = cityCode,
            districts = districts.map { it.toUI() },
            pickupAvailability = pickupAvailability,
            dropOffAvailability = dropOffAvailability,
            isExpanded = false
        )
    }

    private fun District.toUI(): DistrictUI {
        return DistrictUI(
            zoneId = zoneId,
            zoneName = zoneName,
            zoneOtherName = zoneOtherName,
            districtId = districtId,
            districtName = districtName,
            districtOtherName = districtOtherName,
            pickupAvailability = pickupAvailability,
            dropOffAvailability = dropOffAvailability,
            coverage = coverage
        )
    }
}