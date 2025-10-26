package voidthinking.backend.model

import kotlinx.serialization.Serializable

@Serializable
data class AssetManifest(
    val name: String,
    val id: String,
    val description: String,
    val type: String,
    val tags: List<String>,
)
