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
import com.vashkpi.digitalretailgroup.utils.safeNavigate
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber


@AndroidEntryPoint
class LoginPhoneFragment : BaseFragment() {

    private val viewModel: LoginPhoneViewModel by viewModels()

    private var _binding: FragmentLoginPhoneBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginPhoneBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.phone.inputType = InputType.TYPE_CLASS_NUMBER;
        binding.phone.keyListener = DigitsKeyListener.getInstance("1234567890+-() ");
        val phoneNumberValueListener = object : MaskedTextChangedListener.ValueListener {
            var rawValue = ""
            override fun onTextChanged(
                maskFilled: Boolean,
                extractedValue: String,
                formattedValue: String
            ) {
//                Log.d("TAG_extr", extractedValue)
//                Log.d("TAG_filled", maskFilled.toString())
//                Log.d("TAG_formatted", formattedValue)
                rawValue = Regex("[^0-9]").replace(formattedValue, "")
            }
        }
        val listener: MaskedTextChangedListener = MaskedTextChangedListener.installOn(
            binding.phone,
            "+7 ([000]) [000]-[00]-[00]",
            phoneNumberValueListener
        )
        binding.phone.setHint(listener.placeholder())

        binding.phone.apply {
            doBeforeTextChanged { text, start, count, after ->

            }
            doOnTextChanged { text, start, before, count ->

            }
            doAfterTextChanged {
//                loginViewModel.loginDataChanged(
//                    phoneNumberValueListener.rawValue,
//                    binding.password.text.toString()
//                )
                if (phoneNumberValueListener.rawValue.length > 10) {
                    viewModel.postNavigationEvent(LoginPhoneFragmentDirections.actionLoginPhoneFragmentToLoginCodeFragment(it.toString()))
                }
            }
        }

//        val shader: Shader = LinearGradient(
//            0f, 0f, binding.contactUs.paint.measureText(binding.contactUs.text.toString()), 0f,
//            ContextCompat.getColor(requireContext(), R.color.clickable_green_text_color), ContextCompat.getColor(requireContext(), R.color.clickable_yellow_text_color), Shader.TileMode.CLAMP
//        )
//        binding.contactUs.paint.shader = shader

        viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.navigationEvent.collect {
                    Timber.i("collecting navigation event ${it}")
                    findNavController().safeNavigate(it)
                }
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}