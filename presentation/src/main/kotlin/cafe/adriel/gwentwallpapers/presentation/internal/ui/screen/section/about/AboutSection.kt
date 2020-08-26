package cafe.adriel.gwentwallpapers.presentation.internal.ui.screen.section.about

import android.os.Build
import cafe.adriel.gwentwallpapers.domain.interactor.GetSettingsInteractor
import cafe.adriel.gwentwallpapers.domain.model.AppInfo
import cafe.adriel.gwentwallpapers.presentation.R
import cafe.adriel.gwentwallpapers.presentation.databinding.SectionAboutBinding
import cafe.adriel.gwentwallpapers.presentation.internal.ktx.activity
import cafe.adriel.gwentwallpapers.presentation.internal.ktx.adapter
import cafe.adriel.gwentwallpapers.presentation.internal.ktx.authorBorderDrawableId
import cafe.adriel.gwentwallpapers.presentation.internal.ktx.coroutineScope
import cafe.adriel.gwentwallpapers.presentation.internal.ktx.openUrl
import cafe.adriel.gwentwallpapers.presentation.internal.ktx.sendEmail
import cafe.adriel.gwentwallpapers.presentation.internal.model.link.ContactLink
import cafe.adriel.gwentwallpapers.presentation.internal.model.link.GameInviteLink
import cafe.adriel.gwentwallpapers.presentation.internal.model.link.LegalLink
import cafe.adriel.gwentwallpapers.presentation.internal.model.link.OpenSourceLink
import cafe.adriel.gwentwallpapers.presentation.internal.ui.adapter.authorInfo
import cafe.adriel.gwentwallpapers.presentation.internal.ui.adapter.quote
import cafe.adriel.gwentwallpapers.presentation.internal.ui.adapter.section
import cafe.adriel.gwentwallpapers.presentation.internal.ui.adapter.selection
import cafe.adriel.gwentwallpapers.presentation.internal.ui.adapter.text
import cafe.adriel.gwentwallpapers.shared.di.InjectionQualifier.Interactor
import cafe.adriel.gwentwallpapers.shared.logger.Logger
import com.mikepenz.fastadapter.dsl.itemAdapter
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.koin.core.qualifier.named

internal class AboutSection(private val binding: SectionAboutBinding) : KoinComponent {

    private val getSettings by inject<GetSettingsInteractor>(named<Interactor.GetSettings>())
    private val appInfo by inject<AppInfo>()
    private val logger by inject<Logger>()

    private val aboutAdapter by adapter(binding.coroutineScope) {
        itemAdapter {
            authorInfo(getSettings().theme.authorBorderDrawableId)

            text(binding.activity.getString(R.string.about_intro_message_1))

            text(binding.activity.getString(R.string.about_intro_message_2))

            selection(GameInviteLink.values()) { binding.activity.openUrl(it.url, logger) }

            text(binding.activity.getString(R.string.about_intro_message_3))

            selection(ContactLink.values(), onSelected = ::openLink)

            section(R.string.about_open_source)

            text(binding.activity.getString(R.string.about_open_source_message))

            selection(OpenSourceLink.values()) { binding.activity.openUrl(it.url, logger) }

            section(R.string.about_legal)

            text(binding.activity.getString(R.string.about_legal_message_1))

            text(binding.activity.getString(R.string.about_legal_message_2))

            selection(LegalLink.values()) { binding.activity.openUrl(it.url, logger) }

            quote(binding.activity.getString(R.string.about_legal_copyright))
        }
    }

    private val emailSubject by lazy {
        binding.activity.getString(
            R.string.about_email_subject,
            binding.activity.getString(R.string.app_name),
            appInfo.version,
            Build.VERSION.SDK_INT
        )
    }

    init {
        setupViews()
    }

    private fun setupViews() {
        binding.aboutList.apply {
            setItemViewCacheSize(aboutAdapter.itemCount)
            adapter = aboutAdapter
        }
    }

    private fun openLink(link: ContactLink) {
        when (link) {
            ContactLink.EMAIL -> binding.activity.sendEmail(link.url, emailSubject)
            else -> binding.activity.openUrl(link.url, logger)
        }
    }
}
