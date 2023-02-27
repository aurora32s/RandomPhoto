package com.haman.core.common.state

import androidx.annotation.StringRes

/**
 * 여러 Feature 에서 공통적으로 발생할 수 있는 Event(State) 들을 MainActivity 에서
 * 관리하도록 추가한 인터페이스입니다.
 */
sealed interface UiEvent {
    // 초기 상태
    object Initialized : UiEvent

    /**
     * 화면에 Toast 를 띄워줍니다.
     * @param message Toast 에 띄워줄 message resource Id
     * @param position Toast 가 보일 위치
     */
    data class Toast(
        @StringRes
        val message: Int,
        val position: ToastPosition = ToastPosition.BOTTOM
    ) : UiEvent

    object CompleteLoadInitData : UiEvent
}

/**
 * Toast 위치
 */
enum class ToastPosition {
    BOTTOM, // 하단
    MIDDLE // 중간
}