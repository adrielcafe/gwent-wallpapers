package cafe.adriel.gwentwallpapers.presentation.internal.ui.helper

import android.graphics.drawable.Drawable
import cafe.adriel.gwentwallpapers.domain.model.Wallpaper
import cafe.adriel.gwentwallpapers.presentation.R
import cafe.adriel.gwentwallpapers.presentation.internal.ktx.originalImageUrl
import cafe.adriel.gwentwallpapers.presentation.internal.ktx.thumbnailImageUrl
import cafe.adriel.gwentwallpapers.presentation.internal.ui.adapter.WallpaperModelItem
import cafe.adriel.gwentwallpapers.presentation.internal.ui.view.WallpaperView.Companion.IMAGE_HEIGHT
import cafe.adriel.gwentwallpapers.presentation.internal.ui.view.WallpaperView.Companion.IMAGE_WIDTH
import com.bumptech.glide.ListPreloader.PreloadModelProvider
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.RequestManager
import com.bumptech.glide.integration.recyclerview.RecyclerViewPreloader
import com.bumptech.glide.load.DecodeFormat.PREFER_ARGB_8888
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.util.FixedPreloadSizeProvider

internal typealias ImageLoader = RequestManager
internal typealias ImageRequest = RequestBuilder<Drawable>

internal class ImageLoaderHelper(
    private val imageLoader: ImageLoader,
    private val getItem: (position: Int) -> WallpaperModelItem
) {

    companion object {
        private const val MAX_PRELOAD = 6
        private const val TRANSITION_DURATION = 100
    }

    private val crossFadeTransition = DrawableTransitionOptions.withCrossFade(TRANSITION_DURATION)

    private val sizeProvider = FixedPreloadSizeProvider<Wallpaper>(IMAGE_WIDTH, IMAGE_HEIGHT)

    private val modelProvider = object : PreloadModelProvider<Wallpaper> {
        override fun getPreloadItems(position: Int): MutableList<Wallpaper> =
            mutableListOf(getItem(position).model)

        override fun getPreloadRequestBuilder(item: Wallpaper): RequestBuilder<*>? =
            getThumbnailImageRequest(item)
    }

    val preloaderListener = RecyclerViewPreloader(imageLoader, modelProvider, sizeProvider, MAX_PRELOAD)

    fun getOriginalImageRequest(wallpaper: Wallpaper, placeholder: Drawable): ImageRequest =
        imageLoader.load(wallpaper.originalImageUrl)
            .placeholder(placeholder)
            .format(PREFER_ARGB_8888)

    fun getThumbnailImageRequest(wallpaper: Wallpaper): ImageRequest =
        imageLoader.load(wallpaper.thumbnailImageUrl)
            .error(R.drawable.bg_placeholder_error)
            .override(IMAGE_WIDTH, IMAGE_HEIGHT)
            .format(PREFER_ARGB_8888)
            .transition(crossFadeTransition)
            .dontTransform()
}
