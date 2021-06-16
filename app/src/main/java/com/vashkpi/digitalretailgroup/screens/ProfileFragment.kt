package com.vashkpi.digitalretailgroup.screens

import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.addRepeatingJob
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.datepicker.MaterialDatePicker
import com.vashkpi.digitalretailgroup.AppConstants
import com.vashkpi.digitalretailgroup.R
import com.vashkpi.digitalretailgroup.data.local.DataStoreRepository
import com.vashkpi.digitalretailgroup.data.models.datastore.UserInfo
import com.vashkpi.digitalretailgroup.screens.base.BaseFragment
import com.vashkpi.digitalretailgroup.databinding.FragmentProfileBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding, ProfileViewModel>(FragmentProfileBinding::inflate) {

    @Inject
    lateinit var dataStoreRepository: DataStoreRepository

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
            //findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToSaveProfileDataDialogFragment())
            if (isRegistration) {
                //save directly and navigate to barcode
                viewModel.saveProfileData(getUserInfoFromFields())
            }
            else {
                //show dialog
                viewModel.saveProfileData(getUserInfoFromFields())
            }
        }

        binding.logoutBtn.setOnClickListener {
//            showMessage(
//                R.string.snackbar_msg_message_removed,
//                R.string.snackbar_btn_cancel,
//                {
//
//                },
//                2000)
        }

        //TODO move date picker to a separate nav component fragment
        val builder = MaterialDatePicker.Builder.datePicker()
        val picker = builder.build()
        val outputDateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }

        picker.addOnPositiveButtonClickListener {
            binding.birthDateText.setText(outputDateFormat.format(it))
        }

        binding.birthDateText.setOnClickListener {
            picker.show(parentFragmentManager, picker.toString())
        }

        lifecycleScope.launch {
            dataStoreRepository.getUserInfo.collect {
                binding.surnameText.setText(it.surname)
                binding.firstNameText.setText(it.name)
                binding.middleNameText.setText(it.middle_name)
                binding.birthDateText.setText(it.date_of_birth)
                when (it.gender) {
                    AppConstants.GenderValues.FEMALE.value -> {
                        binding.radioGroup.check(R.id.radio_female)
                    }
                    else -> {
                        binding.radioGroup.check(R.id.radio_male)
                    }
                }
                this@launch.cancel()
            }
        }

    }

    override fun observeViews() {
        super.observeViews()

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
    }

    override fun observeViewModel() {
        super.observeViewModel()

        viewLifecycleOwner.addRepeatingJob(Lifecycle.State.STARTED) {
            viewModel.profileDataChanged.collect {
                Timber.d("profileDataChanged: $it")
                if (it) {
                    binding.saveBtn.visibility = View.VISIBLE
                }
                else {
                    binding.saveBtn.visibility = View.GONE
                }
            }
        }

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
            gender
        )
    }

    private fun notifyProfileDataChanged() {
        viewModel.profileDataChanged(
            getUserInfoFromFields()
        )
    }

}