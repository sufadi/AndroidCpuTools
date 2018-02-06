package com.fadisu.cpurun.util;

import android.content.Context;
import android.widget.Toast;

public class CustomToast {

    private static Toast mToast;

    public static void showToast(Context mContext, String text, int duration) {
        if (mToast != null) {
            mToast.setText(text);
        } else {
            mToast = Toast.makeText(mContext, text, Toast.LENGTH_SHORT);
        }
        mToast.show();
    }

    public static void showToast(Context mContext, int resId, int duration) {
        showToast(mContext, mContext.getResources().getString(resId), duration);
    }
}
