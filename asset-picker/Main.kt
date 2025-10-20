package voidthinking.assetpicker

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import voidthinking.star.box.plugin.StarDotBoxPlugin
import voidthinking.assetpicker.ui.App

@Suppress("unused")
class Main : StarDotBoxPlugin {
    override val name: String
        get() = "资源选择器"

    override fun start() = main()
}

fun main() = application {
    Window(
        title = "资源选择器",
        onCloseRequest = ::exitApplication,
    ) {
        App()
    }
}
