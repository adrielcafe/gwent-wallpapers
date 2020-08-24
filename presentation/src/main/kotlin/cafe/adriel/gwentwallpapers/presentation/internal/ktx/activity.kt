package cafe.adriel.gwentwallpapers.presentation.internal.ktx

import android.Manifest.permission
import android.app.Activity
import android.content.ClipDescription
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.view.View
import android.view.WindowManager
import androidx.core.app.ShareCompat
import cafe.adriel.gwentwallpapers.presentation.R
import cafe.adriel.gwentwallpapers.shared.logger.Logger
import com.markodevcic.peko.PermissionResult
import com.markodevcic.peko.requestPermissionsAsync

private const val MIME_TYPE_EMAIL = "message/rfc822"

internal suspend fun Activity.hasWritePermission() =
    requestPermissionsAsync(permission.WRITE_EXTERNAL_STORAGE) is PermissionResult.Granted

internal fun Activity.tintStatusBar() {
    window.apply {
        clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        statusBarColor = when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> Color.TRANSPARENT
            else -> attrColor(R.attr.colorPrimaryDark)
        }
    }
}

internal fun Activity.tintStatusBarIcons(white: Boolean) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        window.decorView.systemUiVisibility = when (white) {
            true -> View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            false -> View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    }
}

internal fun Activity.sendEmail(to: String, subject: String) =
    ShareCompat.IntentBuilder
        .from(this)
        .addEmailTo(to)
        .setSubject(subject)
        .setType(MIME_TYPE_EMAIL)
        .startChooser()

internal fun Activity.shareFile(file: Uri, mimeType: String) =
    ShareCompat.IntentBuilder
        .from(this)
        .addStream(file)
        .setType(mimeType)
        .createChooserIntent()
        .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        .let(::startActivity)

internal fun Activity.shareText(text: String) =
    ShareCompat.IntentBuilder
        .from(this)
        .setText(text)
        .setType(ClipDescription.MIMETYPE_TEXT_PLAIN)
        .startChooser()

internal fun Activity.openUrl(url: String, logger: Logger): Boolean =
    try {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
        true
    } catch (e: Throwable) {
        logger.e("Open url '$url' error -> $e", e)
        false
    }

internal fun Activity.openAppOnPlayStore(marketUrl: String, playStoreUrl: String, logger: Logger) {
    if (openUrl(marketUrl, logger).not()) {
        openUrl(playStoreUrl, logger)
    }
}
