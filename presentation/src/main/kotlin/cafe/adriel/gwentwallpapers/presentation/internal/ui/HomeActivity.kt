package cafe.adriel.gwentwallpapers.presentation.internal.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cafe.adriel.gwentwallpapers.presentation.databinding.ScreenHomeBinding
import cafe.adriel.gwentwallpapers.presentation.internal.ktx.inflater
import cafe.adriel.gwentwallpapers.presentation.internal.ktx.tintStatusBar
import cafe.adriel.gwentwallpapers.presentation.internal.ui.screen.HomeScreen
import cafe.adriel.gwentwallpapers.presentation.internal.ui.screen.section.about.AboutSection
import cafe.adriel.gwentwallpapers.presentation.internal.ui.screen.section.settings.SettingsSection
import cafe.adriel.gwentwallpapers.presentation.internal.ui.screen.section.wallpapers.WallpapersSection
import daio.io.dresscode.matchDressCode

internal class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        matchDressCode()
        tintStatusBar()

        super.onCreate(savedInstanceState)

        ScreenHomeBinding.inflate(inflater).apply {
            setupScreen(this)
            setContentView(root)
        }
    }

    private fun setupScreen(binding: ScreenHomeBinding) {
        HomeScreen(binding)

        WallpapersSection(binding.wallpapersSection)
        SettingsSection(binding.settingsSection)
        AboutSection(binding.aboutSection)
    }
}
