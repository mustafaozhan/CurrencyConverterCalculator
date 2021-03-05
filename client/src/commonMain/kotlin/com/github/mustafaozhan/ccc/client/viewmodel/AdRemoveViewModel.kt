/*
 * Copyright (c) 2021 Mustafa Ozhan. All rights reserved.
 */

package com.github.mustafaozhan.ccc.client.viewmodel

import com.github.mustafaozhan.ccc.client.base.BaseData
import com.github.mustafaozhan.ccc.client.base.BaseEffect
import com.github.mustafaozhan.ccc.client.base.BaseEvent
import com.github.mustafaozhan.ccc.client.base.BaseSEEDViewModel
import com.github.mustafaozhan.ccc.client.base.BaseState
import com.github.mustafaozhan.ccc.client.model.RemoveAdType
import com.github.mustafaozhan.ccc.client.util.toUnit
import com.github.mustafaozhan.ccc.client.util.update
import com.github.mustafaozhan.ccc.common.settings.SettingsRepository
import com.github.mustafaozhan.logmob.kermit
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock

class AdRemoveViewModel(
    private val settingsRepository: SettingsRepository
) : BaseSEEDViewModel(), AdRemoveEvent {
    // region SEED
    private val _state = MutableStateFlow(AdRemoveState())
    override val state: StateFlow<AdRemoveState> = _state

    private val _effect = Channel<AdRemoveEffect>(1)
    override val effect = _effect.receiveAsFlow().conflate()

    override val event = this as AdRemoveEvent

    override val data: BaseData? = null
    // endregion

    init {
        kermit.d { "AdRemoveViewModel init" }
        _state.update(adRemoveTypes = listOf(RemoveAdType.VIDEO))
    }

    fun updateAddFreeDate() = Clock.System.now().toEpochMilliseconds().let {
        settingsRepository.adFreeActivatedDate = it
    }

    fun showLoadingView(shouldShow: Boolean) {
        _state.update(loading = shouldShow)
    }

    override fun onCleared() {
        kermit.d { "AdRemoveViewModel onCleared" }
        super.onCleared()
    }

    override fun onWatchVideoClick() = clientScope.launch {
        _effect.send(AdRemoveEffect.WatchVideo)
    }.toUnit()

    override fun onAdRemoveItemClick(type: RemoveAdType) = clientScope.launch {
        _effect.send(AdRemoveEffect.Billing(type))
    }.toUnit()
}

// region SEED
data class AdRemoveState(
    val adRemoveTypes: List<RemoveAdType> = listOf(),
    val loading: Boolean = false
) : BaseState() {
    // for ios
    constructor() : this(listOf<RemoveAdType>(), false)
}

interface AdRemoveEvent : BaseEvent {
    fun onWatchVideoClick()
    fun onAdRemoveItemClick(type: RemoveAdType)
}

sealed class AdRemoveEffect : BaseEffect() {
    object WatchVideo : AdRemoveEffect()
    data class Billing(val period: RemoveAdType) : AdRemoveEffect()
}
// endregion
