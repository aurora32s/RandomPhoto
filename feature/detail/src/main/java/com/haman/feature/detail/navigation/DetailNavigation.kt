package com.haman.feature.detail.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.haman.core.designsystem.theme.DaangnBlackTheme
import com.haman.core.model.ui.ImageUiModel
import com.haman.feature.detail.DetailScreen
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

const val DetailRoute = "detail"
private const val ImageIdArgs = "image_id"
private const val ImageArgs = "image"
private val arguments = listOf(
    navArgument(ImageArgs) { type = NavType.StringType }
)
private const val DetailNavigationRoute = "${DetailRoute}/{${ImageIdArgs}}/{${ImageArgs}}"

/**
 * Detail Screen 으로 이동
 * @param imageId 이미지의 id
 * @param image 이미지 정보
 */
fun NavController.navigateToDetail(imageId: String, image: ImageUiModel) {
    val jsonString = Json.encodeToString(image.copy(imageUrl = ""))
    this.navigate("${DetailRoute}/${imageId}/${jsonString}")
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
        // 이미지의 id 와 해당 이미지 정보
        val imageId = it.arguments?.getString(ImageIdArgs)
        val argument = it.arguments?.getString(ImageArgs)
        val image = Json.decodeFromString<ImageUiModel>(argument ?: "")
        DaangnBlackTheme {
            DetailScreen(
                imageId = imageId,
                image = image,
                onBackPressed = onBackPressed
            )
        }
    }
}