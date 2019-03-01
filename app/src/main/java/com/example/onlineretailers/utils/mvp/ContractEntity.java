package com.example.onlineretailers.utils.mvp;

import com.example.onlineretailers.utils.callback.MyCallBack;

import java.util.Map;

public interface ContractEntity {

    interface IModel{
        void requestDataGet(String url, String params, Class clazz, MyCallBack myCallBack);
        void requestDataPost(String url, Map<String, String> params, Class clazz, MyCallBack myCallBack);
        void requestDataPut(String url,Map<String,String> params,Class clazz,MyCallBack myCallBack);
        void requestDelete(String quxiao, Map<String, String> map, Class clazz, MyCallBack myCallBack);
        void requestImagePost(String url,Map<String,String> map,Class clazz,MyCallBack myCallBack);
        void imagePost(String url,Map<String,Object> map,Class clazz,MyCallBack myCallBack);
    }
     interface IPresenter{
         /**
          * get请求
          * @param url
          * @param params
          * @param clazz
          */
        void startRequestGet(String url, String params, Class clazz);

         /**
          * post请求
          * @param url
          * @param params
          * @param clazz
          */
        void startRequestPost(String url, Map<String, String> params, Class clazz);

         /**
          * put请求
          * @param url
          * @param params
          * @param clazz
          */
        void startRequestPut(String url, Map<String, String> params, Class clazz);

         /**
          * delete请求
          * @param url
          * @param params
          * @param clazz
          */
        void sendMessageDelete(String url, Map<String, String> params, Class clazz);
         /**
          * 上传头像
          * */
         void imagePostRequest(String url,Map<String,String> map,Class clazz);
         /**
          * 多图上传
          * */
         void imagesPostRequest(String url,Map<String,Object> map,Class clazz);
    }
    interface IView <T>{
        void getDataSuccess(T data);
        void getDataFail(String error);
    }
}
