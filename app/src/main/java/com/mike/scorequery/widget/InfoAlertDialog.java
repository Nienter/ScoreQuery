package com.mike.scorequery.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.mike.scorequery.R;


/**
 * 信息提示窗
 * Created by mike on 2016/11/8.
 */
public class InfoAlertDialog {
    private Context context;
    private AlertDialog alertDialog;
    private LinearLayout mLl_Content;
    private LayoutInflater layoutInflater;

    public Adapter getAdapter() {
        return adapter;
    }

    public void setAdapter(Adapter adapter) {
        this.adapter = adapter;
        mLl_Content.removeAllViews();
        mLl_Content.addView(adapter.getContentView(layoutInflater,this));
        adapter.initContentData();
    }

    private Adapter adapter;

    public InfoAlertDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        alertDialog = builder.create();
        layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.alertdialog_public, null);
        mLl_Content = (LinearLayout) view.findViewById(R.id.alert_dialog_ll);
        alertDialog.setView(view);
    }

    public void show() {
        alertDialog.show();
        //alertDialog.getWindow().setContentView(mLl_Content);
    }

    public void dismiss() {
        alertDialog.dismiss();
    }

    public boolean isShowing() {
        return alertDialog.isShowing();
    }

    public void setCancelable(boolean cancelable) {
        alertDialog.setCancelable(cancelable);
    }

    public interface Adapter {
        View getContentView(LayoutInflater layoutInflater, InfoAlertDialog infoAlertDialog);

        void initContentData();
    }

    public void setGravity(int gravity) {
        alertDialog.getWindow().setGravity(gravity);
    }

    public void setLayout(int width, int height) {
        alertDialog.getWindow().setLayout(width, height);
    }
    public void setContentView(View view){
        alertDialog.getWindow().setContentView(view);
    }

}
