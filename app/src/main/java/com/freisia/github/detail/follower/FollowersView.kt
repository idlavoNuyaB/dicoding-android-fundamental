package com.freisia.github.detail.follower

import com.freisia.github.api.model.Users

interface FollowersView {
    fun getData(user: ArrayList<Users?>)
    fun notFound()
    fun found()
}