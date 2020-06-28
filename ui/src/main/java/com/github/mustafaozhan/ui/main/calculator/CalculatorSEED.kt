/*
 Copyright (c) 2020 Mustafa Ozhan. All rights reserved.
 */
package com.github.mustafaozhan.ui.main.calculator

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.github.mustafaozhan.basemob.model.BaseEffect
import com.github.mustafaozhan.basemob.model.BaseEvent
import com.github.mustafaozhan.basemob.model.BaseState
import com.github.mustafaozhan.basemob.model.BaseStateBacking
import com.github.mustafaozhan.data.model.Currency
import com.github.mustafaozhan.data.model.Rates
import com.github.mustafaozhan.data.preferences.PreferencesRepository
import com.github.mustafaozhan.ui.main.MainData

data class CalculatorState(
    private val backing: CalculatorStateBacking
) : BaseState() {
    val input: LiveData<String> = backing._input
    val base: LiveData<String> = backing._base
    val currencyList: LiveData<MutableList<Currency>> = backing._currencyList
    val output: LiveData<String> = backing._output
    val symbol: LiveData<String> = backing._symbol
    val loading: LiveData<Boolean> = backing._loading
}

@Suppress("ConstructorParameterNaming")
data class CalculatorStateBacking(
    val _input: MediatorLiveData<String> = MediatorLiveData<String>(),
    val _base: MediatorLiveData<String> = MediatorLiveData<String>(),
    val _currencyList: MutableLiveData<MutableList<Currency>> = MutableLiveData<MutableList<Currency>>(),
    val _output: MutableLiveData<String> = MutableLiveData(""),
    val _symbol: MutableLiveData<String> = MutableLiveData(""),
    val _loading: MutableLiveData<Boolean> = MutableLiveData(true)
) : BaseStateBacking()

interface CalculatorEvent : BaseEvent {
    fun onKeyPress(key: String)
    fun onItemClick(currency: Currency, conversion: String)
    fun onItemLongClick(currency: Currency): Boolean
    fun onBarClick()
    fun onSpinnerItemSelected(base: String)
}

sealed class CalculatorEffect : BaseEffect()
object ErrorEffect : CalculatorEffect()
object FewCurrencyEffect : CalculatorEffect()
object OpenBarEffect : CalculatorEffect()
object MaximumInputEffect : CalculatorEffect()
data class OfflineSuccessEffect(val date: String?) : CalculatorEffect()
data class ShowRateEffect(val text: String, val name: String) : CalculatorEffect()

data class CalculatorData(
    private val preferencesRepository: PreferencesRepository
) : MainData(preferencesRepository) {

    companion object {
        internal const val MAXIMUM_INPUT = 15
        internal const val KEY_DEL = "DEL"
        internal const val KEY_AC = "AC"
        internal const val CHAR_DOT = '.'
    }

    var rates: Rates? = null
}