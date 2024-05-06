package data

import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.Density


data class Transform(val offset: Offset = Offset.Zero, val scale: Float = 1f, val density: Float = 2f) {
    operator fun plus(other: Offset): Transform {
        return Transform(offset + other, scale, density)
    }

    operator fun times(other: Float): Transform {
        return Transform(offset, scale * other, density)
    }
}