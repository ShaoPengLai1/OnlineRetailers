package com.example.onlineretailers.utils.mvp.model;

import com.example.onlineretailers.utils.callback.MyCallBack;
import com.example.onlineretailers.utils.mvp.ContractEntity;
import com.example.onlineretailers.utils.network.RetrofitManager;
import com.google.gson.Gson;

import java.util.Map;

public class IModelImpl implements ContractEntity.IModel {
    @Override
    public void requestDataGet(String url, String params, final Class clazz, final MyCallBack myCallBack) {
        RetrofitManager.getRetrofitManager().get(url, new RetrofitManager.HttpListener() {
            @Override
            public void onSuccess(String data) {
                try{
                    Object o = new Gson().fromJson(data, clazz);
                    if(myCallBack != null){
                        myCallBack.onSuccess(o);
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                    if (myCallBack != null) {
                        myCallBack.onFail(e.getMessage());
                    }
                }
            }

            @Override
            public void onFail(String error) {
                if(myCallBack != null){
                    myCallBack.onFail(error);
                }
            }
        });
    }

    @Override
    public void requestDataPost(String url, Map<String, String> params, final Class clazz, final MyCallBack myCallBack) {
        RetrofitManager.getRetrofitManager().post(url, params, new RetrofitManager.HttpListener() {
            @Override
            public void onSuccess(String data) {
                try{
                    Object o = new Gson().fromJson(data, clazz);
                    if(myCallBack != null){
                        myCallBack.onSuccess(o);
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                    if (myCallBack != null) {
                        myCallBack.onFail(e.getMessage());
                    }
                }
            }

            @Override
            public void onFail(String error) {
                if(myCallBack != null){
                    myCallBack.onFail(error);
                }
            }
        });
    }

    @Override
    public void requestDataPut(String url, Map<String, String> params, final Class clazz, final MyCallBack myCallBack) {
        RetrofitManager.getRetrofitManager().put(url, params, new RetrofitManager.HttpListener() {
            @Override
            public void onSuccess(String data) {
                try{
                    Object o = new Gson().fromJson(data, clazz);
                    if(myCallBack != null){
                        myCallBack.onSuccess(o);
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                    if (myCallBack != null) {
                        myCallBack.onFail(e.getMessage());
                    }
                }
            }

            @Override
            public void onFail(String error) {
                if(myCallBack != null){
                    myCallBack.onFail(error);
                }
            }
        });
    }

    @Override
    public void requestDelete(String url, Map<String, String> params, final Class clazz, final MyCallBack myCallBack) {
        RetrofitManager.getRetrofitManager().delete(url, params, new RetrofitManager.HttpListener() {
            @Override
            public void onSuccess(String data) {
                try{
                    Object o = new Gson().fromJson(data, clazz);
                    if(myCallBack != null){
                        myCallBack.onSuccess(o);
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                    if (myCallBack != null) {
                        myCallBack.onFail(e.getMessage());
                    }
                }
            }

            @Override
            public void onFail(String error) {
                if(myCallBack != null){
                    myCallBack.onFail(error);
                }
            }
        });
    }
}
