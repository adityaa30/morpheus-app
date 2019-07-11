package com.morpheus.aditya.drive.api

import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface DriveApi {

    @FormUrlEncoded
    @POST("data/update/")
    fun updateData(@Field("image_string") imageString: String): Observable<Response<DataResponse>>
}