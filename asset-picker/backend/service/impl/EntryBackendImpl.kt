package voidthinking.backend.service

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import voidthinking.backend.model.Asset
import voidthinking.backend.model.AssetManifest
import java.nio.file.FileVisitResult
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.SimpleFileVisitor
import java.nio.file.attribute.BasicFileAttributes


class EntryBackendImpl(val rootDir: Path) : EntryBackend {
    private var cachedAssets: List<Asset> = emptyList()
    private var fileModifiedTimes: MutableMap<Path, Long> = mutableMapOf()
    private var directoryModifiedTimes: MutableMap<String, Long> = mutableMapOf()

    override fun scanAssets(directory: Path): List<Asset> {
        if (!Files.exists(directory) || !Files.isDirectory(directory)) {
            return emptyList()
        }

        val modifiedDirectories = mutableSetOf<Path>()
        val currentDirectoryTimes = mutableMapOf<String, Long>()

        Files.walkFileTree(directory, object : SimpleFileVisitor<Path>() {
            override fun preVisitDirectory(dir: Path, attrs: BasicFileAttributes): FileVisitResult {
                val dirModifiedTime = Files.getLastModifiedTime(dir).toMillis()
                val dirPathStr = dir.toString()
                currentDirectoryTimes[dirPathStr] = dirModifiedTime

                if (dirModifiedTime > (directoryModifiedTimes[dirPathStr] ?: 0)) {
                    modifiedDirectories.add(dir)
                }

                return FileVisitResult.CONTINUE
            }
        })

        if (modifiedDirectories.isEmpty() && cachedAssets.isNotEmpty()) {
            return cachedAssets
        }

        directoryModifiedTimes.putAll(currentDirectoryTimes)

        val updatedAssets = mutableListOf<Asset>()
        val updatedFileTimes = mutableMapOf<Path, Long>()

        if (modifiedDirectories.isNotEmpty()) {
            for (modifiedDir in modifiedDirectories) {
                scanDirectoryTree(modifiedDir, updatedAssets, updatedFileTimes)
            }
            for (cachedAsset in cachedAssets) {
                val parentDir = cachedAsset.manifestPath.parent
                if (parentDir !in modifiedDirectories) {
                    updatedAssets.add(cachedAsset)
                    updatedFileTimes[cachedAsset.manifestPath] =
                        Files.getLastModifiedTime(cachedAsset.manifestPath).toMillis()
                }
            }
        }

        fileModifiedTimes = updatedFileTimes
        return updatedAssets
    }

    private fun scanDirectoryTree(
        directory: Path,
        assets: MutableList<Asset>,
        fileTimes: MutableMap<Path, Long>
    ) {
        Files.walkFileTree(directory, object : SimpleFileVisitor<Path>() {
            override fun visitFile(file: Path, attrs: BasicFileAttributes): FileVisitResult {
                if (file.toString().endsWith(".json") && file.parent != null) {
                    processAssetFile(file, assets, fileTimes)
                }
                return FileVisitResult.CONTINUE
            }
        })
    }

    private fun processAssetFile(
        file: Path,
        assets: MutableList<Asset>,
        fileTimes: MutableMap<Path, Long>
    ) {
        val fileModifiedTime = Files.getLastModifiedTime(file).toMillis()
        fileTimes[file] = fileModifiedTime

        val cachedAsset = cachedAssets.find { it.manifestPath == file }
        if (cachedAsset != null && fileModifiedTime <= (fileModifiedTimes[file] ?: 0)) {
            assets.add(cachedAsset)
        } else {
            val fileNameWithoutExtension = file.fileName.toString().substringBeforeLast(".")
            val parentDir = file.parent

            val jsonString = Files.readString(file)
            val manifest = Json.decodeFromString<AssetManifest>(jsonString)

            val supportedImageExtensions = listOf(".png", ".jpg", ".jpeg", ".gif", ".bmp")
            val previewPath = supportedImageExtensions
                .map { ext -> parentDir.resolve("$fileNameWithoutExtension$ext") }
                .find { imagePath -> Files.exists(imagePath) }

            assets.add(Asset(manifest, file, previewPath))
        }
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
