package com.example.instantcar.activities.utilities

import java.math.BigDecimal

class MathOperation {
    //calculate rental fee
    fun calculateTotalRentalAmount(carRentalRate: Double, bookingDayAmount: Int): BigDecimal {
        //multiply
        val totalRentalAmount = carRentalRate * bookingDayAmount
        //set to 2 decimal places using BigDecimal setScale
        return BigDecimal(totalRentalAmount.toString()).setScale(2)
    }

}