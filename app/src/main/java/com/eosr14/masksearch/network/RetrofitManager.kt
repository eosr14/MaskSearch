package com.eosr14.masksearch.network

import com.eosr14.masksearch.common.KAKAO_BASE_URL
import com.eosr14.masksearch.common.STORE_BASE_URL
import com.eosr14.masksearch.model.KaKaoKeyWord
import com.eosr14.masksearch.model.MaskStoreModel
import com.eosr14.masksearch.network.services.KaKaoService
import com.eosr14.masksearch.network.services.MaskStoreService
import io.reactivex.Single

object RetrofitManager {

    private fun provideStore(): MaskStoreService {
        return RetrofitClient().provideRetrofit(STORE_BASE_URL).create(MaskStoreService::class.java)
    }

    private fun provideKaKao(): KaKaoService {
        return RetrofitClient().provideRetrofit(KAKAO_BASE_URL).create(KaKaoService::class.java)
    }

    fun requestMaskStore(lat: Double, lng: Double, meter: Int): Single<MaskStoreModel> {
        return provideStore().requestMaskStore(lat, lng, meter)
    }

    fun requestKaKaoKeyWord(query: String): Single<KaKaoKeyWord> {
        return provideKaKao().requestKaKaoKeyWord(query)
    }

}