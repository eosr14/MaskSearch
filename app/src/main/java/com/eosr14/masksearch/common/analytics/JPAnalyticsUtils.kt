package com.eosr14.masksearch.common.analytics

interface ProviderType {
    fun sendLog(event: String, parameter: HashMap<String, Any>)
    fun sendUserProperty(key: String, value: String)
}

interface AnalyticsType {
    fun register(provider: ProviderType)
    fun sendLog(eventName: String, parameter: HashMap<String, Any>)
}
