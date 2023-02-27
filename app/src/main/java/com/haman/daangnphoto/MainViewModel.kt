package com.haman.daangnphoto

import androidx.lifecycle.ViewModel
import com.haman.core.common.state.ToastPosition
import com.haman.core.common.state.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {

    private val _uiEvent = MutableStateFlow<UiEvent>(UiEvent.Initialized)
    val uiEvent: StateFlow<UiEvent?>
        get() = _uiEvent.asStateFlow()

    fun toast(
        position: ToastPosition,
        message: Int
    ) {
        _uiEvent.value = UiEvent.Toast(
            message = message,
            position = position
        )
    }

    fun completeLoadInitData() {
        _uiEvent.value = UiEvent.CompleteLoadInitData
    }
}

