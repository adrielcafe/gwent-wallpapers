package cafe.adriel.gwentwallpapers.presentation.internal.ktx

import android.content.Context
import androidx.core.content.FileProvider
import java.io.File

internal fun File.toProviderUri(context: Context, providerAuthority: String) =
    FileProvider.getUriForFile(context, providerAuthority, this)
