package com.vashkpi.digitalretailgroup.screens

import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.vashkpi.digitalretailgroup.R
import com.vashkpi.digitalretailgroup.data.models.domain.formatDateToNotificationDate
import com.vashkpi.digitalretailgroup.databinding.FragmentViewNotificationBinding
import com.vashkpi.digitalretailgroup.screens.base.BaseFragment
import com.vashkpi.digitalretailgroup.utils.setButtons
import com.vashkpi.digitalretailgroup.utils.setUpWithNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class ViewNotificationFragment : BaseFragment<FragmentViewNotificationBinding, ViewNotificationViewModel>(FragmentViewNotificationBinding::inflate) {

    companion object {
        const val REQUEST_KEY = "VIEW_NOTIFICATION_DIALOG_REQUEST_KEY"
        const val RESULT_DELETE = "RESULT_DELETED"
        const val NOTIFICATION_ID = "NOTIFICATION_ID"
    }

    override val viewModel: ViewNotificationViewModel by viewModels()

    private val args: ViewNotificationFragmentArgs by navArgs()

    override fun setupToolbar() {
        getCustomToolbarBinding().setUpWithNavController(
            titleText = null,
            navController = findNavController()
        ).setButtons(
            buttonIcons = arrayOf(
                R.drawable.ic_trash
            )
        ) { id ->
            when (id) {
                R.id.button0 -> {
                    setFragmentResult(
                        REQUEST_KEY, bundleOf(
                            REQUEST_KEY to RESULT_DELETE,
                            NOTIFICATION_ID to args.notification.notification_id
                        )
                    )
                    viewModel.onDeletePressed()
                }
            }
        }
    }

    override fun setUpViews() {
        super.setUpViews()

//        val toolbar = binding.customToolbar.toolbar
//        val navController = findNavController()
//        toolbar.setupWithNavController(navController)

        val notification = args.notification

        binding.title.text = notification.title
        binding.text.text = notification.text
        binding.date.text = notification.date.formatDateToNotificationDate()

//        toolbar.inflateMenu(R.menu.toolbar_menu_notifications)
//        toolbar.setOnMenuItemClickListener {
//            when (it.itemId) {
//                R.id.delete -> {
//                    //viewModel.postNavigationEvent(BarcodeFragmentDirections.actionNavigationBarcodeToNotificationsFragment())
//                    setFragmentResult(
//                        REQUEST_KEY, bundleOf(
//                            REQUEST_KEY to RESULT_DELETE,
//                            NOTIFICATION_ID to notification.notification_id
//                        )
//                    )
//                    viewModel.onDeletePressed()
//                    true
//                }
//                else -> false
//            }
//        }

        if (!notification.read) {
            viewModel.markRead(notification)
        }

    }

    override fun observeViewModel() {
        super.observeViewModel()

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.notificationRemovedEvent.collect {
                findNavController().navigateUp()
            }
        }

//        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
//            viewModel.notification.collect {
//                it?.let {
//                    binding.title.text = it.title
//                    binding.text.text = it.text
//                    binding.date.text = it.text
//                }
//            }
//        }

    }

}