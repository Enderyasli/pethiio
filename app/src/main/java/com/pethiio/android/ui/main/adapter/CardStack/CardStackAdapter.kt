package com.pethiio.android.ui.main.adapter.CardStack

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pethiio.android.R
import com.pethiio.android.data.model.member.PetSearchResponse

class CardStackAdapter(
    private var navController: NavController,
    private var petSearchList: List<PetSearchResponse>
) : RecyclerView.Adapter<CardStackAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(inflater.inflate(R.layout.item_spot, parent, false))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val searchResponse = petSearchList[position]
        holder.petName.text = "${searchResponse.name}, ${searchResponse.age}"
        holder.breed.text = searchResponse.breed
        holder.ownerName.text = searchResponse.owner

        Glide.with(holder.petImage)
            .load(searchResponse.avatar)
            .apply(RequestOptions().override(1080, 1920))
            .into(holder.petImage)

        Glide.with(holder.ownerImage)
            .load(searchResponse.ownerAvatar)
            .apply(RequestOptions().override(100, 100))
            .into(holder.ownerImage)

        holder.itemView.setOnClickListener { v ->

            val bundle =
                bundleOf("memberId" to searchResponse.ownerId, "animalId" to searchResponse.petId.toString())

            navController.navigate(R.id.navigation_pet_detail, bundle)

            Toast.makeText(v.context, searchResponse.name, Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int {
        return petSearchList.size
    }

    fun setPetSearchList(searchList: List<PetSearchResponse>) {
        this.petSearchList = searchList
        notifyDataSetChanged()
    }

    fun getPetSearchList(): List<PetSearchResponse> {
        return petSearchList
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val petName: TextView = view.findViewById(R.id.item_pet_name)
        val ownerName: TextView = view.findViewById(R.id.item_owner_name)
        var breed: TextView = view.findViewById(R.id.item_breed)
        var petImage: ImageView = view.findViewById(R.id.item_pet_image)
        var ownerImage: ImageView = view.findViewById(R.id.owner_image)
    }

}
