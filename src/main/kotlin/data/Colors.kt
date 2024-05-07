package data

import androidx.compose.material.ButtonColors
import androidx.compose.material.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

class Colors {
    companion object {

        private val darkColorScheme = androidx.compose.material.Colors(
            primary = Color(0xFF2E7D32),         // Forest Green
            primaryVariant = Color(0xFF2E7D32),  // Deep Purple
            secondary = Color(0xFF2E7D32),       // Teal
            secondaryVariant = Color(0xFF1F1F1F),
            background = Color.DarkGray,      // Dark grey
            surface = Color.Gray,         // Another shade of dark
            error = Color(0xFFCF6679),           // Red-ish
            onPrimary = Color(0xFFFFFFFF),       // White for contrast on primary
            onSecondary = Color(0xFF000000),     // Black for contrast on secondary
            onBackground = Color(0xFFFFFFFF),    // White for contrast on background
            onSurface = Color(0xFFFFFFFF),       // White for contrast on surface
            onError = Color(0xFF000000),         // Black for contrast on error
            isLight = false                             // It's a dark theme
        )

        var currentScheme = darkColorScheme

        // Primary action button
        @Composable
        fun primaryButton() = ButtonDefaults.buttonColors(
            backgroundColor = currentScheme.primary,
            contentColor = currentScheme.onPrimary
        )
    }
}


