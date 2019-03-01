package com.example.onlineretailers.displayview.order.fragment;

import android.content.Intent;
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

import com.example.onlineretailers.Online.adapter.order.adapter.OrdelAllAdaper;
import com.example.onlineretailers.Online.adapter.order.bean.DeleteOrderBean;
import com.example.onlineretailers.Online.adapter.order.bean.OrderBean;
import com.example.onlineretailers.Online.adapter.order.bean.TakeBean;
import com.example.onlineretailers.R;
import com.example.onlineretailers.displayview.order.activity.PayMentActivity;
import com.example.onlineretailers.utils.api.Apis;
import com.example.onlineretailers.utils.mvp.ContractEntity;
import com.example.onlineretailers.utils.mvp.presenter.IPresenterImpl;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class AllFragment extends Fragment implements ContractEntity.IView {


    @BindView(R.id.recycleview)
    RecyclerView recycleview;
    Unbinder unbinder;
    private int status = 0;
    private int page;
    private int count = 5;
    private IPresenterImpl iPresenter;
    private OrdelAllAdaper allAdaper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.order_all_fragment, container, false);
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
        //创建适配器
        allAdaper = new OrdelAllAdaper(getActivity());
        recycleview.setAdapter(allAdaper);
        initData();
        //取消订单
        allAdaper.setCallBackDel(new OrdelAllAdaper.CallBackDel() {
            @Override
            public void callBack(String orderId, int position) {
                allAdaper.setDel(position);
                iPresenter.sendMessageDelete(String.format(Apis.DELETE_ORDER_DELETE,orderId),null, DeleteOrderBean.class);
            }
        });
        //去支付
        allAdaper.setCallBackPay(new OrdelAllAdaper.CallBackPay() {
            @Override
            public void callBack(String orderId, Double payAmount) {
                Intent intent = new Intent(getActivity(), PayMentActivity.class);
                intent.putExtra("orderId",orderId);
                intent.putExtra("payAmount",payAmount);
                startActivity(intent);
            }
        });
        //确认收货
        allAdaper.setCallBackWait(new OrdelAllAdaper.CallBackWait() {
            @Override
            public void callBack(String orderId) {
                Map<String,String> map = new HashMap<>();
                map.put("orderId",orderId);
                iPresenter.startRequestPut(Apis.CONFIRM_RECEIPT_PUT,map, TakeBean.class);
            }
        });
    }


    private void initData() {
        iPresenter.startRequestGet(Apis.FIND_ORDER_LIST_BYSTATUS_GET+"?status=0&page=1&count=5",null,OrderBean.class);
//        iPresenter.startRequestGet(String.format(Apis.FIND_ORDER_LIST_BYSTATUS_GET,status,page,count),null, OrderBean.class);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        iPresenter.onDetach();
    }

    @Override
    public void getDataSuccess(Object data) {
        if(data instanceof OrderBean){
            OrderBean orderBean = (OrderBean) data;
            if(orderBean==null || !orderBean.isSuccess()){
                Toast.makeText(getActivity(),orderBean.getMessage(),Toast.LENGTH_SHORT).show();
            }else{
//
                allAdaper.setmOrder(orderBean.getOrderList());
            }
        }else if(data instanceof TakeBean){
            TakeBean takeBean = (TakeBean) data;
            Toast.makeText(getActivity(),takeBean.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void getDataFail(String error) {
        Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
    }
}
