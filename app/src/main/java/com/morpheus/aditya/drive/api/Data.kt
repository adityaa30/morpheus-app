package com.morpheus.aditya.drive.api

import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("EAR")
    val ear: Double,

    @SerializedName("alarm")
    val alarm: Boolean
)