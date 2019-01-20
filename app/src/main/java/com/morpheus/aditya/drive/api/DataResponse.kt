package com.morpheus.aditya.drive.api

import com.google.gson.annotations.SerializedName

data class DataResponse(
    @SerializedName("data")
    val data: Data,

    @SerializedName("status_code")
    val statusCode: Int
)