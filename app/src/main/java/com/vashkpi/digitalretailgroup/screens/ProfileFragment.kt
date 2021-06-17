package com.vashkpi.digitalretailgroup.screens

import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.*
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.datepicker.MaterialDatePicker
import com.vashkpi.digitalretailgroup.AppConstants
import com.vashkpi.digitalretailgroup.R
import com.vashkpi.digitalretailgroup.data.models.datastore.UserInfo
import com.vashkpi.digitalretailgroup.data.models.datastore.convertGenderRadioGroupIdToString
import com.vashkpi.digitalretailgroup.screens.base.BaseFragment
import com.vashkpi.digitalretailgroup.databinding.FragmentProfileBinding
import com.vashkpi.digitalretailgroup.screens.dialogs.SaveProfileDataDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
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
            if (isRegistration) {
                //save directly and navigate to barcode
                viewModel.saveProfileData(getUserInfoFromFields(), true)
            }
            else {
                //show dialog
                viewModel.postNavigationEvent(ProfileFragmentDirections.actionProfileFragmentToSaveProfileDataDialogFragment(false))
            }
        }

        binding.logoutBtn.setOnClickListener {
            if (isRegistration) {
                //show dialog
                viewModel.postNavigationEvent(ProfileFragmentDirections.actionProfileFragmentToSaveProfileDataDialogFragment(true))
            }
            else {
                //log out
                //clear datastore and move to launcher fragment
                viewModel.logout()
            }
        }

        binding.surnameText.doAfterTextChanged {
            notifyProfileDataChanged()
        }

        binding.firstNameText.doAfterTextChanged {
            notifyProfileDataChanged()
        }

        binding.middleNameText.doAfterTextChanged {
            notifyProfileDataChanged()
        }

        binding.birthDateText.doAfterTextChanged {
            notifyProfileDataChanged()
        }

        binding.radioGroup.setOnCheckedChangeListener { group, checkedId ->
            notifyProfileDataChanged()
        }

        //TODO move date picker to a separate nav component fragment
        val builder = MaterialDatePicker.Builder.datePicker()
        val picker = builder.build()

//        val outputDateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).apply {
//            timeZone = TimeZone.getTimeZone("UTC")
//        }
        val outputDateFormat = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault()).apply {
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
                if (isRegistration) {
                    //as if "save" button was pressed
                    viewModel.saveProfileData(getUserInfoFromFields(), true)
                }
                else {
                    viewModel.saveProfileData(getUserInfoFromFields(), false)
                }
            }
            else if (bundle[SaveProfileDataDialogFragment.REQUEST_KEY] == SaveProfileDataDialogFragment.RESULT_DO_NOT_SAVE) {
                if (isRegistration) {
                    //navigate to barcode directly
                    viewModel.postNavigationEvent(ProfileFragmentDirections.actionProfileFragmentToNavigationBarcode())
                }
                else {
                    viewModel.setViewsFromLocalValues()
                }
            }
        }

    }

    override fun observeViewModel() {
        super.observeViewModel()

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.name.collect {
                if (binding.firstNameText.text.toString() != it) {
                    Timber.d("firstNameText changed to: $it")
                    binding.firstNameText.setText(it)
                }
                else {
                    Timber.d("firstNameText change observed, but was the same: $it")
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.surname.collect {
                if (binding.surnameText.text.toString() != it) {
                    Timber.d("surname changed to: $it")
                    binding.surnameText.setText(it)
                }
                else {
                    Timber.d("surname changed observe, but was the same: $it")
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.middleName.collect {
                if (binding.middleNameText.text.toString() != it) {
                    Timber.d("middleNameText changed to: $it")
                    binding.middleNameText.setText(it)
                }
                else {
                    Timber.d("middleNameText changed observe, but was the same: $it")
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.birthDate.collect {
                if (binding.birthDateText.text.toString() != it) {
                    Timber.d("birthDateText changed to: $it")
                    binding.birthDateText.setText(it)
                }
                else {
                    Timber.d("birthDateText changed observe, but was the same: $it")
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.genderRadioId.collect {
                if (binding.radioGroup.checkedRadioButtonId != it) {
                    Timber.d("checkedRadioButtonId change to: $it")
                    binding.radioGroup.check(it)
                }
                else {
                    Timber.d("checkedRadioButtonId change observed, but was the same: $it")
                }
            }
        }

        //

        viewLifecycleOwner.addRepeatingJob(Lifecycle.State.STARTED) {
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
            getUserInfoFromFields()
        )
    }

    private fun getUserInfoFromFields(): UserInfo {
        return UserInfo(
            binding.firstNameText.text.toString(),
            binding.surnameText.text.toString(),
            binding.middleNameText.text.toString(),
            binding.birthDateText.text.toString(),
            binding.radioGroup.checkedRadioButtonId.convertGenderRadioGroupIdToString())
    }

}