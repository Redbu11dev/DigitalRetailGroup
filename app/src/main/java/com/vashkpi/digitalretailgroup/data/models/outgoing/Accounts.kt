package com.vashkpi.digitalretailgroup.data.models.outgoing

import com.vashkpi.digitalretailgroup.data.models.datastore.UserInfo

data class Accounts (
    val user_id: String,
    val user_info: UserInfo
)