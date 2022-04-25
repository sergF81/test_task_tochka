package com.github


import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


//Создаем интерфейс для Ретрофита
interface InterfaceAPI {

    @GET("search/users?per_page=30")
    fun getLoginUser(
        @Query("q") userSearch: String,
        @Query("page") pageNumber: Int,
    ): Call<Users<Item>>

}
