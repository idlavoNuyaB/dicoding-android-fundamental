package com.freisia.github

import com.freisia.github.api.APIService
import com.freisia.github.api.Constant
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class GithubApplication {
    companion object Factory{
        fun create(): APIService {
            val httpLoggingInterceptor = HttpLoggingInterceptor()
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            val okHttpClient = OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor).build()
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(Constant.BASE_URL)
                .client(okHttpClient)
                .build()

            return retrofit.create(APIService::class.java)
        }
    }
}