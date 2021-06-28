package com.pethiio.android.ui.main.view.customViews;


import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.pethiio.android.R;
import com.pethiio.android.data.model.member.MemberListResponse;

import java.util.List;

public class MemberListSpinner extends BaseAdapter {
    Context context;
    LayoutInflater inflter;
    List<MemberListResponse> memberListResponse;

    public MemberListSpinner(Context applicationContext, List<MemberListResponse> memberListResponse) {
        this.context = applicationContext;
        this.memberListResponse = memberListResponse;


        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return memberListResponse.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.spinner_row_member_list, null);
        ImageView icon = (ImageView) view.findViewById(R.id.iv_profile);
        TextView names = (TextView) view.findViewById(R.id.name_tv);

        if (memberListResponse.get(i).getAvatar() != null)
            Glide.with(context)
                    .load(memberListResponse.get(i).getAvatar())
                    .apply(new RequestOptions().override(100, 100))
                    .into(icon);
        names.setText(memberListResponse.get(i).getName());
        return view;
    }
}