package com.freisia.github.detail.following

import com.freisia.github.api.model.Users

interface FollowingView {
    fun getData(user: ArrayList<Users?>)
    fun notFound()
    fun found()
}