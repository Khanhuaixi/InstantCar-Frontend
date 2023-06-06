package com.example.instantcar.activities.model

data class Booking(
    val id: Int? = null,
    val bookingId: String = "",
    var bookingStartDate: String = "",
    var bookingEndDate: String = "",
    val bookingPaymentMethod: String = "",
    val carId: String = "",
    val userId: String = ""
)
