package com.muztus.premium_select_feature

import com.muztus.core_mvi.BaseViewModel
import com.muztus.domain_layer.model.PremiumModel
import com.muztus.premium_select_feature.data.premiaDisksList
import com.muztus.premium_select_feature.model.PremiaSelectActions
import com.muztus.premium_select_feature.model.PremiaSelectState
import com.muztus.premium_select_feature.model.PremiumAction
import com.muztus.premium_select_feature.model.PremiumSelectRoute
import kotlinx.coroutines.flow.MutableStateFlow

class PremiaSelectViewModel : BaseViewModel.Base<PremiaSelectState, PremiaSelectActions>(
    mState = MutableStateFlow(PremiaSelectState())
), PremiumAction {

    init {
        val premiumData = premiaDisksList.mapIndexed { index, item ->
            PremiumModel.Base(
                premiumNumber = index,
                premiumProgress = 0,
                premiumLvlAmount = item.size,
                premiumImage = item.first(),
            )
        }
        updateInfo {
            copy(data = premiumData)
        }
    }

    override fun setInputActions(action: PremiaSelectActions) {
        action.handel(this)
    }

    override fun selectPremia(premia: PremiumModel) {
        sendRoute(PremiumSelectRoute.GoLevelSelect(premia.premiumNumber()))
    }

}