package cafe.adriel.gwentwallpapers.shared.adapter

interface AdapterOption<T : Enum<T>> {

    val title: String

    val type: T
        get() = this as T
}
