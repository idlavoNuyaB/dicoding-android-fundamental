package com.freisia.github.ui.favorite

import android.app.Application
import androidx.lifecycle.*
import com.freisia.github.data.db.GithubDatabase
import com.freisia.github.data.db.repository.UserRepository
import com.freisia.github.data.model.Users
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

class FavoriteViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: UserRepository
    val allUser: LiveData<List<Users>>
    private var total by Delegates.notNull<Int>()
    var isFounds : MutableLiveData<Boolean> = MutableLiveData()
    var isLoading : MutableLiveData<Boolean> = MutableLiveData()

    init {
        val userDao = GithubDatabase.getDatabase(application).userDao()
        repository = UserRepository(userDao)
        allUser = repository.allUser
    }

    fun read(max : Int){
        isLoading.value = true
        total = max
        getResult()
    }

    private fun getResult(){
        if(allUser.value.isNullOrEmpty()){
            isFounds.value = false
            isLoading.value = false
        } else {
            isFounds.value = true
            isLoading.value = false
        }
    }

    fun destroy() = viewModelScope.launch(Dispatchers.IO) {
        GithubDatabase.destroyInstance()
    }

    fun delete(users: Users) {
        val observer = Observer<List<Users>> {
                user ->
            if(!user.isNullOrEmpty()){
                for(i in user.indices){
                    if(user[i] == users){
                        viewModelScope.launch(Dispatchers.IO) {
                            repository.delete(users)
                        }
                    }
                }
            }
        }
        allUser.observeForever(observer)
        allUser.removeObserver(observer)
    }

    fun deleteAll() = viewModelScope.launch(Dispatchers.IO){
        repository.deleteAll()
    }
}