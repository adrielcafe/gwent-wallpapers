package cafe.adriel.gwentwallpapers.presentation.internal.ui.dialog

import android.content.Context
import android.view.inputmethod.EditorInfo
import androidx.core.widget.doOnTextChanged
import cafe.adriel.gwentwallpapers.domain.model.card.CardColor
import cafe.adriel.gwentwallpapers.domain.model.card.CardFaction
import cafe.adriel.gwentwallpapers.domain.model.card.CardFavorite
import cafe.adriel.gwentwallpapers.domain.model.card.CardRarity
import cafe.adriel.gwentwallpapers.domain.model.card.CardSet
import cafe.adriel.gwentwallpapers.domain.model.card.CardSide
import cafe.adriel.gwentwallpapers.domain.model.card.CardType
import cafe.adriel.gwentwallpapers.domain.model.search.SearchFilter
import cafe.adriel.gwentwallpapers.presentation.R
import cafe.adriel.gwentwallpapers.presentation.databinding.DialogSearchFilterBinding
import cafe.adriel.gwentwallpapers.presentation.internal.ktx.adapter
import cafe.adriel.gwentwallpapers.presentation.internal.ktx.coroutineScope
import cafe.adriel.gwentwallpapers.presentation.internal.ktx.inflater
import cafe.adriel.gwentwallpapers.presentation.internal.ui.adapter.selection
import cafe.adriel.gwentwallpapers.presentation.internal.ui.adapter.text
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.mikepenz.fastadapter.dsl.itemAdapter

internal class SearchFilterDialog(
    private val context: Context,
    private val currentFilter: SearchFilter,
    private val onSelected: (SearchFilter) -> Unit
) : Dialog {

    override val dialog by lazy {
        BottomSheetDialog(context).apply {
            setContentView(binding.root)
            setupBottomSheet()
            setupButtons()
            setupFilters()
            updateFilterStatus()
        }
    }

    private val binding by lazy { DialogSearchFilterBinding.inflate(context.inflater) }

    private val filterAdapter by adapter(binding.coroutineScope) {
        itemAdapter {
            text(context.getString(R.string.dialog_filter_favorite))

            selection(
                values = CardFavorite.values(),
                selectedValue = { selectedFilter.favorite },
                onSelected = { selectedFilter = selectedFilter.copy(favorite = it) }
            )

            text(context.getString(R.string.dialog_filter_side))

            selection(
                values = CardSide.values(),
                selectedValue = { selectedFilter.side },
                onSelected = {
                    selectedFilter = selectedFilter.copy(side = it)
                    updateFilterStatus()
                }
            )

            text(
                title = context.getString(R.string.dialog_filter_color),
                enabled = { cardFrontFilterEnabled }
            )

            selection(
                values = CardColor.values(),
                selectedValue = { selectedFilter.color },
                enabled = { cardFrontFilterEnabled },
                onSelected = { selectedFilter = selectedFilter.copy(color = it) }
            )

            text(
                title = context.getString(R.string.dialog_filter_rarity),
                enabled = { cardFrontFilterEnabled }
            )

            selection(
                values = CardRarity.values(),
                selectedValue = { selectedFilter.rarity },
                enabled = { cardFrontFilterEnabled },
                onSelected = { selectedFilter = selectedFilter.copy(rarity = it) }
            )

            text(
                title = context.getString(R.string.dialog_filter_type),
                enabled = { cardFrontFilterEnabled }
            )

            selection(
                values = CardType.values(),
                selectedValue = { selectedFilter.type },
                enabled = { cardFrontFilterEnabled },
                onSelected = { selectedFilter = selectedFilter.copy(type = it) }
            )

            text(
                title = context.getString(R.string.dialog_filter_faction),
                enabled = { cardFrontFilterEnabled }
            )

            selection(
                values = CardFaction.values(),
                selectedValue = { selectedFilter.faction },
                enabled = { cardFrontFilterEnabled },
                onSelected = { selectedFilter = selectedFilter.copy(faction = it) }
            )

            text(
                title = context.getString(R.string.dialog_filter_set),
                enabled = { cardFrontFilterEnabled }
            )

            selection(
                values = CardSet.values(),
                selectedValue = { selectedFilter.set },
                enabled = { cardFrontFilterEnabled },
                onSelected = { selectedFilter = selectedFilter.copy(set = it) }
            )
        }
    }

    private var selectedFilter = currentFilter

    private val cardFrontFilterEnabled: Boolean
        get() = selectedFilter.side != CardSide.BACK

    private fun setupBottomSheet() {
        binding.root.post {
            dialog.behavior.apply {
                state = BottomSheetBehavior.STATE_EXPANDED
                skipCollapsed = true
                isDraggable = false
            }
        }
    }

    private fun setupButtons() {
        binding.apply {
            close.setOnClickListener {
                dismiss()
            }
            clearFilter.setOnClickListener {
                SearchFilter().apply()
                dismiss()
            }
            applyFilter.setOnClickListener {
                getSelectedFilter().apply()
                dismiss()
            }
        }
    }

    private fun setupFilters() {
        binding.filterName.apply {
            setText(selectedFilter.name)
            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE -> clearFocus()
                }
                false
            }
            doOnTextChanged { text, _, _, _ ->
                selectedFilter = selectedFilter.copy(name = text?.toString() ?: "")
            }
        }

        binding.filterList.apply {
            setItemViewCacheSize(filterAdapter.itemCount)
            adapter = filterAdapter
        }
    }

    private fun updateFilterStatus() {
        binding.filterNameTitle.isEnabled = cardFrontFilterEnabled
        binding.filterName.isEnabled = cardFrontFilterEnabled
        filterAdapter.notifyAdapterDataSetChanged()
    }

    private fun getSelectedFilter(): SearchFilter =
        when (selectedFilter.side) {
            CardSide.ALL,
            CardSide.FRONT -> selectedFilter
            CardSide.BACK -> selectedFilter.copy(
                name = "",
                color = CardColor.ALL,
                rarity = CardRarity.ALL,
                type = CardType.ALL,
                faction = CardFaction.ALL,
                set = CardSet.ALL
            )
        }

    private fun SearchFilter.apply() {
        if (currentFilter != this) {
            onSelected(this)
        }
    }
}
