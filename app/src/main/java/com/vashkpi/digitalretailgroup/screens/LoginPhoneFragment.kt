package com.vashkpi.digitalretailgroup.screens

import android.R.attr.endColor
import android.R.attr.startColor
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Bundle
import android.text.InputType
import android.text.method.DigitsKeyListener
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doBeforeTextChanged
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.redmadrobot.inputmask.MaskedTextChangedListener
import com.vashkpi.digitalretailgroup.R
import com.vashkpi.digitalretailgroup.databinding.FragmentLoginPhoneBinding
import com.vashkpi.digitalretailgroup.screens.base.BaseFragment
import com.vashkpi.digitalretailgroup.utils.hideKeyboard
import com.vashkpi.digitalretailgroup.utils.safeNavigate
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber


@AndroidEntryPoint
class LoginPhoneFragment : BaseFragment<FragmentLoginPhoneBinding, LoginPhoneViewModel>(FragmentLoginPhoneBinding::inflate) {

    override val viewModel: LoginPhoneViewModel by viewModels()

    override fun setUpViews() {
        super.setUpViews()

        binding.phone.inputType = InputType.TYPE_CLASS_NUMBER
        binding.phone.keyListener = DigitsKeyListener.getInstance("1234567890+-() ")
        val phoneNumberValueListener = object : MaskedTextChangedListener.ValueListener {
            var rawValue = ""
            override fun onTextChanged(
                maskFilled: Boolean,
                extractedValue: String,
                formattedValue: String
            ) {

                rawValue = Regex("[^0-9]").replace(formattedValue, "")
                Timber.i("phone: $rawValue")

//                Timber.i("extractedValue: $extractedValue")
//                Timber.i("formattedValue: $formattedValue")
            }
        }
        val listener: MaskedTextChangedListener = MaskedTextChangedListener.installOn(
            binding.phone,
            "+7 ([000]) [000]-[00]-[00]",
            phoneNumberValueListener
        )
        binding.phone.hint = listener.placeholder()

        binding.phone.apply {
            doAfterTextChanged {
                if (phoneNumberValueListener.rawValue.length > 10) {
                    binding.phone.hideKeyboard()
                    val phoneRaw = phoneNumberValueListener.rawValue
                    val phoneFormatted = binding.phone.text.toString()
                    //viewModel.postNavigationEvent(LoginPhoneFragmentDirections.actionLoginPhoneFragmentToLoginCodeFragment(it.toString()))
                    binding.phone.setText("")
                    //viewModel.loginWithPhone(phone)
                    viewModel.loginWithPhone(phoneRaw, phoneFormatted)
                }
            }
        }

        binding.phone.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                binding.phone.hideKeyboard()
            }
        }

        binding.text4.setOnClickListener {

        }

        binding.loginProblems.setOnClickListener {

        }

        binding.contactUs.setOnClickListener {

        }
    }

    override fun observeViewModel() {
        super.observeViewModel()


    }

}