package voidthinking.texteditor.ui.colorpicker

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.github.skydoves.colorpicker.compose.BrightnessSlider
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController
import voidthinking.texteditor.state.colors.predefinedColors
import voidthinking.texteditor.util.color.toHex

@Composable
fun ColorPickerDialog(
    onDismissRequest: () -> Unit = {},
    value: Color,
    onValueChanged: (Color) -> Unit,
) {
    val initialColor = remember { value }
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Text("文本颜色")
        },
        confirmButton = {
            TextButton(onClick = {
                onDismissRequest()
            }) {
                Text("完成")
            }
        },
        dismissButton = null,
        icon = null,
        text = {
            val controller = rememberColorPickerController()
            LaunchedEffect(Unit) {
                controller.selectByColor(value, true)
            }

            val selectedColor by controller.selectedColor
            var textFieldValue by remember { mutableStateOf(value.toHex()) }
            LaunchedEffect(selectedColor) {
                if (selectedColor == value) {
                    return@LaunchedEffect
                }
                onValueChanged(selectedColor)
                textFieldValue = selectedColor.toHex()
            }
            LaunchedEffect(textFieldValue) {
                val value = textFieldValue.takeIf { it.length == 6 }?.toIntOrNull(16) ?: return@LaunchedEffect
                val color = Color(value.toLong() or 0xFF000000)
                if (color == selectedColor) {
                    return@LaunchedEffect
                }
                controller.selectByColor(color, true)
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.height(IntrinsicSize.Min),
            ) {
                Column(
                    modifier = Modifier
                        .width(192.dp)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    HsvColorPicker(
                        modifier = Modifier.size(192.dp),
                        controller = controller,
                        initialColor = value,
                    )

                    BrightnessSlider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(32.dp),
                        controller = controller,
                    )

                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        prefix = { Text("#") },
                        value = textFieldValue,
                        onValueChange = { textFieldValue = it },
                    )
                }
                Column(
                    modifier = Modifier
                        .width(256.dp)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(32.dp)
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.outline,
                                shape = MaterialTheme.shapes.medium,
                            )
                            .clip(MaterialTheme.shapes.medium)
                    ) {
                        Spacer(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .background(initialColor)
                        )
                        Spacer(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .background(selectedColor)
                        )
                    }

                    var selectedIndex by remember { mutableIntStateOf(0) }
                    val selectedColor = predefinedColors[selectedIndex].second
                    SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                        predefinedColors.forEachIndexed { index, colors ->
                            SegmentedButton(
                                shape = SegmentedButtonDefaults.itemShape(
                                    index = index,
                                    count = predefinedColors.size,
                                ),
                                onClick = { selectedIndex = index },
                                selected = index == selectedIndex,
                                label = { Text(colors.first) }
                            )
                        }
                    }

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxHeight()
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.outline,
                                shape = MaterialTheme.shapes.medium,
                            )
                            .clip(MaterialTheme.shapes.medium)
                            .height(64.dp),
                        verticalArrangement = Arrangement.Top,
                    ) {
                        items(selectedColor.colors) { item ->
                            TextButton(
                                shape = MaterialTheme.shapes.medium,
                                onClick = {
                                    controller.selectByColor(item.color, true)
                                },
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    Spacer(
                                        modifier = Modifier
                                            .size(16.dp)
                                            .background(item.color)
                                    )
                                    Text(
                                        modifier = Modifier.weight(1f),
                                        text = item.name,
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    )
}
