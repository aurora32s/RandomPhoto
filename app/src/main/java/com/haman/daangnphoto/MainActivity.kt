package com.haman.daangnphoto

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dagger.hilt.android.AndroidEntryPoint
import com.haman.core.designsystem.theme.DaangnPhotoTheme
import com.haman.daangnphoto.ui.DaangnPhotoApp

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DaangnPhotoTheme {
                DaangnPhotoApp()
            }
        }
    }
}