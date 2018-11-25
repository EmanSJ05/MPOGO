package com.emansj.mpogo.helper;

import android.app.Activity;
import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.LayoutRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatButton;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

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


    public static void showCustomDialog(final Context ctx, int layoutRes, final Runnable methodCloseToRun, final Runnable methodCancelToRun) {
        final Dialog dialog = new Dialog(ctx);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(layoutRes);
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        final Button btnClose = (AppCompatButton) dialog.findViewById(R.id.btnClose);
        final Button btnCancel = (AppCompatButton) dialog.findViewById(R.id.btnCancel);

        if (btnClose != null) {
            btnClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(ctx, ((AppCompatButton) v).getText().toString() + btnClose.getText().toString() + " Clicked", Toast.LENGTH_SHORT).show();
                    if (methodCloseToRun != null) {
                        methodCloseToRun.run();
                    }
                    dialog.dismiss();
                }
            });
        }

        if (btnCancel != null) {
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(ctx, ((AppCompatButton) v).getText().toString() + btnCancel.getText().toString() + " Clicked", Toast.LENGTH_SHORT).show();
                    if (methodCancelToRun != null) {
                        methodCancelToRun.run();
                    }
                    dialog.dismiss();
                }
            });
        }

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    public static class TimeAgo {
        private static final int SECOND_MILLIS = 1000;
        private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
        private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
        private static final int DAY_MILLIS = 24 * HOUR_MILLIS;

        public static String getTimeAgo(long time) {
            if (time < 1000000000000L) {
                time *= 1000;
            }

            long now = System.currentTimeMillis();
            if (time > now || time <= 0) {
                return null;
            }


            final long diff = now - time;
            if (diff < MINUTE_MILLIS) {
                return "Baru saja";
            } else if (diff < 2 * MINUTE_MILLIS) {
                return "semenit lalu";
            } else if (diff < 50 * MINUTE_MILLIS) {
                return diff / MINUTE_MILLIS + " menit lalu";
            } else if (diff < 90 * MINUTE_MILLIS) {
                return "sejam lalu";
            } else if (diff < 24 * HOUR_MILLIS) {
                return diff / HOUR_MILLIS + " jam lalu";
            } else if (diff < 48 * HOUR_MILLIS) {
                return "kemarin";
            } else {
                return diff / DAY_MILLIS + " hari lalu";
            }
        }
    }
}
