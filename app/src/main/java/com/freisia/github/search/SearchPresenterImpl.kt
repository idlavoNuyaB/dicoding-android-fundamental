package com.freisia.github.search

import android.content.Context
import com.freisia.github.GithubApplication
import com.freisia.github.api.model.Result
import com.freisia.github.api.model.Users
import kotlinx.coroutines.*
import retrofit2.Response

class SearchPresenterImpl(context: Context) : SearchPresenter {
    private var searchText: String = ""
    private var pageLength: Int = 10
    private var pageNum: Int = 1
    private var searchViews: SearchViews = context as SearchViews
    private lateinit var tempUrl: List<String>
    private var listUser: ArrayList<Users?> = ArrayList()
    private var urls: String? = null
    private var itemSize = 0
    private var total = 0
    private var coroutineJobR : Job? = null
    private var coroutineJobU: Job? = null
    override suspend fun searchResult(search: String) {
        if(this.searchText != search && listUser.isNotEmpty()){
            resetData()
        }
        this.searchText = search
        getResult()
    }

    override suspend fun onLoadMore() {
        if(itemSize != total && itemSize != 0){
            pageNum++
            getResult()
        }
    }

    private fun resetData() {
        pageLength = 10
        pageNum = 1
        itemSize = 0
        total = 0
        listUser.clear()
    }

    private fun getResult(){
        coroutineJobR = CoroutineScope(Dispatchers.IO).launch {
            try{
                val response = GithubApplication.create().getUser(searchText,pageLength,pageNum)
                withContext(Dispatchers.Main){
                    if(response.isSuccessful){
                        result(response)
                    }
                    else{
                        noResult()
                    }
                }

            } catch (e: Exception){
                withContext(Dispatchers.Main){
                    noResult()
                }
            }
        }
    }
    private fun noResult(){
        searchViews.notFound()
    }
    private fun result(response : Response<Result?>){
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
            noResult()
        }
    }
    private fun getUser(urls : String?){
        if (urls != null) {
            coroutineJobU = CoroutineScope(Dispatchers.IO).launch {
                try{
                    val response = GithubApplication.create().getUserbyDetail(urls)
                    withContext(Dispatchers.Main){
                        if(response.isSuccessful){
                            user(response)
                        }
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main){
                        noResult()
                    }
                }
            }
        }
    }
    private fun user(response: Response<Users>){
        if(listUser.size == this.itemSize - 1){
            listUser.add(response.body())
            searchViews.getUser(listUser)
            searchViews.found()
        } else if(listUser.size < this.itemSize - 1){
            listUser.add(response.body())
        }
    }
}