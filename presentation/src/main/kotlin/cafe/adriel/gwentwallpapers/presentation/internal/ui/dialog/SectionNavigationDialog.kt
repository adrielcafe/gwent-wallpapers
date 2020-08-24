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
) {

    private val menuBinding by lazy { DialogSectionNavigationMenuBinding.inflate(context.inflater) }

    private val dialog by lazy {
        BottomSheetDialog(context).apply {
            setContentView(menuBinding.root)
            setup()
        }
    }

    private fun setup() {
        menuBinding.options.apply {
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

    fun show() = dialog.show()

    fun dismiss() = dialog.dismiss()
}
