package com.haman.daangnphoto.ui

import androidx.core.splashscreen.SplashScreen

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.haman.core.common.state.ToastPosition
import com.haman.core.common.state.UiEvent
import com.haman.core.designsystem.component.ContentText
import com.haman.daangnphoto.MainViewModel
import com.haman.daangnphoto.navigation.DaangnNavHost
import kotlinx.coroutines.delay

@Composable
fun DaangnPhotoApp(
    splashScreen: SplashScreen,
    viewModel: MainViewModel = hiltViewModel(),
    navController: NavHostController = rememberNavController()
) {
    val uiEvent = viewModel.uiEvent.collectAsState()
    splashScreen.setKeepOnScreenCondition {
        when (uiEvent.value) {
            UiEvent.Initialized -> true
            else -> false
        }
    }
    Scaffold { padding ->
        Box(modifier = Modifier.fillMaxSize()) {
            DaangnNavHost(
                navController = navController,
                mainViewModel = viewModel
            )

            Event(
                event = uiEvent.value
            )
        }
    }
}

@Composable
fun BoxScope.Event(
    event: UiEvent?
) {
    when (event) {
        UiEvent.Initialized -> {
            SplashScreen()
        }
        is UiEvent.Toast -> Toast(
            type = event.position,
            message = event.message
        )
        UiEvent.CompleteLoadInitData -> {}
        null -> {}
    }
}

@Composable
fun BoxScope.Toast(
    type: ToastPosition,
    @StringRes
    message: Int
) {
    val visible = remember { mutableStateOf(false) }
    LaunchedEffect(key1 = message) {
        visible.value = true
        delay(3000L)
        visible.value = false
    }

    if (visible.value) {
        Row(
            modifier = Modifier
                .align(
                    when (type) {
                        ToastPosition.BOTTOM -> Alignment.BottomCenter
                        ToastPosition.MIDDLE -> Alignment.Center
                    }
                )
                .padding(16.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(MaterialTheme.colors.surface.copy(alpha = 0.7f))
                .padding(16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            ContentText(text = stringResource(id = message))
        }
    }
}