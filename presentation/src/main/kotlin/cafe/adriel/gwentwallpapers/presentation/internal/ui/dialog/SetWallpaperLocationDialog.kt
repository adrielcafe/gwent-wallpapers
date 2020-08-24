package cafe.adriel.gwentwallpapers.presentation.internal.ui.dialog

import android.content.Context
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import cafe.adriel.gwentwallpapers.domain.model.settings.AutoWallpaperScreen
import cafe.adriel.gwentwallpapers.presentation.R
import cafe.adriel.gwentwallpapers.presentation.databinding.DialogSetWallpaperLocationBinding
import cafe.adriel.gwentwallpapers.presentation.internal.ktx.inflater
import com.google.android.material.bottomsheet.BottomSheetDialog

internal class SetWallpaperLocationDialog(
    private val context: Context,
    private val onSelected: (AutoWallpaperScreen) -> Unit
) {

    private val binding by lazy { DialogSetWallpaperLocationBinding.inflate(context.inflater) }

    private val dialog by lazy {
        BottomSheetDialog(context).apply {
            setContentView(binding.root)
            setup()
        }
    }

    private fun setup() {
        binding.options.setNavigationItemSelectedListener { menuItem ->
            val selectedLocation = when (menuItem.itemId) {
                R.id.action_set_wallpaper_location_home_screen -> AutoWallpaperScreen.HOME
                R.id.action_set_wallpaper_location_lock_screen -> AutoWallpaperScreen.LOCK
                R.id.action_set_wallpaper_location_both_screens -> AutoWallpaperScreen.BOTH
                else -> throw IllegalArgumentException("MenuItem not mapped -> ${menuItem.title}")
            }
            onSelected(selectedLocation)
            dismiss()
            true
        }
    }

    fun show() {
        if (VERSION.SDK_INT < VERSION_CODES.N) {
            onSelected(AutoWallpaperScreen.BOTH)
        } else {
            dialog.show()
        }
    }

    fun dismiss() = dialog.dismiss()
}
