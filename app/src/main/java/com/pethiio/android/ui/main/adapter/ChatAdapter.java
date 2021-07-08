package com.pethiio.android.ui.main.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

import com.pethiio.android.R;
import com.pethiio.android.data.EventBus.ChatEvent;
import com.pethiio.android.data.model.chat.ChatRoomResponse;

import org.jetbrains.annotations.NotNull;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final Context context;
    List<ChatRoomResponse> list;
    private int MEMBER_ID = 0;

    public ChatAdapter(Context context, List<ChatRoomResponse> list, int memberId) {
        this.context = context;
        this.list = list;
        this.MEMBER_ID = memberId;
    }

    private class MessageInViewHolder extends RecyclerView.ViewHolder {

        TextView messageTV, dateTV;

        MessageInViewHolder(final View itemView) {
            super(itemView);
            messageTV = itemView.findViewById(R.id.message_text);
        }

        void bind(int position) {
            ChatRoomResponse messageModel = list.get(position);
            messageTV.setText(messageModel.getContent());
        }
    }

    private class MessageOutViewHolder extends RecyclerView.ViewHolder {

        TextView messageTV, dateTV;

        MessageOutViewHolder(final View itemView) {
            super(itemView);
            messageTV = itemView.findViewById(R.id.message_text);
        }

        void bind(int position) {
            ChatRoomResponse messageModel = list.get(position);
            messageTV.setText(messageModel.getContent());
        }
    }

    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        if (viewType != MEMBER_ID) {
            return new MessageInViewHolder(LayoutInflater.from(context).inflate(R.layout.incoming_msg_layout, parent, false));
        } else {
            return new MessageOutViewHolder(LayoutInflater.from(context).inflate(R.layout.outgoing_msg_layout, parent, false));

        }
    }

    @Override
    public void onBindViewHolder(@NotNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) != MEMBER_ID) {
            ((MessageInViewHolder) holder).bind(position);
        } else {
            ((MessageOutViewHolder) holder).bind(position);
        }
    }

    public void addMessage(ChatEvent message) {

        ChatRoomResponse chatRoomResponse =
                new ChatRoomResponse(message.getContent(), message.getId(), message.getSenderMemberId(), message.getTime());

        list.add(chatRoomResponse);
//        notifyDataSetChanged();
        notifyItemInserted(list.size() - 1);
    }

    public int getListSize(){
       return list.size();
    }
    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position).getSenderMemberId();
    }
}
