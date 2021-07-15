package com.pethiio.android.ui.main.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pethiio.android.R
import com.pethiio.android.data.model.Message
import com.pethiio.android.data.model.chat.ChatListResponse
import com.pethiio.android.data.model.socket.ChatSendMessage
import com.pethiio.android.data.socket.SocketIO
import com.pethiio.android.utils.PreferenceHelper
import com.pethiio.android.utils.Resource
import kotlinx.android.synthetic.main.message_list_item.view.*

class MessageAdapter(
    private var context: Context,
    private var navController: NavController,
    private val chatList: List<ChatListResponse>,
    private val memberId: Int
) : RecyclerView.Adapter<MessageAdapter.DataViewHolder>() {

    class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(context: Context, chatList: ChatListResponse) {

            itemView.name_tv.text = chatList.name
//            itemView.message_tv.text = user.message

//            Glide.with(itemView.profile_image)
//                .load(user.url)
//                .into(itemView.profile_image)


            itemView.setOnClickListener {
            }

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

        val chat: ChatListResponse = chatList[position]

        holder.itemView.name_tv.text = chat.name
//            itemView.message_tv.text = user.message

//            Glide.with(itemView.profile_image)
//                .load(user.url)
//                .into(itemView.profile_image)


        holder.itemView.setOnClickListener {
            val bundle =
                bundleOf("roomId" to chat.id, "memberId" to memberId, "memberName" to chat.name)


            navController.navigate(R.id.navigation_chat, bundle)
        }


    }


}