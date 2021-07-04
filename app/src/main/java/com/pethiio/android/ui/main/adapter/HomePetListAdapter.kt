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
import kotlinx.android.synthetic.main.item_animal.view.*
import kotlinx.android.synthetic.main.item_animal.view.pet_name_tv
import kotlinx.android.synthetic.main.item_pets.view.*

class HomePetListAdapter(
    val context: Context,
    private val petList: List<PetListResponse>
) : RecyclerView.Adapter<HomePetListAdapter.DataViewHolder>() {

    class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(context: Context, pet: PetListResponse) {
            if (!TextUtils.isEmpty(pet.photo))
                Glide.with(context)
                    .load(pet.photo)
                    .into(itemView.home_pet_image)

            itemView.pet_name_tv.text = pet.name


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        DataViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_animal, parent,
                false
            )
        )

    override fun getItemCount(): Int = petList.size

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) =
        holder.bind(context, petList[position])


}