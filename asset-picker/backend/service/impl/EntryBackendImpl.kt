package voidthinking.backend.service

import voidthinking.backend.model.Asset
import java.nio.file.Files
import java.nio.file.Path
import java.util.stream.Collectors

class EntryBackendImpl(val rootDir: Path) : EntryBackend {
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

                val supportedImageExtensions = listOf(".png", ".jpg", ".jpeg", ".gif", ".bmp")
                val previewPath = supportedImageExtensions
                    .map { ext -> parentDir.resolve("$fileNameWithoutExtension$ext") }
                    .find { imagePath -> Files.exists(imagePath) }

                Asset(jsonPath, previewPath)
            }
            .collect(Collectors.toList())
    }

    override fun searchAssets(assets: List<Asset>, query: String): List<Asset> {
        if (query.isEmpty()) return assets

        return assets.filter { asset ->
            val fileName = asset.jsonFile.fileName.toString().substringBeforeLast(".")
            fileName.contains(query, ignoreCase = true)
        }
    }
}