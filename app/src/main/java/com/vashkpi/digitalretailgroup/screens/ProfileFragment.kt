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
            if (bundle["data"] == SaveProfileDataDialogFragment.RESULT_SAVE) {
                if (isRegistration) {
                    //as if "save" button was pressed
                    viewModel.saveProfileData(getUserInfoFromFields(), true)
                }
                else {
                    viewModel.saveProfileData(getUserInfoFromFields(), false)
                }
            }
            else if (bundle["data"] == SaveProfileDataDialogFragment.RESULT_DO_NOT_SAVE) {
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

        viewModel.name.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .onEach {
                if (binding.firstNameText.text.toString() != it) {
                    Timber.d("firstNameText changed to: $it")
                    binding.firstNameText.setText(it)
                }
                else {
                    Timber.d("firstNameText changed observed, but was the same: $it")
                }
            }.launchIn(lifecycleScope)

        viewModel.surname.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .onEach {
                if (binding.surnameText.text.toString() != it) {
                    Timber.d("surname changed to: $it")
                    binding.surnameText.setText(it)
                }
                else {
                    Timber.d("surname changed observed, but was the same: $it")
                }
            }.launchIn(lifecycleScope)

        viewModel.middleName.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .onEach {
                if (binding.middleNameText.text.toString() != it) {
                    Timber.d("middleNameText changed to: $it")
                    binding.middleNameText.setText(it)
                }
                else {
                    Timber.d("middleNameText changed observed, but was the same: $it")
                }
            }.launchIn(lifecycleScope)

        viewModel.birthDate.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .onEach {
                if (binding.birthDateText.text.toString() != it) {
                    Timber.d("birthDateText changed to: $it")
                    binding.birthDateText.setText(it)
                }
                else {
                    Timber.d("birthDateText changed observed, but was the same: $it")
                }
            }.launchIn(lifecycleScope)

        viewModel.genderRadioId.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .onEach {
                if (binding.radioGroup.checkedRadioButtonId != it) {
                    Timber.d("checkedRadioButtonId changed to: $it")
                    binding.radioGroup.check(it)
                }
                else {
                    Timber.d("checkedRadioButtonId changed observed, but was the same: $it")
                }
            }.launchIn(lifecycleScope)

        //

//        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
//            viewModel.name.collect {
//                if (binding.firstNameText.text.toString() != it) {
//                    Timber.d("firstNameText changed to: $it")
//                    binding.firstNameText.setText(it)
//                }
//                else {
//                    Timber.d("firstNameText changed observed, but was the same: $it")
//                }
//            }
//        }
//
//        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
//            viewModel.surname.collect {
//                if (binding.surnameText.text.toString() != it) {
//                    Timber.d("surname changed to: $it")
//                    binding.surnameText.setText(it)
//                }
//                else {
//                    Timber.d("surname changed observed, but was the same: $it")
//                }
//            }
//        }
//
//        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
//            viewModel.middleName.collect {
//                if (binding.middleNameText.text.toString() != it) {
//                    Timber.d("middleNameText changed to: $it")
//                    binding.middleNameText.setText(it)
//                }
//                else {
//                    Timber.d("middleNameText changed observed, but was the same: $it")
//                }
//            }
//        }
//
//        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
//            viewModel.birthDate.collect {
//                if (binding.birthDateText.text.toString() != it) {
//                    Timber.d("birthDateText changed to: $it")
//                    binding.birthDateText.setText(it)
//                }
//                else {
//                    Timber.d("birthDateText changed observed, but was the same: $it")
//                }
//            }
//        }
//
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.genderRadioId.collect {
                if (binding.radioGroup.checkedRadioButtonId != it) {
                    Timber.d("checkedRadioButtonId changed to: $it")
                    binding.radioGroup.check(it)
                }
                else {
                    Timber.d("checkedRadioButtonId changed observed, but was the same: $it")
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

//        viewLifecycleOwner.addRepeatingJob(Lifecycle.State.STARTED) {
//            viewModel.profileDataChanged.collect {
//                Timber.d("profileDataChanged: $it")
//                if (it) {
//                    binding.saveBtn.visibility = View.VISIBLE
//                }
//                else {
//                    binding.saveBtn.visibility = View.GONE
//                }
//            }
//        }
//
//        viewLifecycleOwner.addRepeatingJob(Lifecycle.State.STARTED) {
//            viewModel.resetViewsEvent.collect {
//                Timber.d("collecting reset views event: $it")
//                setViewsFromLocalValues()
//            }
//        }

    }

    private fun notifyProfileDataChanged() {
        viewModel.profileDataChanged(
            getUserInfoFromFields()
        )
    }

    private fun getUserInfoFromFields(): UserInfo {
        val gender = when (binding.radioGroup.checkedRadioButtonId) {
            R.id.radio_male -> {
                AppConstants.GenderValues.MALE.value
            }
            else -> {
                AppConstants.GenderValues.FEMALE.value
            }
        }

        return UserInfo(
            binding.firstNameText.text.toString(),
            binding.surnameText.text.toString(),
            binding.middleNameText.text.toString(),
            binding.birthDateText.text.toString(),
            gender)
    }

}



//package com.vashkpi.digitalretailgroup.screens
//
//import android.view.View
//import androidx.core.widget.doAfterTextChanged
//import androidx.core.widget.doOnTextChanged
//import androidx.fragment.app.setFragmentResultListener
//import androidx.fragment.app.viewModels
//import androidx.lifecycle.Lifecycle
//import androidx.lifecycle.addRepeatingJob
//import androidx.lifecycle.lifecycleScope
//import androidx.lifecycle.repeatOnLifecycle
//import androidx.navigation.fragment.findNavController
//import androidx.navigation.fragment.navArgs
//import androidx.navigation.ui.setupWithNavController
//import com.google.android.material.datepicker.MaterialDatePicker
//import com.vashkpi.digitalretailgroup.AppConstants
//import com.vashkpi.digitalretailgroup.R
//import com.vashkpi.digitalretailgroup.data.local.DataStoreRepository
//import com.vashkpi.digitalretailgroup.data.models.datastore.UserInfo
//import com.vashkpi.digitalretailgroup.screens.base.BaseFragment
//import com.vashkpi.digitalretailgroup.databinding.FragmentProfileBinding
//import com.vashkpi.digitalretailgroup.screens.dialogs.SaveProfileDataDialogFragment
//import com.vashkpi.digitalretailgroup.utils.safeNavigate
//import dagger.hilt.android.AndroidEntryPoint
//import kotlinx.coroutines.cancel
//import kotlinx.coroutines.flow.collect
//import kotlinx.coroutines.launch
//import kotlinx.coroutines.runBlocking
//import timber.log.Timber
//import java.text.SimpleDateFormat
//import java.util.*
//import javax.inject.Inject
//
//@AndroidEntryPoint
//class ProfileFragment : BaseFragment<FragmentProfileBinding, ProfileViewModel>(FragmentProfileBinding::inflate) {
//
//    @Inject
//    lateinit var dataStoreRepository: DataStoreRepository
//
//    override val viewModel: ProfileViewModel by viewModels()
//
//    private val args: ProfileFragmentArgs by navArgs()
//
//    override fun setUpViews() {
//        super.setUpViews()
//
//        val toolbar = binding.customToolbar.toolbar
//
//        val navController = findNavController()
//        toolbar.setupWithNavController(navController)
//        //toolbar.setNavigationIcon(R.drawable.ic_bell)
//        //toolbar.nav
//
//        val isRegistration = args.isRegistration
//
//        if (isRegistration) {
//            binding.saveBtn.setText(R.string.profile_btn_1_save_data)
//            binding.logoutBtn.setText(R.string.profile_btn_2_later)
//            //findNavController().currentDestination?.label = getString(R.string.profile_title)
//            toolbar.setTitle(R.string.profile_title_is_registration)
//
//        }
//        else {
//            binding.saveBtn.setText(R.string.profile_btn_1_save)
//            binding.logoutBtn.setText(R.string.profile_btn_2_logout)
//            toolbar.setTitle(R.string.profile_title)
//        }
//
//        binding.saveBtn.setOnClickListener {
//            if (isRegistration) {
//                //save directly and navigate to barcode
//                viewModel.saveProfileData(getUserInfoFromFields(), true)
//            }
//            else {
//                //show dialog
//                findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToSaveProfileDataDialogFragment(false))
//            }
//        }
//
//        binding.logoutBtn.setOnClickListener {
//            if (isRegistration) {
//                //show dialog
//                findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToSaveProfileDataDialogFragment(true))
//            }
//            else {
//                //log out
//                //clear datastore and move to launcher fragment
//                lifecycleScope.launch {
//                    dataStoreRepository.clearDataStore()
//                    viewModel.postNavigationEvent(ProfileFragmentDirections.actionGlobalLogoutToLauncher())
//                }
//            }
//
////            showMessage(
////                R.string.snackbar_msg_message_removed,
////                R.string.snackbar_btn_cancel,
////                {
////
////                },
////                2000)
//        }
//
//        binding.surnameText.doAfterTextChanged {
//            notifyProfileDataChanged()
//        }
//
//        binding.firstNameText.doAfterTextChanged {
//            notifyProfileDataChanged()
//        }
//
//        binding.middleNameText.doAfterTextChanged {
//            notifyProfileDataChanged()
//        }
//
//        binding.birthDateText.doAfterTextChanged {
//            notifyProfileDataChanged()
//        }
//
//        binding.radioGroup.setOnCheckedChangeListener { group, checkedId ->
//            notifyProfileDataChanged()
//        }
//
//        //TODO move date picker to a separate nav component fragment
//        val builder = MaterialDatePicker.Builder.datePicker()
//        val picker = builder.build()
////        val outputDateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).apply {
////            timeZone = TimeZone.getTimeZone("UTC")
////        }
//        val outputDateFormat = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault()).apply {
//            timeZone = TimeZone.getTimeZone("UTC")
//        }
//
//        picker.addOnPositiveButtonClickListener {
//            binding.birthDateText.setText(outputDateFormat.format(it))
//        }
//
//        binding.birthDateText.setOnClickListener {
//            picker.show(parentFragmentManager, picker.toString())
//        }
//
//        setViewsFromLocalValues()
//
//        //Listen for fragment results
//        setFragmentResultListener(SaveProfileDataDialogFragment.REQUEST_KEY) { key, bundle ->
//            // read from the bundle
//            Timber.d("Received fragment result: $bundle")
//            if (bundle["data"] == SaveProfileDataDialogFragment.RESULT_SAVE) {
//                if (isRegistration) {
//                    //as if "save" button was pressed
//                    viewModel.saveProfileData(getUserInfoFromFields(), true)
//                }
//                else {
//                    viewModel.saveProfileData(getUserInfoFromFields(), false)
//                }
//            }
//            else if (bundle["data"] == SaveProfileDataDialogFragment.RESULT_DO_NOT_SAVE) {
//                if (isRegistration) {
//                    //navigate to barcode directly
//                    binding.root.post {
//                        viewModel.postNavigationEvent(ProfileFragmentDirections.actionProfileFragmentToNavigationBarcode())
//                    }
//                }
//                else {
//                    setViewsFromLocalValues()
//                }
//            }
//        }
//
//    }
//
//    override fun observeViewModel() {
//        super.observeViewModel()
//
//        viewLifecycleOwner.addRepeatingJob(Lifecycle.State.STARTED) {
//            viewModel.profileDataChanged.collect {
//                Timber.d("profileDataChanged: $it")
//                if (it) {
//                    binding.saveBtn.visibility = View.VISIBLE
//                }
//                else {
//                    binding.saveBtn.visibility = View.GONE
//                }
//            }
//        }
//
//        viewLifecycleOwner.addRepeatingJob(Lifecycle.State.STARTED) {
//            viewModel.resetViewsEvent.collect {
//                Timber.d("collecting reset views event: $it")
//                setViewsFromLocalValues()
//            }
//        }
//
//    }
//
//    private fun setViewsFromLocalValues() {
//        lifecycleScope.launch {
//            dataStoreRepository.getUserInfo.collect {
//                binding.surnameText.setText(it.surname)
//                binding.firstNameText.setText(it.name)
//                binding.middleNameText.setText(it.middle_name)
//                binding.birthDateText.setText(it.date_of_birth)
//                when (it.gender) {
//                    AppConstants.GenderValues.FEMALE.value -> {
//                        binding.radioGroup.check(R.id.radio_female)
//                    }
//                    else -> {
//                        binding.radioGroup.check(R.id.radio_male)
//                    }
//                }
//                this@launch.cancel()
//            }
//        }
//    }
//
//    private fun getUserInfoFromFields(): UserInfo {
//        val gender = when (binding.radioGroup.checkedRadioButtonId) {
//            R.id.radio_male -> {
//                AppConstants.GenderValues.MALE.value
//            }
//            else -> {
//                AppConstants.GenderValues.FEMALE.value
//            }
//        }
//
//        return UserInfo(
//            binding.firstNameText.text.toString(),
//            binding.surnameText.text.toString(),
//            binding.middleNameText.text.toString(),
//            binding.birthDateText.text.toString(),
//            gender
//        )
//    }
//
//    private fun notifyProfileDataChanged() {
//        viewModel.profileDataChanged(
//            getUserInfoFromFields()
//        )
//    }
//
//}