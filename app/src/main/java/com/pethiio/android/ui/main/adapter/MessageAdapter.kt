package com.pethiio.android.ui.main.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pethiio.android.R
import com.pethiio.android.data.model.Message
import kotlinx.android.synthetic.main.message_list_item.view.*

class MessageAdapter(
    val context: Context,
    private val users: ArrayList<Message>
) : RecyclerView.Adapter<MessageAdapter.DataViewHolder>() {

    class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(context: Context, user: Message) {

            itemView.name_tv.text = user.name
            itemView.message_tv.text = user.message

            Glide.with(itemView.profile_image)
                .load(user.url)
                .into(itemView.profile_image)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        DataViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.message_list_item, parent,
                false
            )
        )

    override fun getItemCount(): Int = users.size

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.bind(context, users[position])

        if (position == users.size - 1)
            holder.itemView.divider.visibility = View.GONE
    }

    fun addData(list: List<Message>) {
        users.addAll(list)
    }

}