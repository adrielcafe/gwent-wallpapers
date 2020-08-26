package cafe.adriel.gwentwallpapers.presentation.internal.ui.dialog

import android.content.Context
import cafe.adriel.gwentwallpapers.domain.model.search.SearchSorter
import cafe.adriel.gwentwallpapers.presentation.R
import cafe.adriel.gwentwallpapers.presentation.databinding.DialogSearchSorterBinding
import cafe.adriel.gwentwallpapers.presentation.internal.ktx.inflater
import com.google.android.material.bottomsheet.BottomSheetDialog

internal class SearchSorterDialog(
    private val context: Context,
    private val currentSorter: SearchSorter,
    private val onSelected: (SearchSorter) -> Unit
) : Dialog {

    override val dialog by lazy {
        BottomSheetDialog(context).apply {
            setContentView(binding.root)
            setup()
        }
    }

    private val binding by lazy { DialogSearchSorterBinding.inflate(context.inflater) }

    private fun setup() {
        binding.options.apply {
            setCheckedItem(
                when (currentSorter) {
                    is SearchSorter.Random -> R.id.action_sort_random
                    is SearchSorter.Name -> when (currentSorter.ascending) {
                        true -> R.id.action_sort_name_asc
                        false -> R.id.action_sort_name_desc
                    }
                }
            )
            setNavigationItemSelectedListener { menuItem ->
                val selectedSorter = when (menuItem.itemId) {
                    R.id.action_sort_random -> SearchSorter.Random
                    R.id.action_sort_name_asc -> SearchSorter.Name(ascending = true)
                    R.id.action_sort_name_desc -> SearchSorter.Name(ascending = false)
                    else -> throw IllegalArgumentException("MenuItem not mapped -> ${menuItem.title}")
                }
                if (currentSorter != selectedSorter) {
                    onSelected(selectedSorter)
                }
                dismiss()
                true
            }
        }
    }
}
