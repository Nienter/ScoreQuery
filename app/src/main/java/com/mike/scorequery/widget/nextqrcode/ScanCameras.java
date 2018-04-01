package com.mike.scorequery.widget.nextqrcode;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;

import com.github.yoojia.qrcode.camera.Cameras;
import com.mike.scorequery.utils.LogTools;

import java.io.ByteArrayOutputStream;

/**
 * 重写截图,截到任何位置的位图
 * Created by mike on 2016/10/25.
 */
public class ScanCameras extends Cameras {

    public static Bitmap previewCapture(Camera camera, byte[] data) {
        LogTools logTools = new LogTools("ScanCameras");
        final Camera.Parameters parameters = camera.getParameters();
        final int width = parameters.getPreviewSize().width;
        final int height = parameters.getPreviewSize().height;
        logTools.d(width);
        logTools.d(height);
        final YuvImage yuv = new YuvImage(data, parameters.getPreviewFormat(), width, height, null);
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        yuv.compressToJpeg(new Rect(0, 0, width, height), 100, out);// Best
        final byte[] bytes = out.toByteArray();
        final Bitmap src = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        final Matrix matrix = new Matrix();
        matrix.setRotate(90);
        final int originWidth = src.getWidth();
        final int originHeight = src.getHeight();
        logTools.d(originWidth);
        logTools.d(originHeight);
        final int targetWH = originWidth > originHeight ? originHeight : originWidth;
        final int offsetX = originWidth > originHeight ? (originWidth - originHeight) : 0;
        logTools.d(offsetX);
        final int offsetY = originWidth > originHeight ? 0 : (originHeight - originWidth);
        logTools.d(offsetY);
        return Bitmap.createBitmap(src, 0, 0, targetWH, targetWH, matrix, true);
    }

    public static Bitmap previewCapture( Camera.Parameters parameters, byte[] data, int captureWidth, int captureHeight, int captureOffsetX, int captureOffsetY) {
        LogTools logTools = new LogTools("ScanCameras");
        int width = parameters.getPreviewSize().width;
        int height = parameters.getPreviewSize().height;
        logTools.d(width);
        logTools.d(height);
        YuvImage yuv = new YuvImage(data, parameters.getPreviewFormat(), width, height, null);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        yuv.compressToJpeg(new Rect(0, 0, width, height), 100, out);// Best
        byte[] bytes = out.toByteArray();
        Bitmap src = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        Matrix matrix = new Matrix();
        matrix.setRotate(90);
        logTools.d(captureWidth);
        logTools.d(captureHeight);
        logTools.d(captureOffsetX);
        logTools.d(captureOffsetY);
        return Bitmap.createBitmap(src, captureOffsetX, captureOffsetY, captureWidth, captureHeight, matrix, true);
    }
}
