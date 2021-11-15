package com.freisia.github.api

class Constant {
    companion object{
        const val TOKEN_GITHUB: String = "50000911d3b08548b0f67952559c7d19698e69a7"
        const val BASE_URL: String = "https://api.github.com"
        const val GET_USER: String = "/search/users"
        const val GET_USER_DETAILS: String = "/users/{username}"
        const val GET_USER_FOLLOWER: String = "/users/{username}/followers"
        const val GET_USER_FOLLOWING: String = "/users/{username}/following"
    }
}