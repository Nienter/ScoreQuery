package com.mike.scorequery;

import android.app.Application;

import org.xutils.x;

import cn.bmob.v3.Bmob;


/**
 * Created by mike on 2017/3/25.
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AppContext.getInstance().init(this);
        x.Ext.init(this);
//        x.Ext.setDebug(BuildConfig.DEBUG); // 开启debug会影响性能
        Bmob.initialize(this, "b41c63b31bae100fc4af4d0f6479ed76");
    }
}
