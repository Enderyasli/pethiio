package com.pethiio.android.ui.main.view.fragments;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.pethiio.android.R;
import com.pethiio.android.data.EventBus.FilterEvent;
import com.pethiio.android.utils.CommonFunctions;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;


public class FilterBottomSheet extends BottomSheetDialogFragment {

    FilterBottomSheet instance;

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((View) getView().getParent()).setBackgroundColor(Color.TRANSPARENT);




    }


    @Override
    public void onDestroy() {
        //unregister event bus
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.filter_bottom_sheet, container, false);
        init(root);
        return root;
    }


    public static FilterBottomSheet newInstance() {
        return new FilterBottomSheet();
    }

    private void init(View root) {

    }

    @Override
    public void setupDialog(@NonNull @NotNull Dialog dialog, int style) {
        super.setupDialog(dialog, style);
    }
}
