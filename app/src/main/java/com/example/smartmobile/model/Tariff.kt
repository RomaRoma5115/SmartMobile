package com.example.smartmobile.model

import com.google.gson.annotations.SerializedName

data class Tariff(
    @SerializedName("name")var name: String? = null,
    @SerializedName("description")var description: String? = null,
    @SerializedName("countSms")var countSms: String? = null,
    @SerializedName("packageInternet")var packageInternet: String? = null,
    @SerializedName("countMinutes")var countMinutes: String? = null,
    @SerializedName("_id")var tariffId: String? = null,
)
