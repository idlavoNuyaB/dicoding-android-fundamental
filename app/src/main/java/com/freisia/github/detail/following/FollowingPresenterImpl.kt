package com.freisia.github.detail.following

import androidx.fragment.app.Fragment
import com.freisia.github.GithubApplication
import com.freisia.github.api.model.Follow
import com.freisia.github.api.model.Users
import kotlinx.coroutines.*
import retrofit2.Response

class FollowingPresenterImpl(fragment: Fragment) : FollowingPresenter {
    private var pageNumIng: Int = 1
    private var pageLength: Int = 10
    private var totalIng = 0
    private var itemIng = 0
    private var coroutineJobIng : Job? = null
    private var coroutineJobUIng : Job? = null
    private var listUserIng: ArrayList<Users?> = ArrayList()
    private lateinit var login : String
    private var urls : String? = null
    private lateinit var tempUrl: List<String>
    private var followingView:FollowingView = fragment as FollowingView

    override suspend fun followings(url: String?, total: Int) {
        this.login = url!!
        this.totalIng = total
        if (this.totalIng == 0) {
            followingView.notFound()
        } else {
            getFollowing()
        }
    }
    private fun noResult() {
        followingView.notFound()
    }
    private fun getFollowing() {
        coroutineJobIng = CoroutineScope(Dispatchers.IO).launch {
            try {
                val responseFollowing = GithubApplication.create().getUserFollowing(login, pageLength, pageNumIng)
                withContext(Dispatchers.Main) {
                    if (responseFollowing.isSuccessful && itemIng != totalIng) {
                        resultFollowing(responseFollowing)
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main){
                    if(listUserIng.isNullOrEmpty()){
                        noResult()
                    }
                    else {
                        pageNumIng --
                        followingView.found()
                    }
                }
            }
        }
    }

    private fun resultFollowing(response: Response<ArrayList<Follow>>) {
        itemIng += response.body()!!.size
        for (i in 0 until response.body()!!.size) {
            urls = response.body()!![i].url
            tempUrl = urls!!.split("/").map { it.trim() }
            getUserFollowing(tempUrl[4])
        }
    }

    private fun getUserFollowing(url: String) {
        coroutineJobUIng = CoroutineScope(Dispatchers.IO).launch {
            try {
                val responseUser = GithubApplication.create().getUserbyDetail(url)
                withContext(Dispatchers.Main) {
                    if (responseUser.isSuccessful) {
                        userFollowing(responseUser)
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main){
                    if(listUserIng.isNullOrEmpty()){
                        noResult()
                    }
                    else {
                        pageNumIng --
                        var temp = listUserIng.take(pageNumIng * pageLength)
                        listUserIng = temp as ArrayList<Users?>
                        followingView.found()
                    }
                }
            }
        }
    }

    private fun userFollowing(response: Response<Users>) {
        if (listUserIng.size == this.itemIng - 1) {
            listUserIng.add(response.body())
            followingView.getData(listUserIng)
            followingView.found()
        } else if (listUserIng.size < this.itemIng - 1) {
            listUserIng.add(response.body())
        }
    }

    override suspend fun onFollowingLoadMore() {
        if (totalIng != itemIng && itemIng != 0) {
            pageNumIng++
        }
        getFollowing()
    }
}