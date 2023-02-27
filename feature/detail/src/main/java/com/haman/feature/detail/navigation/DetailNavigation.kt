package com.haman.feature.detail.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.haman.core.designsystem.theme.DaangnBlackTheme
import com.haman.feature.detail.DetailScreen

const val DetailRoute = "detail"
private const val ImageIdArgs = "image_id"
private const val AuthorIdArgs = "author_name"
private val arguments = listOf(
    navArgument(ImageIdArgs) { type = NavType.StringType },
    navArgument(AuthorIdArgs) { type = NavType.StringType }
)
private const val DetailNavigationRoute = "${DetailRoute}/{${ImageIdArgs}}/{${AuthorIdArgs}}"


fun NavController.navigateToDetail(imageId: String, author: String) {
    this.navigate("${DetailRoute}/$imageId/$author")
}

/**
 * DetailScreen
 * @param onBackPressed 이전 화면으로 이동
 */
fun NavGraphBuilder.detailScreen(
    onBackPressed: () -> Unit
) {
    composable(
        route = DetailNavigationRoute,
        arguments = arguments
    ) {
        // 이미지의 id 와 해당 이미지 작성자
        val imageId = it.arguments?.getString(ImageIdArgs)
        val authorName = it.arguments?.getString(AuthorIdArgs)
        DaangnBlackTheme {
            DetailScreen(
                imageId = imageId,
                author = authorName,
                onBackPressed = onBackPressed
            )
        }
    }
}