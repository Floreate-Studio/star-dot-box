package voidthinking.texteditor.util.color

import androidx.compose.ui.graphics.Color

fun Color.toHex() = (red * 255f).toInt().toString(16).uppercase().padStart(2, '0') +
        (green * 255f).toInt().toString(16).uppercase().padStart(2, '0') +
        (blue * 255f).toInt().toString(16).uppercase().padStart(2, '0')
