package com.vashkpi.digitalretailgroup.data.models.network.dto

import com.vashkpi.digitalretailgroup.data.models.network.dto.UserInfoDto

data class AccountsDto (
    val user_id: String,
    val user_infoDto: UserInfoDto
)