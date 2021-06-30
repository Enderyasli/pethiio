package com.pethiio.android.ui.main.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pethiio.android.R
import kotlinx.android.synthetic.main.item_purpose_layout.view.*


class PurposeAdapter(
    val context: Context,
    private val purposes: ArrayList<String>,
) : RecyclerView.Adapter<PurposeAdapter.DataViewHolder>() {

    private var selectedPos = 0


    class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(context: Context, purpose: String) {
            itemView.purpose_tv.text = purpose
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        DataViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_purpose_layout, parent,
                false
            )
        )

    override fun getItemCount(): Int = purposes.size

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.bind(context, purposes[position])


//        if (position != selectedPos) {
//            holder.itemView.character_title_tv.setBackgroundResource(R.drawable.character_item_bg)
//            holder.itemView.character_title_tv.setTextColor(
//                ContextCompat.getColor(
//                    context,
//                    R.color.grey
//                )
//            )
//            holder.itemView.isSelected = false
//        }


        holder.itemView.setOnClickListener {

//            selectedPos = position
//            notifyDataSetChanged()
//            if (!it.isSelected) {
//
//                it.isSelected = true
//
//            } else {
//
//                it.isSelected = false
//            }
//

        }
    }

    fun addData(list: List<String>) {
        purposes.addAll(list)
    }

}