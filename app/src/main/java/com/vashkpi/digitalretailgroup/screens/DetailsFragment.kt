package com.vashkpi.digitalretailgroup.screens

import androidx.core.text.parseAsHtml
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.setupWithNavController
import com.vashkpi.digitalretailgroup.R
import com.vashkpi.digitalretailgroup.databinding.FragmentDetailsBinding
import com.vashkpi.digitalretailgroup.screens.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class DetailsFragment : BaseFragment<FragmentDetailsBinding, DetailsViewModel>(FragmentDetailsBinding::inflate) {

    override val viewModel: DetailsViewModel by viewModels()

    private val args: DetailsFragmentArgs by navArgs()

    override fun setUpViews() {
        super.setUpViews()

//        val toolbar = binding.customToolbar.toolbar
//        val navController = findNavController()
//        toolbar.setupWithNavController(navController)

        val type = args.type

//        when (type) {
//            0 -> {
//                toolbar.title = getString(R.string.description_title_save)
//                viewModel.getSavePointsRules()
//            }
//            1 -> {
//                toolbar.title = getString(R.string.description_title_get)
//                viewModel.getSpendPointsRules()
//            }
//            2 -> {
//                toolbar.title = getString(R.string.description_title_promotion_rules)
//                viewModel.getSpendPointsRules()
//            }
//            else -> {
//                toolbar.title = "Unknown"
//            }
//        }

    }

    override fun observeViewModel() {
        super.observeViewModel()

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.ruleText.collect {
                binding.text.text = it.parseAsHtml()
            }
        }

    }

    override fun onStart() {
        super.onStart()

        viewModel.logOpenedRulesEvent()
    }

}