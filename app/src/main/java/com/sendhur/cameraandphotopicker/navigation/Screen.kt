package com.sendhur.cameraandphotopicker.navigation

sealed class Screen(val route: String) {
    object GalleryScreen : Screen("gallery_screen")
}