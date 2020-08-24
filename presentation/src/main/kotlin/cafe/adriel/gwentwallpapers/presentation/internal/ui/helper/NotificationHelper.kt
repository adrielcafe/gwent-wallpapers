package cafe.adriel.gwentwallpapers.presentation.internal.ui.helper

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.graphics.drawable.toBitmap
import cafe.adriel.gwentwallpapers.presentation.R
import cafe.adriel.gwentwallpapers.presentation.internal.ktx.attrColor
import cafe.adriel.gwentwallpapers.presentation.internal.ktx.drawable
import cafe.adriel.gwentwallpapers.presentation.internal.ui.HomeActivity

internal class NotificationHelper(
    private val context: Context
) {

    private companion object {
        const val NOTIFICATION_ID = 0
        const val CHANNEL_ID = "GwentWallpapers"
    }

    private val contentIntent: PendingIntent
        get() = PendingIntent.getActivity(
            context,
            0,
            Intent(context, HomeActivity::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT
        )

    fun showNotification(title: String, message: String? = null) {
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setLargeIcon(context.drawable(R.mipmap.ic_launcher_round)?.toBitmap())
            .setSmallIcon(R.drawable.ic_notification)
            .setContentIntent(contentIntent)
            .setContentTitle(title)
            .setContentText(message)
            .setColor(context.attrColor(R.attr.colorPrimary))
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(context).apply {
            createChannel()
            notify(NOTIFICATION_ID, notification)
        }
    }

    private fun NotificationManagerCompat.createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && getNotificationChannel(CHANNEL_ID) == null) {
            NotificationChannel(
                CHANNEL_ID, context.getString(R.string.app_name),
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                enableLights(true)
                lightColor = context.attrColor(R.attr.colorPrimary)
                lockscreenVisibility = Notification.VISIBILITY_PUBLIC
                createNotificationChannel(this)
            }
        }
    }
}
