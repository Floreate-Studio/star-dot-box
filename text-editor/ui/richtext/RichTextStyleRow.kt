package voidthinking.texteditor.ui.richtext

import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ClearAll
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.FormatColorText
import androidx.compose.material.icons.outlined.FormatBold
import androidx.compose.material.icons.outlined.FormatItalic
import androidx.compose.material.icons.outlined.FormatStrikethrough
import androidx.compose.material.icons.outlined.FormatUnderlined
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.isSpecified
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import com.mohamedrejeb.richeditor.model.RichTextState
import voidthinking.texteditor.ui.colorpicker.ColorPickerDialog

@Composable
fun RichTextStyleRow(
    modifier: Modifier = Modifier,
    state: RichTextState,
) {
    LazyRow(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        item {
            RichTextStyleButton(
                onClick = {
                    state.toggleSpanStyle(
                        SpanStyle(
                            fontStyle = FontStyle.Italic
                        )
                    )
                },
                isSelected = state.currentSpanStyle.fontStyle == FontStyle.Italic,
                icon = Icons.Outlined.FormatItalic
            )
        }

        item {
            var colorPickerShow by remember { mutableStateOf(false) }
            if (colorPickerShow) {
                val focusManager = LocalFocusManager.current
                ColorPickerDialog(
                    onDismissRequest = {
                        // To workaround IME bug
                        focusManager.clearFocus(true)
                        colorPickerShow = false
                    },
                    value = state.currentSpanStyle.color.takeIf { it.isSpecified } ?: Color.White,
                    onValueChanged = {
                        state.toggleSpanStyle(
                            SpanStyle(color = it)
                        )
                    },
                )
            }
            RichTextStyleButton(
                onClick = {
                    colorPickerShow = true
                },
                icon = Icons.Filled.FormatColorText,
                isSelected = state.currentSpanStyle.color.luminance() < 0.8,
                tint = state.currentSpanStyle.color,
            )
        }

        item {
            RichTextStyleButton(
                onClick = {
                    if (state.selection.collapsed) {
                        state.clearSpanStyles()
                    } else {
                        state.clearSpanStyles(state.selection)
                    }
                },
                icon = Icons.Filled.DeleteSweep,
            )
        }
    }
}