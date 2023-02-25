package com.haman.feature.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.haman.feature.home.HomeScreen

const val HomeRoute = "home"

fun NavController.navigateToHome() {
    this.navigate(HomeRoute)
}

/**
 * HomeScreen
 * @param toDetail 상세 화면으로 이동
 */
fun NavGraphBuilder.homeScreen(toDetail: (String) -> Unit) {
    composable(
        route = HomeRoute
    ) {
        HomeScreen()
    }
}