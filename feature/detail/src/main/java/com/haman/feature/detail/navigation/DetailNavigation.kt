package com.haman.feature.detail.navigation

import androidx.navigation.*
import androidx.navigation.compose.composable
import com.haman.core.designsystem.theme.DaangnBlackTheme
import com.haman.feature.detail.DetailScreen

const val DetailRoute = "detail"
private const val ImageIdArgs = "image_id"
private val arguments = listOf(navArgument(ImageIdArgs) { type = NavType.IntType })
private const val DetailNavigationRoute = "${DetailRoute}/{${ImageIdArgs}}"


fun NavController.navigateToDetail(navOptions: NavOptions? = null) {
    this.navigate(DetailRoute, navOptions)
}

/**
 * DetailScreen
 * @param onBackPressed 이전 화면으로 이동
 */
fun NavGraphBuilder.detailScreen(onBackPressed: () -> Unit) {
    composable(
        route = DetailRoute,
        arguments = arguments
    ) {
        val imageId = "14"
        DaangnBlackTheme {
            DetailScreen(imageId = imageId, onBackPressed = onBackPressed)
        }
    }
}