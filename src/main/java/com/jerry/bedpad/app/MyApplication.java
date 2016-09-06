package com.jerry.bedpad.app;

import android.app.Activity;
import android.app.Application;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jerry on 2016/6/23.
 */
public class MyApplication extends Application {


    private static List<Activity> activityList = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public MyApplication() {
        super();
    }

    /**
     * 添加一个activity到集合中
     *
     * @param activity
     */
    public static void add(Activity activity) {
        // 设置强制竖屏
        if (activity.getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        toggleHideyBar(activity);
        activityList.add(activity);
        x.Ext.init(activity.getApplication());
        x.view().inject(activity);
    }


    /**
     * 切换app是否显示和隐藏标题栏和菜单栏
     */
    public static void toggleHideyBar(Activity activity) {

        // The UI options currently enabled are represented by a bitfield.
        // getSystemUiVisibility() gives us that bitfield.
        int uiOptions = activity.getWindow().getDecorView().getSystemUiVisibility();
        int newUiOptions = uiOptions;
        boolean isImmersiveModeEnabled =
                ((uiOptions | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY) == uiOptions);
        if (isImmersiveModeEnabled) {
            Log.i("THR", "Turning immersive mode mode off. ");
        } else {
            Log.i("THR", "Turning immersive mode mode on.");
        }
        // Navigation bar hiding:  Backwards compatible to ICS.
        if (Build.VERSION.SDK_INT >= 14) {
            newUiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        }

        // Status bar hiding: Backwards compatible to Jellybean
        if (Build.VERSION.SDK_INT >= 16) {
            newUiOptions ^= View.SYSTEM_UI_FLAG_FULLSCREEN;
        }

        // Immersive mode: Backward compatible to KitKat.
        // Note that this flag doesn't do anything by itself, it only augments the behavior
        // of HIDE_NAVIGATION and FLAG_FULLSCREEN.  For the purposes of this sample
        // all three flags are being toggled together.
        // Note that there are two immersive mode UI flags, one of which is referred to as "sticky".
        // Sticky immersive mode differs in that it makes the navigation and status bars
        // semi-transparent, and the UI flag does not get cleared when the user interacts with
        // the screen.
        if (Build.VERSION.SDK_INT >= 18) {
            newUiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        }

        activity.getWindow().getDecorView().setSystemUiVisibility(newUiOptions);
    }


    /**
     * 对话框形式的窗口
     * @param activity
     */
    public static void makeActivity2Dialog(Activity activity) {
        WindowManager manager = activity.getWindowManager();
        Display display = manager.getDefaultDisplay(); // 为获取屏幕宽、高
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes(); // 获取对话框当前的参值
        lp.height = (int) (display.getHeight() * 0.55); // 高度设置为屏幕的1.0
        lp.width = (int) (display.getWidth() * 0.8); // 宽度设置为屏幕的0.8
        lp.alpha = 1.0f; // 设置本身透明度
        lp.dimAmount = 0.6f; // 设置黑暗度
        activity.getWindow().setAttributes(lp); // 设置生效
        activity.getWindow().setGravity(Gravity.CENTER); // 设置靠右对齐
    }

    /**
     * 退出应用
     */
    public static void clearAll() {
        for (Activity a : activityList) {
            a.finish();
        }
    }


}
