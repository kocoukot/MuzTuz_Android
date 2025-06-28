package com.muztus.core_mvi

import kotlinx.coroutines.flow.MutableStateFlow

interface AdsActivity {

    val _adState: MutableStateFlow<Boolean>

    fun showAd()

    fun onCoinsGot(amount: Int)

    fun addLoaded(isLoaded: Boolean)
}