package cafe.adriel.gwentwallpapers.presentation.internal.ui.screen.section.wallpapers

import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import cafe.adriel.broker.BrokerSubscriber
import cafe.adriel.broker.lifecycle.subscribe
import cafe.adriel.broker.removeRetained
import cafe.adriel.gwentwallpapers.domain.interactor.IsFavoriteInteractor
import cafe.adriel.gwentwallpapers.domain.model.Wallpaper
import cafe.adriel.gwentwallpapers.presentation.R
import cafe.adriel.gwentwallpapers.presentation.databinding.AdapterWallpaperBinding
import cafe.adriel.gwentwallpapers.presentation.databinding.SectionWallpapersBinding
import cafe.adriel.gwentwallpapers.presentation.internal.ktx.activity
import cafe.adriel.gwentwallpapers.presentation.internal.ktx.adapter
import cafe.adriel.gwentwallpapers.presentation.internal.ktx.addOnBackPressedCallback
import cafe.adriel.gwentwallpapers.presentation.internal.ktx.coroutineScope
import cafe.adriel.gwentwallpapers.presentation.internal.ktx.disableChangeAnimations
import cafe.adriel.gwentwallpapers.presentation.internal.ktx.koinScope
import cafe.adriel.gwentwallpapers.presentation.internal.ktx.notifyItemChangedById
import cafe.adriel.gwentwallpapers.presentation.internal.model.ScreenState
import cafe.adriel.gwentwallpapers.presentation.internal.model.WallpaperEvent
import cafe.adriel.gwentwallpapers.presentation.internal.model.WallpaperEvent.WallpaperExpander
import cafe.adriel.gwentwallpapers.presentation.internal.ui.adapter.WallpaperModelAdapter
import cafe.adriel.gwentwallpapers.presentation.internal.ui.adapter.wallpaper
import cafe.adriel.gwentwallpapers.presentation.internal.ui.helper.ConnectivityHelper
import cafe.adriel.gwentwallpapers.presentation.internal.ui.helper.ImageLoaderHelper
import cafe.adriel.gwentwallpapers.presentation.internal.ui.helper.WallpaperExpanderHelper
import cafe.adriel.gwentwallpapers.presentation.internal.ui.screen.section.wallpapers.WallpapersAction.Search
import cafe.adriel.gwentwallpapers.presentation.internal.ui.screen.section.wallpapers.WallpapersAction.ToggleFavorite
import cafe.adriel.gwentwallpapers.shared.di.InjectionQualifier.Interactor
import cafe.adriel.hal.emit
import cafe.adriel.hal.observeState
import com.mikepenz.fastadapter.dsl.modelAdapter
import org.koin.androidx.viewmodel.scope.viewModel
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.named

internal class WallpapersSection(private val binding: SectionWallpapersBinding) : KoinComponent {

    private companion object {
        const val LIST_COLUMN_COUNT = 2
    }

    private val viewModel by binding.koinScope.viewModel<WallpapersViewModel>(binding.activity)
    private val eventSubscriber by binding.koinScope.inject<BrokerSubscriber>()
    private val isFavorite by inject<IsFavoriteInteractor>(named<Interactor.IsFavorite>())
    private val connectivityHelper by binding.koinScope.inject<ConnectivityHelper>()
    private val wallpaperExpanderHelper by binding.koinScope.inject<WallpaperExpanderHelper>()
    private val imageLoaderHelper by binding.koinScope.inject<ImageLoaderHelper> {
        parametersOf(wallpaperModelAdapter::getAdapterItem)
    }

    private val wallpaperAdapter by adapter(binding.coroutineScope) {
        modelAdapter(
            interceptor = { model: Wallpaper ->
                wallpaper(
                    imageRequest = imageLoaderHelper.getThumbnailImageRequest(model),
                    wallpaper = model,
                    isFavorite = isFavorite,
                    onToggleFavorite = { viewModel emit ToggleFavorite(it) },
                    onSelected = ::expandWallpaper
                )
            },
            block = { wallpaperModelAdapter = this }
        )
    }
    private lateinit var wallpaperModelAdapter: WallpaperModelAdapter

    private val backPressedCallback by lazy { binding.addOnBackPressedCallback(::onBackPressed) }

    init {
        setupViews()
        setupListeners()
    }

    private fun setupViews() {
        binding.wallpaperList.apply {
            layoutManager = GridLayoutManager(context, LIST_COLUMN_COUNT)
            adapter = wallpaperAdapter
            disableChangeAnimations()
            addOnScrollListener(imageLoaderHelper.preloaderListener)
        }
    }

    private fun setupListeners() {
        viewModel.observeState(binding.coroutineScope, onStateChanged = ::handleState)

        eventSubscriber.apply {
            subscribe<WallpaperEvent.SearchRequested>(binding.activity, emitRetained = true) { event ->
                removeRetained<WallpaperEvent.SearchRequested>()
                viewModel emit Search(event.filter, event.sorter)
            }

            subscribe<WallpaperEvent.FavoriteChanged>(binding.activity) { event ->
                wallpaperAdapter.notifyItemChangedById(event.wallpaper.id)
            }

            subscribe<WallpaperExpander.Expanded>(binding.activity) { event ->
                onWallpaperExpanded(event.wallpaper)
            }

            subscribe<WallpaperExpander.Shrinked>(binding.activity) {
                onWallpaperShrinked()
            }
        }
    }

    private fun handleState(state: WallpapersState) {
        wallpaperModelAdapter.setNewList(state.wallpapers)

        when {
            state.loading -> {
                binding.loader.show()
                binding.state.setState(ScreenState.LOADING)
            }
            state.wallpapers.isEmpty() -> {
                binding.loader.hide()
                binding.state.setState(ScreenState.EMPTY)
            }
            else -> {
                binding.loader.hide()
                binding.state.setState(ScreenState.CONTENT)
            }
        }
    }

    private fun onBackPressed() {
        wallpaperExpanderHelper.shrink(binding)
    }

    private fun onWallpaperExpanded(wallpaper: Wallpaper) {
        backPressedCallback.isEnabled = true

        binding.wallpaperTitle.apply {
            text = wallpaper.name
            isVisible = wallpaper.name.isNullOrBlank().not()
        }

        binding.wallpaperArtist.apply {
            text = context.getString(R.string.wallpaper_author, wallpaper.artist)
            isVisible = wallpaper.artist.isNullOrBlank().not()
        }
    }

    private fun onWallpaperShrinked() {
        backPressedCallback.isEnabled = false
    }

    private fun expandWallpaper(itemBinding: AdapterWallpaperBinding, wallpaper: Wallpaper) =
        connectivityHelper.runIfConnected(binding) {
            val imageRequest = imageLoaderHelper.getOriginalImageRequest(
                wallpaper = wallpaper,
                placeholder = itemBinding.image.drawable ?: itemBinding.image.background
            )
            wallpaperExpanderHelper.expand(imageRequest, wallpaper, binding, itemBinding)
        }
}
