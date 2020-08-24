package cafe.adriel.gwentwallpapers.presentation.internal.ui.view

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import cafe.adriel.gwentwallpapers.domain.model.search.SearchFilter
import cafe.adriel.gwentwallpapers.presentation.R
import cafe.adriel.gwentwallpapers.presentation.databinding.MenuItemFilterBinding

internal class FilterMenuItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val binding by lazy { MenuItemFilterBinding.bind(this) }

    init {
        inflate(context, R.layout.menu_item_filter, this)
    }

    override fun setOnClickListener(l: OnClickListener?) {
        binding.icon.setOnClickListener(l)
    }

    fun updateBadge(filter: SearchFilter) {
        binding.badge.text = filter.count.toString()
    }
}
