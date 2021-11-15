package com.freisia.github.search

import com.freisia.github.api.model.Users

interface SearchViews {
    fun getUser(user: ArrayList<Users?>)
    fun notFound()
    fun found()
}