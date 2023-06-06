package com.example.instantcar.activities.api

import com.example.instantcar.activities.model.Booking
import com.example.instantcar.activities.model.Car
import com.example.instantcar.activities.model.CarModel
import retrofit2.Response

class Repository {
    suspend fun getCarModels(): Response<List<CarModel>> {
        return RetrofitInstance.api.getCarModels()
    }

    suspend fun getCarModelByCarModelId(car_model_id: String): Response<CarModel> {
        return RetrofitInstance.api.getCarModelByCarModelId(car_model_id)
    }

    suspend fun getCars(): Response<List<Car>> {
        return RetrofitInstance.api.getCars()
    }

    suspend fun getCarByCarId(car_id: String): Response<Car> {
        return RetrofitInstance.api.getCarByCarId(car_id)
    }

    suspend fun getBookings(): Response<List<Booking>> {
        return RetrofitInstance.api.getBookings()
    }

    suspend fun getBookingByBookingId(booking_id: String): Response<Booking> {
        return RetrofitInstance.api.getBookingByBookingId(booking_id)
    }

    suspend fun getBookingsByUserId(user_id: String): Response<List<Booking>> {
        return RetrofitInstance.api.getBookingsByUserId(user_id)
    }
}