package com.haman.daangnphoto

import androidx.annotation.StringRes
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

    /**
     * @param position Toast 를 보여줄 위치(Bottom, Middle)
     * @param message Toast 에 보여줄 메시지의 Resource Id
     */
    fun toast(
        position: ToastPosition,
        @StringRes message: Int
    ) {
        _uiEvent.value = UiEvent.Toast(
            message = message,
            position = position
        )
    }

    // 초기 데이터를 받아온 경우
    fun completeLoadInitData() {
        _uiEvent.value = UiEvent.CompleteLoadInitData
    }
}

