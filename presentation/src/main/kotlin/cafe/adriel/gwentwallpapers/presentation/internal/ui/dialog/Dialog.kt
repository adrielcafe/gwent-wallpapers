package cafe.adriel.gwentwallpapers.presentation.internal.ui.dialog

import com.google.android.material.bottomsheet.BottomSheetDialog

internal interface Dialog {

    val dialog: BottomSheetDialog

    fun show() = dialog.show()

    fun dismiss() = dialog.dismiss()
}
