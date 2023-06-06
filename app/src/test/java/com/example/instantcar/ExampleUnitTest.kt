package com.example.instantcar

import com.example.instantcar.activities.utilities.MathOperation
import org.junit.Assert.assertEquals
import org.junit.Test
import java.math.BigDecimal

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun check_MakePayment_Multiplication() {
        val carRentalRate = 10.46
        val bookingDayAmount = 2
        val totalPrice = MathOperation().calculateTotalRentalAmount(carRentalRate, bookingDayAmount)
        assertEquals(totalPrice, BigDecimal((carRentalRate * bookingDayAmount).toString()).setScale(2))
    }
}