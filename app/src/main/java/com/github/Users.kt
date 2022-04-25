package com.github

import com.google.gson.annotations.SerializedName


data class Users<Item>(
    @SerializedName("items")

    val items: List<Item>

)

data class Item(
    @SerializedName("login")

    val loginUser: String,
    val id: String,
    @SerializedName("avatar_url")
    val avatarUrl: String

)




