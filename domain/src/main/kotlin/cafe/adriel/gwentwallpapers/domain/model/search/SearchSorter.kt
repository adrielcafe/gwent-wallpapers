package cafe.adriel.gwentwallpapers.domain.model.search

sealed class SearchSorter {

    object Random : SearchSorter()
    data class Name(val ascending: Boolean) : SearchSorter()
}
