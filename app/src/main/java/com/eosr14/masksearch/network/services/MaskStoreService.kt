package com.eosr14.masksearch.network.services

import com.eosr14.masksearch.model.MaskStoreModel
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface MaskStoreService {

    @GET("/corona19-masks/v1/storesByGeo/json")
    fun requestMaskStore(
        @Query("lat") lat: Double,
        @Query("lng") lng: Double,
        @Query("m") meter: Int
    ): Single<MaskStoreModel>

}