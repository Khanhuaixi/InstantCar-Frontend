package com.example.instantcar.activities.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.instantcar.R
import com.example.instantcar.activities.model.Car
import com.example.instantcar.activities.model.CarModel
import com.example.instantcar.activities.ui.activity.CarDetailActivity
import java.math.BigDecimal


class CarAdapter(
    private val context: Context,
    private val carDataSet: List<Car>,
    private val carModelDataSet: List<CarModel>
): RecyclerView.Adapter<CarAdapter.ItemViewHolder>() {

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.
    // Each data item is just an Car object.
    class ItemViewHolder(private val view: View): RecyclerView.ViewHolder(view) {
        val carNameTextView: TextView = view.findViewById(R.id.carNameCarTextView)
        val carIdTextView: TextView = view.findViewById(R.id.carIdCarTextView)
        val carAddressTextView: TextView = view.findViewById(R.id.carAddressCarTextView)
        val carRentalRateTextView: TextView = view.findViewById(R.id.carRentalRateCarTextView)
        val carImageImageView: ImageView = view.findViewById(R.id.carImageCarImageView)
    }

    /**
     * Create new views (invoked by the layout manager)
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        // create a new view
        //inflate list_item to parent (recycler view)
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_view_car, parent, false)

        return ItemViewHolder(adapterLayout)
    }

    /**
     * Replace the contents of a view (invoked by the layout manager)
     */
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val carItem = carDataSet[position]

        val carModelItem = getCarModel(carItem.carModelId)

        holder.carNameTextView.text = carModelItem.carBrandName + " " + carModelItem.carModelName
        holder.carIdTextView.text = carItem.carId
        holder.carAddressTextView.text = carItem.carAddress
        holder.carRentalRateTextView.text = "RM" + BigDecimal(carItem.carRentalRate.toString()).setScale(2).toString()
        holder.itemView.setOnClickListener {
            startCarDetailActivity(carItem.carId)
        }
        //local: holder.imageView.setImageResource(item.image)
        //server: (AFTER ADDING DEPENDENCIES), download (load) and visualize (into)
        Glide.with(this.context).load(carModelItem.carImageLink).into(holder.carImageImageView)
    }

    /**
     * Return the size of your dataset (invoked by the layout manager)
     */
    override fun getItemCount() = carDataSet.size

    private fun getCarModel(carModelId: String): CarModel {
        for (carModelItem in carModelDataSet) {
            if (carModelItem.carModelId == carModelId) {
                return carModelItem
            }
        }
        return error("Car model not found")
    }

    private fun startCarDetailActivity(selectedCarId: String) {
            val intent = Intent (this.context, CarDetailActivity::class.java)
            intent.putExtra("selectedCarId", selectedCarId)
            this.context.startActivity(intent)
    }
}