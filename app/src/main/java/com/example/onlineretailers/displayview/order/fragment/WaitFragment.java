package com.example.onlineretailers.displayview.order.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.onlineretailers.Online.adapter.order.adapter.OrderWaitAdaper;
import com.example.onlineretailers.Online.adapter.order.bean.OrderBean;
import com.example.onlineretailers.Online.adapter.order.bean.TakeBean;
import com.example.onlineretailers.R;
import com.example.onlineretailers.utils.api.Apis;
import com.example.onlineretailers.utils.mvp.ContractEntity;
import com.example.onlineretailers.utils.mvp.presenter.IPresenterImpl;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class WaitFragment extends Fragment implements ContractEntity.IView {


    @BindView(R.id.recycleview)
    RecyclerView recycleview;
    Unbinder unbinder;
    private OrderWaitAdaper waitAdaper;
    private IPresenterImpl iPresenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.order_wait_fragment, container, false);

        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        iPresenter=new IPresenterImpl(this);
        //创建布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        recycleview.setLayoutManager(layoutManager);
        waitAdaper=new OrderWaitAdaper(getActivity());
        recycleview.setAdapter(waitAdaper);
        initData();
        //确认收货
        waitAdaper.setCallBackWait(new OrderWaitAdaper.CallBackWait() {
            @Override
            public void callBack(String orderId) {
                Map<String,String> map = new HashMap<>();
                map.put("orderId",orderId);
                iPresenter.startRequestPut(Apis.CONFIRM_RECEIPT_PUT,map, TakeBean.class);
            }
        });
    }

    private void initData() {
        iPresenter.startRequestGet(Apis.FIND_ORDER_LIST_BYSTATUS_GET+"?status=2&page=1&count=5",null, OrderBean.class);
    }

    @Override
    public void getDataSuccess(Object data) {
        if(data instanceof OrderBean){
            OrderBean orderBean = (OrderBean) data;
            if(orderBean==null || !orderBean.isSuccess()){
                Toast.makeText(getActivity(),orderBean.getMessage(),Toast.LENGTH_SHORT).show();
            }else{
//
                waitAdaper.setmOrder(orderBean.getOrderList());
            }
        }else if(data instanceof TakeBean){
            TakeBean takeBean = (TakeBean) data;
            Toast.makeText(getActivity(),takeBean.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void getDataFail(String error) {
        Toast.makeText(getActivity(),error,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
