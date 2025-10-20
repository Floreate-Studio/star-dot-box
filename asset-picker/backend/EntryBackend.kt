package voidthinking.backend;

import java.nio.file.Path

interface EntryBackend {

}

private class EntryBackendImpl(val rootDir: Path) : EntryBackend {

}

fun EntryBackend(rootDir: Path): EntryBackend = EntryBackendImpl(rootDir)
