package cafe.adriel.gwentwallpapers.presentation.internal.worker

import android.content.Context
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import cafe.adriel.gwentwallpapers.domain.interactor.GetRandomWallpaperInteractor
import cafe.adriel.gwentwallpapers.domain.interactor.GetSettingsInteractor
import cafe.adriel.gwentwallpapers.domain.model.settings.AutoWallpaperFrequency
import cafe.adriel.gwentwallpapers.domain.model.wallpaper.Wallpaper
import cafe.adriel.gwentwallpapers.presentation.R
import cafe.adriel.gwentwallpapers.presentation.internal.ui.helper.NotificationHelper
import cafe.adriel.gwentwallpapers.presentation.internal.ui.helper.WallpaperHelper
import cafe.adriel.gwentwallpapers.shared.di.InjectionQualifier.Dispatcher
import cafe.adriel.gwentwallpapers.shared.di.InjectionQualifier.Interactor
import cafe.adriel.gwentwallpapers.shared.ktx.quoted
import cafe.adriel.gwentwallpapers.shared.logger.Logger
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.koin.core.qualifier.named

internal class AutoWallpaperWorker(
    private val context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params), KoinComponent {

    companion object {
        private const val WORK_NAME = "GwentAutoWallpaper"

        fun enqueueWork(workManager: WorkManager, frequency: AutoWallpaperFrequency, delayed: Boolean) {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
            val request = PeriodicWorkRequestBuilder<AutoWallpaperWorker>(frequency.interval, frequency.timeUnit)
                .apply { if (delayed) setInitialDelay(frequency.interval, frequency.timeUnit) }
                .setConstraints(constraints)
                .build()
            workManager.enqueueUniquePeriodicWork(WORK_NAME, ExistingPeriodicWorkPolicy.REPLACE, request)
        }

        fun cancelWork(workManager: WorkManager) =
            workManager.cancelUniqueWork(WORK_NAME)
    }

    private val getSettings by inject<GetSettingsInteractor>(named<Interactor.GetSettings>())
    private val getRandomWallpaper by inject<GetRandomWallpaperInteractor>(named<Interactor.GetRandomWallpaper>())
    private val wallpaperHelper by inject<WallpaperHelper>()
    private val notificationHelper by inject<NotificationHelper>()
    private val dispatcher by inject<CoroutineDispatcher>(named<Dispatcher.Worker>())
    private val logger by inject<Logger>()

    private val Wallpaper.notificationMessage: String?
        get() {
            val cardName = name?.quoted().orEmpty()
            val artistName = artist?.let { context.getString(R.string.wallpaper_info_author, it) }.orEmpty()
            return "$cardName $artistName".ifBlank { null }
        }

    override suspend fun doWork(): Result = withContext(dispatcher) {
        try {
            setRandomWallpaper()
        } catch (e: Throwable) {
            logger.e("Auto wallpaper worker error -> $e", e)
            Result.failure()
        }
    }

    private suspend fun setRandomWallpaper(): Result {
        val autoWallpaper = getSettings().autoWallpaper
        val wallpaper = getRandomWallpaper(autoWallpaper.onlyFavorites)

        return when {
            autoWallpaper.enabled.not() -> Result.success()

            wallpaper != null ->
                if (wallpaperHelper.setDirectly(wallpaper, autoWallpaper.screen)) {
                    if (autoWallpaper.showNotification) {
                        notificationHelper.showNotification(
                            title = context.getString(R.string.message_new_wallpaper_set),
                            message = wallpaper.notificationMessage
                        )
                    }
                    Result.success()
                } else {
                    Result.retry()
                }

            autoWallpaper.onlyFavorites -> {
                if (autoWallpaper.showNotification) {
                    notificationHelper.showNotification(context.getString(R.string.message_no_favorite_found))
                }
                Result.success()
            }

            else -> Result.success()
        }
    }
}
