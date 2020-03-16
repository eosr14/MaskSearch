package com.eosr14.masksearch.network

import com.eosr14.masksearch.common.STORE_BASE_URL
import com.eosr14.masksearch.model.MaskStoreModel
import com.eosr14.masksearch.network.services.MaskStoreService
import io.reactivex.Single

object RetrofitManager {

    private fun provideStore(): MaskStoreService {
        return RetrofitClient().provideRetrofit(STORE_BASE_URL).create(MaskStoreService::class.java)
    }

    fun requestMaskStore(lat: Double, lng: Double, meter: Int): Single<MaskStoreModel> {
        return provideStore().requestMaskStore(lat, lng, meter)
    }

}