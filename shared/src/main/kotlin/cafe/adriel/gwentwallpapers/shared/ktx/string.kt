package cafe.adriel.gwentwallpapers.shared.ktx

import java.text.Normalizer
import java.util.Locale

val String.normalized: String
    get() = Normalizer
        .normalize(this, Normalizer.Form.NFD)
        .trim()
        .toLowerCase(Locale.ENGLISH)

fun String.matches(query: String): Boolean =
    query.normalized in this.normalized

fun String.quoted(): String =
    """"$this""""
