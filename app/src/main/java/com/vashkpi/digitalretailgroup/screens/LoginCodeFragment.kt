package com.vashkpi.digitalretailgroup.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doBeforeTextChanged
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.setupWithNavController
import com.vashkpi.digitalretailgroup.R
import com.vashkpi.digitalretailgroup.databinding.FragmentLauncherBinding
import com.vashkpi.digitalretailgroup.databinding.FragmentLoginCodeBinding
import com.vashkpi.digitalretailgroup.screens.base.BaseFragment
import com.vashkpi.digitalretailgroup.utils.safeNavigate
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class LoginCodeFragment : BaseFragment() {

    private val viewModel: LoginCodeViewModel by viewModels()

    private val args: LoginCodeFragmentArgs by navArgs()

    private var _binding: FragmentLoginCodeBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginCodeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        progressView = binding.progressView.root

        val navController = findNavController()
        binding.customToolbar.toolbar.setupWithNavController(navController)

        val phoneStringFakeSpaces = args.phoneString.replace(" ", " ")
        binding.text2.text = String.format(getString(R.string.login_code_description), phoneStringFakeSpaces)

        binding.phone.apply {
            doAfterTextChanged {
                if (it.toString().length > 3) {
                    viewModel.confirmCode(args.phoneString, it.toString())
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.navigationEvent.collect {
                    Timber.i("collecting navigation event ${it}")
                    findNavController().safeNavigate(it)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.progressViewVisible.collect {
                    //Timber.i("collecting navigation event ${it}")
                    setProgressViewEnabled(it)
                }
            }
        }

//        viewLifecycleOwner.lifecycleScope.launch {
//            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
//                viewModel.errorMessageOneShot.collect {
//                    //Timber.i("collecting navigation event ${it}")
//                    //setProgressViewEnabled(it)
//                    //findNavController().navigate(R.id.dial)
//                    findNavController().navigate(LoginCodeFragmentDirections.actionGlobalMessageDialog())
//                }
//            }
//        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}