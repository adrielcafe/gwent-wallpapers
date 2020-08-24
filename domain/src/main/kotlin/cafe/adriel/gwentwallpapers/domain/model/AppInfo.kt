package cafe.adriel.gwentwallpapers.domain.model

import java.io.File

data class AppInfo(
    val version: String,
    val releaseBuild: Boolean,
    val marketUri: String,
    val playStoreUrl: String,
    val fileProviderAuthority: String,
    val cardsStorageFile: File,
    val favoritesStorageFile: File,
    val settingsStorageFile: File
)
