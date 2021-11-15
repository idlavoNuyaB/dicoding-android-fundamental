package com.freisia.github.api

import com.freisia.github.api.model.Follow
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.Call
import com.freisia.github.api.model.Result
import com.freisia.github.api.model.Users
import retrofit2.Response
import retrofit2.http.Query

interface APIService {
    @GET(Constant.GET_USER)
    @Headers("Authorization: token " + Constant.TOKEN_GITHUB)
    suspend fun getUser(@Query("q") username: String,
                @Query("per_page") per_page: Int,
                @Query("page") page: Int): Response<Result?>

    @GET(Constant.GET_USER_DETAILS)
    @Headers("Authorization: token " + Constant.TOKEN_GITHUB)
    suspend fun getUserbyDetail(@Path("username") username: String): Response<Users>

    @GET(Constant.GET_USER_FOLLOWER)
    @Headers("Authorization: token " + Constant.TOKEN_GITHUB)
    suspend fun getUserFollowers(@Path("username") username: String,
                         @Query("per_page") per_page: Int,
                         @Query("page") page: Int): Response<ArrayList<Follow>>

    @GET(Constant.GET_USER_FOLLOWING)
    @Headers("Authorization: token " + Constant.TOKEN_GITHUB)
    suspend fun getUserFollowing(@Path("username") username: String,
                         @Query("per_page") per_page: Int,
                         @Query("page") page: Int): Response<ArrayList<Follow>>

}