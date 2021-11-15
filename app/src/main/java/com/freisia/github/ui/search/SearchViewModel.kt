package com.freisia.github.ui.search

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.freisia.github.GithubApplication
import com.freisia.github.data.model.Result
import com.freisia.github.data.model.Users
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class SearchViewModel(application: Application) : AndroidViewModel(application) {
    private var searchText: String = ""
    private var pageLength: Int = 10
    private var pageNum: Int = 1
    private lateinit var tempUrl: List<String>
    private var listUser: ArrayList<Users?> = ArrayList()
    private var urls: String? = null
    private var itemSize = 0
    private var total = 0
    var isFounds : MutableLiveData<Boolean> = MutableLiveData()
    var isLoading : MutableLiveData<Boolean> = MutableLiveData()
    var listSearch : MutableLiveData<List<Users?>> = MutableLiveData()


    fun searchResult(search: String) = viewModelScope.launch(Dispatchers.IO){
        withContext(Dispatchers.Main){
            isLoading.value = true
        }
        if(searchText != search && listUser.isNotEmpty()){
            resetData()
        }
        searchText = search
        getResult()
    }

    private fun resetData() {
        pageLength = 10
        pageNum = 1
        itemSize = 0
        total = 0
        listUser.clear()
    }

    private fun getResult() = viewModelScope.launch(Dispatchers.IO){
        try{
            delay(500L)
            val response = GithubApplication.create().getUser(searchText,pageLength,pageNum)
            withContext(Dispatchers.Main){
                if(response.isSuccessful){
                    result(response)
                }
                else{
                    isFounds.value = false
                }
            }

        } catch (e: Exception){
            withContext(Dispatchers.Main){
                isFounds.value = false
            }
        }
    }
    private fun result(response: Response<Result?>){
        if (!response.body()?.items.isNullOrEmpty()){
            total = response.body()!!.totalCount
            itemSize += response.body()!!.items.size
            for(i in 0 until response.body()!!.items.size){
                urls = response.body()!!.items[i].url
                tempUrl = urls!!.split("/").map { it.trim() }
                getUser(tempUrl[4])
            }
        }
        else {
            isFounds.value = false
        }
    }
    private fun getUser(urls : String?) {
        if (urls != null) {
            viewModelScope.launch(Dispatchers.IO) {
                try{
                    delay(500L)
                    val response = GithubApplication.create().getUserbyDetail(urls)
                    withContext(Dispatchers.Main){
                        if(response.isSuccessful){
                            user(response)
                        }
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main){
                        isFounds.value = false
                    }
                }
            }
        }
    }
    private fun user(response: Response<Users>){
        if(listUser.size == this.itemSize - 1){
            listUser.add(response.body())
            isFounds.value = true
            listSearch.value = listUser
            isLoading.value = false
        } else if(listUser.size < this.itemSize - 1){
            listUser.add(response.body())
        }
    }
    fun onLoadMore(){
        if(itemSize != total && itemSize != 0){
            pageNum++
            getResult()
        }
    }
}