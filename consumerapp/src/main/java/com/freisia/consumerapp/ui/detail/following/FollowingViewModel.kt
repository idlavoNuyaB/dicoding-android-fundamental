package com.freisia.consumerapp.ui.detail.following

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.freisia.consumerapp.GithubApplication
import com.freisia.consumerapp.data.model.Follow
import com.freisia.consumerapp.data.model.Users
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import kotlin.properties.Delegates

class FollowingViewModel(application: Application) : AndroidViewModel(application) {
    private var pageNumIng: Int = 1
    private var pageLength: Int = 10
    private var totalIng = 0
    private var itemIng = 0
    private var listUserIng: ArrayList<Users?> = ArrayList()
    private lateinit var login : String
    private var urls : String? = null
    private lateinit var tempUrl: List<String>
    private var isFound by Delegates.notNull<Boolean>()
    var isFounds : MutableLiveData<Boolean> = MutableLiveData()
    var listFollowing : MutableLiveData<List<Users?>> = MutableLiveData()

    fun checkFollowing(url: String, total: Int){
        login = url
        totalIng = total
        if (this.totalIng == 0) {
            isFound = false
            isFounds.value = isFound
        } else {
            getFollowing()
        }
    }
    private fun getFollowing() = viewModelScope.launch(Dispatchers.IO){
        try{
            val responseFollowing = GithubApplication.create().getUserFollowing(login, pageLength, pageNumIng)
            if (responseFollowing.isSuccessful && itemIng != totalIng) {
                resultFollowing(responseFollowing)
            }
        } catch (e : Exception){
            withContext(Dispatchers.Main){
                if(listUserIng.isNullOrEmpty()){
                    isFound = false
                    isFounds.value = isFound
                }
                else {
                    pageNumIng--
                    isFound = true
                    isFounds.value = isFound
                }
            }
        }
    }
    private fun resultFollowing(response: Response<ArrayList<Follow>>){
        itemIng += response.body()!!.size
        for (i in 0 until response.body()!!.size) {
            urls = response.body()!![i].url
            tempUrl = urls!!.split("/").map { it.trim() }
            getUserFollowing(tempUrl[4])
        }
    }
    private fun getUserFollowing(url: String){
        try{
            viewModelScope.launch(Dispatchers.IO) {
                val responseUser = GithubApplication.create().getUserbyDetail(url)
                withContext(Dispatchers.Main) {
                    if (responseUser.isSuccessful) {
                        userFollowing(responseUser)
                    }
                }
            }
        } catch (e : Exception) {
            if(listUserIng.isNullOrEmpty()){
                isFound = false
                isFounds.value = isFound
            }
            else {
                pageNumIng--
                isFound = true
                isFounds.value = isFound
            }
        }
    }
    private fun userFollowing(response: Response<Users>){
        if (listUserIng.size == this.itemIng - 1) {
            listUserIng.add(response.body())
            isFound = true
            isFounds.value = isFound
            listFollowing.value = listUserIng
        } else if (listUserIng.size < this.itemIng - 1) {
            listUserIng.add(response.body())
        }
    }
    fun onLoadMore(){
        if (totalIng != itemIng && itemIng != 0) {
            pageNumIng++
        }
        getFollowing()
    }
}