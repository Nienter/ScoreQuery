package com.mike.scorequery.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

import com.mike.scorequery.R;


/**
 * Created by Mike on 2016/10/21.
 */
public class LoadingDialog extends Dialog {
    private TextView textView;
    public LoadingDialog(Context context) {
        super(context, R.style.DoingDialogTheme);
    }

    public LoadingDialog(Context context, int themeResId) {
        super(context, themeResId);
    }
    protected LoadingDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_loading);
        textView = (TextView) findViewById(R.id.loading_tv_text);
    }
    public void setText(String text){
        textView.setText(text);
    }
}
