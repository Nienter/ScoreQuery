package com.mike.scorequery.widget.nextqrcode;

import android.content.Context;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.widget.Toast;

import com.github.yoojia.qrcode.camera.CameraPreviewView;
import com.github.yoojia.qrcode.camera.Cameras;
import com.github.yoojia.qrcode.camera.CaptureCallback;
import com.github.yoojia.qrcode.camera.DelayedFocusLooper;
import com.mike.scorequery.utils.LogTools;

import java.util.List;

/**
 * Created by mike on 2016/10/25.
 */
public class ScoreLiveCameraView extends CameraPreviewView {
    private static final String TAG = ScoreLiveCameraView.class.getSimpleName();
    private LogTools logTools=new LogTools(TAG);
    public int getCaptureHeight() {
        return captureHeight;
    }

    public void setCaptureHeight(int captureHeight) {
        this.captureHeight = captureHeight;
    }

    public int getCaptureOffsetX() {
        return captureOffsetX;
    }

    public void setCaptureOffsetX(int captureOffsetX) {
        this.captureOffsetX = captureOffsetX;
    }

    public int getCaptureOffsetY() {
        return captureOffsetY;
    }

    public void setCaptureOffsetY(int captureOffsetY) {
        this.captureOffsetY = captureOffsetY;
    }

    public int getCaptureWidth() {
        return captureWidth;
    }

    public void setCaptureWidth(int captureWidth) {
        this.captureWidth = captureWidth;
    }

    private int captureWidth;
    private int captureHeight;
    private int captureOffsetX;
    private int captureOffsetY;
    public ScoreLiveCameraView(Context context) {
        super(context);
    }

    public ScoreLiveCameraView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScoreLiveCameraView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    private Camera mCamera;

    //############################//
    //修改源码,增加的参数
    private Camera.Parameters mParameters;
    //###############################//
    private CaptureCallback mCaptureCallback;

    private final Camera.PreviewCallback mPreviewCallback = new Camera.PreviewCallback() {
        @Override
        public void onPreviewFrame(byte[] data, Camera camera) {
            Log.i(TAG, "-> Got preview frame data");
            mCaptureCallback.onCaptured(ScanCameras.previewCapture(mParameters,data,captureWidth,captureHeight,captureOffsetX,captureOffsetY));
        }
    };

    private final DelayedFocusLooper mFocusLooper = new DelayedFocusLooper() {

        private final Camera.AutoFocusCallback mHandler = new Camera.AutoFocusCallback() {
            @Override public void onAutoFocus(boolean success, Camera camera) {
                if (success){
                    camera.setOneShotPreviewCallback(mPreviewCallback);
                }else{
                    Log.w(TAG, "-> Request focus, but fail !");
                }
            }
        };

        @Override public void callAutoFocus() {
            mCamera.autoFocus(mHandler);
        }
    };

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mCamera = Cameras.openBackDefault();
        if (mCamera != null){
            setCamera(mCamera);

            //**************************************//
            //处理图像预览变形的代码
            Camera.Parameters parameters = mCamera.getParameters();//获取camera的parameter实例
            List<Camera.Size> sizeList = parameters.getSupportedPreviewSizes();//获取所有支持的camera尺寸
            Camera.Size optionSize = getOptimalPreviewSize(sizeList,getHeight(),getWidth());//获取一个最为适配的camera.size
            parameters.setPreviewSize(optionSize.width,optionSize.height);//把camera.size赋值到parameters
            mCamera.setParameters(parameters);//把parameters设置给camera
            //**************************************//
        }
        super.surfaceCreated(holder);
    }

    private Camera.Size getOptimalPreviewSize(List<Camera.Size> sizes, int w, int h) {
        final double ASPECT_TOLERANCE = 0.1;
        double targetRatio = (double) w / h;
        if (sizes == null) return null;

        Camera.Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;

        int targetHeight = h;

        // Try to find an size match aspect ratio and size
        for (Camera.Size size : sizes) {
            logTools.d(size.width);
            logTools.d(size.height);
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue;
            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }

        // Cannot find the one match the aspect ratio, ignore the requirement
        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Camera.Size size : sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }
        return optimalSize;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        super.surfaceDestroyed(holder);
        mFocusLooper.stop();
        if (mCamera != null){
            mCamera.release();
        }
    }

    /**
     * 启动自动对焦拍摄
     * @param delay 每次聚焦的延时时间，单位：毫秒
     * @param captureCallback 聚焦后的拍摄图片回调接口
     */
    public void startAutoCapture(int delay, CaptureCallback captureCallback) {
        mCaptureCallback = captureCallback;
        if (mCamera != null){
            mParameters=mCamera.getParameters();
            mFocusLooper.start(delay);
        }else{
            Toast.makeText(getContext(), "OPEN CAMERA FAIL", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 停止自动对焦拍摄
     */
    public void stopAutoCapture(){
        mFocusLooper.stop();
    }

    /**
     * @return 返回当前设备是否支持自动对焦拍摄功能
     */
    public boolean isAutoCaptureSupported(){
        return Cameras.isAutoFocusSupported(mCamera);
    }
}
