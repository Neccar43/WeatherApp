package com.abdulkerim.weatherapp.service

import com.abdulkerim.weatherapp.model.WeatherModel
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {
    //Retrofit ile kullanım
    @GET("weather?")
    fun getWeather(@Query("q") cityName: String, @Query("appid") apiKey: String): Call<WeatherModel>

    //coroutines ile kullanım
    /*@GET("weather?")
   suspend fun getWeather(@Query("q") cityName: String, @Query("appid") apiKey: String): Response<WeatherModel>*/
}