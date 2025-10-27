package voidthinking.backend.service

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import voidthinking.backend.model.Asset
import voidthinking.backend.model.AssetManifest
import java.nio.file.Files
import java.nio.file.Path
import java.util.stream.Collectors

class EntryBackendImpl(val rootDir: Path) : EntryBackend {
    private var cachedAssets: List<Asset> = emptyList()

    override fun scanAssets(directory: Path): List<Asset> {
        if (!Files.exists(directory) || !Files.isDirectory(directory)) {
            return emptyList()
        }

        return Files.walk(directory)
            .filter { path ->
                Files.isRegularFile(path) &&
                        path.toString().endsWith(".json") &&
                        path.parent != null
            }
            .map { jsonPath ->
                val fileNameWithoutExtension = jsonPath.fileName.toString().substringBeforeLast(".")
                val parentDir = jsonPath.parent

                val jsonString = Files.readString(jsonPath)
                val manifest = Json.decodeFromString<AssetManifest>(jsonString)

                val supportedImageExtensions = listOf(".png", ".jpg", ".jpeg", ".gif", ".bmp")
                val previewPath = supportedImageExtensions
                    .map { ext -> parentDir.resolve("$fileNameWithoutExtension$ext") }
                    .find { imagePath -> Files.exists(imagePath) }

                Asset(manifest, jsonPath, previewPath)
            }
            .collect(Collectors.toList())
    }

    override fun searchAssets(assets: List<Asset>, query: String): List<Asset> {
        if (query.isEmpty()) return assets

        return assets.filter { asset ->
            val fileName = asset.manifestPath.fileName.toString().substringBeforeLast(".")
            fileName.contains(query, ignoreCase = true)
        }
    }

    override fun refreshAssets(scope: CoroutineScope, callback: (List<Asset>) -> Unit) {
        scope.launch {
            scanAssets(rootDir).also { cachedAssets = it }
            callback(cachedAssets)
        }
    }
}
