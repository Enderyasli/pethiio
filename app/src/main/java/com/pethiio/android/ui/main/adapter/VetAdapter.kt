package com.pethiio.android.ui.main.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pethiio.android.R
import com.pethiio.android.data.model.vet.VetResponse
import kotlinx.android.synthetic.main.item_vet.view.*


class VetAdapter(
    private var navController: NavController,
    val context: Context,
    private val vetList: List<VetResponse>
) : RecyclerView.Adapter<VetAdapter.DataViewHolder>() {

    class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(context: Context, pet: VetResponse, navController: NavController) {

            if (!TextUtils.isEmpty(pet.avatar))
                Glide.with(context)
                    .load(pet.avatar)
                    .into(itemView.profile_vet)

            itemView.name_tv.text = pet.name
            itemView.title_tv.text = pet.title
            itemView.description_tv.text = pet.detail
//            itemView.name_tv.text = pet.name

//            itemView.whatsapp.setOnClickListener {
//                val url = "https://api.whatsapp.com/send?phone=${pet.phone}"
//                navController.navigate(Uri.parse(url))
//            }


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        DataViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_vet, parent,
                false
            )
        )

    override fun getItemCount(): Int = vetList.size

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {

        holder.bind(context, vetList[position],navController)

    }


}