package voidthinking.backend.service

import voidthinking.backend.model.Asset
import java.nio.file.Path

interface EntryBackend {
    fun scanAssets(directory: Path): List<Asset>
    fun searchAssets(assets: List<Asset>, query: String): List<Asset>
}

fun EntryBackend(rootDir: Path): EntryBackend = EntryBackendImpl(rootDir)
