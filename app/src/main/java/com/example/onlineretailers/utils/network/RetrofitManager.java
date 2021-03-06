package com.example.onlineretailers.utils.network;

import com.example.onlineretailers.utils.network.custom.BaseApis;
import com.example.onlineretailers.utils.network.custom.CustomIntercept;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 *
 * @author Peng
 * @time 2019/2/18 18:49
 */

public class RetrofitManager <T> {

    //TODO 外网域名
    private final String BASE_URL="http://mobile.bwstudent.com/small/";
    //TODO 内网
//    private final String BASE_URL="http://172.17.8.100/small/";
    private static RetrofitManager retrofitManager;
    private final OkHttpClient httpClient;
    private BaseApis mBaseApis;

    public static RetrofitManager getRetrofitManager() {
        if (retrofitManager==null){
            synchronized (RetrofitManager.class){
                if (retrofitManager==null){
                    retrofitManager=new RetrofitManager();
                }
            }
        }
        return retrofitManager;
    }
    private RetrofitManager(){
        OkHttpClient.Builder builder=new OkHttpClient.Builder();
        builder.connectTimeout(15,TimeUnit.SECONDS);
        builder.readTimeout(15,TimeUnit.SECONDS);
        builder.writeTimeout(15,TimeUnit.SECONDS);
        builder.addInterceptor(new CustomIntercept());
        builder.retryOnConnectionFailure(true);
        httpClient=builder.build();
        Retrofit retrofit=new Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .client(httpClient)
                .build();
        mBaseApis=retrofit.create(BaseApis.class);
    }

    public Map<String, RequestBody> generateRequestBody(Map<String, String> requesrDataMap) {
        Map<String, RequestBody> requestBodyMap = new HashMap<>();
        for (String key : requesrDataMap.keySet()) {
            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"),
                    requesrDataMap.get(key) == null ? "" : requesrDataMap.get(key));
            requestBodyMap.put(key, requestBody);
        }
        return requestBodyMap;
    }
    /**
     * get请求
     * @param url
     */
    public void get(String url,HttpListener listener){
        mBaseApis.get(url)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getObserver(listener));
    }

    /**
     * 表单post请求
     */
    public void postFormBody(String url, Map<String, RequestBody> map,HttpListener listener) {
        if (map == null) {
            map = new HashMap<>();
        }
        mBaseApis.postFormBody(url,map)
                //后台执行在哪个线程
                .subscribeOn(Schedulers.io())
                //最终完成后执行在哪个线程
                .observeOn(AndroidSchedulers.mainThread())
                //设置rxjava
                .subscribe(getObserver(listener));

    }
    /**
     * post请求
     * @param url
     * @param params
     */
    public void post(String url, Map<String,String> params,HttpListener listener){
        mBaseApis.post(url,params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getObserver(listener));
    }

    /**
     * put请求
     * @param url
     * @param params
     * @param listener
     */
    public void put(String url,Map<String,String> params,HttpListener listener){
        if (params == null){
            params =new HashMap<>();
        }
        mBaseApis.put(url,params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getObserver(listener));
    }

    /**
     * delete请求
     * @param quxiao
     * @param params
     * @param listener
     */
    public void delete(String quxiao, Map<String, String> params, HttpListener listener) {
        if(params==null){
            params=new HashMap<>();
        }
        mBaseApis.delete(quxiao,params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getObserver(listener));
    }
    /**
     *上传头像
     * */
    public static MultipartBody filesMultipar(Map<String,String> map){
        MultipartBody.Builder builder = new MultipartBody.Builder();
        for(Map.Entry<String,String> entry:map.entrySet()){
            File file = new File(entry.getValue());
            builder.addFormDataPart(entry.getKey(),"tp.png",
                    RequestBody.create(MediaType.parse("multipart/form-data"),file));
        }
        builder.setType(MultipartBody.FORM);
        MultipartBody multipartBody = builder.build();
        return multipartBody;
    }
    public void imagePost(String url, Map<String,String> map,HttpListener listener){
        if(map == null){
            map = new HashMap<>();
        }
        MultipartBody multipartBody = filesMultipar(map);
        mBaseApis.imagePost(url,multipartBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getObserver(listener));
    }
    /**
     * 上传多张图片
     */
    public void postFileMore(String url, Map<String, Object> map,HttpListener listener) {
        if (map == null) {
            map = new HashMap<>();
        }
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        builder.addFormDataPart("commodityId", String.valueOf(map.get("commodityId")));
        if(!String.valueOf(map.get("orderId")).equals("")){
            builder.addFormDataPart("orderId", String.valueOf(map.get("orderId")));
        }
        builder.addFormDataPart("content", String.valueOf(map.get("content")));
        if (!map.get("image").equals("")) {
            List<String> image = (List<String>) map.get("image");
            for(int i=0;i<image.size();i++){
                File file = new File(image.get(i));
                builder.addFormDataPart("image", file.getName(),RequestBody.create(MediaType.parse("multipart/form-data"),file));
            }

        }
        builder.setType(MultipartBody.FORM);
        MultipartBody multipartBody = builder.build();
        mBaseApis.imagePost(url, multipartBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getObserver(listener));
    }

    private Observer getObserver(final HttpListener listener){
        Observer observer = new Observer<ResponseBody>() {
            @Override
            public void onNext(ResponseBody responseBody) {
                try {
                    String data = responseBody.string();
                    if (listener != null) {
                        listener.onSuccess(data);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    if (listener != null) {
                        listener.onFail(e.getMessage());
                    }
                }
            }

            @Override
            public void onError(Throwable e) {
                if (listener != null) {
                    listener.onFail(e.getMessage());
                }
            }

            @Override
            public void onCompleted() {

            }
        };

        return observer;
    }

    private HttpListener listener;

    public void result(HttpListener listener) {
        this.listener = listener;
    }

    public interface HttpListener {
        void onSuccess(String data);

        void onFail(String error);
    }
}
