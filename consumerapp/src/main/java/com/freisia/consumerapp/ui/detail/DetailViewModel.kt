package com.freisia.consumerapp.ui.detail

import android.app.Application
import androidx.lifecycle.*
import com.freisia.consumerapp.data.model.Users
import com.freisia.consumerapp.data.source.UserSource

class DetailViewModel(application: Application) : AndroidViewModel(application) {
    val allUser: LiveData<List<Users>>

    init {
        allUser = UserSource(application.applicationContext,0,0)
    }
}