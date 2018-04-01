package com.mike.scorequery.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import com.mike.scorequery.R;
import com.mike.scorequery.utils.LogTools;
import com.mike.scorequery.widget.InfoAlertDialog;
import com.mike.scorequery.widget.LoadingDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.xutils.x;

import pub.devrel.easypermissions.EasyPermissions;


/**
 * Created by Mike on 2017/3/17.
 */
public class BaseActivity extends AppCompatActivity {

    protected LogTools logTools;
    private LoadingDialog loadingDialog;
    private InfoAlertDialog infoAlertDialog;
    protected ProgressDialog pd;
    private static final String TAG = "BaseActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
        logTools = new LogTools(this.getClass().getSimpleName());
        EventBus.getDefault().register(this);
        x.view().inject(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }


    /**
     * 退出应用
     *
     * @param eixtAppEvent
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEixtAppEvent(EixtAppEvent eixtAppEvent) {
        finish();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public class EixtAppEvent {

    }



    /**
     * @param enable   false 显示，true 隐藏
     */
    public void setFullScreen(boolean enable) {
        // TODO Auto-generated method stub
        WindowManager.LayoutParams p = getWindow().getAttributes();
        if (enable) {
            p.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;//|=：或等于，取其一

        } else {
            p.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);//&=：与等于，取其二同时满足，     ~ ： 取反

        }
        getWindow().setAttributes(p);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

    /**
     * 默认加载弹窗
     */
    public void showLoadingDialog() {
        showLoadingDialog(R.string.loading);
    }

    /**
     * 带标题的
     *
     * @param text
     */
    public void showLoadingDialog(String text) {
        showLoadingDialog(text, false);
    }

    /**
     * 带标题的
     *
     * @param textId
     */
    public void showLoadingDialog(int textId) {
        showLoadingDialog(getResources().getString(textId));
    }

    /**
     * @param text
     * @param isCancelable 是否可取消的
     */
    public void showLoadingDialog(String text, boolean isCancelable) {
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(this);
        }
        if (!loadingDialog.isShowing()) {
            loadingDialog.show();
        }
        loadingDialog.setText(TextUtils.isEmpty(text) || TextUtils.isEmpty(text.trim()) ?
                getResources().getString(R.string.loading) : text);
        // loadingDialog.setCancelable(isCancelable);
        loadingDialog.setCanceledOnTouchOutside(isCancelable);
    }

    /**
     * @param textId
     * @param isCancelable 是否可取消的
     */
    public void showLoadingDialog(int textId, boolean isCancelable) {
        showLoadingDialog(getResources().getString(textId), isCancelable);
    }

    public void closeLoadingDialog() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }

    /**
     * 弹提示窗
     */
    public void showInfoAlertDialog(InfoAlertDialog.Adapter adapter){


        if (infoAlertDialog == null) {
            infoAlertDialog = new InfoAlertDialog(this);
        }
        if (!infoAlertDialog.isShowing()) {
            infoAlertDialog.show();
        }
        infoAlertDialog.setAdapter(adapter);
        infoAlertDialog.setCancelable(false);
    }


    public void closeInfoAlertDialog() {
        if (infoAlertDialog != null && infoAlertDialog.isShowing()) {
            infoAlertDialog.dismiss();
        }
    }

    // 判断是否有可用的网络
    protected boolean checkNetworkState() {
        boolean flag = false;
        // 得到网络连接信息
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        // 去进行判断网络是否连接
        if (manager.getActiveNetworkInfo() != null) {
            flag = manager.getActiveNetworkInfo().isAvailable();
        }
        if (!flag) {
            View view = LayoutInflater.from(BaseActivity.this).inflate(
                    R.layout.mydialog, null);
            new AlertDialog.Builder(BaseActivity.this)
                    .setView(view)
                    .setPositiveButton("设置",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0,
                                                    int arg1) {
                                    Intent intent = new Intent(
                                            "android.settings.WIRELESS_SETTINGS");
                                    startActivity(intent);
                                }
                            }).setNegativeButton("取消", null).show();
        } else {
            pd = ProgressDialog.show(BaseActivity.this, null, "正在查询......");
        }

        return flag;
    }


}
