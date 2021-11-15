package com.freisia.consumerapp.ui.favorite

import android.app.Application
import androidx.lifecycle.*
import com.freisia.consumerapp.data.model.Users
import com.freisia.consumerapp.data.source.UserSource
import kotlin.properties.Delegates

class FavoriteViewModel(application: Application) : AndroidViewModel(application) {
    val allUser: MutableLiveData<List<Users>>
    private val limit = 10
    private var offset = -1
    private var total by Delegates.notNull<Int>()
    private val applications = application
    var allUserByPage: MutableLiveData<List<Users>>
    var isFounds : MutableLiveData<Boolean> = MutableLiveData()
    var isLoading : MutableLiveData<Boolean> = MutableLiveData()

    init {
        allUser = UserSource(application.applicationContext,0,0)
        allUserByPage = UserSource(application.applicationContext,limit,offset)
    }

    fun read(max : Int){
        isLoading.value = true
        total = max
        getResult()
    }

    private fun getResult(){
        if(allUserByPage.value.isNullOrEmpty()){
            isFounds.value = false
            isLoading.value = false
        } else {
            isFounds.value = true
            isLoading.value = false
        }
    }

    fun onLoadMore(){
        if(total <= offset && offset != 0){
            offset += 10
            allUserByPage = UserSource(applications.applicationContext,limit,offset)
            getResult()
        }
    }
}