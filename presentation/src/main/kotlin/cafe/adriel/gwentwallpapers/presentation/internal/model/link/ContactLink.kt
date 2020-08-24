package cafe.adriel.gwentwallpapers.presentation.internal.model.link

import cafe.adriel.gwentwallpapers.shared.adapter.AdapterOption

internal enum class ContactLink(override val title: String, val url: String) : AdapterOption<ContactLink> {
    EMAIL("Email", "me@adriel.cafe"),
    GITHUB("GitHub", "https://github.com/adrielcafe/"),
    LINKEDIN("LinkedIn", "https://linkedin.com/in/adrielcafe/"),
    TWITTER("Twitter", "https://twitter.com/adrielcafe")
}
