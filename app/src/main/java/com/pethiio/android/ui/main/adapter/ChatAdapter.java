package com.pethiio.android.ui.main.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

import com.pethiio.android.R;
import com.pethiio.android.data.EventBus.ChatEvent;
import com.pethiio.android.data.model.chat.ChatRoomResponse;
import com.pethiio.android.utils.Constants;

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

        TextView messageTv, timeTv;

        MessageInViewHolder(final View itemView) {
            super(itemView);
            messageTv = itemView.findViewById(R.id.message_text);
            timeTv = itemView.findViewById(R.id.time_text);
        }

        void bind(int position) {
            ChatRoomResponse messageModel = list.get(position);
            messageTv.setText(messageModel.getContent());
            timeTv.setText(getClock(messageModel));
        }
    }

    private class MessageOutViewHolder extends RecyclerView.ViewHolder {

        TextView messageTv, timeTv;

        MessageOutViewHolder(final View itemView) {
            super(itemView);
            messageTv = itemView.findViewById(R.id.message_text);
            timeTv = itemView.findViewById(R.id.time_text);

        }

        void bind(int position) {
            ChatRoomResponse messageModel = list.get(position);
            messageTv.setText(messageModel.getContent());
            timeTv.setText(getClock(messageModel));
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

    private String getClock(ChatRoomResponse messageModel) {
        try {
            Calendar cal = Calendar.getInstance();
            Date convertedDate;
            @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
            convertedDate = dateFormat.parse(messageModel.getTime());

            cal.setTime(convertedDate);
            int hours = cal.get(Calendar.HOUR_OF_DAY);
            int mins = cal.get(Calendar.MINUTE);
            return hours + ":" + mins;

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public int getListSize() {
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
