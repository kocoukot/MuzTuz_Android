package com.muztus.domain_layer.usecase

import com.muztus.domain_layer.repos.GameRepository

class GetPremiumDataUseCase(
    private val gameRepository: GameRepository
) {
    suspend operator fun invoke(premiumIndex: Int) =
        gameRepository.getSelectedPremiumData(premiumIndex)
}