package com.example.instantcar.activities.api

import com.example.instantcar.activities.model.Booking
import com.example.instantcar.activities.model.Car
import com.example.instantcar.activities.model.CarModel
import com.example.instantcar.activities.utilities.Constants.BASE_URL
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.*

interface ApiInterface {
    @Headers("Content-Type:application/json")
    @POST("api/CarModels")
    fun postCarModels(
        @Body carModel: CarModel,
    ): Call<CarModel>

    @Headers("Content-Type:application/json")
    @POST("api/Cars")
    fun postCar(
        @Body car: Car,
    ): Call<Car>

    @Headers("Content-Type:application/json")
    @POST("api/Bookings")
    fun postBooking(
        @Body booking: Booking,
    ): Call<Booking>

    @Headers("Content-Type:application/json")
    @PUT("api/CarModels/{car_model_id}")
    fun putCarModel(
        @Body carModel: CarModel,
        @Path("car_model_id") car_model_id: String,
    ): Call<CarModel>

    @Headers("Content-Type:application/json")
    @PUT("api/Cars/{car_id}")
    fun putCar(
        @Body car: Car,
        @Path("car_id") car_id: String,
    ): Call<Car>

    @Headers("Content-Type:application/json")
    @PUT("api/Bookings/{booking_id}")
    fun putBooking(
        @Body booking: Booking,
        @Path("booking_id") booking_id: String,
    ): Call<Booking>

    @Headers("Content-Type:application/json")
    @DELETE("api/CarModels/{car_model_id}")
    fun deleteCarModels(
        @Path("car_model_id") car_model_id: String,
    ): Call<CarModel>

    @Headers("Content-Type:application/json")
    @DELETE("api/Cars/{car_id}")
    fun deleteCar(
        @Path("car_id") car_id: String,
    ): Call<Car>

    @Headers("Content-Type:application/json")
    @DELETE("api/Bookings/{booking_id}")
    fun deleteBooking(
        @Path("booking_id") booking_id: String,
    ): Call<Booking>

    companion object {
        fun create(): ApiInterface {

            val okHttpClient = OkHttpClient.Builder().build()

            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .client(okHttpClient)
                .build()

            return retrofit.create(ApiInterface::class.java)
        }
    }
}