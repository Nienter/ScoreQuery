package com.mike.scorequery.utils;

import android.content.Context;
import android.graphics.Bitmap;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Create by：ml_bright on 2015/8/28 14:14
 * Email: 2504509903@qq.com
 */
public class FileUtil {

    public static void saveBitmap2file(Bitmap bmp, String path, OnDownloadBitmapListener listener) {
        File file = new File(path);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                listener.onResult(false, "创建图片文件失败");
                return;
            }
        }

        Bitmap.CompressFormat format = Bitmap.CompressFormat.JPEG;
        int quality = 100;
        OutputStream stream = null;
        try {
            stream = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        boolean result = bmp.compress(format, quality, stream);
        if (result) {
            listener.onResult(true, file.getAbsolutePath());
        }
        else {
            listener.onResult(false, "下载图片失败");
        }
    }


    /**
     * 保存bitmap到系统图库
     */
    public static void saveImageToFile(Context context, String targetFilePath, Bitmap bmp, OnDownloadBitmapListener listener) {
        // 首先保存图片
        File file = new File(targetFilePath);

        if (file.exists()) {
            callbackSaveImage(context, listener, file);
            return;
        }

        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            listener.onResult(false, null);
            return;
        } catch (IOException e) {
            e.printStackTrace();
            listener.onResult(false, null);
            return;
        }
        callbackSaveImage(context, listener, file);
    }


    private static void callbackSaveImage(Context context, OnDownloadBitmapListener listener, File file) {
        // 最后通知图库更新
        //        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file)));
        listener.onResult(true, file.getAbsolutePath());
    }


    public interface OnDownloadBitmapListener {
        void onResult(boolean success, String savePath);
    }
}
