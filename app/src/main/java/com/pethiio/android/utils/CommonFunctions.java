package com.pethiio.android.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.navigation.NavController;

import com.pethiio.android.R;

public class CommonFunctions {

    public static int getScreenHeight(Context context) {

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        assert wm != null;
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        return metrics.heightPixels;

    }

    public static void checkLogin(@Nullable Integer errorCode, NavController navController) {
        if (errorCode!=null && errorCode == 403)
            navController.navigate(R.id.navigation_pet_add);
    }




}
