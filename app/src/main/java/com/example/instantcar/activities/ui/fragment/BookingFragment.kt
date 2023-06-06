package com.example.instantcar.activities.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.instantcar.R
import com.example.instantcar.activities.adapter.BookingAdapter
import com.example.instantcar.activities.api.ApiViewModel
import com.example.instantcar.activities.api.ApiViewModelFactory
import com.example.instantcar.activities.api.Repository
import com.example.instantcar.activities.model.Booking
import com.example.instantcar.activities.model.Car
import com.example.instantcar.activities.model.CarModel
import com.google.firebase.auth.FirebaseAuth

class BookingFragment : Fragment() {
    private lateinit var bookingRecyclerView: RecyclerView
    private lateinit var bookingAdapter: BookingAdapter

    private lateinit var viewModel: ApiViewModel

    private var carDataSet: List<Car> = ArrayList()
    private var carModelDataSet: List<CarModel> = ArrayList()
    private var bookingDataSet: List<Booking> = ArrayList()

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_booking, container, false)

        auth = FirebaseAuth.getInstance()

        loadCarModels()

        // Inflate the layout for this fragment
        return view
    }

    private fun loadCarModels() {
        val repository = Repository()
        val viewModelFactory = ApiViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory)[ApiViewModel::class.java]

        viewModel.getCarModels()

        viewModel.carModels.observe(viewLifecycleOwner) { response ->
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
        //get current logged in user
        val user = auth.currentUser

        val repository = Repository()
        val viewModelFactory = ApiViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory)[ApiViewModel::class.java]



        viewModel.getCars()

        viewModel.cars.observe(viewLifecycleOwner) { response ->
            if (response.isSuccessful) {
                carDataSet = response.body()!!

                loadBookings()
            }
            else {
                Log.d("Error", response.message())
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun loadBookings() {
        //get current logged in user
        val user = auth.currentUser
        val uid = user?.uid

        val repository = Repository()
        val viewModelFactory = ApiViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory)[ApiViewModel::class.java]

        if ( uid != null) {
            viewModel.getBookingsByUserId(uid)
        }

        viewModel.bookings.observe(viewLifecycleOwner) { response ->
            if (response.isSuccessful) {
                bookingDataSet = response.body()!!
                bookingAdapter = BookingAdapter(requireFragmentManager(), requireContext(), bookingDataSet, carDataSet, carModelDataSet)

                bookingRecyclerView = requireView().findViewById(R.id.bookingsBookingRecyclerView)
                bookingRecyclerView.layoutManager = LinearLayoutManager(requireContext())
                bookingRecyclerView.adapter = bookingAdapter
                bookingRecyclerView.setHasFixedSize(true)
                bookingAdapter.notifyDataSetChanged()
            }
            else {
                Log.d("Error", response.message())
            }
        }
    }


}