package com.freisia.github.api.model

import com.google.gson.annotations.SerializedName

data class Result (
    @SerializedName("total_count") val totalCount: Int,
    @SerializedName("incomplete_results") val incompleteResults: Boolean,
    @SerializedName("items") val items: ArrayList<Item>
)