package com.pethiio.android.ui.main.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.pethiio.android.R
import kotlinx.android.synthetic.main.item_layout.view.*

class PetSearchDetailCharacterAdapter(
    val context: Context,
    private val characterList: List<String>
) : RecyclerView.Adapter<PetSearchDetailCharacterAdapter.DataViewHolder>() {


    class DataViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        fun bind(character: String) {

            itemView.character_title_tv.text = character


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        DataViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.pet_search_item, parent,
                false
            )
        )

    override fun getItemCount(): Int = characterList.size

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) =
        holder.bind(characterList[position])


}