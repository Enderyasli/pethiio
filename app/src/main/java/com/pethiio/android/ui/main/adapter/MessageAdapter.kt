package com.pethiio.android.ui.main.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pethiio.android.R
import com.pethiio.android.data.model.Message
import com.pethiio.android.data.model.chat.ChatListResponse
import com.pethiio.android.utils.Resource
import kotlinx.android.synthetic.main.message_list_item.view.*

class MessageAdapter(
    private var context: Context,
    private var navController: NavController,
    private val chatList: List<ChatListResponse>
) : RecyclerView.Adapter<MessageAdapter.DataViewHolder>() {

    class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(context: Context, chatList: ChatListResponse) {

            itemView.name_tv.text = chatList.name
//            itemView.message_tv.text = user.message

//            Glide.with(itemView.profile_image)
//                .load(user.url)
//                .into(itemView.profile_image)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        DataViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.message_list_item, parent,
                false
            )
        )

    override fun getItemCount(): Int = chatList.size

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        holder.bind(context, chatList[position])

    }


}