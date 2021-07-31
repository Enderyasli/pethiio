package com.pethiio.android.ui.main.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.request.RequestOptions
import com.pethiio.android.R
import com.pethiio.android.data.model.chat.ChatListResponse
import com.pethiio.android.utils.GlideApp
import kotlinx.android.synthetic.main.message_list_item.view.*

class MessageAdapter(
    private var context: Context,
    private var navController: NavController,
    private val chatList: List<ChatListResponse>,
    private val memberId: Int
) : RecyclerView.Adapter<MessageAdapter.DataViewHolder>() {

    class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(context: Context, chatList: ChatListResponse) {


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
        holder.itemView.message_tv.text = chat.lastMessage
        holder.itemView.time_tv.text = chat.lastMessageTime

        if(chat.unSeenMessagesCount>0) {
            holder.itemView.unseen_tv.text = chat.unSeenMessagesCount.toString()
            holder.itemView.unseen_ly.visibility = View.VISIBLE
        } else{
            holder.itemView.unseen_ly.visibility=View.GONE
        }

        GlideApp.with(holder.itemView.profile_image)
            .load(chat.avatar)
            .apply(RequestOptions().override(200, 200))
            .into(holder.itemView.profile_image)



        holder.itemView.setOnClickListener {
            val bundle =
                bundleOf(
                    "petId" to chat.petId,
                    "petAvatar" to chat.avatar,
                    "roomId" to chat.id,
                    "memberId" to memberId,
                    "memberName" to chat.name
                )


            navController.navigate(R.id.action_navigation_message_to_navigation_chat, bundle)
        }


    }


}