package com.blogspot.devofandroid.task1

import com.google.gson.annotations.SerializedName

data class JsonModel(
    @SerializedName("status")
    var status: String? = null
)
