package com.task.bosta.features

import app.cash.turbine.test
import com.task.bosta.data.TestData
import com.task.bosta.domain.DeliveryAreaDomainState
import com.task.bosta.domain.DeliveryAreaUseCase
import com.task.bosta.presentation.area.DeliveryAreaUIState
import com.task.bosta.presentation.area.viewmodel.DeliveryAreaViewModule
import com.task.bosta.utils.BaseViewModelTest
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DeliveryAreaViewModelTest : BaseViewModelTest() {

    private val mockUseCase: DeliveryAreaUseCase = mockk()
    private lateinit var viewModel: DeliveryAreaViewModule

    @Before
    override fun setUp() {
        super.setUp()
        viewModel = DeliveryAreaViewModule(mockUseCase, testDispatcher)
    }

    @Test
    fun `initial state is Loading`() = runTest {
        viewModel.deliveryAreaStateFlow.test {
            assertEquals(DeliveryAreaUIState.Loading, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getDeliveryAreas success emits correct state`() = runTest {
        // Given
        val stateFlow = MutableSharedFlow<DeliveryAreaDomainState>()
        coEvery { mockUseCase.invoke() } returns stateFlow

        // When
        viewModel.getDeliveryAreas()

        val collectJob = launch {
            viewModel.deliveryAreaStateFlow.collect {}
        }

        assertEquals(DeliveryAreaUIState.Loading, viewModel.deliveryAreaStateFlow.value)

        stateFlow.emit(TestData.testDomainSuccess)

        // Then
        assertEquals(
            DeliveryAreaUIState.OnSuccess(listOf(TestData.alexandriaCity)),
            viewModel.deliveryAreaStateFlow.value
        )

        collectJob.cancel()
    }

    @Test
    fun `getDeliveryAreas failure emits error state`() = runTest {
        // Given
        val stateFlow = MutableSharedFlow<DeliveryAreaDomainState>()
        coEvery { mockUseCase.invoke() } returns stateFlow

        // When
        viewModel.getDeliveryAreas()

        val collectJob = launch {
            viewModel.deliveryAreaStateFlow.collect {}
        }

        assertEquals(DeliveryAreaUIState.Loading, viewModel.deliveryAreaStateFlow.value)

        stateFlow.emit(TestData.testDomainError)

        // Then
        val currentState = viewModel.deliveryAreaStateFlow.value as DeliveryAreaUIState.OnFailed
        assertEquals("Failed to load data", currentState.msg)

        collectJob.cancel()
    }

    @Test
    fun `filterCities with empty query shows all cities`() = runTest {
        // Given
        val stateFlow = MutableSharedFlow<DeliveryAreaDomainState>()
        coEvery { mockUseCase.invoke() } returns stateFlow

        viewModel.getDeliveryAreas()
        stateFlow.emit(TestData.testDomainSuccess)

        // When
        viewModel.filterCities("")

        // Then
        val state = viewModel.deliveryAreaStateFlow.value as DeliveryAreaUIState.OnSuccess
        assertEquals(1, state.cities.size)
        assertEquals(2, state.cities[0].districts.size)
        assertEquals("Alexandria", state.cities[0].cityName)
    }

    @Test
    fun `filterCities with unique district name shows only matching district`() = runTest {
        // Given
        val stateFlow = MutableSharedFlow<DeliveryAreaDomainState>()
        coEvery { mockUseCase.invoke() } returns stateFlow

        viewModel.getDeliveryAreas()
        stateFlow.emit(TestData.testDomainSuccess)

        // When
        viewModel.filterCities("Qetaa ElTarik")

        // Then
        val state = viewModel.deliveryAreaStateFlow.value as DeliveryAreaUIState.OnSuccess
        assertEquals(1, state.cities.size)
        assertEquals(1, state.cities[0].districts.size)
        assertEquals("Qetaa ElTarik ElSahrawi", state.cities[0].districts[0].districtName)
        assertTrue(state.cities[0].isExpanded)
    }

    @Test
    fun `filterCities with city name shows all districts in that city`() = runTest {
        // Given
        val stateFlow = MutableSharedFlow<DeliveryAreaDomainState>()
        coEvery { mockUseCase.invoke() } returns stateFlow

        viewModel.getDeliveryAreas()
        stateFlow.emit(TestData.testDomainSuccess)

        // When
        viewModel.filterCities("Alexandria")

        // Then
        val state = viewModel.deliveryAreaStateFlow.value as DeliveryAreaUIState.OnSuccess
        assertEquals(1, state.cities.size)
        assertEquals(2, state.cities[0].districts.size)
        assertEquals("Alexandria", state.cities[0].cityName)
        assertTrue(state.cities[0].isExpanded)
    }

    @Test
    fun `filterCities with non-matching query returns empty list`() = runTest {
        // Given
        val stateFlow = MutableSharedFlow<DeliveryAreaDomainState>()
        coEvery { mockUseCase.invoke() } returns stateFlow

        viewModel.getDeliveryAreas()
        stateFlow.emit(TestData.testDomainSuccess)

        // When
        viewModel.filterCities("NonExistentLocation")

        // Then
        val state = viewModel.deliveryAreaStateFlow.value as DeliveryAreaUIState.OnSuccess
        assertEquals(0, state.cities.size)
    }

    @Test
    fun `toggleCityExpansion changes expansion state`() = runTest {
        // Given
        val stateFlow = MutableSharedFlow<DeliveryAreaDomainState>()
        coEvery { mockUseCase.invoke() } returns stateFlow

        viewModel.getDeliveryAreas()
        stateFlow.emit(TestData.testDomainSuccess)


        val initialState = viewModel.deliveryAreaStateFlow.value as DeliveryAreaUIState.OnSuccess
        assertFalse(initialState.cities[0].isExpanded)

        // When
        viewModel.toggleCityExpansion("Jrb6X6ucjiYgMP4T7")

        // Then
        val expandedState = viewModel.deliveryAreaStateFlow.value as DeliveryAreaUIState.OnSuccess
        assertTrue(expandedState.cities[0].isExpanded)

        viewModel.toggleCityExpansion("Jrb6X6ucjiYgMP4T7")

        // Then
        val collapsedState = viewModel.deliveryAreaStateFlow.value as DeliveryAreaUIState.OnSuccess
        assertFalse(collapsedState.cities[0].isExpanded)
    }

    @Test
    fun `toggleCityExpansion with invalid id does not change state`() = runTest {
        // Given
        val stateFlow = MutableSharedFlow<DeliveryAreaDomainState>()
        coEvery { mockUseCase.invoke() } returns stateFlow

        viewModel.getDeliveryAreas()
        stateFlow.emit(TestData.testDomainSuccess)

        val beforeState = viewModel.deliveryAreaStateFlow.value as DeliveryAreaUIState.OnSuccess

        // When
        viewModel.toggleCityExpansion("InvalidId")

        // Then
        val afterState = viewModel.deliveryAreaStateFlow.value as DeliveryAreaUIState.OnSuccess
        assertEquals(beforeState, afterState)
    }

    @Test
    fun `filterCities with zone name shows matching districts`() = runTest {
        // Given
        val stateFlow = MutableSharedFlow<DeliveryAreaDomainState>()
        coEvery { mockUseCase.invoke() } returns stateFlow

        viewModel.getDeliveryAreas()
        stateFlow.emit(TestData.testDomainSuccess)


        // When
        viewModel.filterCities("Abu Yousef")

        // Then
        val state = viewModel.deliveryAreaStateFlow.value as DeliveryAreaUIState.OnSuccess
        assertEquals(1, state.cities.size)
        assertEquals(2, state.cities[0].districts.size)
        assertEquals("Abu Yousef", state.cities[0].districts[0].zoneName)
        assertEquals("Abu Yousef", state.cities[0].districts[1].zoneName)
        assertTrue(state.cities[0].isExpanded)
    }

    @Test
    fun `filterCities with city other name shows all districts`() = runTest {
        // Given
        val stateFlow = MutableSharedFlow<DeliveryAreaDomainState>()
        coEvery { mockUseCase.invoke() } returns stateFlow

        viewModel.getDeliveryAreas()
        stateFlow.emit(TestData.testDomainSuccess)

        // When
        viewModel.filterCities("الاسكندريه")

        // Then
        val state = viewModel.deliveryAreaStateFlow.value as DeliveryAreaUIState.OnSuccess
        assertEquals(1, state.cities.size)
        assertEquals(2, state.cities[0].districts.size)
        assertEquals("الاسكندريه", state.cities[0].cityOtherName)
        assertTrue(state.cities[0].isExpanded)
    }
}