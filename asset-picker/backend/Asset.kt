package voidthinking.backend

import java.nio.file.Path

data class Asset(
    val jsonFile: Path,
    val preview: Path? = null
)
