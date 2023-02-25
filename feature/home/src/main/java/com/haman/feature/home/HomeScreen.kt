package com.haman.feature.home

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.haman.core.ui.item.AsyncImage

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel()
) {
    AsyncImage(id = "0", load = viewModel::getImageByUrl)
}