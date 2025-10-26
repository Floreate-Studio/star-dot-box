package voidthinking.backend.model

import java.nio.file.Path

data class Asset(
    val manifest: AssetManifest,
    val manifestPath: Path,
    val previewPath: Path? = null
)
