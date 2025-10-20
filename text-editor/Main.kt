package voidthinking.texteditor

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import voidthinking.star.box.plugin.StarDotBoxPlugin
import voidthinking.texteditor.ui.App

@Suppress("unused")
class Main : StarDotBoxPlugin {
    override val name: String
        get() = "文本编辑器"

    override fun start() = main()
}

fun main() = application {
    Window(
        title = "文本编辑器",
        onCloseRequest = ::exitApplication,
    ) {
        App()
    }
}
