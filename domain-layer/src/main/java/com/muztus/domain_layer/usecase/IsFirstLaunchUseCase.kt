package com.muztus.domain_layer.usecase

import com.muztus.domain_layer.repos.GameRepository

class IsFirstLaunchUseCase(
    private val gameRepository: GameRepository
) {
    operator fun invoke() = gameRepository.isFirstLaunch
}