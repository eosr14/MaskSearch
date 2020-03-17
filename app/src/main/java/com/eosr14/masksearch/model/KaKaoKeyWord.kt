package com.eosr14.masksearch.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class KaKaoKeyWord(
    @SerializedName("documents") @Expose val documents: ArrayList<Document>
) {
    data class Document(
        @SerializedName("address_name") @Expose val addressName: String,
        @SerializedName("road_address_name") @Expose val roadAddressName: String,
        @SerializedName("category_group_name") @Expose val categoryGroupName: String,
        @SerializedName("category_name") @Expose val categoryName: String,
        @SerializedName("place_name") @Expose val placeName: String,
        @SerializedName("x") @Expose val x: String?,
        @SerializedName("y") @Expose val y: String?
    )
//    "address_name": "서울 강서구 화곡동 662-5",
//    "category_group_code": "SW8",
//    "category_group_name": "지하철역",
//    "category_name": "교통,수송 > 지하철,전철 > 수도권5호선",
//    "distance": "",
//    "id": "21160683",
//    "phone": "02-6311-5181",
//    "place_name": "까치산역 5호선",
//    "place_url": "http://place.map.kakao.com/21160683",
//    "road_address_name": "서울 강서구 강서로 지하 54",
//    "x": "126.846444264065",
//    "y": "37.5322306724471"
}