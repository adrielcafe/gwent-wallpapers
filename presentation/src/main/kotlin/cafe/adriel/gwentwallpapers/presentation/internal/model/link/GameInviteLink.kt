package cafe.adriel.gwentwallpapers.presentation.internal.model.link

import cafe.adriel.gwentwallpapers.shared.adapter.AdapterOption

internal enum class GameInviteLink(override val title: String, val url: String) : AdapterOption<GameInviteLink> {
    INVITE("Let's play Gwent!", "https://playgwent.com/invite-a-friend/K0QX78TVLX")
}
