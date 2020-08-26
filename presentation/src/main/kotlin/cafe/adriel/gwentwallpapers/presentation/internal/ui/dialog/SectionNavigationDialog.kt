package cafe.adriel.gwentwallpapers.presentation.internal.ui.dialog

import android.content.Context
import cafe.adriel.gwentwallpapers.presentation.databinding.DialogSectionNavigationMenuBinding
import cafe.adriel.gwentwallpapers.presentation.internal.ktx.inflater
import cafe.adriel.gwentwallpapers.presentation.internal.model.ScreenSection
import com.google.android.material.bottomsheet.BottomSheetDialog

internal class SectionNavigationDialog(
    private val context: Context,
    private val currentSection: ScreenSection,
    private val onSelected: (ScreenSection) -> Unit
) : Dialog {

    override val dialog by lazy {
        BottomSheetDialog(context).apply {
            setContentView(binding.root)
            setup()
        }
    }

    private val binding by lazy { DialogSectionNavigationMenuBinding.inflate(context.inflater) }

    private fun setup() {
        binding.options.apply {
            setCheckedItem(currentSection.itemId)
            setNavigationItemSelectedListener { menuItem ->
                val selectedSection = ScreenSection.values().first { it.itemId == menuItem.itemId }
                if (currentSection != selectedSection) {
                    onSelected(selectedSection)
                }
                dismiss()
                true
            }
        }
    }
}
