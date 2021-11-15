package com.freisia.github.detail.follower

import androidx.fragment.app.Fragment
import com.freisia.github.GithubApplication
import com.freisia.github.api.model.Follow
import com.freisia.github.api.model.Users
import kotlinx.coroutines.*
import retrofit2.Response

class FollowersPresenterImpl(context : Fragment) : FollowersPresenter {
    private var pageLength: Int = 10
    private var pageNumErs: Int = 1
    private var totalErs = 0
    private var itemErs = 0
    private var coroutineJobErs : Job? = null
    private var coroutineJobUErs: Job? = null
    private var listUserErs: ArrayList<Users?> = ArrayList()

    private lateinit var login : String
    private var urls : String? = null
    private lateinit var tempUrl: List<String>
    private var followersViews: FollowersView = context as FollowersView

    override suspend fun follower(url: String? , total: Int) {
        this.login = url!!
        this.totalErs = total
        if(this.totalErs == 0){
            followersViews.notFound()
        } else{
            getFollower()
        }
    }

    private fun noResult(){
        followersViews.notFound()
    }
    private fun getFollower(){
        coroutineJobErs = CoroutineScope(Dispatchers.IO).launch {
            try{
                val responseFollower = GithubApplication.create().getUserFollowers(login,pageLength,pageNumErs)
                withContext(Dispatchers.Main){
                    if(responseFollower.isSuccessful && totalErs != itemErs){
                        resultFollower(responseFollower)
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main){
                    if(listUserErs.isNullOrEmpty()){
                        noResult()
                    } else {
                        pageNumErs--
                        followersViews.found()
                    }
                }
            }
        }
    }

    private fun resultFollower(response: Response<ArrayList<Follow>>){
        itemErs += response.body()!!.size
        for(i in 0 until response.body()!!.size){
            urls = response.body()!![i].url
            tempUrl = urls!!.split("/").map { it.trim() }
            getUserFollower(tempUrl[4])
        }
    }

    private fun getUserFollower(url: String){
        coroutineJobUErs = CoroutineScope(Dispatchers.IO).launch {
            try{
                val responseUser = GithubApplication.create().getUserbyDetail(url)
                withContext(Dispatchers.Main){
                    if(responseUser.isSuccessful){
                        userFollower(responseUser)
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main){
                    noResult()
                }
            }
        }
    }

    private fun userFollower(response: Response<Users>){
        if(listUserErs.size == this.itemErs - 1){
            listUserErs.add(response.body())
            followersViews.getData(listUserErs)
            followersViews.found()
        } else if(listUserErs.size < this.itemErs - 1){
            listUserErs.add(response.body())
        }
    }

    override suspend fun onFollowerLoadMore() {
        if(totalErs != itemErs && itemErs != 0){
            pageNumErs++
        }
        getFollower()
    }


}