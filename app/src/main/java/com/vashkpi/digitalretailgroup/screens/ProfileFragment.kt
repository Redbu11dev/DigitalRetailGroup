package com.vashkpi.digitalretailgroup.screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.datepicker.MaterialDatePicker
import com.vashkpi.digitalretailgroup.R
import com.vashkpi.digitalretailgroup.screens.base.BaseFragment
import com.vashkpi.digitalretailgroup.databinding.FragmentProfileBinding
import com.vashkpi.digitalretailgroup.utils.showMessage
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding, NotificationsViewModel>(FragmentProfileBinding::inflate) {

    //private val viewModel: ProfileViewModel by viewModels()
    override val viewModel: NotificationsViewModel by viewModels()

    private val args: ProfileFragmentArgs by navArgs()

    override fun setUpViews() {
        super.setUpViews()

        val root: View = binding.root

        val navController = findNavController()
        binding.customToolbar.toolbar.setupWithNavController(navController)
        //binding.customToolbar.toolbar.setNavigationIcon(R.drawable.ic_bell)
        //binding.customToolbar.toolbar.nav

        if (args.isRegistration) {
            binding.saveBtn.setText(R.string.profile_btn_1_save_data)
            binding.logoutBtn.setText(R.string.profile_btn_2_later)
        }
        else {
            binding.saveBtn.setText(R.string.profile_btn_1_save)
            binding.logoutBtn.setText(R.string.profile_btn_2_logout)
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

//    private var _binding: FragmentProfileBinding? = null
//    // This property is only valid between onCreateView and
//    // onDestroyView.
//    private val binding get() = _binding!!
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        _binding = FragmentProfileBinding.inflate(inflater, container, false)
//        val root: View = binding.root
//
//        val navController = findNavController()
//        binding.customToolbar.toolbar.setupWithNavController(navController)
//        //binding.customToolbar.toolbar.setNavigationIcon(R.drawable.ic_bell)
//        //binding.customToolbar.toolbar.nav
//
//        if (args.isRegistration) {
//            binding.saveBtn.setText(R.string.profile_btn_1_save_data)
//            binding.logoutBtn.setText(R.string.profile_btn_2_later)
//        }
//        else {
//            binding.saveBtn.setText(R.string.profile_btn_1_save)
//            binding.logoutBtn.setText(R.string.profile_btn_2_logout)
//        }
//
//        binding.saveBtn.setOnClickListener {
//            //findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToSaveProfileDataDialogFragment())
//        }
//
//        binding.logoutBtn.setOnClickListener {
////            showMessage(
////                R.string.snackbar_msg_message_removed,
////                R.string.snackbar_btn_cancel,
////                {
////
////                },
////                2000)
//        }
//
//        //TODO move date picker to a separate nav component fragment
//        val builder = MaterialDatePicker.Builder.datePicker()
//        val picker = builder.build()
//        val outputDateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).apply {
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
//        return root
//    }
}