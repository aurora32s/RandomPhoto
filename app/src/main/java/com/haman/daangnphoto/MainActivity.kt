package com.haman.daangnphoto

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.haman.core.designsystem.theme.DaangnPhotoTheme
import com.haman.daangnphoto.ui.DaangnPhotoApp
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        setContent {
            DaangnPhotoTheme {
                DaangnPhotoApp(splashScreen)
            }
        }
    }
}