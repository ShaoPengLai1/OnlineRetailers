package com.example.onlineretailers.utils.application;

import android.app.Application;
import android.content.Context;

import com.facebook.drawee.backends.pipeline.Fresco;

/**
 *
 * @author Peng
 * @time 2019/2/18 18:58
 */

public class App extends Application {

    private static Context mContext;
    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
        mContext=getApplicationContext();
    }
    public static Context getApplication(){
        return mContext;
    }
    //private long exitTime = 0;
}
