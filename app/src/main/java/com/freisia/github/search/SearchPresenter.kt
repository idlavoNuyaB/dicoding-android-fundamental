package com.freisia.github.search

interface SearchPresenter {
    suspend fun searchResult(search: String)
    suspend fun onLoadMore()
}