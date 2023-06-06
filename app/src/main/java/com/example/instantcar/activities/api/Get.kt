package com.example.instantcar.activities.api

import com.example.instantcar.activities.model.Booking
import com.example.instantcar.activities.model.Car
import com.example.instantcar.activities.model.CarModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface Get {
    @GET("api/CarModels")
    suspend fun getCarModels(
    ): Response<List<CarModel>>

    @GET("api/CarModels/{car_model_id}")
    suspend fun getCarModelByCarModelId(
        @Path("car_model_id") car_model_id: String,
    ): Response<CarModel>

    @GET("api/Cars")
    suspend fun getCars(
    ): Response<List<Car>>

    @GET("api/Cars/{car_id}")
    suspend fun getCarByCarId(
        @Path("car_id") car_id: String,
    ): Response<Car>

    @GET("api/Bookings")
    suspend fun getBookings(
    ): Response<List<Booking>>

    @GET("api/Bookings/{booking_id}")
    suspend fun getBookingByBookingId(
        @Path("booking_id") booking_id: String,
    ): Response<Booking>

    @GET("api/Bookings/user_id")
    suspend fun getBookingsByUserId(
        @Query("user_id") user_id: String,
    ): Response<List<Booking>>

}