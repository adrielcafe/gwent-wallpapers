package cafe.adriel.gwentwallpapers.presentation.internal.ktx

import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.Drawable
import androidx.annotation.AttrRes
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.content.res.use

internal val SCREEN_WIDTH = Resources.getSystem().displayMetrics.widthPixels
internal val SCREEN_HEIGHT = Resources.getSystem().displayMetrics.heightPixels

internal fun Context.drawable(@DrawableRes resId: Int): Drawable? =
    AppCompatResources.getDrawable(this, resId)

internal fun Context.color(@ColorRes resId: Int): Int =
    ContextCompat.getColor(this, resId)

internal fun Context.attrColor(@AttrRes resId: Int): Int =
    obtainStyledAttributes(intArrayOf(resId)).use { attrs ->
        attrs.getColor(0, 0)
    }
