package com.vashkpi.digitalretailgroup.screens

import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.datepicker.MaterialDatePicker
import com.vashkpi.digitalretailgroup.R
import com.vashkpi.digitalretailgroup.data.models.domain.UserInfo
import com.vashkpi.digitalretailgroup.data.models.domain.convertGenderRadioGroupIdToString
import com.vashkpi.digitalretailgroup.data.models.domain.convertGenderStringToRadioGroupId
import com.vashkpi.digitalretailgroup.databinding.FragmentProfileBinding
import com.vashkpi.digitalretailgroup.screens.base.BaseFragment
import com.vashkpi.digitalretailgroup.screens.dialogs.SaveProfileDataDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding, ProfileViewModel>(FragmentProfileBinding::inflate) {

    override val viewModel: ProfileViewModel by viewModels()

    private val args: ProfileFragmentArgs by navArgs()

    override fun setUpViews() {
        super.setUpViews()

        val toolbar = binding.customToolbar.toolbar

        val navController = findNavController()

        toolbar.setupWithNavController(navController)
        //NavigationUI.setupWithNavController(toolbar, navController, AppBarConfiguration(navController.graph))

//        val configuration = AppBarConfiguration(navController.graph)
//        navController.addOnDestinationChangedListener(
//            //ToolbarOnDestinationChangedListener(toolbar, configuration)
//            NavController.OnDestinationChangedListener { controller, destination, arguments ->  }
//        )
////        navController.addOnDestinationChangedListener { controller, destination, arguments ->
////
////        }
//        toolbar.setNavigationOnClickListener {
//            NavigationUI.navigateUp(
//                navController,
//                configuration
//            )
//        }

        //toolbar.nav

        //toolbar.setNavigationIcon(R.drawable.ic_bell)
        //toolbar.nav

        val isRegistration = args.isRegistration

        if (isRegistration) {
            binding.saveBtn.setText(R.string.profile_btn_1_save_data)
            binding.logoutBtn.setText(R.string.profile_btn_2_later)
            //findNavController().currentDestination?.label = getString(R.string.profile_title)
            toolbar.setTitle(R.string.profile_title_is_registration)

        }
        else {
            binding.saveBtn.setText(R.string.profile_btn_1_save)
            binding.logoutBtn.setText(R.string.profile_btn_2_logout)
            toolbar.setTitle(R.string.profile_title)
        }

        binding.saveBtn.setOnClickListener {
            viewModel.onSaveButtonClick(isRegistration)
        }

        binding.logoutBtn.setOnClickListener {
            viewModel.onSecondaryButtonClick(isRegistration)
        }

        binding.surnameText.doAfterTextChanged {
            if (lifecycle.currentState >= Lifecycle.State.RESUMED) {
                notifyProfileDataChanged()
            }
        }

        binding.firstNameText.doAfterTextChanged {
            if (lifecycle.currentState >= Lifecycle.State.RESUMED) {
                notifyProfileDataChanged()
            }
        }

        binding.middleNameText.doAfterTextChanged {
            if (lifecycle.currentState >= Lifecycle.State.RESUMED) {
                notifyProfileDataChanged()
            }
        }

        binding.birthDateText.doAfterTextChanged {
            if (lifecycle.currentState >= Lifecycle.State.RESUMED) {
                notifyProfileDataChanged()
            }
        }

        binding.radioGroup.setOnCheckedChangeListener { group, checkedId ->
            if (lifecycle.currentState >= Lifecycle.State.RESUMED) {
                notifyProfileDataChanged()
            }
        }

        //TODO move date picker to a separate nav component fragment
        val builder = MaterialDatePicker.Builder.datePicker()
        val picker = builder.build()

//        val outputDateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).apply {
//            timeZone = TimeZone.getTimeZone("UTC")
//        }
//        val outputDateFormat = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault()).apply {
//            timeZone = TimeZone.getTimeZone("UTC")
//        }
        val outputDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }

        picker.addOnPositiveButtonClickListener {
            binding.birthDateText.setText(outputDateFormat.format(it))
        }

        binding.birthDateText.setOnClickListener {
            picker.show(parentFragmentManager, picker.toString())
        }

        //Listen for fragment results
        setFragmentResultListener(SaveProfileDataDialogFragment.REQUEST_KEY) { key, bundle ->
            // read from the bundle
            Timber.d("Received fragment result: $bundle")
            if (bundle[SaveProfileDataDialogFragment.REQUEST_KEY] == SaveProfileDataDialogFragment.RESULT_SAVE) {
                viewModel.onSaveInfoDialogPositiveButtonClick(isRegistration)
            }
            else if (bundle[SaveProfileDataDialogFragment.REQUEST_KEY] == SaveProfileDataDialogFragment.RESULT_DO_NOT_SAVE) {
                viewModel.onSaveInfoDialogNegativeButtonClick(isRegistration)
            }
        }

    }

    override fun observeViewModel() {
        super.observeViewModel()

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.localUserInfo.collect {
                it?.let {
                    if (binding.firstNameText.text.toString() != it.name) {
                        binding.firstNameText.setText(it.name)
                    }
                    if (binding.surnameText.text.toString() != it.surname) {
                        binding.surnameText.setText(it.surname)
                    }
                    if (binding.middleNameText.text.toString() != it.middle_name) {
                        binding.middleNameText.setText(it.middle_name)
                    }
                    if (binding.birthDateText.text.toString() != it.date_of_birth) {
                        binding.birthDateText.setText(it.date_of_birth)
                    }
                    if (binding.radioGroup.checkedRadioButtonId != it.gender.convertGenderStringToRadioGroupId()) {
                        binding.radioGroup.check(it.gender.convertGenderStringToRadioGroupId())
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.profileDataHasChanges.collect {
                //Timber.d("checkedRadioButtonId changed to: $it")
                if (it) {
                    binding.saveBtn.visibility = View.VISIBLE
                }
                else {
                    binding.saveBtn.visibility = View.GONE
                }
            }
        }

    }

    private fun notifyProfileDataChanged() {
        viewModel.profileDataChanged(
            UserInfo(
                binding.firstNameText.text.toString(),
                binding.surnameText.text.toString(),
                binding.middleNameText.text.toString(),
                binding.birthDateText.text.toString(),
                binding.radioGroup.checkedRadioButtonId.convertGenderRadioGroupIdToString())
        )
    }

}