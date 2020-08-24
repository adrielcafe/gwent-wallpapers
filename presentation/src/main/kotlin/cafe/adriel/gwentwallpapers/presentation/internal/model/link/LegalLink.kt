package cafe.adriel.gwentwallpapers.presentation.internal.model.link

import cafe.adriel.gwentwallpapers.shared.adapter.AdapterOption

internal enum class LegalLink(override val title: String, val url: String) : AdapterOption<LegalLink> {
    GUIDELINES("Fan Content Guidelines", "https://playgwent.com/en/fan-content"),
    USER_AGREEMENT("CDPR User Agreement", "https://regulations.cdprojektred.com/en/user_agreement")
}
