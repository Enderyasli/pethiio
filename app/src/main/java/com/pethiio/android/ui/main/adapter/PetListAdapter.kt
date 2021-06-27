package com.pethiio.android.ui.main.adapter

import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pethiio.android.R
import com.pethiio.android.data.model.PetListResponse
import kotlinx.android.synthetic.main.item_pets.view.*

class PetListAdapter(
    val context: Context,
    private val petList: ArrayList<PetListResponse>,
    private val newButtonTitle: String,

    ) : RecyclerView.Adapter<PetListAdapter.DataViewHolder>() {

    class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(context: Context, pet: PetListResponse) {

            itemView.pet_layout.visibility = View.VISIBLE
            itemView.add_pet_layout.visibility = View.GONE

            if (!TextUtils.isEmpty(pet.photo))
                Glide.with(context)
                    .load(pet.photo)
                    .into(itemView.pet_image)

            itemView.pet_name_tv.text = pet.name


        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        DataViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_pets, parent,
                false
            )
        )

    override fun getItemCount(): Int = petList.size + 1

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        if (position != petList.size)
            holder.bind(context, petList[position])
        else {
            holder.itemView.add_new_button.text = newButtonTitle
            holder.itemView.pet_layout.visibility = View.GONE
            holder.itemView.add_pet_layout.visibility = View.VISIBLE

        }


    }

    fun addData(list: List<PetListResponse>) {
        petList.addAll(list)
    }

}