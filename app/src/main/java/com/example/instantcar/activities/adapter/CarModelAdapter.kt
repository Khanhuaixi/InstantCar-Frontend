package com.example.instantcar.activities.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.instantcar.R
import com.example.instantcar.activities.model.CarModel

class CarModelAdapter(
    private val context: Context,
    private val carModelDataset: List<CarModel>
): RecyclerView.Adapter<CarModelAdapter.ItemViewHolder>() {

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.
    // Each data item is just an Car Model object.
    class ItemViewHolder(private val view: View): RecyclerView.ViewHolder(view) {
        val carNameTextView: TextView = view.findViewById(R.id.carNameCarModelTextView)
        val carImageImageView: ImageView = view.findViewById(R.id.carImageCarModelImageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        // create a new view
        //inflate list_item to parent (recycler view)
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_view_car_model, parent, false)

        return ItemViewHolder(adapterLayout)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = carModelDataset[position]
        holder.carNameTextView.text = item.carBrandName + " " + item.carModelName
        //local: holder.imageView.setImageResource(item.image)
        //server: (AFTER ADDING DEPENDENCIES), download (load) and visualize (into)
        Glide.with(this.context).load(item.carImageLink).into(holder.carImageImageView)
    }

    override fun getItemCount() = carModelDataset.size
}