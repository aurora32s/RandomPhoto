package com.haman.daangnphoto.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.haman.feature.detail.navigation.DetailRoute
import com.haman.feature.detail.navigation.detailScreen

@Composable
fun DaangnNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: String = "${DetailRoute}"
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {
        detailScreen { }
    }
}