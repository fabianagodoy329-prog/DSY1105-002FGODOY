package com.example.fabiana_godoy_modoguardian.ui.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration

enum class WindowSizeClass {
    COMPACT,
    MEDIUM,
    EXPANDED
}

@Composable
fun rememberWindowSizeClass(): WindowSizeClass {
    val configuration = LocalConfiguration.current
    val screenWidthDp = configuration.screenWidthDp

    return when {
        screenWidthDp < 600 -> WindowSizeClass.COMPACT
        screenWidthDp < 840 -> WindowSizeClass.MEDIUM
        else -> WindowSizeClass.EXPANDED
    }
}
