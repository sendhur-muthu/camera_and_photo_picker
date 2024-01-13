package com.sendhur.cameraandphotopicker.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sendhur.cameraandphotopicker.MainViewModel
import com.sendhur.cameraandphotopicker.screens.Gallery

@Composable
fun Navigation(viewModel: MainViewModel) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.GalleryScreen.route) {
        composable(route = Screen.GalleryScreen.route) {
            Gallery(viewModel = viewModel)
        }
    }
}