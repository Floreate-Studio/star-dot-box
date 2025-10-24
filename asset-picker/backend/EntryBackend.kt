package voidthinking.backend

import java.nio.file.Files
import java.nio.file.Path
import java.util.stream.Collectors

interface EntryBackend {
    fun scanAssets(directory: Path): List<Asset>
}


private class EntryBackendImpl(val rootDir: Path) : EntryBackend {
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
}

fun EntryBackend(rootDir: Path): EntryBackend = EntryBackendImpl(rootDir)