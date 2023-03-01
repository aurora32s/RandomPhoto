package com.haman.daangnphoto

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import com.haman.core.common.state.ToastPosition
import com.haman.core.common.state.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {
    val uiEvent = MutableSharedFlow<UiEvent>(
        replay = 3,
        onBufferOverflow = BufferOverflow.DROP_LATEST
    )
}

