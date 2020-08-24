package cafe.adriel.gwentwallpapers.presentation.internal.ui.view

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import cafe.adriel.gwentwallpapers.domain.model.search.SearchSorter
import cafe.adriel.gwentwallpapers.presentation.R
import cafe.adriel.gwentwallpapers.presentation.databinding.MenuItemSortBinding

internal class SortMenuItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val binding by lazy { MenuItemSortBinding.bind(this) }

    init {
        inflate(context, R.layout.menu_item_sort, this)
    }

    override fun setOnClickListener(l: OnClickListener?) {
        binding.icon.setOnClickListener(l)
    }

    fun updateBadge(sorter: SearchSorter) {
        binding.badge.setChipIconResource(
            when (sorter) {
                is SearchSorter.Random -> R.drawable.ic_sort_random
                is SearchSorter.Name -> when (sorter.ascending) {
                    true -> R.drawable.ic_sort_asc
                    false -> R.drawable.ic_sort_desc
                }
            }
        )
    }
}
