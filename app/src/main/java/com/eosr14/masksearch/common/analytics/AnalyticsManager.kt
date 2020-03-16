package com.eosr14.masksearch.common.analytics

object AnalyticsManager : AnalyticsType {

    private val providers: ArrayList<ProviderType> = arrayListOf()

    override fun register(provider: ProviderType) {
        providers.add(provider)
    }

    override fun sendLog(eventName: String, parameter: HashMap<String, Any>) {
        providers.forEach { provider ->
            provider.sendLog(eventName, parameter)
        }
    }

}
