package com.example.instantcar.activities.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.instantcar.R
import com.example.instantcar.activities.api.ApiInterface
import com.example.instantcar.activities.api.ApiViewModel
import com.example.instantcar.activities.api.ApiViewModelFactory
import com.example.instantcar.activities.api.Repository
import com.example.instantcar.activities.model.Booking
import com.example.instantcar.activities.model.Car
import com.example.instantcar.activities.model.CarModel
import com.google.firebase.auth.FirebaseAuth
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class MakePaymentActivity : AppCompatActivity() {
    private lateinit var bookingDayAmountTextView: TextView
    private lateinit var carNameTextView: TextView
    private lateinit var carIdTextView: TextView
    private lateinit var carImageImageView: ImageView
    private lateinit var carAddressTextView: TextView
    private lateinit var bookingDateTextView: TextView
    private lateinit var totalPriceTextView: TextView
    private lateinit var cimbBankButton: Button
    private lateinit var publicBankButton: Button
    private lateinit var maybankButton: Button
    private lateinit var affinBankButton: Button

    private lateinit var viewModel: ApiViewModel
    private var carDataSet: List<Car> = ArrayList()
    private var carModelDataSet: List<CarModel> = ArrayList()

    private lateinit var paymentMethod: String

    private lateinit var auth: FirebaseAuth

    private lateinit var carId: String
    private lateinit var bookingStartDate: String
    private lateinit var bookingEndDate: String
    private lateinit var bookingDayAmount: String
    private lateinit var totalPrice: String

    private lateinit var carItem: Car
    private lateinit var carModelItem: CarModel

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_make_payment)
        bookingDayAmountTextView = findViewById(R.id.bookingDayAmountMakePaymentTextView)
        carNameTextView = findViewById(R.id.carNameMakePaymentTextView)
        carIdTextView = findViewById(R.id.carIdMakePaymentTextView)
        carImageImageView = findViewById(R.id.carImageMakePaymentImageView)
        carAddressTextView = findViewById(R.id.carAddressMakePaymentTextView)
        bookingDateTextView = findViewById(R.id.bookingDateMakePaymentTextView)
        totalPriceTextView = findViewById(R.id.totalPriceMakePaymentTextView)
        cimbBankButton = findViewById(R.id.cimbBankMakePaymentButton)
        publicBankButton = findViewById(R.id.publicBankMakePaymentButton)
        maybankButton = findViewById(R.id.maybankMakePaymentButton)
        affinBankButton = findViewById(R.id.affinBankMakePaymentButton)

        loadCarModels()

        paymentMethod = "Unknown"

        auth = FirebaseAuth.getInstance()

        carId = intent.getStringExtra("carId").toString()
        bookingStartDate = intent.getStringExtra("bookingStartDate").toString()
        bookingEndDate = intent.getStringExtra("bookingEndDate").toString()
        bookingDayAmount = intent.getStringExtra("bookingDayAmount").toString()
        totalPrice = intent.getStringExtra("totalPrice").toString()

        if (bookingStartDate != bookingEndDate) {
            bookingDateTextView.text = "$bookingStartDate to $bookingEndDate"
            bookingDayAmountTextView.text = "For $bookingDayAmount days"
        } else {
            bookingDateTextView.text = "$bookingEndDate"
            bookingDayAmountTextView.text = "For $bookingDayAmount day"
        }

        totalPriceTextView.text = "RM$totalPrice"

        cimbBankButton.setOnClickListener {
            paymentMethod = "CIMB Bank"
            makeBooking()
        }

        publicBankButton.setOnClickListener {
            paymentMethod = "Public Bank"
            makeBooking()
        }

        maybankButton.setOnClickListener {
            paymentMethod = "Maybank"
            makeBooking()
        }

        affinBankButton.setOnClickListener {
            paymentMethod = "Affin Bank"
            makeBooking()
        }
    }

    private fun loadCarModels() {
        val repository = Repository()
        val viewModelFactory = ApiViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory)[ApiViewModel::class.java]

        viewModel.getCarModels()

        viewModel.carModels.observe(this) { response ->
            if (response.isSuccessful) {
                carModelDataSet = response.body()!!

                loadCars()
            }
            else {
                Log.d("Error", response.message())
            }
        }
    }

    private fun loadCars() {
        val repository = Repository()
        val viewModelFactory = ApiViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory)[ApiViewModel::class.java]

        viewModel.getCars()

        viewModel.cars.observe(this) { response ->
            if (response.isSuccessful) {
                carDataSet = response.body()!!

                carId = intent.getStringExtra("carId").toString()

                carItem = getCar(carId)
                carModelItem = getCarModel(carItem.carModelId)

                carNameTextView.text = carModelItem.carBrandName + " " + carModelItem.carModelName
                carIdTextView.text = carItem.carId
                Glide.with(this).load(carModelItem.carImageLink).into(carImageImageView)
                carAddressTextView.text = carItem.carAddress
            }
            else {
                Log.d("Error", response.message())
            }
        }
    }

    private fun getCar(carId: String): Car {
        for (carItem in carDataSet) {
            if (carItem.carId == carId) {
                return carItem
            }
        }
        return error("Car not found")
    }

    private fun getCarModel(carModelId: String): CarModel {
        for (carModelItem in carModelDataSet) {
            if (carModelItem.carModelId == carModelId) {
                return carModelItem
            }
        }
        return error("Car model not found")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun makeBooking() {
        //get current logged in user
        val user = auth.currentUser

        try {
            //upload booking to database
            val booking = Booking(
                bookingId = "B" + Calendar.getInstance().timeInMillis.toString(),
                bookingStartDate = bookingStartDate,
                bookingEndDate = bookingEndDate,
                bookingPaymentMethod = paymentMethod,
                carId = carId,
                userId = user?.uid.toString()
            )
            Log.d("Booking: ", booking.toString())
            //call POST api
            val apiInterface = ApiInterface.create().postBooking(booking)
            apiInterface.enqueue(object : Callback<Booking> {
                override fun onResponse(
                    call: Call<Booking>,
                    response: Response<Booking>,
                ) {
                    Log.d("OnResponse: ", response.message())
                }

                override fun onFailure(call: Call<Booking>, t: Throwable) {
                    Log.d("OnFailure: ", "failed")
                }
            })

            //indicate booked and paid successfully
            Toast.makeText(
                this,
                "Successfully Placed Booking",
                Toast.LENGTH_LONG
            ).show()
            //show booking successful
            startActivity(Intent(this@MakePaymentActivity, BookingSuccessfulActivity::class.java))
            finish()
        } catch (e: Exception) {
            //in case connection failure
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show()
        }
    }
}


