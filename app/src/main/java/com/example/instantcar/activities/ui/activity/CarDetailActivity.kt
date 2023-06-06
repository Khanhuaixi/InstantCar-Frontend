package com.example.instantcar.activities.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.instantcar.R
import com.example.instantcar.activities.api.ApiViewModel
import com.example.instantcar.activities.api.ApiViewModelFactory
import com.example.instantcar.activities.api.Repository
import com.example.instantcar.activities.model.Car
import com.example.instantcar.activities.model.CarModel
import com.example.instantcar.activities.utilities.MathOperation
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.properties.Delegates

class CarDetailActivity : AppCompatActivity() {
    private lateinit var carImageImageView: ImageView
    private lateinit var carNameTextView: TextView
    private lateinit var totalPaymentTextView: TextView
    private lateinit var bookingDayAmountTextView: TextView
    private lateinit var carAddressTextView: TextView
    private lateinit var carVehicleTypeTextView: TextView
    private lateinit var carPetrolTypeTextView: TextView
    private lateinit var carTransmissionTypeTextView: TextView
    private lateinit var proceedButton: Button
    private lateinit var bookingDateTextView: TextView
    private lateinit var editBookingDateImageButton: ImageButton

    private var bookingDayAmount by Delegates.notNull<Long>()
    private lateinit var totalPrice: BigDecimal
    private lateinit var simpleDateFormat: SimpleDateFormat
    private var dateNow by Delegates.notNull<Long>()
    private lateinit var bookingStartDate: String
    private lateinit var bookingEndDate: String

    private lateinit var viewModel: ApiViewModel
    private var carDataSet: List<Car> = ArrayList()
    private var carModelDataSet: List<CarModel> = ArrayList()
    private lateinit var selectedCarId: String
    private lateinit var selectedCar: Car
    private lateinit var selectedCarCarModel: CarModel

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_car_detail)

        carImageImageView = findViewById(R.id.carImageCarDetailImageView)
        carNameTextView = findViewById(R.id.carNameCarDetailTextView)
        totalPaymentTextView = findViewById(R.id.totalPaymentCarDetailTextView)
        bookingDayAmountTextView = findViewById(R.id.bookingDayAmountCarDetailTextView)
        carAddressTextView = findViewById(R.id.carAddressCarDetailTextView)
        carVehicleTypeTextView = findViewById(R.id.carVehicleTypeCarDetailTextView)
        carPetrolTypeTextView = findViewById(R.id.carPetrolTypeCarDetailTextView)
        carTransmissionTypeTextView = findViewById(R.id.carTransmissionTypeCarDetailTextView)
        proceedButton = findViewById(R.id.proceedCarDetailButton)
        bookingDateTextView = findViewById(R.id.bookingDateCarDetailTextView)
        editBookingDateImageButton = findViewById(R.id.editBookingDateCarDetailImageButton)

        bookingDayAmount = 1
        totalPrice = BigDecimal(0.00).setScale(2)
        simpleDateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        dateNow = Calendar.getInstance().timeInMillis
        bookingStartDate = simpleDateFormat.format(dateNow)
        bookingEndDate = simpleDateFormat.format(dateNow)

        selectedCarId = intent.getStringExtra("selectedCarId").toString()
//        Log.d("selectedCarId", selectedCarId)
//        selectedCar = getSelectedCar(selectedCarId)
//        Log.d("selectedCar", selectedCar.toString())
//        selectedCarCarModel = getSelectedCarCarModel(selectedCar.carModelId)
//        Log.d("selectedCarCarModel", selectedCarCarModel.toString())
        loadCarModels()
//        loadCarDetail(selectedCar, selectedCarCarModel)

        editBookingDateImageButton.setOnClickListener {
            showDateRangePicker(selectedCar)
        }

        proceedButton.setOnClickListener {
            makePayment()
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

                selectedCarId = intent.getStringExtra("selectedCarId").toString()
                selectedCar = getSelectedCar(selectedCarId)
                selectedCarCarModel = getSelectedCarCarModel(selectedCar.carModelId)

                loadCarDetail(selectedCar, selectedCarCarModel)
            }
            else {
                Log.d("Error", response.message())
            }
        }
    }

    private fun getSelectedCar(carId: String): Car {
        for (carItem in carDataSet) {
            if (carItem.carId == carId) {
                return carItem
            }
        }
        return error("Car not found")
    }

    private fun getSelectedCarCarModel(carModelId: String): CarModel {
        for (carModelItem in carModelDataSet) {
            if (carModelItem.carModelId == carModelId) {
                return carModelItem
            }
        }
        return error("Car model not found")
    }

    @SuppressLint("SetTextI18n")
    private fun loadCarDetail(car: Car, carModel: CarModel) {
        //car image
        Glide.with(this).load(carModel.carImageLink).into(carImageImageView)
        //car brand and car model
        carNameTextView.text = carModel.carBrandName + " " + carModel.carModelName
        //car rental
        totalPrice =
            MathOperation().calculateTotalRentalAmount(car.carRentalRate, bookingDayAmount.toInt())
        totalPaymentTextView.text = "RM$totalPrice"
        //booking day amount
        bookingDateTextView.text = "$bookingDayAmount day"
        //car address
        carAddressTextView.text = car.carAddress
        //booking date
        bookingDateTextView.text = simpleDateFormat.format(dateNow)
        //vehicle type
        carVehicleTypeTextView.text = carModel.carVehicleType
        //petrol type
        carPetrolTypeTextView.text = carModel.carPetrolType
        //transmission type
        carTransmissionTypeTextView.text = carModel.carTransmissionType
    }

    @SuppressLint("SetTextI18n")
    private fun showDateRangePicker(car: Car) {
        //set constraint to disable dates before today
        val constraintsBuilder =
            CalendarConstraints.Builder()
                .setValidator(
                    DateValidatorPointForward.now()
                )

        //build dateRangePicker
        val dateRangePicker = MaterialDatePicker.Builder.dateRangePicker()
            .setTitleText("Select Booking Date")
            .setCalendarConstraints(
                constraintsBuilder.build()
            )
            .setSelection(
                androidx.core.util.Pair(
                    Calendar.getInstance().timeInMillis,
                    Calendar.getInstance().timeInMillis
                )
            )
            .build()

        //setup positive button
        dateRangePicker.addOnPositiveButtonClickListener {
            //get selected date range
            val startDate = it.first
            val endDate = it.second
            //format selected date
            bookingStartDate = simpleDateFormat.format(startDate)
            bookingEndDate = simpleDateFormat.format(endDate)
            //update text view for new selected booking date
            //calculate and update booking day amount text view
            if (bookingStartDate != bookingEndDate) {
                bookingDateTextView.text = "$bookingStartDate to $bookingEndDate"
                bookingDayAmount = TimeUnit.MILLISECONDS.toDays(endDate - startDate) + 1
                bookingDayAmountTextView.text = "$bookingDayAmount days"
            } else {
                bookingDateTextView.text = "$bookingEndDate"
                bookingDayAmount = 1
                bookingDayAmountTextView.text = "$bookingDayAmount day"
            }
            //update text view for new total rental amount
            totalPrice = MathOperation().calculateTotalRentalAmount(car.carRentalRate, bookingDayAmount.toInt())
            totalPaymentTextView.text = "RM$totalPrice"
        }

        //show dateRangePicker
        dateRangePicker.show(supportFragmentManager, "date_picker")
    }

    private fun makePayment() {
        val intent = Intent(this, MakePaymentActivity::class.java)
        intent.putExtra("carId", selectedCarId)
        intent.putExtra("bookingStartDate", bookingStartDate)
        intent.putExtra("bookingEndDate", bookingEndDate)
        intent.putExtra("totalPrice", totalPrice.toString())
        intent.putExtra("bookingDayAmount", bookingDayAmount.toString())
        startActivity(intent)
    }
}