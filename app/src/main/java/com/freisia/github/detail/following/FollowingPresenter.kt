package com.freisia.github.detail.following

interface FollowingPresenter {
    suspend fun onFollowingLoadMore()
    suspend fun followings(url : String?,total: Int)

}