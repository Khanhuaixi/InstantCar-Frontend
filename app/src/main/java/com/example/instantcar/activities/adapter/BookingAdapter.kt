package com.example.instantcar.activities.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.instantcar.R
import com.example.instantcar.activities.api.ApiInterface
import com.example.instantcar.activities.model.Booking
import com.example.instantcar.activities.model.Car
import com.example.instantcar.activities.model.CarModel
import com.example.instantcar.activities.utilities.Dialog
import com.google.android.material.chip.Chip
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*


class BookingAdapter(
    private val fragmentManager: FragmentManager,
    private val context: Context,
    private val bookingDataSet: List<Booking>,
    private val carDataSet: List<Car>,
    private val carModelDataSet: List<CarModel>
) : RecyclerView.Adapter<BookingAdapter.ItemViewHolder>() {

    private lateinit var simpleDateFormat: SimpleDateFormat

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.
    // Each data item is just an Booking object.
    class ItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val carImageImageView: ImageView = view.findViewById(R.id.carImageBookingImageView)
        val carIdChip: Chip = view.findViewById(R.id.carIdBookingChip)
        val carNameTextView: TextView = view.findViewById(R.id.carNameBookingTextView)
        val carAddressTextView: TextView = view.findViewById(R.id.carAddressBookingTextView)
        val bookingDateTextView: TextView = view.findViewById(R.id.bookingDateBookingTextView)
        val bookingPaymentMethodTextView: TextView =
            view.findViewById(R.id.bookingPaymentMethodBookingTextView)
        val editBookingDateButton: Button = view.findViewById(R.id.editBookingDateBookingButton)
        val cancelBookingButton: Button = view.findViewById(R.id.cancelBookingBookingButton)
    }

    /**
     * Create new views (invoked by the layout manager)
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        // create a new view
        //inflate list_item to parent (recycler view)
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_view_booking, parent, false)

        simpleDateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())

        return ItemViewHolder(adapterLayout)
    }

    /**
     * Replace the contents of a view (invoked by the layout manager)
     */
    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        var bookingItem = bookingDataSet[position]
        val carItem = getCar(bookingItem.carId)
        val carModelItem = getCarModel(carItem.carModelId)

        Glide.with(this.context).load(carModelItem.carImageLink).into(holder.carImageImageView)
        holder.carIdChip.text = carItem.carId
        holder.carNameTextView.text =
            carModelItem.carBrandName + " " + carModelItem.carModelName//context.resources.getString(item.stringResourceId)
        holder.carAddressTextView.text = "Location: " + carItem.carAddress
        val bookingStartDate = bookingItem.bookingStartDate
        val bookingEndDate = bookingItem.bookingEndDate
        if (bookingStartDate != bookingEndDate) {
            holder.bookingDateTextView.text = "Booking Date: $bookingStartDate to $bookingEndDate"
        } else {
            holder.bookingDateTextView.text = "Booking Date: $bookingEndDate"
        }
        holder.bookingPaymentMethodTextView.text =
            "Payment Method: " + bookingItem.bookingPaymentMethod

        holder.editBookingDateButton.setOnClickListener {
            val pattern = "dd-MM-yyyy"
            val simpleDateFormat = SimpleDateFormat(pattern)
            val bookingDate = bookingItem.bookingStartDate
            val currentDate = simpleDateFormat.format(Date())

            val convertedBookingDate = simpleDateFormat.parse(bookingDate)
            val convertedCurrentTime = simpleDateFormat.parse(currentDate)

            //title and message variables for dialogs
            lateinit var title: String
            lateinit var message: String

            //compare if current date time is earlier than booking date
            if( convertedCurrentTime < convertedBookingDate){
                showDateRangePicker(bookingItem, holder.bookingDateTextView)
            }else{
                title = "Unable to Edit Booking Date"
                message = "You can only edit bookings before the booking date. \n" +
                        "Therefore, this booking cannot be edited. Sorry for the inconvenience."
                Dialog().showAlertDialog(title, message, context)
            }
        }

        holder.cancelBookingButton.setOnClickListener {
            val pattern = "dd-MM-yyyy"
            val simpleDateFormat = SimpleDateFormat(pattern)
            val bookingDate = bookingItem.bookingStartDate
            val currentDate = simpleDateFormat.format(Date())

            val convertedBookingDate = simpleDateFormat.parse(bookingDate)
            val convertedCurrentTime = simpleDateFormat.parse(currentDate)

            //title and message variables for dialogs
            lateinit var title: String
            lateinit var message: String
            lateinit var toastText: String

            //compare if current date time is earlier than booking date
            if( convertedCurrentTime < convertedBookingDate){
                title = "Cancel Booking"
                message = "Are you sure you want to cancel this booking?"
                toastText = "Booking Successfully Cancelled"
                Dialog().showConfirmDialog(title, message, toastText, context, bookingItem.bookingId, ::deleteBooking)
            }else{
                title = "Unable to Cancel Booking"
                message = "You can only cancel bookings before the booking date. \n" +
                        "Therefore, this booking cannot be canceled. Sorry for the inconvenience."
                Dialog().showAlertDialog(title, message, context)
            }
        }
    }

    /**
     * Return the size of your dataset (invoked by the layout manager)
     */
    override fun getItemCount() = bookingDataSet.size

    private fun getCarModel(carModelId: String): CarModel {
        for (carModelItem in carModelDataSet) {
            if (carModelItem.carModelId == carModelId) {
                return carModelItem
            }
        }
        return error("Car model not found")
    }

    private fun getCar(carId: String): Car {
        for (carItem in carDataSet) {
            if (carItem.carId == carId) {
                return carItem
            }
        }
        return error("Car not found")
    }

    private fun deleteBooking(bookingId: String) {
        val apiInterface = ApiInterface.create().deleteBooking(bookingId)

        apiInterface.enqueue(object : Callback<Booking> {
            override fun onResponse(call: Call<Booking>, response: Response<Booking>) {
                Log.d("Response", response.code().toString())
            }

            override fun onFailure(call: Call<Booking>, t: Throwable) {
                Toast.makeText(
                    context,
                    "Error. Booking could not be canceled." + t.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

    }

    private fun editBooking(booking: Booking, textViewToUpdate: TextView) {
        val apiInterface = ApiInterface.create().putBooking(booking, booking.bookingId)
        apiInterface.enqueue(object : Callback<Booking> {
            override fun onResponse(call: Call<Booking>, response: Response<Booking>) {
                val bookingStartDate = booking.bookingStartDate
                val bookingEndDate = booking.bookingEndDate
                textViewToUpdate.text = generateBookingDateTextViewText(bookingStartDate, bookingEndDate)
                Log.d("Response", response.code().toString() + ": " + response.message())
                Toast.makeText(context, "Booking is updated", Toast.LENGTH_SHORT)
                    .show()
            }

            override fun onFailure(call: Call<Booking>, t: Throwable) {
                Toast.makeText(context, "Error. Booking date could not be updated.", Toast.LENGTH_SHORT).show()
            }
        })

    }

    private fun generateBookingDateTextViewText(bookingStartDate: String, bookingEndDate: String): String {
        val bookingDateTextViewText: String = if (bookingStartDate != bookingEndDate) {
            "Booking Date: $bookingStartDate to $bookingEndDate"
        } else {
            "Booking Date: $bookingEndDate"
        }
        return bookingDateTextViewText
    }

    @SuppressLint("SetTextI18n")
    private fun showDateRangePicker(booking: Booking, textViewToUpdate: TextView) {

        val constraintsBuilder =
            CalendarConstraints.Builder()
                .setValidator(
                    DateValidatorPointForward.now()
                )

        val datePickerRange = MaterialDatePicker.Builder.dateRangePicker()
            .setTitleText("Edit Booking Date")
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
        datePickerRange.show(fragmentManager, "date_picker")

        datePickerRange.addOnPositiveButtonClickListener {
            val startDate = it.first
            val endDate = it.second

            booking.bookingStartDate = simpleDateFormat.format(startDate)
            booking.bookingEndDate = simpleDateFormat.format(endDate)

            editBooking(booking, textViewToUpdate)
        }
    }
}