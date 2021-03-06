package com.jerry.bedpad.util;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;

/**
 * @author Jerry Tan
 * @version 1.0
 * @description 单位转换工具类
 * @date 2015年8月25日 上午9:37:42
 * @Company Buzzlysoft
 * @email thrforever@126.com
 * @remark
 */
public class DensityUtils {
    private DensityUtils() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * dp转px
     *
     * @param context
     * @return
     */
    public static int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }

    /**
     * sp转px
     *
     * @param context
     * @return
     */
    public static int sp2px(Context context, float spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spVal, context.getResources().getDisplayMetrics());
    }

    /**
     * px转dp
     *
     * @param context
     * @param pxVal
     * @return
     */
    public static float px2dp(Context context, float pxVal) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (pxVal / scale);
    }

    /**
     * px转sp
     *
     * @param pxVal
     * @return
     */
    public static float px2sp(Context context, float pxVal) {
        return (pxVal / context.getResources().getDisplayMetrics().scaledDensity);
    }

    /**
     * 获取屏幕分辨率的方法
     *
     * @param activity
     */
    public static void getDisplay(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        L.i("手机屏幕分辨率为：" + dm.widthPixels + "*" + dm.heightPixels);
        float xdpi = activity.getResources().getDisplayMetrics().xdpi;
        float ydpi = activity.getResources().getDisplayMetrics().ydpi;
        L.i("手机屏幕dpi为：x(" + xdpi + ")，y(" + ydpi + ")");

    }

    /**
     * 获取屏幕宽度
     *
     * @param activity
     * @return
     */
    public static int getScreenWidth(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    /**
     * 获取屏幕高度
     *
     * @param activity
     * @return
     */
    public static int getScreenHeight(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }


//    /**
//     * 获取屏幕亮度
//     *
//     * @param com.jerry.roundindicator.activity
//     * @return
//     */
//    public static int getScreenBrightness(Activity com.jerry.roundindicator.activity) {
//        int value = 0;
//        ContentResolver cr = com.jerry.roundindicator.activity.getContentResolver();
//        try {
//            value = Settings.System.getInt(cr, Settings.System.SCREEN_BRIGHTNESS);
//        } catch (Settings.SettingNotFoundException e) {
//            e.printStackTrace();
//        }
//        return value;
//    }
//
//    /**
//     * 调整屏幕亮度
//     */
//    public static void setBrightness(Context context, int value) {
//
//        WindowManager.LayoutParams params = com.jerry.roundindicator.activity.getWindow().getAttributes();
//        params.screenBrightness = value / 255f;
//        com.jerry.roundindicator.activity.getWindow().setAttributes(params);
//        Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, value);
//    }
}
