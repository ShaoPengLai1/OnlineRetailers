package com.example.onlineretailers.utils.callback;

public interface MyCallBack <T> {
    void onSuccess(T data);
    void onFail(String error);
}
