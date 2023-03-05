package com.abdulkerim.weatherapp.view

import android.graphics.drawable.Icon
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.abdulkerim.weatherapp.R
import com.abdulkerim.weatherapp.databinding.ActivityMainBinding
import com.abdulkerim.weatherapp.model.WeatherModel
import com.abdulkerim.weatherapp.service.WeatherService
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private val BASE_URL = "https://api.openweathermap.org/data/2.5/"
private lateinit var binding: ActivityMainBinding

private var job: Job? = null
private var exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
    println(throwable.localizedMessage)
}

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //bf46a377f1ab9cc3a21ef0117596bf64
        //https://api.openweathermap.org/data/2.5/weather?q=Kutahya&appid=bf46a377f1ab9cc3a21ef0117596bf64


        binding.okButton.setOnClickListener {
            if (binding.countryEdit.text.toString().isNotEmpty()) {
                val cityName = binding.countryEdit.text.toString()
                loadData(cityName)
            } else {
                Snackbar.make(it, "Lütfen şehir adı girin.", Snackbar.LENGTH_SHORT).show()
            }

        }


    }

    private fun loadData(cityName: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())//Gson formatında gelen verileri dönüştürmemizi sağlıyor
            .build()
        //.create(WeatherService::class.java)//coroutines ile kullanırken bunu ekliyoruz

        /*job = CoroutineScope(Dispatchers.IO+ exceptionHandler).launch {
            val response = retrofit.getWeather(cityName, "bf46a377f1ab9cc3a21ef0117596bf64")
            withContext(Dispatchers.Main){
                if (response.isSuccessful) {
                    response.body()?.let {
                        val temp = (it.main.temp) - 273.15
                        var condition: String? = null
                        for (weather in it.weather) {
                            condition = weather.main

                        }
                        binding.tempetureText.text = temp.toString()


                        var havaDurumu = ""

                        when (condition) {
                            "Thunderstorm" -> havaDurumu = "Fırtına"
                            "Drizzle" -> havaDurumu = "Çiselemeli Yağmur"
                            "Rain" -> havaDurumu = "Yağmurlu"
                            "Snow" -> havaDurumu = "Karlı"
                            "Mist", "Smoke", "Haze" -> havaDurumu = "Sisli"
                            "Dust", "Sand", "Ash" -> havaDurumu = "Tozlu"
                            "Clear" -> havaDurumu = "Açık"
                            "Clouds" -> havaDurumu = "Bulutlu"
                            else -> havaDurumu = "Hava durumu belirsiz"
                        }
                        binding.coundationText.text = havaDurumu


                    }
                }
            }

        }*/

        //Retrofit ile kullanım
        val service =
            retrofit.create(WeatherService::class.java)//retrofit ile apiyi birbirine bağladık
        val call =
            service.getWeather(cityName, "bf46a377f1ab9cc3a21ef0117596bf64") //metodumuzu getirdik

        call.enqueue(object : Callback<WeatherModel> {
            override fun onResponse(call: Call<WeatherModel>, response: Response<WeatherModel>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        val temp = (it.main.temp) - 273.15
                        var condition: String? = null
                        for (weather in it.weather) {
                            condition = weather.main

                        }
                        binding.tempetureText.text = temp.toString()

                        var havaDurumu = ""
                        val image = binding.coundationImage

                        when (condition) {
                            "Thunderstorm" -> {
                                havaDurumu = "Fırtına"
                                image.setImageResource(R.drawable.stormy)
                            }
                            "Drizzle" -> {
                                havaDurumu = "Çiselemeli Yağmur"
                                image.setImageResource(R.drawable.stormy)

                            }
                            "Rain" -> {
                                havaDurumu = "Yağmurlu"
                                image.setImageResource(R.drawable.rainy)

                            }
                            "Snow" -> {
                                havaDurumu = "Karlı"
                                image.setImageResource(R.drawable.snowy)

                            }
                            "Mist", "Smoke", "Haze" -> {
                                havaDurumu = "Sisli"
                                image.setImageResource(R.drawable.foggy)

                            }
                            "Dust", "Sand", "Ash" -> {
                                havaDurumu = "Tozlu"
                                image.setImageResource(R.drawable.dusty)

                            }
                            "Clear" -> {
                                havaDurumu = "Açık"
                                image.setImageResource(R.drawable.sunny)

                            }
                            "Clouds" -> {
                                havaDurumu = "Bulutlu"
                                image.setImageResource(R.drawable.cloudy)

                            }
                            else -> {
                                havaDurumu = "Hava durumu belirsiz"
                            }
                        }
                        binding.coundationText.text = havaDurumu


                    }
                }
            }

            override fun onFailure(call: Call<WeatherModel>, t: Throwable) {
                println(t)
            }

        })
    }
}