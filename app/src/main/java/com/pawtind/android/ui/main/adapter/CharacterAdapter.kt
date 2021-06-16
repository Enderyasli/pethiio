package com.pawtind.android.ui.main.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.pawtind.android.R
import kotlinx.android.synthetic.main.item_layout.view.*

class CharacterAdapter(
    val context: Context,
    private val users: ArrayList<String>
) : RecyclerView.Adapter<CharacterAdapter.DataViewHolder>() {

    class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(context: Context, character: String) {
            itemView.character_title_tv.text = character

            itemView.setOnClickListener {
                if(!it.isSelected){
                    itemView.character_title_tv.setBackgroundResource(R.drawable.character_item_selected_bg)
                    itemView.character_title_tv.setTextColor(ContextCompat.getColor(context,R.color.white))
                    it.isSelected=true
                }else{
                    itemView.character_title_tv.setBackgroundResource(R.drawable.character_item_bg)
                    itemView.character_title_tv.setTextColor(ContextCompat.getColor(context,R.color.grey))
                    it.isSelected=false
                }


            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        DataViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_layout, parent,
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