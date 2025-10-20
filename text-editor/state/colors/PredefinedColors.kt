package voidthinking.texteditor.state.colors

import androidx.compose.ui.graphics.Color

data class ColorItem(
    val name: String,
    val color: Color,
)

data class PredefinedColors(
    val colors: List<ColorItem>,
)

val predefinedColors = listOf(
    "深色底" to PredefinedColors(
        colors = listOf(
            ColorItem(name = "标题 | 副标题", color = Color(0xFFECE5D8)),
            ColorItem(name = "普通文字", color = Color(0xFFF6F2EE)),
            ColorItem(name = "普通文字 1", color = Color(0xFFFFFFFF)),
            ColorItem(name = "普通文字 2", color = Color(0xFFFFCC33)),
            ColorItem(name = "关键字 1", color = Color(0xFF37FFFF)),
            ColorItem(name = "关键字 2", color = Color(0xFFFF5E41)),
            ColorItem(name = "警示文字", color = Color(0xFF80C0FF)),
            ColorItem(name = "水元素", color = Color(0xFFFF9999)),
            ColorItem(name = "火元素", color = Color(0xFF80FFD7)),
            ColorItem(name = "风元素", color = Color(0xFFFFACFF)),
            ColorItem(name = "雷元素", color = Color(0xFF99FF88)),
            ColorItem(name = "草元素", color = Color(0xFF99FFFF)),
            ColorItem(name = "冰元素", color = Color(0xFFFFE699)),
            ColorItem(name = "岩元素", color = Color(0xFFFFE699)),
        )
    ),
    "浅色底" to PredefinedColors(
        colors = listOf(
            ColorItem(name = "文字（标准）", color = Color(0xFF84603E)),
            ColorItem(name = "文字 1", color = Color(0xFF4A5366)),
            ColorItem(name = "文字 2（标准）", color = Color(0xFF555555)),
            ColorItem(name = "关键字 1", color = Color(0xFFF39000)),
            ColorItem(name = "关键字 2", color = Color(0xFF3399CC)),
            ColorItem(name = "警示文字", color = Color(0xFFFF5E41)),
            ColorItem(name = "水元素", color = Color(0xFF0075DE)),
            ColorItem(name = "火元素", color = Color(0xFFEF5534)),
            ColorItem(name = "风元素", color = Color(0xFF19C78E)),
            ColorItem(name = "雷元素", color = Color(0xFFB863EE)),
            ColorItem(name = "草元素", color = Color(0xFF7BB100)),
            ColorItem(name = "冰元素", color = Color(0xFF2BB7D5)),
            ColorItem(name = "岩元素", color = Color(0xFFE09800)),
        )
    ),
)