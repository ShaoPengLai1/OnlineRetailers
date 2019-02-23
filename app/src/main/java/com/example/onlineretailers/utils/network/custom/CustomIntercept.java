package com.example.onlineretailers.utils.network.custom;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.example.onlineretailers.utils.application.App;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 
 * @author Peng
 * @time 2019/2/18 18:57
 */

public class CustomIntercept implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        SharedPreferences preferences = App.getApplication().getSharedPreferences("UserShao", Context.MODE_PRIVATE);
        String userId = preferences.getString("userId", "");
        String sessionId = preferences.getString("sessionId", "");
        Request.Builder builder=request.newBuilder();
        builder.method(request.method(),request.body());
        if (!TextUtils.isEmpty(userId) && !TextUtils.isEmpty(sessionId)){
            builder.addHeader("userId",userId);
            builder.addHeader("sessionId",sessionId);
        }
        Request reques=builder.build();
        return chain.proceed(reques);
    }
}
