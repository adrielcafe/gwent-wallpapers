package cafe.adriel.gwentwallpapers.presentation.internal.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.view.isVisible
import cafe.adriel.gwentwallpapers.presentation.R
import cafe.adriel.gwentwallpapers.presentation.databinding.ViewStateBinding
import cafe.adriel.gwentwallpapers.presentation.internal.model.ScreenState

internal class StateView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayoutCompat(context, attrs, defStyleAttr) {

    private val binding by lazy { ViewStateBinding.bind(this) }

    init {
        inflate(context, R.layout.view_state, this)
        gravity = Gravity.CENTER
        orientation = VERTICAL
    }

    fun setState(state: ScreenState) {
        val (visible, message) = when (state) {
            ScreenState.CONTENT -> false to null
            ScreenState.LOADING -> true to R.string.state_loading
            ScreenState.EMPTY -> true to R.string.state_empty
        }

        binding.icon.isVisible = visible
        binding.message.isVisible = visible
        binding.message.text = message?.let(context::getString)
    }
}
