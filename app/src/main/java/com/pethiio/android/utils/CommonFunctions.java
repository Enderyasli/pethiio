package com.pethiio.android.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.navigation.NavController;

import com.pethiio.android.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

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
        if (errorCode != null && errorCode == 403)
            navController.navigate(R.id.navigation_welcome);
    }


    public static String saveImage(String fileDir, String savePath, Bitmap image) {
        Log.e("SPLASH", "FİLE SAVE METHOD");
        String savedImagePath = null;
        String imageFileName = savePath;
        File storageDir = new File(fileDir);
        boolean success = true;
        if (!storageDir.exists()) {
            success = storageDir.mkdirs();
        } else {
        }
        if (success) {
            Log.e("SPLASH", "FİLE EXIST");
            File imageFile = new File(storageDir, imageFileName);
            savedImagePath = imageFile.getAbsolutePath();
            try {
                Log.e("SPLASH", "FİLE SAVE SUCCESS");
                OutputStream fOut = new FileOutputStream(imageFile);
                image.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                fOut.close();

            } catch (Exception e) {
                Log.e("SPLASH", "FİLE SAVE ERROR");
                e.printStackTrace();
            }

            // Add the image to the system gallery
        } else Log.e("SPLASH", "FİLE NOT EXIST");
        return savedImagePath;
    }


}
