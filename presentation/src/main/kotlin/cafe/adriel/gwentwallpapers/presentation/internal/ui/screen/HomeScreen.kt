package cafe.adriel.gwentwallpapers.presentation.internal.ui.screen

import androidx.annotation.IdRes
import cafe.adriel.broker.BrokerSubscriber
import cafe.adriel.broker.lifecycle.subscribe
import cafe.adriel.gwentwallpapers.domain.interactor.IsFavoriteInteractor
import cafe.adriel.gwentwallpapers.domain.model.AppInfo
import cafe.adriel.gwentwallpapers.domain.model.Wallpaper
import cafe.adriel.gwentwallpapers.domain.model.search.SearchFilter
import cafe.adriel.gwentwallpapers.domain.model.search.SearchSorter
import cafe.adriel.gwentwallpapers.domain.model.settings.AutoWallpaperScreen
import cafe.adriel.gwentwallpapers.presentation.R
import cafe.adriel.gwentwallpapers.presentation.databinding.ScreenHomeBinding
import cafe.adriel.gwentwallpapers.presentation.internal.ktx.activity
import cafe.adriel.gwentwallpapers.presentation.internal.ktx.coroutineScope
import cafe.adriel.gwentwallpapers.presentation.internal.ktx.currentSection
import cafe.adriel.gwentwallpapers.presentation.internal.ktx.getMenuItemActionView
import cafe.adriel.gwentwallpapers.presentation.internal.ktx.hide
import cafe.adriel.gwentwallpapers.presentation.internal.ktx.hideItems
import cafe.adriel.gwentwallpapers.presentation.internal.ktx.koinScope
import cafe.adriel.gwentwallpapers.presentation.internal.ktx.navigate
import cafe.adriel.gwentwallpapers.presentation.internal.ktx.openAppOnPlayStore
import cafe.adriel.gwentwallpapers.presentation.internal.ktx.shareText
import cafe.adriel.gwentwallpapers.presentation.internal.ktx.show
import cafe.adriel.gwentwallpapers.presentation.internal.ktx.showItemsIf
import cafe.adriel.gwentwallpapers.presentation.internal.ktx.tintStatusBarIcons
import cafe.adriel.gwentwallpapers.presentation.internal.model.ScreenSection
import cafe.adriel.gwentwallpapers.presentation.internal.model.WallpaperEvent
import cafe.adriel.gwentwallpapers.presentation.internal.model.WallpaperEvent.WallpaperExpander
import cafe.adriel.gwentwallpapers.presentation.internal.ui.dialog.SearchFilterDialog
import cafe.adriel.gwentwallpapers.presentation.internal.ui.dialog.SearchSorterDialog
import cafe.adriel.gwentwallpapers.presentation.internal.ui.dialog.SectionNavigationDialog
import cafe.adriel.gwentwallpapers.presentation.internal.ui.dialog.SetWallpaperLocationDialog
import cafe.adriel.gwentwallpapers.presentation.internal.ui.helper.ConnectivityHelper
import cafe.adriel.gwentwallpapers.presentation.internal.ui.helper.WallpaperExpanderHelper
import cafe.adriel.gwentwallpapers.presentation.internal.ui.helper.WallpaperHelper
import cafe.adriel.gwentwallpapers.presentation.internal.ui.view.FilterMenuItemView
import cafe.adriel.gwentwallpapers.presentation.internal.ui.view.SortMenuItemView
import cafe.adriel.gwentwallpapers.shared.di.InjectionQualifier.Interactor
import cafe.adriel.gwentwallpapers.shared.logger.Logger
import cafe.adriel.hal.currentState
import cafe.adriel.hal.emit
import cafe.adriel.hal.observeState
import com.google.android.material.bottomappbar.BottomAppBar
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.scope.viewModel
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.koin.core.qualifier.named

internal class HomeScreen(private val binding: ScreenHomeBinding) : KoinComponent {

    private val viewModel by binding.koinScope.viewModel<HomeViewModel>(binding.activity)
    private val eventSubscriber by binding.koinScope.inject<BrokerSubscriber>()
    private val connectivityHelper by binding.koinScope.inject<ConnectivityHelper>()
    private val wallpaperExpanderHelper by binding.koinScope.inject<WallpaperExpanderHelper>()
    private val wallpaperHelper by inject<WallpaperHelper>()
    private val isFavorite by inject<IsFavoriteInteractor>(named<Interactor.IsFavorite>())
    private val appInfo by inject<AppInfo>()
    private val logger by inject<Logger>()

    private val wallpaperMenuItemIds = setOf(
        R.id.action_favorite,
        R.id.action_set_as,
        R.id.action_share_wallpaper,
        R.id.action_download,
        R.id.action_copy_url
    )

    init {
        setupHelpers()
        setupViews()
        setupListeners()
    }

    private fun setupHelpers() {
        connectivityHelper.init()
    }

    private fun setupViews() {
        binding.navigation.apply {
            setNavigationOnClickListener {
                showSectionNavigationDialog()
            }
            setOnMenuItemClickListener { menuItem ->
                when (viewModel.currentState.section) {
                    ScreenSection.WALLPAPERS -> handleWallpapersMenuItemClicked(menuItem.itemId)
                    ScreenSection.ABOUT -> handleAboutMenuItemClicked(menuItem.itemId)
                }
                true
            }
        }

        binding.setWallpaper.apply {
            hide()
            setOnClickListener { showSetWallpaperLocationDialog() }
        }

        updateSection(viewModel.currentState.section)
        updateSearchBadges(viewModel.currentState.filter, viewModel.currentState.sorter)
    }

    private fun setupListeners() {
        viewModel.observeState(binding.coroutineScope) { state ->
            updateSection(state.section)
            updateSearchBadges(state.filter, state.sorter)
        }

        eventSubscriber.apply {
            subscribe<WallpaperEvent.FavoriteChanged>(binding.activity) { event ->
                updateFavoriteIcon(event.favorite)
            }

            subscribe<WallpaperExpander.Expanded>(binding.activity) { event ->
                onWallpaperExpanded(event.wallpaper)
            }

            subscribe<WallpaperExpander.Shrinked>(binding.activity) {
                onWallpaperShrinked()
            }
        }
    }

    private fun onWallpaperExpanded(wallpaper: Wallpaper) {
        binding.setWallpaper.show {
            binding.navigation.menu.showItemsIf { it.itemId in wallpaperMenuItemIds }
            binding.coroutineScope.launch {
                updateFavoriteIcon(isFavorite(wallpaper))
            }
        }

        binding.navigation.apply {
            navigationIcon = null
            menu.hideItems()
        }
    }

    private fun onWallpaperShrinked() {
        binding.setWallpaper.hide {
            binding.navigation.setNavigationIcon(R.drawable.ic_menu)
            updateSearchBadges(viewModel.currentState.filter, viewModel.currentState.sorter)
        }

        updateSection(viewModel.currentState.section)
    }

    private fun updateSection(section: ScreenSection) {
        binding.activity.tintStatusBarIcons(section.whiteStatusBar)

        binding.navigation.apply {
            when {
                binding.sectionContainer.currentSection == ScreenSection.WALLPAPERS &&
                    section == ScreenSection.WALLPAPERS ->
                    setFabAlignmentModeAndReplaceMenu(BottomAppBar.FAB_ALIGNMENT_MODE_END, section.menuId)
                else -> replaceMenu(section.menuId)
            }

            menu.findItem(R.id.action_app_version)
                ?.title = appInfo.version
        }

        binding.sectionContainer.navigate(section)
    }

    private fun updateSearchBadges(filter: SearchFilter, sorter: SearchSorter) {
        binding.navigation
            .getMenuItemActionView<FilterMenuItemView>(R.id.action_filter)
            ?.apply {
                updateBadge(filter)
                setOnClickListener { showSearchFilterDialog() }
            }

        binding.navigation
            .getMenuItemActionView<SortMenuItemView>(R.id.action_sort)
            ?.apply {
                setOnClickListener { showSearchSorterDialog() }
                updateBadge(sorter)
            }
    }

    private fun updateFavoriteIcon(favorite: Boolean) {
        binding.navigation.menu
            .findItem(R.id.action_favorite)
            ?.setIcon(if (favorite) R.drawable.ic_favorite_on else R.drawable.ic_favorite_off)
    }

    private fun handleWallpapersMenuItemClicked(@IdRes itemId: Int) {
        when (val status = wallpaperExpanderHelper.currentStatus) {
            is WallpaperExpander.Expanded -> when (itemId) {
                R.id.action_favorite -> viewModel emit HomeAction.ToggleFavorite(status.wallpaper)

                R.id.action_copy_url -> wallpaperHelper.copyUrlToClipboard(status.wallpaper, binding)

                R.id.action_share_wallpaper -> connectivityHelper.runIfConnected(binding) {
                    wallpaperHelper.share(status.wallpaper, binding)
                }

                R.id.action_download -> connectivityHelper.runIfConnected(binding) {
                    wallpaperHelper.save(status.wallpaper, binding)
                }

                R.id.action_set_as -> connectivityHelper.runIfConnected(binding) {
                    wallpaperHelper.setExternally(status.wallpaper, binding)
                }
            }
        }
    }

    private fun handleAboutMenuItemClicked(@IdRes itemId: Int) {
        when (itemId) {
            R.id.action_rate_review ->
                binding.activity.openAppOnPlayStore(appInfo.marketUri, appInfo.playStoreUrl, logger)

            R.id.action_share_app ->
                binding.activity.shareText(binding.activity.getString(R.string.about_share_app, appInfo.playStoreUrl))
        }
    }

    private fun setWallpaper(wallpaper: Wallpaper, screen: AutoWallpaperScreen) {
        connectivityHelper.runIfConnected(binding) {
            wallpaperHelper.setDirectly(wallpaper, screen, binding)
        }
    }

    private fun showSectionNavigationDialog() =
        SectionNavigationDialog(
            context = binding.activity,
            currentSection = viewModel.currentState.section,
            onSelected = { viewModel emit HomeAction.Navigate(it) }
        ).show()

    private fun showSearchFilterDialog() =
        SearchFilterDialog(
            context = binding.activity,
            currentFilter = viewModel.currentState.filter,
            onSelected = { viewModel emit HomeAction.UpdateSearchFilter(it) }
        ).show()

    private fun showSearchSorterDialog() =
        SearchSorterDialog(
            context = binding.activity,
            currentSorter = viewModel.currentState.sorter,
            onSelected = { viewModel emit HomeAction.UpdateSearchSorter(it) }
        ).show()

    private fun showSetWallpaperLocationDialog() =
        SetWallpaperLocationDialog(
            context = binding.activity,
            onSelected = { screen ->
                when (val status = wallpaperExpanderHelper.currentStatus) {
                    is WallpaperExpander.Expanded -> setWallpaper(status.wallpaper, screen)
                }
            }
        ).show()
}
