package com.freisia.consumerapp.utilitas

import android.net.Uri
import com.freisia.consumerapp.BuildConfig

class Constant {
    companion object{
        const val TOKEN_GITHUB: String = BuildConfig.MY_TOKEN_GITHUB
        const val BASE_URL: String = "https://api.github.com"
        const val GET_USER_DETAILS: String = "/users/{username}"
        const val GET_USER_FOLLOWER: String = "/users/{username}/followers"
        const val GET_USER_FOLLOWING: String = "/users/{username}/following"
        const val AUTHORITY = "com.freisia.github.provider"
        const val TABLE_NAME = "Users"
        val CONTENT_URI = Uri.parse("content://${AUTHORITY}/${TABLE_NAME}")
    }
}