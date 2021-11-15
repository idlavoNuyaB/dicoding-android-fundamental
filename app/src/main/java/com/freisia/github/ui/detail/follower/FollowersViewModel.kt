package com.freisia.github.ui.detail.follower

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.freisia.github.GithubApplication
import com.freisia.github.data.model.Follow
import com.freisia.github.data.model.Users
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import kotlin.properties.Delegates

class FollowersViewModel(application: Application) : AndroidViewModel(application) {
    private var pageLength: Int = 10
    private var pageNumErs: Int = 1
    private var totalErs = 0
    private var itemErs = 0
    private lateinit var login : String
    private var urls : String? = null
    private lateinit var tempUrl: List<String>
    private var listUserErs: ArrayList<Users?> = ArrayList()
    private var isFound by Delegates.notNull<Boolean>()
    var isFounds : MutableLiveData<Boolean> = MutableLiveData()
    var listFollowers : MutableLiveData<List<Users?>> = MutableLiveData()

    fun checkFollower(url:String, total:Int) {
        login = url
        totalErs = total
        if(totalErs == 0){
            isFound = false
            isFounds.value = isFound
        } else {
            getFollower()
        }
    }
    private fun getFollower() = viewModelScope.launch(Dispatchers.IO){
        try{
            val responseFollower = GithubApplication.create().getUserFollowers(login,pageLength,pageNumErs)
            if(responseFollower.isSuccessful && totalErs != itemErs) {
                resultFollower(responseFollower)
            }
        } catch (e:Exception) {
            withContext(Dispatchers.Main){
                if (listUserErs.isNullOrEmpty()) {
                    isFound = false
                    isFounds.value = isFound
                } else {
                    pageNumErs--
                    isFound = true
                    isFounds.value = isFound
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
    private fun getUserFollower(url: String) {
        try{
            viewModelScope.launch(Dispatchers.IO) {
                val responseUser = GithubApplication.create().getUserbyDetail(url)
                if(responseUser.isSuccessful){
                    withContext(Dispatchers.Main){
                        userFollower(responseUser)
                    }
                }
            }
        } catch (e : Exception){
            if (listUserErs.isNullOrEmpty()) {
                isFound = false
                isFounds.value = isFound
            } else {
                pageNumErs--
                isFound = true
                isFounds.value = isFound
            }
        }
    }
    private fun userFollower(response: Response<Users>){
        if(listUserErs.size == this.itemErs - 1){
            listUserErs.add(response.body())
            isFound = true
            isFounds.value = isFound
            listFollowers.value = listUserErs
        } else if(listUserErs.size < this.itemErs - 1){
            listUserErs.add(response.body())
        }
    }
    fun onLoadMore(){
        if(totalErs != itemErs && itemErs != 0){
            pageNumErs++
        }
        getFollower()
    }
}