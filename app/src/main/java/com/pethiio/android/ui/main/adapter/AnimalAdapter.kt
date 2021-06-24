package com.pethiio.android.ui.main.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pethiio.android.R
import kotlinx.android.synthetic.main.animal_item.view.*

class AnimalAdapter(
    val context: Context,
    private val users: ArrayList<String>
) : RecyclerView.Adapter<AnimalAdapter.DataViewHolder>() {

    class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(context: Context, petName: String) {
            itemView.pet_name_tv.text = petName

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        DataViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.animal_item, parent,
                false
            )
        )

    override fun getItemCount(): Int = users.size

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) =
        holder.bind(context, users[position])

    fun addData(list: List<String>) {
        users.addAll(list)
    }

}