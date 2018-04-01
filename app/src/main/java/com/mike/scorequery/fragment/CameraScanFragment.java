package com.mike.scorequery.fragment;

import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.yoojia.qrcode.camera.CameraPreviewView;
import com.github.yoojia.qrcode.camera.CaptureCallback;
import com.mike.scorequery.R;
import com.mike.scorequery.activity.BaseActivity;
import com.mike.scorequery.widget.nextqrcode.ScoreLiveCameraView;

/**
 * 相机扫描
 * Created by mike on 2016/12/10.
 */
public class CameraScanFragment extends BaseFragment implements View.OnClickListener {


    private TextView mTv_Title;
    private TextView mTv_Right;
    private ImageView mIv_left;

    private ScoreLiveCameraView mPayLiveCameraView;
    private ImageView scanLine;
    private RelativeLayout scanCropView;
    private FrameLayout scanContainer;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ((BaseActivity)getActivity()).setFullScreen(true);
        View view = inflater.inflate(R.layout.fragment_camera_scan, null);
        mTv_Title= (TextView) view.findViewById(R.id.titlebar_tv_title);
        mTv_Right= (TextView) view.findViewById(R.id.titlebar_tv_right);
        mIv_left= (ImageView) view.findViewById(R.id.titlebar_iv_left);

        if("query".equals(getActivity().getIntent().getStringExtra("scanType"))){
            mTv_Title.setText("查询药品");
        }else if("add".equals(getActivity().getIntent().getStringExtra("scanType"))){
            mTv_Title.setText("添加药品");
        }
        mIv_left.setOnClickListener(this);
      //  mTv_Right.setOnClickListener(this);
//        mTv_Right.setVisibility(View.VISIBLE);
//        Drawable drawable= getResources()
//                .getDrawable(R.mipmap.icon_small_scan);
//        //这一步必须要做,否则不会显示.
//        drawable.setBounds(0, 0, drawable.getMinimumWidth(),
//                drawable.getMinimumHeight());
//        mTv_Right.setCompoundDrawables(drawable,null,null,null);
        mPayLiveCameraView = (ScoreLiveCameraView) view.findViewById(R.id.fragment_scan_plcv);
        scanCropView = (RelativeLayout) view.findViewById(R.id.fragment_scan_rl_scan);
        scanContainer = (FrameLayout) view.findViewById(R.id.fragment_scan_fl_container);
        scanLine = (ImageView) view.findViewById(R.id.fragment_scan_iv_scanline);
        //扫描线动画
        TranslateAnimation animation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT,
                0.9f);
        animation.setDuration(4500);
        animation.setRepeatCount(-1);
        animation.setRepeatMode(Animation.RESTART);
        scanLine.startAnimation(animation);
        mPayLiveCameraView.setPreviewReadyCallback(new CameraPreviewView.PreviewReadyCallback() {
            @Override
            public void onStarted(Camera camera) {
                initCrop(camera);
                mPayLiveCameraView.startAutoCapture(1000, (CaptureCallback) getActivity());
            }

            @Override
            public void onStopped() {
                mPayLiveCameraView.stopAutoCapture();
            }
        });

        return view;
    }

    /**
     * 初始化截图参数
     */
    private void initCrop(Camera camera) {
        //获取相机参数
        Camera.Parameters parameters = camera.getParameters();
        int cameraWidth = parameters.getPreviewSize().height;
        int cameraHeight = parameters.getPreviewSize().width;
        int[] location = new int[2];
        scanCropView.getLocationInWindow(location);
        int cropLeft = location[0];
        int cropTop = location[1];
        int cropWidth = scanCropView.getWidth();
        int cropHeight = scanCropView.getHeight();
        int containerWith = scanContainer.getWidth();
        int containerHeight = scanContainer.getHeight();
        int x = cropLeft * cameraWidth / containerWith;
        int y = cropTop * cameraHeight / containerHeight;
        int width = cropWidth * cameraWidth / containerWith;
        int height = cropHeight * cameraHeight / containerHeight;
        mPayLiveCameraView.setCaptureWidth(width);
        mPayLiveCameraView.setCaptureHeight(height);
        mPayLiveCameraView.setCaptureOffsetX(y);
        mPayLiveCameraView.setCaptureOffsetY(x);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.titlebar_iv_left:
                getActivity().finish();
                break;
//            case R.id.titlebar_tv_right:
//
//                break;
        }
    }

    public void setTitle(String title){
        mTv_Title.setText(title);
    }

}
