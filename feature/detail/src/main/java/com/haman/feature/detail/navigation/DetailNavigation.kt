package com.haman.feature.detail.navigation

import androidx.navigation.*
import androidx.navigation.compose.composable
import com.haman.core.designsystem.theme.DaangnBlackTheme
import com.haman.feature.detail.DetailScreen

const val DetailRoute = "detail"
private const val ImageIdArgs = "image_id"
private val arguments = listOf(navArgument(ImageIdArgs) { type = NavType.StringType })
private const val DetailNavigationRoute = "${DetailRoute}/{${ImageIdArgs}}"


fun NavController.navigateToDetail(imageId: String) {
    this.navigate("${DetailRoute}/$imageId")
}

/**
 * DetailScreen
 * @param onBackPressed 이전 화면으로 이동
 */
fun NavGraphBuilder.detailScreen(onBackPressed: () -> Unit) {
    composable(
        route = DetailNavigationRoute,
        arguments = arguments
    ) {
        val imageId = it.arguments?.getString(ImageIdArgs)
        DaangnBlackTheme {
            DetailScreen(
                imageId = imageId,
                onBackPressed = onBackPressed
            )
        }
    }
}