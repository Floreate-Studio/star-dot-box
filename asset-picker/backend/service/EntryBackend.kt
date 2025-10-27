package voidthinking.backend.service

import kotlinx.coroutines.CoroutineScope
import voidthinking.backend.model.Asset
import java.nio.file.Path

interface EntryBackend {
    fun scanAssets(directory: Path): List<Asset>
    fun searchAssets(assets: List<Asset>, query: String): List<Asset>
    fun refreshAssets(scope: CoroutineScope, callback: (List<Asset>) -> Unit)
}

fun EntryBackend(rootDir: Path): EntryBackend = EntryBackendImpl(rootDir)
