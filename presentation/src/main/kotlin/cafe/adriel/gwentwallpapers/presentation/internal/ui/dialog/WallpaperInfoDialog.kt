package cafe.adriel.gwentwallpapers.presentation.internal.ui.dialog

import android.content.Context
import androidx.core.view.isVisible
import cafe.adriel.gwentwallpapers.domain.model.wallpaper.WallpaperInfo
import cafe.adriel.gwentwallpapers.presentation.R
import cafe.adriel.gwentwallpapers.presentation.databinding.DialogWallpaperInfoBinding
import cafe.adriel.gwentwallpapers.presentation.internal.ktx.adapter
import cafe.adriel.gwentwallpapers.presentation.internal.ktx.coroutineScope
import cafe.adriel.gwentwallpapers.presentation.internal.ktx.inflater
import cafe.adriel.gwentwallpapers.presentation.internal.ui.adapter.quote
import cafe.adriel.gwentwallpapers.presentation.internal.ui.adapter.tags
import cafe.adriel.gwentwallpapers.presentation.internal.ui.adapter.text
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.mikepenz.fastadapter.dsl.itemAdapter

internal class WallpaperInfoDialog(
    private val context: Context,
    private val wallpaperInfo: WallpaperInfo
) : Dialog {

    override val dialog by lazy {
        BottomSheetDialog(context).apply {
            setContentView(binding.root)
            setup()
        }
    }

    private val binding by lazy { DialogWallpaperInfoBinding.inflate(context.inflater) }

    private val infoAdapter by adapter(binding.coroutineScope) {
        itemAdapter {
            wallpaperInfo.flavor?.let { quote(it) }

            wallpaperInfo.info?.let { text(it) }

            tags(wallpaperInfo.tags)
        }
    }

    private fun setup() {
        binding.infoList.apply {
            setItemViewCacheSize(infoAdapter.itemCount)
            adapter = infoAdapter
        }

        binding.dialogTitle.text = wallpaperInfo.name

        binding.dialogSubtitle.apply {
            text = context.getString(R.string.wallpaper_info_author, wallpaperInfo.artist)
            isVisible = wallpaperInfo.artist.isNullOrBlank().not()
        }

        binding.close.setOnClickListener {
            dismiss()
        }
    }
}
