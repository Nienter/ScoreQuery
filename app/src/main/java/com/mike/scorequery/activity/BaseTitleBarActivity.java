package com.mike.scorequery.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mike.scorequery.R;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;


/**
 * 带标题栏的页面,简单自定义
 * Created by mike on 2016/10/27.
 */
public abstract class BaseTitleBarActivity extends BaseActivity implements View.OnClickListener {

    protected TextView mTv_Title;
    protected TextView mTv_Right;
    protected ImageView mIv_left;
    private FrameLayout mFl_Content;
    protected RelativeLayout mRl_TitleBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_titlebar);
        mTv_Title= (TextView) findViewById(R.id.titlebar_tv_title);
        mRl_TitleBar= (RelativeLayout) findViewById(R.id.titlebar_rl_titlebar);
        mIv_left= (ImageView) findViewById(R.id.titlebar_iv_left);
        mTv_Right= (TextView) findViewById(R.id.titlebar_tv_right);
        mFl_Content= (FrameLayout) findViewById(R.id.titlebar_fl_content);
        mIv_left.setOnClickListener(this);
        mTv_Right.setOnClickListener(this);
        mFl_Content.addView(getContentView(LayoutInflater.from(this)));
    }
    public abstract View getContentView(LayoutInflater layoutInflater);

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.titlebar_iv_left:
                finish();
                break;
        }
    }


}
