package com.freisia.consumerapp.data.source

import android.content.Context
import android.database.ContentObserver
import android.net.Uri
import androidx.lifecycle.MutableLiveData

abstract class UserSourceLiveData<T>(val context: Context, private val uri: Uri) : MutableLiveData<T>() {
    private lateinit var observer: ContentObserver
    override fun onInactive() {
        context.contentResolver.unregisterContentObserver(observer)
        super.onInactive()
    }

    override fun onActive() {
        postValue(getContentValue())
        observer = object : ContentObserver(null){
            override fun onChange(selfChange: Boolean) {
                postValue(getContentValue())
                super.onChange(selfChange)
            }
        }
        context.contentResolver.registerContentObserver(uri,true,observer)
        super.onActive()
    }

    abstract fun getContentValue() : T
}