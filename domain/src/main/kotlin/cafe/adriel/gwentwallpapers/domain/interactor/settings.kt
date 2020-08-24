package cafe.adriel.gwentwallpapers.domain.interactor

import cafe.adriel.gwentwallpapers.domain.model.settings.Settings

typealias GetSettingsInteractor = suspend () -> Settings
typealias UpdateSettingsInteractor = suspend (Settings) -> Unit
