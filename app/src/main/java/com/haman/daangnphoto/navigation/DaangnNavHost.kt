package com.haman.daangnphoto.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.haman.core.common.state.ToastPosition
import com.haman.feature.detail.navigation.DetailRoute
import com.haman.feature.detail.navigation.detailScreen
import com.haman.feature.detail.navigation.navigateToDetail
import com.haman.feature.home.navigation.HomeRoute
import com.haman.feature.home.navigation.homeScreen

@Composable
fun DaangnNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: String = HomeRoute,
    toast: (ToastPosition, Int) -> Unit
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        homeScreen(navController::navigateToDetail, toast)
        detailScreen(navController::popBackStack)
    }
}