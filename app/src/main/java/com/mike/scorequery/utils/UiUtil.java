package com.mike.scorequery.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.bumptech.glide.BitmapTypeRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.mike.scorequery.AppContext;
import com.mike.scorequery.R;

import java.io.File;

/**
 * <根据图片URL显示ImageView工具类>
 *
 * @author chenml@cncn.com
 * @data: 2015/11/18 15:11
 * @version: V1.0
 */
public class UiUtil {

    public static final int DEFAULT_BG_LOADING = R.mipmap.ic_launcher;
    public static final int DEFAULT_BG_ERROR = R.mipmap.ic_launcher;

    public static void setImage(Context context, ImageView imageView, String imageUrl) {
        setImage(context, imageView, imageUrl, DEFAULT_BG_LOADING, DEFAULT_BG_ERROR);
    }

    /**
     * @param context    将Activity/Fragment作为with()参数的好处是：图片加载会和Activity/Fragment的生命周期保持一致，
     *                   比如 Paused状态在暂停加载，在Resumed的时候又自动重新加载。
     *                   所以我建议传参的时候传递Activity 和 Fragment给Glide，而不是Context。
     * @param imageView
     * @param imageUrl
     * @param loadingRes
     * @param errorRes
     */
    public static void setImage(Context context, ImageView imageView, String imageUrl, int loadingRes, int errorRes) {
        Glide.with(context).load(imageUrl).placeholder(loadingRes).diskCacheStrategy(DiskCacheStrategy.ALL).error(errorRes).into(imageView);
    }

    public static void setImage(Context context, ImageView imageView, File file, int loadingRes, int errorRes) {
        if(loadingRes != 0 && errorRes != 0) {
            Glide.with(context).load(file).placeholder(loadingRes).diskCacheStrategy(DiskCacheStrategy.ALL).error(errorRes).into(imageView);
        } else {
            Glide.with(context).load(file).diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);
        }
    }

    public static void downloadFile(String imageUrl, final String targetFilePath, final FileUtil.OnDownloadBitmapListener listener) {
        BitmapTypeRequest<String> bitmapTypeRequest = Glide.with(AppContext.getInstance().getContext()).load(imageUrl).asBitmap();
        bitmapTypeRequest.into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                if(resource == null) {
                    return;
                }
                FileUtil.saveImageToFile(AppContext.getInstance().getContext(), targetFilePath, resource, listener);
            }
        });
    }

}
