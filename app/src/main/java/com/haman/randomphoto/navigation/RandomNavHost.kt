package com.haman.randomphoto.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.haman.randomphoto.MainViewModel
import com.haman.feature.detail.navigation.detailScreen
import com.haman.feature.detail.navigation.navigateToDetail
import com.haman.feature.home.navigation.HomeRoute
import com.haman.feature.home.navigation.homeScreen

@Composable
fun RandomNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: String = HomeRoute,
    mainViewModel: MainViewModel
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        homeScreen(
            mainViewModel.uiEvent,
            navController::navigateToDetail
        )
        detailScreen(
            navController::popBackStack
        )
    }
}