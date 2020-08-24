package cafe.adriel.gwentwallpapers.presentation.internal.ktx

import android.graphics.PointF
import android.os.Build
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ViewFlipper
import androidx.annotation.IdRes
import androidx.core.view.forEach
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import cafe.adriel.gwentwallpapers.presentation.internal.model.ScreenSection
import cafe.adriel.gwentwallpapers.shared.adapter.AdapterOption
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.floatingactionbutton.FloatingActionButton.OnVisibilityChangedListener

internal val EditText.value: String
    get() = text?.toString() ?: ""

internal val View.windowLocation: PointF
    get() {
        val (x, y) = IntArray(2)
            .also(::getLocationInWindow)
            .map(Int::toFloat)

        val offsetY = when (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            true -> context.statusBarHeight
            false -> 0
        }

        return PointF(x, y - offsetY)
    }

internal fun Menu.showItemsIf(predicate: (MenuItem) -> Boolean) =
    forEach { it.isVisible = predicate(it) }

internal fun Menu.hideItems() =
    forEach { it.isVisible = false }

internal fun FloatingActionButton.show(init: () -> Unit) =
    show(object : OnVisibilityChangedListener() {
        override fun onShown(fab: FloatingActionButton?) = init()
    })

internal fun FloatingActionButton.hide(init: () -> Unit) =
    hide(object : OnVisibilityChangedListener() {
        override fun onHidden(fab: FloatingActionButton?) = init()
    })

internal val ViewFlipper.currentSection: ScreenSection
    get() = ScreenSection.values()[displayedChild]

internal fun ViewFlipper.navigate(section: ScreenSection) {
    if (displayedChild != section.ordinal) {
        displayedChild = section.ordinal
    }
}

internal inline fun <reified T> BottomAppBar.getMenuItemActionView(@IdRes id: Int): T? =
    menu.findItem(id)?.actionView as? T

internal fun RecyclerView.disableChangeAnimations() {
    (itemAnimator as? SimpleItemAnimator)?.supportsChangeAnimations = false
}

internal fun <T> ChipGroup.addOption(
    chip: Chip,
    option: T,
    enabled: Boolean,
    onSelected: ((T) -> Unit)? = null
) where T : Enum<T>, T : AdapterOption<T> =
    chip.apply {
        tag = option.type
        text = option.title
        isEnabled = enabled
        onSelected?.let { setOnClickListener { onSelected(option) } }
    }.let(::addView)

internal fun <T> ChipGroup.checkSelectedOption(tag: T) where T : Enum<T>, T : AdapterOption<T> =
    check(findViewWithTag<View>(tag).id)

internal inline fun <reified T> ChipGroup.checkedOption(): T where T : Enum<T>, T : AdapterOption<T> =
    findViewById<View>(checkedChipId).tag as T
