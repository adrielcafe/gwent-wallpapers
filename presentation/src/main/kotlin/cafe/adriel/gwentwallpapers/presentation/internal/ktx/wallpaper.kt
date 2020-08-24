package cafe.adriel.gwentwallpapers.presentation.internal.ktx

import android.graphics.Bitmap
import android.graphics.Rect
import cafe.adriel.gwentwallpapers.domain.model.Wallpaper
import cafe.adriel.gwentwallpapers.domain.model.card.CardSide

private const val IMAGE_BASE_URL = "https://cdn.jsdelivr.net/gh/adrielcafe/gwent-cards@7.1/cards"

private const val ORIGINAL_FRONT_IMAGE_URL = "$IMAGE_BASE_URL/front/original/%d.webp"
private const val THUMBNAIL_FRONT_IMAGE_URL = "$IMAGE_BASE_URL/front/thumbnail/%d.webp"

private const val ORIGINAL_BACK_IMAGE_URL = "$IMAGE_BASE_URL/back/original/%d.webp"
private const val THUMBNAIL_BACK_IMAGE_URL = "$IMAGE_BASE_URL/back/thumbnail/%d.webp"

internal fun Bitmap.cropHint(desiredHeight: Int): Rect {
    val desiredWidth = SCREEN_WIDTH * height / desiredHeight
    val offsetX = (width - desiredWidth) / 2
    return Rect(offsetX, 0, width - offsetX, height)
}

internal val Wallpaper.originalImageUrl: String
    get() = when (side) {
        CardSide.FRONT -> ORIGINAL_FRONT_IMAGE_URL
        else -> ORIGINAL_BACK_IMAGE_URL
    }.format(id)

internal val Wallpaper.thumbnailImageUrl: String
    get() = when (side) {
        CardSide.FRONT -> THUMBNAIL_FRONT_IMAGE_URL
        else -> THUMBNAIL_BACK_IMAGE_URL
    }.format(id)
