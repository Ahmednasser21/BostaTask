package com.task.bosta.domain

import kotlinx.coroutines.flow.Flow

interface DeliveryAreaUseCase {
   operator fun invoke(): Flow<DeliveryAreaDomainState>
}