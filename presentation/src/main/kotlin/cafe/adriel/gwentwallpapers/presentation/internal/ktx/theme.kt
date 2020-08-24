package cafe.adriel.gwentwallpapers.presentation.internal.ktx

import cafe.adriel.gwentwallpapers.domain.model.settings.Theme
import cafe.adriel.gwentwallpapers.presentation.R

internal val Theme.styleId: Int
    get() = when (this) {
        Theme.DEFAULT -> R.style.AppTheme_Default
        Theme.NEUTRAL -> R.style.AppTheme_FactionNeutral
        Theme.MONSTERS -> R.style.AppTheme_FactionMonsters
        Theme.NILFGAARD -> R.style.AppTheme_FactionNilfgaard
        Theme.NORTHERN_REALMS -> R.style.AppTheme_FactionNorthernRealms
        Theme.SCOIATAEL -> R.style.AppTheme_FactionScoiatael
        Theme.SKELLIGE -> R.style.AppTheme_FactionSkellige
        Theme.SYNDICATE -> R.style.AppTheme_FactionSyndicate
    }

internal val Theme.authorBorderDrawableId: Int
    get() = when (this) {
        Theme.DEFAULT -> R.drawable.img_author_border_default
        Theme.NEUTRAL -> R.drawable.img_author_border_neutral
        Theme.MONSTERS -> R.drawable.img_author_border_monsters
        Theme.NILFGAARD -> R.drawable.img_author_border_nilfgaard
        Theme.NORTHERN_REALMS -> R.drawable.img_author_border_northern_realms
        Theme.SCOIATAEL -> R.drawable.img_author_border_scoiatael
        Theme.SKELLIGE -> R.drawable.img_author_border_skellige
        Theme.SYNDICATE -> R.drawable.img_author_border_syndicate
    }
