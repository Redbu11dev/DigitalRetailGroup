package com.vashkpi.digitalretailgroup

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.vashkpi.digitalretailgroup.data.preferences.DataStoreRepository
import dagger.hilt.android.AndroidEntryPoint
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

        dataStoreRepository.fcmToken = newToken
        Timber.d("new FCM token obtained: $newToken")

        //send request to the server with new token?


    }

}