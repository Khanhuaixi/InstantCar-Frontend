package com.example.instantcar.activities.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.instantcar.R
import com.example.instantcar.activities.adapter.CarAdapter
import com.example.instantcar.activities.adapter.CarModelAdapter
import com.example.instantcar.activities.api.ApiViewModel
import com.example.instantcar.activities.api.ApiViewModelFactory
import com.example.instantcar.activities.api.Repository
import com.example.instantcar.activities.model.Car
import com.example.instantcar.activities.model.CarModel

class ExploreFragment : Fragment() {
    private lateinit var carModelRecyclerView: RecyclerView
    private lateinit var carModelDescriptionTextView: TextView
    private lateinit var carRecyclerView: RecyclerView

    private lateinit var carModelAdapter: CarModelAdapter
    private lateinit var carAdapter: CarAdapter
    private lateinit var viewModel: ApiViewModel

    private var carModelDataSet: List<CarModel> = ArrayList()
    private var carDataSet: List<Car> = ArrayList()

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_explore, container, false)

        loadCarModels()
        //loadCars()

        // Inflate the layout for this fragment
        return view
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun loadCarModels() {
        val repository = Repository()
        val viewModelFactory = ApiViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory)[ApiViewModel::class.java]

        viewModel.getCarModels()

        viewModel.carModels.observe(viewLifecycleOwner) { response ->
            if (response.isSuccessful) {
                carModelDataSet = response.body()!!
                carModelAdapter = CarModelAdapter(requireContext(), carModelDataSet)

                carModelDescriptionTextView = requireView().findViewById(R.id.carModelDescriptionExploreTextView)
                carModelDescriptionTextView.text = "Over " + carModelDataSet.size.toString() + " car models for you to choose car"

                carModelRecyclerView = requireView().findViewById(R.id.carModelExploreRecyclerView)
                carModelRecyclerView.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
                carModelRecyclerView.adapter = carModelAdapter
                carModelRecyclerView.setHasFixedSize(true)
                carModelAdapter.notifyDataSetChanged()

                loadCars()
            }
            else {
                Log.d("Error", response.message())
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun loadCars() {
        val repository = Repository()
        val viewModelFactory = ApiViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory)[ApiViewModel::class.java]

        viewModel.getCars()

        viewModel.cars.observe(viewLifecycleOwner) { response ->
            if (response.isSuccessful) {
                carDataSet = response.body()!!
                carAdapter = CarAdapter(requireContext(), carDataSet, carModelDataSet)

                carRecyclerView = requireView().findViewById(R.id.carExploreRecyclerView)
                carRecyclerView.layoutManager = LinearLayoutManager(requireContext())
                carRecyclerView.adapter = carAdapter
                // Use this setting to improve performance if you know that changes
                // in content do not change the layout size of the RecyclerView
                carRecyclerView.setHasFixedSize(true)
                carAdapter.notifyDataSetChanged()
            }
            else {
                Log.d("Error", response.message())
            }
        }
    }


}