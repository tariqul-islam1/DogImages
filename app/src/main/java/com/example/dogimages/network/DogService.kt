package com.example.dogimages.network

import com.example.dogimages.data.DogResponse
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface DogService {
    @GET("breeds/image/random/{quantity}")
    fun randomDogs(@Path("quantity") quantity: Int?): Observable<DogResponse>

    companion object{
        private const val baseURL = "https://dog.ceo/api/"

        fun create(): DogService {

            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(
                    RxJava2CallAdapterFactory.create())
                .addConverterFactory(
                    GsonConverterFactory.create())
                .baseUrl(baseURL)
                .build()

            return retrofit.create(DogService::class.java)
        }
    }
}