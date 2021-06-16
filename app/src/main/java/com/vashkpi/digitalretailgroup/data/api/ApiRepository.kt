package com.vashkpi.digitalretailgroup.data.api

import android.content.Context
import com.vashkpi.digitalretailgroup.data.models.incoming.ApiGenericAnswer
import com.vashkpi.digitalretailgroup.data.models.outgoing.ConfirmCode
import com.vashkpi.digitalretailgroup.data.models.outgoing.RegisterPhone
import kotlinx.coroutines.flow.Flow
import timber.log.Timber
import javax.inject.Inject

class ApiRepository @Inject constructor(private val  apiService: ApiService) {

    suspend fun registerPhone(registerPhone: RegisterPhone): Flow<Resource<out ApiGenericAnswer?>> {
        Timber.d("trying")
        return networkBoundResource(
            fetch = {
                ApiResponse.create(apiService.registerPhone(registerPhone))
            }
        )
    }

    suspend fun confirmCode(confirmCode: ConfirmCode): Flow<Resource<out ApiGenericAnswer?>> {
        Timber.d("trying")
        return networkBoundResource(
            fetch = {
                ApiResponse.create(apiService.confirmCode(confirmCode))
            }
        )
    }

}