package com.mike.scorequery.widget.nextqrcode;

import android.graphics.Bitmap;
import android.util.Log;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.GlobalHistogramBinarizer;
import com.google.zxing.common.HybridBinarizer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.Map;

/**
 * Created by mike on 2016/12/2.
 */
public class StudentQRCodeDecoder {
    public static final String TAG = "StudentQRCodeDecoder";

    private final MultiFormatReader mFormatReader = new MultiFormatReader();

    private StudentQRCodeDecoder(Builder builder) {
        final Map<DecodeHintType, Object> hints = new EnumMap<>(DecodeHintType.class);
        final Collection<BarcodeFormat> formats = new ArrayList<>();
        formats.add(BarcodeFormat.UPC_A);   // UPC标准码(通用商品)
        formats.add(BarcodeFormat.UPC_E);   // UPC缩短码(商品短码)
        formats.add(BarcodeFormat.EAN_13);
        formats.add(BarcodeFormat.EAN_8);
        //formats.add(BarcodeFormat.RSS14);
        formats.add(BarcodeFormat.CODE_39);
        formats.add(BarcodeFormat.CODE_93);
        formats.add(BarcodeFormat.CODE_128);
        formats.add(BarcodeFormat.ITF);
        formats.add(BarcodeFormat.QR_CODE);
        formats.add(BarcodeFormat.DATA_MATRIX);//也属于一种二维码

        hints.put(DecodeHintType.POSSIBLE_FORMATS, formats);
        hints.put(DecodeHintType.CHARACTER_SET, builder.charset);
        mFormatReader.setHints(hints);
    }

    public String decode(Bitmap qrcodeBitmap) {
        final int width = qrcodeBitmap.getWidth();
        final int height = qrcodeBitmap.getHeight();
        final int[] pixels = new int[width * height];
        qrcodeBitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        final RGBLuminanceSource source = new RGBLuminanceSource(width, height, pixels);
        final BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        try {
            final Result rawResult = mFormatReader.decodeWithState(bitmap);
            return rawResult.getText();
        } catch (NotFoundException e) {
            Log.d(TAG, "Fail to decode bitmap to QRCode content", e);
            return null;
        }finally {
            mFormatReader.reset();
        }
    }
    public String decode1(Bitmap qrcodeBitmap) {
        final int width = qrcodeBitmap.getWidth();
        final int height = qrcodeBitmap.getHeight();
        final int[] pixels = new int[width * height];
        qrcodeBitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        final RGBLuminanceSource source = new RGBLuminanceSource(width, height, pixels);
        final BinaryBitmap bitmap = new BinaryBitmap(new GlobalHistogramBinarizer(source));
        try {
            final Result rawResult = mFormatReader.decodeWithState(bitmap);
            return rawResult.getText();
        } catch (NotFoundException e) {
            Log.d(TAG, "Fail to decode bitmap to QRCode content1", e);
            return null;
        }finally {
            mFormatReader.reset();
        }
    }

    public final static class Builder {

        private String charset = "UTF-8";

        public Builder charset(String charset) {
            this.charset = charset;
            return this;
        }

        public StudentQRCodeDecoder build(){
            return new StudentQRCodeDecoder(this);
        }
    }
}
