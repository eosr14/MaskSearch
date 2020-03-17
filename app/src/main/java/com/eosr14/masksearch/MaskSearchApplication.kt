package com.eosr14.masksearch

import android.app.Application
import com.google.android.gms.ads.MobileAds

class MaskSearchApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        MobileAds.initialize(this) { }
    }

}