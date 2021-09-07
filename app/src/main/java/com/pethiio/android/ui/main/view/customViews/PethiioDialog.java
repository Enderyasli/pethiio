package com.pethiio.android.ui.main.view.customViews;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.airbnb.lottie.LottieAnimationView;
import com.pethiio.android.R;

public class PethiioDialog extends AlertDialog.Builder {

    private AlertDialog mAlertDialog;
    private String message;
    private String title;
    public Button positiveButton;
    public TextView negativeButton;
    private final Context context;
    private boolean isSuccess;
    private boolean isQuestion = false;
    private String positiveBtnTitle;
    private String negativeBtnTitle;


    public PethiioDialog(@NonNull Context context, boolean isSuccess, String message) {
        super(context);
        this.context = context;
        this.isSuccess = isSuccess;
        this.message = message;
        setView();
    }

    public PethiioDialog(@NonNull Context context, boolean isQuestion, String message, @Nullable String positiveBtnTitle, @Nullable String negativeBtnTitle) {
        super(context);
        this.context = context;
        this.isQuestion = isQuestion;
        this.message = message;
        this.positiveBtnTitle = positiveBtnTitle;
        this.negativeBtnTitle = negativeBtnTitle;
        setView();
    }


    public PethiioDialog(@NonNull Context context, boolean isSuccess, String title, String message) {
        super(context);
        this.context = context;
        this.isSuccess = isSuccess;
        this.message = message;
        this.title = title;
        setView();
    }


    public PethiioDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
    }

    public Button getPositiveButton() {
        if (positiveButton == null && mAlertDialog != null)
            positiveButton = mAlertDialog.findViewById(R.id.posButton);
        if (positiveButton != null)
            positiveButton.setVisibility(View.VISIBLE);
        return positiveButton;
    }

    public TextView getNegativeButton() {
        if (negativeButton == null && mAlertDialog != null)
            negativeButton = mAlertDialog.findViewById(R.id.negButton);
        if (negativeButton != null)
            negativeButton.setVisibility(View.VISIBLE);
        return negativeButton;
    }


    private AlertDialog setView() {
        try {
            AlertDialog.Builder builder;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Light_Dialog_Alert);
            } else {
                builder = new AlertDialog.Builder(context);
            }
            builder.setView(R.layout.pethiio_dialog);
            mAlertDialog = builder.create();
            if (mAlertDialog.getWindow() != null && mAlertDialog.getWindow().getAttributes() != null)
                mAlertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
            mAlertDialog.setCancelable(false);
            mAlertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            mAlertDialog.show();

            TextView titleTv = mAlertDialog.findViewById(R.id.title);
            positiveButton = mAlertDialog.findViewById(R.id.posButton);
            negativeButton = mAlertDialog.findViewById(R.id.negButton);
            if (message != null) {
                titleTv.setText(message);
            }

            if (isQuestion) {
                if (!TextUtils.isEmpty(positiveBtnTitle))
                    positiveButton.setText(positiveBtnTitle);
                if (!TextUtils.isEmpty(negativeBtnTitle))
                    negativeButton.setText(negativeBtnTitle);

                else negativeButton.setOnClickListener(v -> mAlertDialog.dismiss());
                positiveButton.setVisibility(View.VISIBLE);
                negativeButton.setVisibility(View.VISIBLE);
            }




            return mAlertDialog;
        } catch (Exception e) {
            return null;
        }

    }


    public void dissmiss() {
        mAlertDialog.dismiss();
    }

    public boolean isShowing() {
        return mAlertDialog.isShowing();
    }

}
