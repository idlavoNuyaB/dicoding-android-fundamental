package com.freisia.github.ui.detail

import android.app.Application
import androidx.lifecycle.*
import com.freisia.github.data.db.GithubDatabase
import com.freisia.github.data.db.repository.UserRepository
import com.freisia.github.data.model.Users
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: UserRepository
    val allUser: LiveData<List<Users>>

    init {
        val userDao = GithubDatabase.getDatabase(application).userDao()
        repository = UserRepository(userDao)
        allUser = repository.allUser
    }

    fun destroy() = viewModelScope.launch(Dispatchers.IO) {
        GithubDatabase.destroyInstance()
    }

    fun insert (users: Users) {
        val observer = Observer<List<Users>> {
            user ->
            if(!user.isNullOrEmpty()){
                for(i in user.indices){
                    if(user[i] != users){
                        viewModelScope.launch(Dispatchers.IO) {
                            repository.insert(users)
                        }
                    }
                }
            } else {
                viewModelScope.launch(Dispatchers.IO) {
                    repository.insert(users)
                }
            }
        }
        allUser.observeForever(observer)
        allUser.removeObserver(observer)
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
}