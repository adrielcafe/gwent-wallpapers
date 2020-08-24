package cafe.adriel.gwentwallpapers.presentation.internal.ktx

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import cafe.adriel.gwentwallpapers.presentation.internal.ui.helper.ImageLoader
import cafe.adriel.gwentwallpapers.presentation.internal.ui.helper.ImageRequest
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.DecodeFormat.PREFER_ARGB_8888
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import java.io.File

internal fun ImageLoader.getFile(url: String): File =
    download(url)
        .format(PREFER_ARGB_8888)
        .submit()
        .get()

internal fun ImageLoader.getBitmap(url: String, width: Int? = null, height: Int? = null): Bitmap =
    asBitmap()
        .load(url)
        .apply {
            if (width != null && height != null) {
                override(width, height)
            }
        }
        .format(PREFER_ARGB_8888)
        .submit()
        .get()

internal fun ImageRequest.addListener(onComplete: (model: Any?, exception: GlideException?) -> Unit): ImageRequest =
    addListener(object : RequestListener<Drawable> {
        override fun onResourceReady(
            resource: Drawable?,
            model: Any?,
            target: Target<Drawable>?,
            dataSource: DataSource?,
            isFirstResource: Boolean
        ): Boolean {
            onComplete(model, null)
            return false
        }

        override fun onLoadFailed(
            e: GlideException?,
            model: Any?,
            target: Target<Drawable>?,
            isFirstResource: Boolean
        ): Boolean {
            onComplete(model, e)
            return false
        }
    })
