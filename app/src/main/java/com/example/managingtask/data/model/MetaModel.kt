package com.example.managingtask.data.model

import com.google.gson.annotations.SerializedName

data class MetaModel (
    @SerializedName("current") val currentPageNumber: Int,
    @SerializedName("limit") val maxItemsPerPage: Int,
    @SerializedName("count") val count: Int
)