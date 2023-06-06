package com.example.instantcar.activities.api

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.instantcar.activities.model.Booking
import com.example.instantcar.activities.model.Car
import com.example.instantcar.activities.model.CarModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class ApiViewModel(private val repo: Repository) : ViewModel(), LifecycleObserver {

    val carModels: MutableLiveData<Response<List<CarModel>>> = MutableLiveData()
    val carModel: MutableLiveData<Response<CarModel>> = MutableLiveData()
    val cars: MutableLiveData<Response<List<Car>>> = MutableLiveData()
    val car: MutableLiveData<Response<Car>> = MutableLiveData()
    val bookings: MutableLiveData<Response<List<Booking>>> = MutableLiveData()
    val booking: MutableLiveData<Response<Booking>> = MutableLiveData()

    fun getCarModels() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repo.getCarModels()
                carModels.postValue(response)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getCarModelByCarModelId(car_model_id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repo.getCarModelByCarModelId(car_model_id)
                carModel.postValue(response)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getCars() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repo.getCars()
                cars.postValue(response)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getCarByCarId(car_id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repo.getCarByCarId(car_id)
                car.postValue(response)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getBookings() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repo.getBookings()
                bookings.postValue(response)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getBookingByBookingId(booking_id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repo.getBookingByBookingId(booking_id)
                booking.postValue(response)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getBookingsByUserId(user_id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repo.getBookingsByUserId(user_id)
                bookings.postValue(response)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}