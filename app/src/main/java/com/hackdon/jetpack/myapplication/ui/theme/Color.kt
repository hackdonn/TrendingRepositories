package com.hackdon.jetpack.myapplication.ui.theme

import androidx.compose.ui.graphics.Color

// Light Theme Colors
val Primary = Color(0xFF1A73E8)
val OnPrimary = Color(0xFFFFFFFF)
val PrimaryContainer = Color(0xFFD3E3FD)

val Background = Color(0xFFFAFAFA)
val Surface = Color(0xFFFFFFFF)
val SurfaceVariant = Color(0xFFF1F3F4)

val OnSurface = Color(0xFF202124)
val OnSurfaceVariant = Color(0xFF5F6368)

val Outline = Color(0xFFDADCE0)

val Error = Color(0xFFD93025)
val ErrorContainer = Color(0xFFFCE8E6)

// Language Colors (GitHub Standard)
val KotlinColor = Color(0xFFA97BFF)
val OtherLanguageColor = Color(0xFF8B8B8B)

// Star Color
val StarColor = Color(0xFFF4B400)

// Helper function to get language color
fun getLanguageColor(language: String?): Color {
    return when (language?.lowercase()) {
        "kotlin" -> KotlinColor
        else -> OtherLanguageColor
    }
}