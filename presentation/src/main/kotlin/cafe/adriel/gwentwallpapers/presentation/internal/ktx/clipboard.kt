package cafe.adriel.gwentwallpapers.presentation.internal.ktx

import android.content.ClipData
import android.content.ClipboardManager

internal fun ClipboardManager.copy(text: String, label: String = "") =
    setPrimaryClip(ClipData.newPlainText(label, text))
