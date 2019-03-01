package com.example.onlineretailers.utils.mvp.presenter;

import com.example.onlineretailers.utils.callback.MyCallBack;
import com.example.onlineretailers.utils.mvp.model.IModelImpl;
import com.example.onlineretailers.utils.mvp.ContractEntity;

import java.util.Map;

public class IPresenterImpl implements ContractEntity.IPresenter {

    private IModelImpl mImodel;
    private ContractEntity.IView mIviw;
    public IPresenterImpl(ContractEntity.IView iView) {
        this.mIviw=iView;
        mImodel = new IModelImpl();
    }
    @Override
    public void startRequestGet(String url, String params, Class clazz) {
        mImodel.requestDataGet(url, params, clazz, new MyCallBack() {
            @Override
            public void onSuccess(Object data) {
                mIviw.getDataSuccess(data);
            }

            @Override
            public void onFail(String error) {
                mIviw.getDataFail(error);
            }
        });
    }

    @Override
    public void startRequestPost(String url, Map<String, String> params, Class clazz) {
        mImodel.requestDataPost(url, params, clazz, new MyCallBack() {
            @Override
            public void onSuccess(Object data) {
                mIviw.getDataSuccess(data);
            }

            @Override
            public void onFail(String error) {
                mIviw.getDataFail(error);
            }
        });
    }

    @Override
    public void startRequestPut(String url, Map<String, String> params, Class clazz) {
        mImodel.requestDataPut(url, params, clazz, new MyCallBack() {
            @Override
            public void onSuccess(Object data) {
                mIviw.getDataSuccess(data);
            }

            @Override
            public void onFail(String error) {
                mIviw.getDataFail(error);
            }
        });
    }

    @Override
    public void sendMessageDelete(String url, Map<String, String> params, Class clazz) {
        mImodel.requestDelete(url, params, clazz, new MyCallBack() {
            @Override
            public void onSuccess(Object data) {
                mIviw.getDataSuccess(data);
            }

            @Override
            public void onFail(String error) {
                mIviw.getDataFail(error);
            }
        });
    }

    @Override
    public void imagePostRequest(String url, Map<String, String> map, Class clazz) {
        mImodel.requestImagePost(url, map, clazz, new MyCallBack() {
            @Override
            public void onSuccess(Object data) {
                mIviw.getDataSuccess(data);
            }

            @Override
            public void onFail(String error) {
                mIviw.getDataFail(error);
            }
        });
    }

    @Override
    public void imagesPostRequest(String url, Map<String, Object> map, Class clazz) {
        mImodel.imagePost(url, map, clazz, new MyCallBack() {
            @Override
            public void onSuccess(Object data) {
                mIviw.getDataSuccess(data);
            }

            @Override
            public void onFail(String error) {
                mIviw.getDataFail(error);
            }
        });
    }

    public void onDetach() {
        if (mImodel != null) {
            mImodel = null;
        }
        if (mIviw != null) {
            mIviw = null;
        }
    }
}
