package com.haman.feature.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.haman.core.common.state.UiEvent
import com.haman.core.model.ui.ImageUiModel
import com.haman.feature.home.HomeScreen
import kotlinx.coroutines.flow.MutableSharedFlow

const val HomeRoute = "home"

fun NavController.navigateToHome() {
    this.navigate(HomeRoute)
}

/**
 * HomeScreen
 * @param uiEvent MainViewModel 에 있는 전체 Ui Event Flow 관리
 * @param toDetail 상세 화면으로 이동
 */
fun NavGraphBuilder.homeScreen(
    uiEvent: MutableSharedFlow<UiEvent>,
    toDetail: (String, ImageUiModel) -> Unit
) {
    composable(
        route = HomeRoute
    ) {
        HomeScreen(uiEvent, toDetail)
    }
}