package cafe.adriel.gwentwallpapers.presentation.internal.model.link

import cafe.adriel.gwentwallpapers.shared.adapter.AdapterOption

internal enum class OpenSourceLink(override val title: String, val url: String) : AdapterOption<OpenSourceLink> {
    REPOSITORY("GitHub Repository", "https://github.com/adrielcafe/gwent-wallpapers/"),
    PRIVACY_POLICY("Privacy Policy", "https://adriel.cafe/privacy_policy/gwent_wallpapers"),
    LICENSE("MIT License", "https://opensource.org/licenses/MIT")
}
