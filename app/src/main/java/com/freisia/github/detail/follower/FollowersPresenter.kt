package com.freisia.github.detail.follower

interface FollowersPresenter {
    suspend fun follower(url : String?,total: Int)
    suspend fun onFollowerLoadMore()

}