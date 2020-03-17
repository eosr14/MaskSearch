package com.eosr14.masksearch.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class MaskStoreModel(
    @SerializedName("count") @Expose val count: Int,
    @SerializedName("stores") @Expose val stores: List<Stores>
) {
    data class Stores(
        @SerializedName("addr") @Expose val addr: String?,
        @SerializedName("code") @Expose val code: String?,
        @SerializedName("created_at") @Expose val createdAt: String?,
        @SerializedName("lat") @Expose val lat: Double?,
        @SerializedName("lng") @Expose val lng: Double?,
        @SerializedName("name") @Expose val name: String?,
        @SerializedName("remain_stat") @Expose val remainStat: String?,
        @SerializedName("stock_at") @Expose val stockAt: String?,
        @SerializedName("type") @Expose val type: String?
    )
}