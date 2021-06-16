package com.vashkpi.digitalretailgroup

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.vashkpi.digitalretailgroup.data.local.DataStoreRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class MyFirebaseService : FirebaseMessagingService() {

    @Inject
    lateinit var dataStoreRepository: DataStoreRepository

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
    }

    override fun onNewToken(newToken: String) {
        super.onNewToken(newToken)

        runBlocking {
            dataStoreRepository.saveFcmToken(newToken)
        }
        Timber.i("new FCM token obtained: $newToken")

        //not very safe, probably should also obtain token in pref init somewhere
    }

}