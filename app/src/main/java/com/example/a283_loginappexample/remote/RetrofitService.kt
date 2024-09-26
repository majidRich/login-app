package com.example.a283_loginappexample.remote

import com.example.a283_loginappexample.remote.loginRepository.LoginRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitService {

    private const val baseUrl = "https://learn.alirezaahmadi.info/api/v1/auth/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val service: LoginRepository = retrofit.create(LoginRepository::class.java)

}