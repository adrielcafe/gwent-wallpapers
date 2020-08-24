package cafe.adriel.gwentwallpapers.data.internal.model

import java.io.Serializable

internal data class GwentCardBack(
    val id: Int,
    val primaryColor: String
) : Serializable {

    companion object {
        @JvmStatic private val serialVersionUID: Long = 0
    }
}
