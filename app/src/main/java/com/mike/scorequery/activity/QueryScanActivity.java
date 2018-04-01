package com.mike.scorequery.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import com.github.yoojia.qrcode.camera.CaptureCallback;
import com.mike.scorequery.R;
import com.mike.scorequery.fragment.BaseFragment;
import com.mike.scorequery.fragment.CameraScanFragment;
import com.mike.scorequery.utils.StringUtils;
import com.mike.scorequery.utils.ToastUtil;
import com.mike.scorequery.widget.nextqrcode.StudentQRCodeDecoder;

import java.util.Random;

/**
 * Created by mike on 2017/4/9.
 */
public class QueryScanActivity extends BaseTitleBarActivity implements CaptureCallback {

    private static final int FOR_OK =1 ;
    private StudentQRCodeDecoder mDecoder;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CameraScanFragment cf = new CameraScanFragment();
        showFragment(cf);
        mDecoder=new StudentQRCodeDecoder.Builder().build();

    }

    @Override
    public View getContentView(LayoutInflater layoutInflater) {
        return layoutInflater.inflate(R.layout.activity_addscan,null);
    }

    private void showFragment(BaseFragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();

               /*
                * add是将一个fragment实例添加到Activity的最上层
                * replace替换containerViewId中的fragment实例，
                *   注意，它首先把containerViewId中所有fragment删除，然后再add进去当前的fragment
                *
                */
        ft.add(R.id.base_rl, fragment, fragment.getClass().getName());
        ft.commit();
    }

    public String codeString;

    @Override
    public void onCaptured(Bitmap bitmap) {

        ToastUtil.showToast(mDecoder.decode1(bitmap));
//        String code=mDecoder.decode(bitmap);
        Random random = new Random();
        int s = 10000 + random.nextInt(10);
        String code = "score"+"|"+String.valueOf(s) + "|" +getRandomString(new Random().nextInt(7));
        if(!TextUtils.isEmpty(code)){
            finish();
            String[] strings= StringUtils.split(code,"|");
            if(!"score".equals(strings[0])){
                ToastUtil.showToast(code);
            }else{
                startActivity(new Intent(this,EnterQueryActivity.class).putExtra("code",code));
            }
           // finishFragment(CameraScanFragment.class.getName());
        }else{
            ToastUtil.showToast("药品二维码信息描中...");
        }
    }

    public void finishFragment(String name) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        BaseFragment fragment = (BaseFragment) fragmentManager.findFragmentByTag(name);
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.remove(fragment);
        ft.commit();
    }
    public static String getRandomString(int length) {
//定义一个字符串（A-Z，a-z，0-9）即62位；
        String str = "zxcvbnmlkjhgfdsaqwertyuiopQWERTYUIOPASDFGHJKLZXCVBNM1234567890";
//由Random生成随机数
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
//长度为几就循环几次
        for (int i = 0; i < length; ++i) {
//产生0-61的数字
            int number = random.nextInt(62);
//将产生的数字通过length次承载到sb中
            sb.append(str.charAt(number));
        }
//将承载的字符转换成字符串
        return sb.toString();
    }
}
