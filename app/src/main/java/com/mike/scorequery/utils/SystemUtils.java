package com.mike.scorequery.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.lang.reflect.Field;

/**
 * Created by Mike on 2016/10/20.
 */
public class SystemUtils {

    private static LogTools logTools=new LogTools("SystemUtils");

    /**
     * Check if network is available.
     *
     * @param context
     * @return
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
        if (mNetworkInfo != null) {
            return mNetworkInfo.isAvailable();
        }
        return false;
    }


    /**
     * 隐藏输入法
     *
     * @param editText
     */
    public static void hideSoftInput(EditText editText, Context context) {
        InputMethodManager imm = (InputMethodManager) context.
                getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }
    /**
     * 打开输入法
     * (这个方法可以实现输入法在窗口上切换显示，如果输入法
     * 在窗口上已经显示，则隐藏，如果隐藏，则显示输入法到窗口上)
     */
    public static void toggleSoftInput(Context context) {
        InputMethodManager imm = (InputMethodManager) context.
                getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }
    /**
     * 打开输入法
     *(提供当前操作的标记，SHOW_FORCED表示强制显示)
     */
    public static void showSoftInput(View view, Context context) {
        InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }

    //获取输入法打开的状态

    public static boolean inputIsOpen(Context context){
        InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        return imm.isActive();
    }

    /**
     * 获取状态栏高度
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object obj = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = Integer.parseInt(field.get(obj).toString());
            logTools.d(field.get(obj).toString());
            return context.getResources().getDimensionPixelSize(x);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

}
