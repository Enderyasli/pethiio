package com.pethiio.android.ui.main.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.pethiio.android.R
import kotlinx.android.synthetic.main.item_layout.view.*


class FilterAdapter(
    val context: Context,
    private val users: ArrayList<String>,
    val filterItemClickListener: FilterItemClickListener
) : RecyclerView.Adapter<FilterAdapter.DataViewHolder>() {

    private var selectedPos = 0
    private val listener: FilterItemClickListener? = null


    class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(context: Context, character: String) {
            itemView.character_title_tv.text = character
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

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.bind(context, users[position])



        if (position != selectedPos) {
            holder.itemView.character_title_tv.setBackgroundResource(R.drawable.character_item_bg)
            holder.itemView.character_title_tv.setTextColor(
                ContextCompat.getColor(
                    context,
                    R.color.grey
                )
            )
            holder.itemView.isSelected = false
        }




        holder.itemView.setOnClickListener {

            filterItemClickListener.onFilterItemClick(users[position]);

            selectedPos = position
            notifyDataSetChanged()
            if (!it.isSelected) {
                holder.itemView.character_title_tv.setBackgroundResource(R.drawable.character_item_selected_bg)
                holder.itemView.character_title_tv.setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.white
                    )
                )
                it.isSelected = true

            } else {
                holder.itemView.character_title_tv.setBackgroundResource(R.drawable.character_item_bg)
                holder.itemView.character_title_tv.setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.grey
                    )
                )
                it.isSelected = false
            }


        }
    }

    fun addData(list: List<String>) {
        users.addAll(list)
    }

}