package com.eosr14.masksearch.common.analytics

import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics

class FirebaseProvider(private val context: Context) : ProviderType {
    override fun sendLog(event: String, parameter: HashMap<String, Any>) {
        val bundle = Bundle().apply {
            parameter.entries.forEach {
                this.putString(it.key, it.value.toString())
            }
        }
        FirebaseAnalytics.getInstance(context).logEvent(event, bundle)
    }

    override fun sendUserProperty(key: String, value: String) =
        FirebaseAnalytics.getInstance(context).setUserProperty(key, value)

}