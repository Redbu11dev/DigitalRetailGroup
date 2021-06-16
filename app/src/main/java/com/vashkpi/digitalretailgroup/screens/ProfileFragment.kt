package com.vashkpi.digitalretailgroup.screens

import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.datepicker.MaterialDatePicker
import com.vashkpi.digitalretailgroup.AppConstants
import com.vashkpi.digitalretailgroup.R
import com.vashkpi.digitalretailgroup.screens.base.BaseFragment
import com.vashkpi.digitalretailgroup.databinding.FragmentProfileBinding
import dagger.hilt.android.AndroidEntryPoint
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

        if (args.isRegistration) {
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
    }

    private fun notifyProfileDataChanged() {
        viewModel.profileDataChanged(
            binding.surnameText.text.toString(),
            binding.firstNameText.text.toString(),
            binding.middleNameText.text.toString(),
            binding.birthDateText.text.toString(),
            AppConstants.GenderValues.MALE
        )
    }

}