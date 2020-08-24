package cafe.adriel.gwentwallpapers.domain.model.settings

import cafe.adriel.gwentwallpapers.shared.adapter.AdapterOption
import java.util.concurrent.TimeUnit

enum class AutoWallpaperFrequency(
    override val title: String,
    val interval: Long,
    val timeUnit: TimeUnit
) : AdapterOption<AutoWallpaperFrequency> {
    EVERY_HOUR("Hour", 1L, TimeUnit.HOURS),
    EVERY_TWELVE_HOURS("Twelve hours", 12L, TimeUnit.HOURS),
    EVERY_DAY("Day", 1L, TimeUnit.DAYS),
    EVERY_WEEK("Week", 7L, TimeUnit.DAYS),
    EVERY_TWO_WEEKS("Two weeks", 14L, TimeUnit.DAYS),
    EVERY_MONTH("Month", 30L, TimeUnit.DAYS)
}
