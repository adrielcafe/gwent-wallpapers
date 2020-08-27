package cafe.adriel.gwentwallpapers.presentation.internal.ui.helper

import android.app.WallpaperManager
import android.content.ClipboardManager
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.provider.MediaStore
import androidx.viewbinding.ViewBinding
import cafe.adriel.gwentwallpapers.domain.model.AppInfo
import cafe.adriel.gwentwallpapers.domain.model.settings.AutoWallpaperScreen
import cafe.adriel.gwentwallpapers.domain.model.wallpaper.Wallpaper
import cafe.adriel.gwentwallpapers.presentation.R
import cafe.adriel.gwentwallpapers.presentation.internal.ktx.SCREEN_HEIGHT
import cafe.adriel.gwentwallpapers.presentation.internal.ktx.SCREEN_WIDTH
import cafe.adriel.gwentwallpapers.presentation.internal.ktx.activity
import cafe.adriel.gwentwallpapers.presentation.internal.ktx.copy
import cafe.adriel.gwentwallpapers.presentation.internal.ktx.cropHint
import cafe.adriel.gwentwallpapers.presentation.internal.ktx.getBitmap
import cafe.adriel.gwentwallpapers.presentation.internal.ktx.getFile
import cafe.adriel.gwentwallpapers.presentation.internal.ktx.getFlags
import cafe.adriel.gwentwallpapers.presentation.internal.ktx.hasWritePermission
import cafe.adriel.gwentwallpapers.presentation.internal.ktx.openAppSettings
import cafe.adriel.gwentwallpapers.presentation.internal.ktx.originalImageUrl
import cafe.adriel.gwentwallpapers.presentation.internal.ktx.shareFile
import cafe.adriel.gwentwallpapers.presentation.internal.ktx.showMessage
import cafe.adriel.gwentwallpapers.presentation.internal.ktx.toProviderUri
import cafe.adriel.gwentwallpapers.presentation.internal.model.MessageType.ERROR
import cafe.adriel.gwentwallpapers.presentation.internal.model.MessageType.SUCCESS
import cafe.adriel.gwentwallpapers.shared.logger.Logger
import java.io.File
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

internal class WallpaperHelper(
    private val context: Context,
    private val wallpaperManager: WallpaperManager,
    private val clipboardManager: ClipboardManager,
    private val imageLoader: ImageLoader,
    private val appInfo: AppInfo,
    private val dispatcher: CoroutineDispatcher,
    private val logger: Logger
) {

    private companion object {
        const val MIME_TYPE_PNG = "image/png"
        const val FILE_NAME = "gwent-card-%d.png"
    }

    suspend fun setDirectly(
        wallpaper: Wallpaper,
        screen: AutoWallpaperScreen,
        binding: ViewBinding? = null
    ): Boolean =
        withContext(dispatcher) {
            runCatching {
                if (VERSION.SDK_INT >= VERSION_CODES.N) {
                    val bitmap = imageLoader.getBitmap(wallpaper.originalImageUrl)
                    val cropHint = bitmap.cropHint(wallpaperManager.desiredMinimumHeight)

                    logger.d("Screen size -> ${SCREEN_WIDTH}x$SCREEN_HEIGHT")
                    logger.d("Bitmap size -> ${bitmap.width}x${bitmap.height}")
                    logger.d("Desired wallpaper height -> ${wallpaperManager.desiredMinimumHeight}")

                    wallpaperManager.setBitmap(bitmap, cropHint, true, screen.getFlags())
                } else {
                    val bitmap = imageLoader.getBitmap(wallpaper.originalImageUrl, SCREEN_WIDTH, SCREEN_HEIGHT)
                    wallpaperManager.setBitmap(bitmap)
                }
            }
        }.fold(
            onSuccess = {
                binding?.showMessage(SUCCESS, R.drawable.ic_set_wallpaper, R.string.message_wallpaper_set)
                true
            },
            onFailure = { error ->
                logger.e("Set wallpaper directly error -> $error", error)
                binding?.showMessage(ERROR, R.drawable.ic_set_wallpaper, R.string.message_generic_error)
                false
            }
        )

    suspend fun setExternally(wallpaper: Wallpaper, binding: ViewBinding) {
        withContext(dispatcher) {
            val intentFlags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_ACTIVITY_NEW_TASK

            runCatching {
                wallpaper.getOriginalImageUri(context)
            }.mapCatching { uri ->
                runCatching {
                    wallpaperManager.getCropAndSetWallpaperIntent(uri)
                        .addFlags(intentFlags)
                        .let(context::startActivity)
                }.recoverCatching {
                    val intent = Intent(Intent.ACTION_ATTACH_DATA)
                        .setDataAndType(uri, MIME_TYPE_PNG)
                        .addCategory(Intent.CATEGORY_DEFAULT)
                        .addFlags(intentFlags)
                    Intent.createChooser(intent, context.getString(R.string.menu_set_as))
                        .setFlags(intentFlags)
                        .let(context::startActivity)
                }
            }
        }.onFailure { error ->
            logger.e("Set wallpaper externally error -> $error", error)
            binding.showMessage(ERROR, R.drawable.ic_set_as, R.string.message_generic_error)
        }
    }

    suspend fun save(wallpaper: Wallpaper, binding: ViewBinding) {
        if (VERSION.SDK_INT < VERSION_CODES.Q && binding.activity.hasWritePermission().not()) {
            binding.showMessage(
                type = ERROR,
                icon = R.drawable.ic_block,
                title = R.string.message_missing_write_permission,
                message = R.string.message_tap_to_open_settings,
                onClick = context::openAppSettings
            )
            return
        }

        withContext(dispatcher) {
            runCatching {
                val wallpaperFile = imageLoader.getFile(wallpaper.originalImageUrl)
                val values = ContentValues().apply {
                    put(MediaStore.Images.Media.MIME_TYPE, MIME_TYPE_PNG)
                    put(MediaStore.Images.Media.DISPLAY_NAME, FILE_NAME.format(wallpaper.id))
                    wallpaper.name?.let { put(MediaStore.Images.Media.TITLE, wallpaper.name) }
                }

                context.contentResolver
                    .insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)!!
                    .also { uri ->
                        context.contentResolver
                            .openOutputStream(uri)!!
                            .use { it.write(wallpaperFile.readBytes()) }
                    }
            }
        }.fold(
            onSuccess = { uri ->
                binding.showMessage(
                    type = SUCCESS,
                    icon = R.drawable.ic_download,
                    title = R.string.message_wallpaper_saved,
                    message = R.string.message_tap_to_open,
                    onClick = { openExternalImage(uri, binding) }
                )
            },
            onFailure = { error ->
                logger.e("Save wallpaper error -> $error", error)
                binding.showMessage(ERROR, R.drawable.ic_download, R.string.message_generic_error)
            }
        )
    }

    suspend fun share(wallpaper: Wallpaper, binding: ViewBinding) {
        withContext(dispatcher) {
            runCatching {
                binding.activity.shareFile(wallpaper.getOriginalImageUri(context), MIME_TYPE_PNG)
            }
        }.onFailure { error ->
            logger.e("Share wallpaper error -> $error", error)
            binding.showMessage(ERROR, R.drawable.ic_share, R.string.message_generic_error)
        }
    }

    fun copyUrlToClipboard(wallpaper: Wallpaper, binding: ViewBinding) {
        clipboardManager.copy(wallpaper.originalImageUrl)
        binding.showMessage(SUCCESS, R.drawable.ic_url, R.string.message_url_copied_clipboard)
    }

    private fun openExternalImage(uri: Uri, binding: ViewBinding) {
        runCatching {
            Intent(Intent.ACTION_VIEW)
                .setDataAndType(uri, MIME_TYPE_PNG)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .let(context::startActivity)
        }.onFailure { error ->
            logger.e("Open external image error -> $error", error)
            binding.showMessage(ERROR, R.drawable.ic_set_as, R.string.message_generic_error)
        }
    }

    private fun Wallpaper.getOriginalImageUri(context: Context): Uri =
        imageLoader.getFile(originalImageUrl)
            .copyTo(File(context.cacheDir, FILE_NAME.format(id)), overwrite = true)
            .toProviderUri(context, appInfo.fileProviderAuthority)
}
