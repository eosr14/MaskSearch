package com.eosr14.masksearch.network.services

import com.eosr14.masksearch.common.KAKAO_API_KEY
import com.eosr14.masksearch.model.KaKaoKeyWord
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface KaKaoService {

    @Headers("Authorization: KakaoAK $KAKAO_API_KEY")
    @GET("/v2/local/search/keyword")
    fun requestKaKaoKeyWord(
        @Query("query") query: String
    ): Single<KaKaoKeyWord>

}