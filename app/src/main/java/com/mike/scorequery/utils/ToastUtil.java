package com.mike.scorequery.utils;

import android.content.Context;
import android.widget.Toast;

import com.mike.scorequery.AppContext;


/**
 * -----------------------------------------------------------
 * 版 权 ： BigTiger 版权所有 (c) 2015
 * 作 者 : BigTiger
 * 版 本 ： 1.0
 * 创建日期 ：2015/6/21 22:32
 * 描 述 ：简易Toast 保持只有一个实例，防止多次调用时长时间依次显示问题
 * <p>
 * -------------------------------------------------------------
 **/
public class ToastUtil {
    private static Toast sToast;

    public static void showToast(int msgResId) {
        showToast(AppContext.getInstance().getString(msgResId));
    }

    public static void showToast(String msg) {
        showToast(AppContext.getInstance().getContext(),msg, Toast.LENGTH_SHORT);
    }
    public static void showToast(Context context, String msg) {
        showToast(context,msg, Toast.LENGTH_SHORT);
    }

    public static void showToast(Context context, String msg, int duration ) {
        if(msg == null || msg.trim().length() == 0) {
            return;
        }
        if (sToast == null) {
            sToast = Toast.makeText(context, msg, duration);
            sToast.show();
        } else {
            sToast.setText(msg);
            sToast.setDuration(duration);
            sToast.show();
        }
    }

    public static void showLongToast( int msgResId) {
        showToast(AppContext.getInstance().getContext(),AppContext.getInstance().getString(msgResId), Toast.LENGTH_LONG);
    }

    public static void showLongToast( String msg) {
        showToast(AppContext.getInstance().getContext(),msg, Toast.LENGTH_LONG);
    }
    public static void cancelToast() {
        if (sToast != null) {
            sToast.cancel();
        }
    }
}
