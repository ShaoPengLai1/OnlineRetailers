package com.example.onlineretailers.utils.mvp;

import com.example.onlineretailers.utils.callback.MyCallBack;

import java.util.Map;

public interface ContractEntity {

    interface IModel{
        void requestDataGet(String url, String params, Class clazz, MyCallBack myCallBack);
        void requestDataPost(String url, Map<String, String> params, Class clazz, MyCallBack myCallBack);
        void requestDataPut(String url,Map<String,String> params,Class clazz,MyCallBack myCallBack);
        void requestDelete(String quxiao, Map<String, String> map, Class clazz, MyCallBack myCallBack);
    }
     interface IPresenter{
        void startRequestGet(String url, String params, Class clazz);
        void startRequestPost(String url, Map<String, String> params, Class clazz);
        void startRequestPut(String url, Map<String, String> params, Class clazz);
        void sendMessageDelete(String url, Map<String, String> params, Class clazz);
    }
    interface IView <T>{
        void getDataSuccess(T data);
        void getDataFail(String error);
    }
}
