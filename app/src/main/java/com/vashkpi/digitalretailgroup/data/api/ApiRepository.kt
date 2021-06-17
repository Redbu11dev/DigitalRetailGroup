package com.vashkpi.digitalretailgroup.data.api

import com.vashkpi.digitalretailgroup.data.models.datastore.UserInfo
import com.vashkpi.digitalretailgroup.data.models.incoming.ApiGenericAnswer
import com.vashkpi.digitalretailgroup.data.models.incoming.ConfirmCodeResponse
import com.vashkpi.digitalretailgroup.data.models.outgoing.Accounts
import com.vashkpi.digitalretailgroup.data.models.outgoing.ConfirmCode
import com.vashkpi.digitalretailgroup.data.models.outgoing.RegisterPhone
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import timber.log.Timber
import javax.inject.Inject

class ApiRepository @Inject constructor(private val  apiService: ApiService) {

    suspend fun registerPhone(registerPhone: RegisterPhone): Flow<Resource<out ApiGenericAnswer?>> {
        Timber.d("trying")
        return simpleNetworkResource(
            fetch = {
                ApiResponse.create(apiService.registerPhone(registerPhone))
            }
        )
    }

    suspend fun confirmCode(confirmCode: ConfirmCode): Flow<Resource<out ConfirmCodeResponse?>> {
        Timber.d("trying")
        return simpleNetworkResource(
            fetch = {
                ApiResponse.create(apiService.confirmCode(confirmCode))
            }
        )
    }

    suspend fun saveProfileInfo(accounts: Accounts): Flow<Resource<out ApiGenericAnswer?>> {
        Timber.d("trying")
        return simpleNetworkResource(
            fetch = {
                ApiResponse.create(apiService.saveProfileInfo(accounts))
            }
        )
    }

}