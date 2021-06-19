package com.vashkpi.digitalretailgroup

import android.app.Application
import android.provider.Settings
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

//Icon conversion (55dp x 55dp)
//dp	px	sp	mm	in	pt
//ldpi	@ 55.00dp	= 41.25px	= 55.00sp	= 8.73mm	= 0.34in	= 24.75pt
//mdpi	@ 55.00dp	= 55.00px	= 55.00sp	= 8.73mm	= 0.34in	= 24.75pt
//tvdpi	@ 55.00dp	= 73.22px	= 55.00sp	= 8.73mm	= 0.34in	= 24.75pt
//hdpi	@ 55.00dp	= 82.50px	= 55.00sp	= 8.73mm	= 0.34in	= 24.75pt
//xhdpi	@ 55.00dp	= 110.00px	= 55.00sp	= 8.73mm	= 0.34in	= 24.75pt
//xxhdpi	@ 55.00dp	= 165.00px	= 55.00sp	= 8.73mm	= 0.34in	= 24.75pt
//xxxhdpi	@ 55.00dp	= 220.00px	= 55.00sp	= 8.73mm	= 0.34in	= 24.75pt


@HiltAndroidApp
class DrgApplication: Application() {

    companion object {
        private var _deviceId = "not_obtained"
        val DEVICE_ID = _deviceId
    }

    override fun onCreate() {
        super.onCreate()

        _deviceId = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            //Timber.plant(TimberReleaseTree())
        }
    }

}