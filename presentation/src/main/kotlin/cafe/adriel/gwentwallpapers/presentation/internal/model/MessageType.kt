package cafe.adriel.gwentwallpapers.presentation.internal.model

import androidx.annotation.AttrRes
import androidx.annotation.ColorRes
import cafe.adriel.gwentwallpapers.presentation.R

internal enum class MessageType(@ColorRes @AttrRes val color: Int) {
    INFO(R.color.info),
    SUCCESS(R.attr.colorPrimaryDark),
    ERROR(R.color.error)
}
