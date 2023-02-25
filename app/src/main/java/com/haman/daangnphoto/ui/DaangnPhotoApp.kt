package com.haman.daangnphoto.ui

import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.haman.daangnphoto.navigation.DaangnNavHost

@Composable
fun DaangnPhotoApp(
    navController: NavHostController = rememberNavController()
) {
    Scaffold { padding ->
        DaangnNavHost(
            navController = navController
        )
    }
}