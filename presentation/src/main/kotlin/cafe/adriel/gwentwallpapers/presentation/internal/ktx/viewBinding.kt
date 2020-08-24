package cafe.adriel.gwentwallpapers.presentation.internal.ktx

import android.graphics.Color
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import cafe.adriel.gwentwallpapers.presentation.internal.model.MessageType
import com.irozon.sneaker.Sneaker
import com.irozon.sneaker.interfaces.OnSneakerClickListener
import kotlinx.coroutines.CoroutineScope
import org.koin.core.scope.Scope
import org.koin.ext.scope

internal val ViewBinding.activity: AppCompatActivity
    get() = root.context as AppCompatActivity

internal val ViewBinding.coroutineScope: CoroutineScope
    get() = activity.lifecycleScope

internal val ViewBinding.koinScope: Scope
    get() = activity.scope

internal fun ViewBinding.addOnBackPressedCallback(action: () -> Unit): OnBackPressedCallback =
    object : OnBackPressedCallback(false) {
        override fun handleOnBackPressed() = action()
    }.also { callback ->
        activity.onBackPressedDispatcher
            .addCallback(activity, callback)
    }

internal fun ViewBinding.showMessage(
    type: MessageType,
    @DrawableRes icon: Int,
    @StringRes title: Int,
    @StringRes message: Int? = null,
    onClick: (() -> Unit)? = null
) {
    val backgroundColor =
        runCatching { activity.color(type.color) }
            .recoverCatching { activity.attrColor(type.color) }
            .getOrDefault(Color.BLACK)

    Sneaker.with(activity)
        .autoHide(true)
        .setIcon(icon, Color.WHITE)
        .setTitle(activity.getString(title), Color.WHITE)
        .setMessage(message?.let(activity::getString) ?: "", Color.WHITE)
        .setOnSneakerClickListener(object : OnSneakerClickListener {
            override fun onSneakerClick(view: View) {
                onClick?.invoke()
            }
        })
        .sneak(backgroundColor)
}
