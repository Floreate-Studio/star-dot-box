package voidthinking.texteditor.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ClipEntry
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.unit.dp
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import com.mohamedrejeb.richeditor.ui.material3.OutlinedRichTextEditor
import com.mohamedrejeb.richeditor.ui.material3.RichTextEditorDefaults
import kotlinx.coroutines.launch
import voidthinking.texteditor.ui.parser.toGI
import voidthinking.texteditor.ui.richtext.RichTextStyleRow
import java.awt.datatransfer.StringSelection

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App() {
    MaterialTheme(
        colorScheme = darkColorScheme(),
    ) {
        Surface {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                val state = rememberRichTextState()
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                ) {
                    RichTextStyleRow(
                        modifier = Modifier.fillMaxWidth(),
                        state = state,
                    )
                    OutlinedRichTextEditor(
                        modifier = Modifier.weight(1f).fillMaxWidth(),
                        state = state,
                        colors = RichTextEditorDefaults.richTextEditorColors(
                            textColor = Color.White,
                        )
                    )
                }

                var output by remember { mutableStateOf("") }
                LaunchedEffect(state.annotatedString) {
                    output = state.toGI()
                }
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    val clipboard = LocalClipboard.current
                    val scope = rememberCoroutineScope()
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            scope.launch {
                                clipboard.setClipEntry(ClipEntry(StringSelection(output)))
                            }
                        },
                    ) {
                        Text("复制")
                    }

                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.outline,
                                shape = MaterialTheme.shapes.medium,
                            )
                            .padding(16.dp),
                        text = output,
                    )
                }
            }
        }
    }
}
