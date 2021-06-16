package com.vashkpi.digitalretailgroup.screens

data class ProfileFormState(
    val surnameError: Int? = null,
    val firstNameError: Int? = null,
    val lastNameError: Int? = null,
    val birthDateError: Int? = null,
    val isDataValid: Boolean = false
)