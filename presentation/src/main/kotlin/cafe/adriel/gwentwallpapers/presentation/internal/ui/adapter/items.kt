package cafe.adriel.gwentwallpapers.presentation.internal.ui.adapter

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.graphics.toColorInt
import androidx.core.view.forEach
import androidx.core.view.isEmpty
import androidx.core.view.isVisible
import cafe.adriel.gwentwallpapers.domain.model.wallpaper.Wallpaper
import cafe.adriel.gwentwallpapers.presentation.R
import cafe.adriel.gwentwallpapers.presentation.databinding.AdapterAuthorInfoBinding
import cafe.adriel.gwentwallpapers.presentation.databinding.AdapterCheckboxBinding
import cafe.adriel.gwentwallpapers.presentation.databinding.AdapterQuoteBinding
import cafe.adriel.gwentwallpapers.presentation.databinding.AdapterSectionBinding
import cafe.adriel.gwentwallpapers.presentation.databinding.AdapterSelectionBinding
import cafe.adriel.gwentwallpapers.presentation.databinding.AdapterTagBinding
import cafe.adriel.gwentwallpapers.presentation.databinding.AdapterTextBinding
import cafe.adriel.gwentwallpapers.presentation.databinding.AdapterWallpaperBinding
import cafe.adriel.gwentwallpapers.presentation.databinding.ItemSelectionBinding
import cafe.adriel.gwentwallpapers.presentation.databinding.ItemSingleSelectionBinding
import cafe.adriel.gwentwallpapers.presentation.databinding.ItemTagBinding
import cafe.adriel.gwentwallpapers.presentation.internal.ktx.ModelBindingItem
import cafe.adriel.gwentwallpapers.presentation.internal.ktx.activity
import cafe.adriel.gwentwallpapers.presentation.internal.ktx.addOption
import cafe.adriel.gwentwallpapers.presentation.internal.ktx.checkSelectedOption
import cafe.adriel.gwentwallpapers.presentation.internal.ktx.checkedOption
import cafe.adriel.gwentwallpapers.presentation.internal.ktx.inflater
import cafe.adriel.gwentwallpapers.presentation.internal.ktx.item
import cafe.adriel.gwentwallpapers.presentation.internal.ktx.model
import cafe.adriel.gwentwallpapers.presentation.internal.ui.helper.ImageRequest
import cafe.adriel.gwentwallpapers.shared.adapter.AdapterOption
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.GenericItem
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.mikepenz.fastadapter.adapters.ModelAdapter

internal typealias Adapter = FastAdapter<GenericItem>
internal typealias AdapterItem = ItemAdapter<GenericItem>
internal typealias WallpaperModelItem = ModelBindingItem<Wallpaper, AdapterWallpaperBinding>
internal typealias WallpaperModelAdapter = ModelAdapter<Wallpaper, WallpaperModelItem>

internal fun Adapter.wallpaper(
    imageRequest: ImageRequest,
    wallpaper: Wallpaper,
    isFavorite: suspend (Wallpaper) -> Boolean,
    onToggleFavorite: (Wallpaper) -> Unit,
    onSelected: (AdapterWallpaperBinding, Wallpaper) -> Unit
): WallpaperModelItem =
    model(wallpaper, wallpaper.id, R.layout.adapter_wallpaper, AdapterWallpaperBinding::inflate) { model ->
        root.setOnClickListener {
            onSelected(this, model)
        }

        favorite.apply {
            setChipIconResource(if (isFavorite(model)) R.drawable.ic_favorite_on else R.drawable.ic_favorite_off)
            setOnClickListener { onToggleFavorite(wallpaper) }
        }

        image.setBackgroundColor(model.color.toColorInt())
        imageRequest.into(image)
    }

internal fun AdapterItem.section(
    @StringRes titleRes: Int,
    @StringRes subtitleRes: Int? = null,
    selected: () -> Boolean = { false },
    onChange: ((Boolean) -> Unit)? = null
) =
    item(R.layout.adapter_section, AdapterSectionBinding::inflate) {
        title.text = activity.getString(titleRes)

        subtitle.apply {
            text = subtitleRes?.let(activity::getString)
            isVisible = subtitleRes != null
        }

        selector.apply {
            setOnCheckedChangeListener(null)
            isVisible = onChange != null
            isChecked = selected()
            onChange?.let { setOnCheckedChangeListener { _, isChecked -> onChange(isChecked) } }
        }
    }

internal fun AdapterItem.text(
    title: String,
    enabled: () -> Boolean = { true }
) =
    item(R.layout.adapter_text, AdapterTextBinding::inflate) {
        root.apply {
            text = title
            isEnabled = enabled()
        }
    }

internal fun AdapterItem.quote(
    text: String,
) =
    item(R.layout.adapter_quote, AdapterQuoteBinding::inflate) {
        title.text = text
    }

internal fun AdapterItem.checkBox(
    @StringRes titleRes: Int,
    selected: () -> Boolean = { false },
    enabled: () -> Boolean = { true },
    onChange: (Boolean) -> Unit
) =
    item(R.layout.adapter_checkbox, AdapterCheckboxBinding::inflate) {
        title.apply {
            text = context.getString(titleRes)
            isEnabled = enabled()
        }

        selector.apply {
            setOnCheckedChangeListener(null)
            isEnabled = enabled()
            isChecked = selected()
            setOnCheckedChangeListener { _, isChecked -> onChange(isChecked) }
        }
    }

internal fun AdapterItem.tags(
    values: List<String>
) =
    item(R.layout.adapter_tag, AdapterTagBinding::inflate) {
        values.forEach { value ->
            ItemTagBinding
                .inflate(activity.inflater)
                .root
                .apply { text = value }
                .let(root::addView)
        }
    }

internal inline fun <reified T> AdapterItem.selection(
    values: Array<T>,
    noinline selectedValue: (() -> T)? = null,
    noinline enabled: () -> Boolean = { true },
    noinline onSelected: (T) -> Unit
) where T : Enum<T>, T : AdapterOption<T> =
    item(R.layout.adapter_selection, AdapterSelectionBinding::inflate) {
        val singleSelection = selectedValue != null
        root.apply {
            isSingleSelection = singleSelection
            isSelectionRequired = singleSelection
            isEnabled = enabled()

            if (isEmpty()) {
                values.forEach { option ->
                    val (binding, callback) = when (singleSelection) {
                        true -> ItemSingleSelectionBinding.inflate(activity.inflater).root to null
                        false -> ItemSelectionBinding.inflate(activity.inflater).root to onSelected
                    }
                    addOption(binding, option, isEnabled, callback)
                }
            } else {
                forEach { chip ->
                    chip.isEnabled = isEnabled
                }
            }

            selectedValue?.invoke()?.let { value ->
                setOnCheckedChangeListener(null)
                checkSelectedOption(value)
                setOnCheckedChangeListener { _, _ -> onSelected(checkedOption()) }
            }
        }
    }

internal fun AdapterItem.authorInfo(
    @DrawableRes borderRes: Int
) =
    item(R.layout.adapter_author_info, AdapterAuthorInfoBinding::inflate) {
        border.setImageResource(borderRes)
    }
