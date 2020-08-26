package cafe.adriel.gwentwallpapers.presentation.internal.ui.helper

import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateLayoutParams
import cafe.adriel.broker.BrokerPublisher
import cafe.adriel.gwentwallpapers.domain.model.wallpaper.Wallpaper
import cafe.adriel.gwentwallpapers.presentation.R
import cafe.adriel.gwentwallpapers.presentation.databinding.AdapterWallpaperBinding
import cafe.adriel.gwentwallpapers.presentation.databinding.SectionWallpapersBinding
import cafe.adriel.gwentwallpapers.presentation.internal.ktx.addListener
import cafe.adriel.gwentwallpapers.presentation.internal.ktx.originalImageUrl
import cafe.adriel.gwentwallpapers.presentation.internal.ktx.showMessage
import cafe.adriel.gwentwallpapers.presentation.internal.ktx.windowLocation
import cafe.adriel.gwentwallpapers.presentation.internal.model.MessageType.ERROR
import cafe.adriel.gwentwallpapers.presentation.internal.model.WallpaperEvent.WallpaperExpander
import cafe.adriel.gwentwallpapers.presentation.internal.ui.view.WallpaperView
import kotlin.properties.Delegates.observable

internal class WallpaperExpanderHelper(eventPublisher: BrokerPublisher) {

    var currentStatus by observable(WallpaperExpander.Shrinked as WallpaperExpander) { _, _, newStatus ->
        eventPublisher.publish(newStatus)
    }

    fun expand(
        imageRequest: ImageRequest,
        wallpaper: Wallpaper,
        sectionBinding: SectionWallpapersBinding,
        itemBinding: AdapterWallpaperBinding
    ) {
        currentStatus = WallpaperExpander.Expanded(wallpaper)

        val itemWindowLocation = itemBinding.image.windowLocation

        sectionBinding.root
            .getConstraintSet(R.id.start)
            .getConstraint(sectionBinding.currentWallpaper.id)
            .apply {
                layout.mWidth = WallpaperView.IMAGE_WIDTH
                layout.mHeight = WallpaperView.IMAGE_HEIGHT
                transform.translationX = itemWindowLocation.x
                transform.translationY = itemWindowLocation.y
            }

        sectionBinding.currentWallpaper.apply {
            background = itemBinding.image.background
            translationX = itemWindowLocation.x
            translationY = itemWindowLocation.y

            updateLayoutParams<ConstraintLayout.LayoutParams> {
                width = WallpaperView.IMAGE_WIDTH
                height = WallpaperView.IMAGE_HEIGHT
            }

            setOnClickListener { shrink(sectionBinding) }
        }

        sectionBinding.apply {
            root.transitionToEnd()
            loader.show()
        }

        imageRequest
            .addListener { model, exception ->
                when (val status = currentStatus) {
                    is WallpaperExpander.Expanded ->
                        if (model == status.wallpaper.originalImageUrl) sectionBinding.loader.hide()
                }

                if (exception != null) {
                    sectionBinding.showMessage(
                        type = ERROR,
                        icon = R.drawable.ic_error,
                        title = R.string.message_error_download_image,
                        message = R.string.message_verify_connection
                    )
                }
            }
            .into(sectionBinding.currentWallpaper)
    }

    fun shrink(sectionBinding: SectionWallpapersBinding) {
        currentStatus = WallpaperExpander.Shrinked

        sectionBinding.apply {
            root.transitionToStart()
            loader.hide()
        }
    }
}
