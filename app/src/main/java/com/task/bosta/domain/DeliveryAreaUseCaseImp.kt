package com.task.bosta.domain

import com.task.bosta.data.repository.DeliveryAreaRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DeliveryAreaUseCaseImp @Inject constructor(
   val  deliveryAreaRepository: DeliveryAreaRepository
): DeliveryAreaUseCase {
    override fun invoke(): Flow<DeliveryAreaDomainState> = flow {
        deliveryAreaRepository.getDeliveryArea()
            .catch { throwable ->
                emit(DeliveryAreaDomainState.OnFailed(throwable.message ?: "Unknown Error"))
            }
            .collect { cityResponse ->
                if (cityResponse.success) {
                    emit(DeliveryAreaDomainState.OnSuccess(cityResponse))
                } else {
                    emit(DeliveryAreaDomainState.OnFailed(cityResponse.message))
                }
            }
    }
}