package cafe.adriel.gwentwallpapers.presentation.internal.ui.view

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import cafe.adriel.gwentwallpapers.presentation.internal.ktx.SCREEN_HEIGHT
import cafe.adriel.gwentwallpapers.presentation.internal.ktx.SCREEN_WIDTH

internal class WallpaperView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    companion object {
        private val COLUMN_WIDTH = SCREEN_WIDTH / 2
        private val COLUMN_HEIGHT = SCREEN_HEIGHT / 2
        val IMAGE_WIDTH = COLUMN_WIDTH
        val IMAGE_HEIGHT = COLUMN_WIDTH * COLUMN_HEIGHT / COLUMN_WIDTH
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = IMAGE_HEIGHT
        setMeasuredDimension(width, height)
    }
}
