package cafe.adriel.gwentwallpapers.presentation.internal.ktx

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.viewbinding.ViewBinding
import cafe.adriel.gwentwallpapers.presentation.internal.ui.adapter.Adapter
import cafe.adriel.gwentwallpapers.presentation.internal.ui.adapter.AdapterItem
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.binding.AbstractBindingItem
import com.mikepenz.fastadapter.binding.ModelAbstractBindingItem
import com.mikepenz.fastadapter.dsl.genericFastAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

internal fun FastAdapter<*>.notifyItemChangedById(id: Int) {
    getItemById(id.toLong())?.second?.let { position ->
        notifyAdapterItemChanged(position)
    }
}

internal fun adapter(scope: CoroutineScope, init: suspend Adapter.() -> Unit): Lazy<Adapter> =
    lazy {
        genericFastAdapter {
            scope.launch {
                init()
                notifyAdapterDataSetChanged()
            }
        }
    }

internal fun <B : ViewBinding> AdapterItem.item(
    type: Int,
    createBinding: (LayoutInflater, ViewGroup?, Boolean) -> B,
    bindView: B.() -> Unit
) {
    BindingItem(type, createBinding, bindView).also { add(it) }
}

internal fun <M, B : ViewBinding> Adapter.model(
    model: M,
    id: Int,
    @LayoutRes layoutId: Int,
    createBinding: (LayoutInflater, ViewGroup?, Boolean) -> B,
    bindView: suspend B.(M) -> Unit
) = ModelBindingItem(model, id.toLong(), layoutId, createBinding, bindView)

internal class BindingItem<B : ViewBinding>(
    override val type: Int,
    val inflate: (LayoutInflater, ViewGroup?, Boolean) -> B,
    val bind: B.() -> Unit
) : AbstractBindingItem<B>() {

    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup?): B = inflate(inflater, parent, false)

    override fun bindView(binding: B, payloads: List<Any>) = binding.bind()
}

internal class ModelBindingItem<M, B : ViewBinding>(
    override var model: M,
    override var identifier: Long,
    override val type: Int,
    val inflate: (LayoutInflater, ViewGroup?, Boolean) -> B,
    val bind: suspend B.(M) -> Unit
) : ModelAbstractBindingItem<M, B>(model) {

    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup?): B = inflate(inflater, parent, false)

    override fun bindView(binding: B, payloads: List<Any>) {
        binding.coroutineScope.launch {
            binding.bind(model)
        }
    }
}
