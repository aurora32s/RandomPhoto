package com.haman.core.common.state

import androidx.annotation.StringRes

sealed interface UiEvent {
    data class Toast(
        @StringRes
        val message: Int,
        val position: ToastPosition = ToastPosition.BOTTOM
    ) : UiEvent
}

enum class ToastPosition {
    BOTTOM, MIDDLE
}