package com.haman.daangnphoto.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.haman.core.common.state.UiEvent
import com.haman.core.designsystem.component.Toast
import com.haman.daangnphoto.MainViewModel
import com.haman.daangnphoto.navigation.DaangnNavHost
import kotlinx.coroutines.delay

// Splash Screen 을 보여줘야 하는 최소 시간
private const val splashTime = 2000L

@Composable
fun DaangnPhotoApp(
    splashScreen: SplashScreen,
    viewModel: MainViewModel = hiltViewModel(),
    navController: NavHostController = rememberNavController()
) {
    val uiEvent = viewModel.uiEvent.collectAsState()
    val timeOutSplashScreen = remember { mutableStateOf(false) }

    LaunchedEffect(key1 = null) {
        delay(splashTime)
        timeOutSplashScreen.value = true
    }

    // 초기 데이터를 받아오고 보여줘야 최소 시간을 넘었을 때 Splash Screen 제거
    splashScreen.setKeepOnScreenCondition {
        if (uiEvent.value is UiEvent.Initialized) true
        else timeOutSplashScreen.value.not()
    }

    Scaffold {
        Box(modifier = Modifier.fillMaxSize()) {
            DaangnNavHost(
                navController = navController,
                mainViewModel = viewModel
            )
            Event(event = uiEvent.value)
        }
    }
}

@Composable
fun BoxScope.Event(
    event: UiEvent?
) {
    when (event) {
        // Toast 띄워주기
        is UiEvent.Toast -> Toast(
            type = event.position,
            message = event.message
        )
        else -> {}
    }
}

