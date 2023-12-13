package com.example.smartmobile.interfaces

import com.example.smartmobile.model.ApiResponse
import com.example.smartmobile.model.Tariff
import com.example.smartmobile.model.User
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface API {
    //User
    @Headers("Content-Type:application/json")
    @POST("user/login")
    fun login(
        @Body body: JsonObject
    ): Call<User>

    @Headers("Content-Type:application/json")
    @POST("user/{userId}")
    fun getUserById(
        @Path("userId") id: String
    ): Call<User>

    @Headers("Content-Type:application/json")
    @POST("user/create")
    fun registration(
        @Body body: JsonObject
    ): Call<User>

    @Headers("Content-Type:application/json")
    @POST("user/update/{userId}")
    fun updateUser(
        @Path("userId") id : String,
        @Body body: JsonObject
    ): Call<User>

    @Headers("Content-Type:application/json")
    @POST("user/delete/{userId}")
    fun deleteUser(
        @Path("userId") id : String
    ): Call<User>

    //Tariff
    @Headers("Content-Type:application/json")
    @GET("/tariff")
    fun getAllTariffs(
    ): Call<ApiResponse<Tariff>>

    @Headers("Content-Type:application/json")
    @POST("/tariff/id/{tariffId}")
    fun getTariffById(
        @Path("tariffId") id :String
    ): Call<Tariff>

    @Headers("Content-Type:application/json")
    @POST("/tariff/delete/{tariffId}")
    fun deleteTariffById(
        @Path("tariffId") id :String
    ): Call<Tariff>

    @Headers("Content-Type:application/json")
    @POST("/tariff")
    fun createTariff(
        @Body body: JsonObject
    ): Call<Tariff>

    @Headers("Content-Type:application/json")
    @POST("/tariff/update/{tariffId}")
    fun updateTariffById(
        @Path("tariffId") id :String,
        @Body body: JsonObject
    ): Call<Tariff>

}