package com.example.a283_loginappexample.remote.loginRepository

import com.example.a283_loginappexample.remote.dataModel.DefaultModel
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface LoginRepository {


    @FormUrlEncoded
    @POST("email/login")
    fun sendCode(
        @Field("email") email: String
    ): Response<DefaultModel>

}