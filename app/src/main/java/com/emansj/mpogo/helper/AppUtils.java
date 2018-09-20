package com.emansj.mpogo.helper;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;

import com.emansj.mpogo.R;

public class AppUtils {

//    // The public static function which can be called from other classes
//    public static void darkenStatusBar(Activity activity, int color) {
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//
//            activity.getWindow().setStatusBarColor(
//                    darkenColor(
//                            ContextCompat.getColor(activity, color)));
//        }
//
//    }
//
//
//    // Code to darken the color supplied (mostly color of toolbar)
//    private static int darkenColor(int color) {
//        float[] hsv = new float[3];
//        Color.colorToHSV(color, hsv);
//        hsv[2] *= 0.8f;
//        return Color.HSVToColor(hsv);
//    }

    public static SpannableString shortCurrency(Context ctx, String strValue){
        String tmp;
        int len;
        StringBuilder stb;
        boolean colored = false;
        ForegroundColorSpan fcsColor = new ForegroundColorSpan(Integer.valueOf(R.color.grey_5));

        tmp = strValue;
        len = tmp.length();

        if (len <= 0) {
            tmp = "";

        }else if (len <= 3) {
            stb = new StringBuilder(tmp);
            //stb = stb.insert(len-1, ".");
            tmp = stb.toString() + " X ";
            fcsColor = new ForegroundColorSpan(ContextCompat.getColor(ctx, R.color.transparent));

        }else if (len <= 6) {
            tmp = tmp.substring(0, len - 2);
            stb = new StringBuilder(tmp);
            stb = stb.insert(tmp.length()-1, ".");
            tmp = stb.toString() + " Rb";
            fcsColor = new ForegroundColorSpan(ContextCompat.getColor(ctx, R.color.purple_500));

        }else if (len <= 9) {
            tmp = tmp.substring(0, len - 5);
            stb = new StringBuilder(tmp);
            stb = stb.insert(tmp.length()-1, ".");
            tmp = stb.toString() + " Jt";
            fcsColor = new ForegroundColorSpan(ContextCompat.getColor(ctx, R.color.green_500));

        }else if (len <= 12) {
            tmp = tmp.substring(0, len - 8);
            stb = new StringBuilder(tmp);
            stb = stb.insert(tmp.length()-1, ".");
            tmp = stb.toString() + " M ";
            fcsColor = new ForegroundColorSpan(ContextCompat.getColor(ctx, R.color.blue_500));

        }else if (len <= 15) {
            tmp = tmp.substring(0, len - 11);
            stb = new StringBuilder(tmp);
            stb = stb.insert(tmp.length()-1, ".");
            tmp = stb.toString() + " T ";
            fcsColor = new ForegroundColorSpan(ContextCompat.getColor(ctx, R.color.red_500));

        }

//        final StyleSpan ssNormal = new StyleSpan(Typeface.NORMAL);
//        final StyleSpan ssBold = new StyleSpan(Typeface.BOLD);

        SpannableString ss = new SpannableString(tmp);
//        ss.setSpan(ssNormal, 0, tmp.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        ss.setSpan(ssBold, 0, tmp.length()-2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(fcsColor, tmp.length()-2, tmp.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);


        return ss;
    }

}
