package com.vashkpi.digitalretailgroup.utils

import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.view.get
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.vashkpi.digitalretailgroup.databinding.CustomToolbarBinding

fun CustomToolbarBinding?.showLogo(
    showLogo: Boolean
): CustomToolbarBinding? {
    this?.let { toolbarBinding ->
        if (showLogo) {
            toolbarBinding.title.visibility = View.GONE
            toolbarBinding.backButtonContainer.visibility = View.GONE

            toolbarBinding.logo.visibility = View.VISIBLE
        } else {
            toolbarBinding.logo.visibility = View.GONE
        }
    }

    return this
}

fun CustomToolbarBinding?.setTitle(
    titleText: String?
): CustomToolbarBinding? {
    this?.let { toolbarBinding ->
        val titleView = toolbarBinding.title
        if (titleText.isNullOrBlank()) {
            titleView.visibility = View.GONE
        } else {
            titleView.text = titleText
            titleView.visibility = View.VISIBLE

            toolbarBinding.logo.visibility = View.GONE
            toolbarBinding.backButtonText.visibility = View.GONE
        }
    }
    return this
}

fun CustomToolbarBinding?.showBackButtonIfAvailable(
    navController: NavController,
    withText: Boolean
): CustomToolbarBinding? {
    this?.let { toolbarBinding ->
        val backButtonContainer = toolbarBinding.backButtonContainer
        if (navController.previousBackStackEntry == null) {
            backButtonContainer.visibility = View.GONE
        } else {
            backButtonContainer.setOnClickListener {
                navController.navigateUp()
            }
            if (withText) {
                toolbarBinding.backButtonText.visibility = View.VISIBLE
            } else {
                toolbarBinding.backButtonText.visibility = View.GONE
            }
            backButtonContainer.visibility = View.VISIBLE
        }
    }

    return this
}

fun CustomToolbarBinding?.setButtons(
    buttonIcons: Array<Int>? = null,
    menuButtonClickListener: (buttonId: Int) -> Unit? = {}
): CustomToolbarBinding? {
    this?.let { toolbarBinding ->
        buttonIcons?.let {
            if (buttonIcons.isNotEmpty()) {
                toolbarBinding.menuButtons.visibility = View.VISIBLE
                it.forEachIndexed { index, iconResId ->
                    if (index > 1) {
                        throw IllegalStateException("can't have more than 2 buttons")
                    }
                    val button = (toolbarBinding.menuButtons[index] as AppCompatImageView)
                    button.visibility = View.VISIBLE
                    button.setImageResource(iconResId)
                    button.setOnClickListener {
                        menuButtonClickListener(it.id)
                    }
                }
            } else {
                toolbarBinding.menuButtons.visibility = View.GONE
            }
        }
    }

    return this
}

fun CustomToolbarBinding?.setUpWithNavController(
    navController: NavController,
    titleText: String?,
    showBackButtonIfAvailable: Boolean = true,
    showBackButtonText: Boolean = titleText?.isBlank() ?: true
): CustomToolbarBinding? {
    this?.let { toolbarBinding ->

        showLogo(false)

        setTitle(titleText)

        if (showBackButtonIfAvailable) {
            showBackButtonIfAvailable(navController, showBackButtonText)
        }

    }

    return this
}