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

import com.example.onlineretailers.Online.adapter.order.adapter.OrderObligationAdaper;
import com.example.onlineretailers.Online.adapter.order.bean.DeleteOrderBean;
import com.example.onlineretailers.Online.adapter.order.bean.OrderBean;
import com.example.onlineretailers.R;
import com.example.onlineretailers.displayview.order.activity.PayMentActivity;
import com.example.onlineretailers.utils.api.Apis;
import com.example.onlineretailers.utils.mvp.ContractEntity;
import com.example.onlineretailers.utils.mvp.presenter.IPresenterImpl;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ObligationFragment extends Fragment implements ContractEntity.IView {

    @BindView(R.id.recycleview)
    RecyclerView recycleview;
    Unbinder unbinder;
    private IPresenterImpl iPresenter;

    private OrderObligationAdaper obligationAdaper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.order_obligation_fragment, container, false);

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
        obligationAdaper = new OrderObligationAdaper(getActivity());
        recycleview.setAdapter(obligationAdaper);
        initData();
        //删除订单
        obligationAdaper.setCallBackAll(new OrderObligationAdaper.CallBackObligation() {
            @Override
            public void callBack(String orderId, int position) {
                obligationAdaper.setDel(position);
                iPresenter.sendMessageDelete(String.format(Apis.DELETE_ORDER_DELETE,orderId), null,DeleteOrderBean.class);
            }
        });
        //去支付
        obligationAdaper.setCallBackPay(new OrderObligationAdaper.CallBackPay() {
            @Override
            public void callBack(String orderId,Double payAmount) {
                Intent intent = new Intent(getActivity(), PayMentActivity.class);
                intent.putExtra("orderId",orderId);
                intent.putExtra("payAmount",payAmount);
                startActivity(intent);
            }
        });

    }

    private void initData() {
        iPresenter.startRequestGet(Apis.FIND_ORDER_LIST_BYSTATUS_GET+"?status=1&page=1&count=5",null, OrderBean.class);
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
                obligationAdaper.setmOrder(orderBean.getOrderList());
            }
        }else if (data instanceof DeleteOrderBean){
            DeleteOrderBean deleteOrderBean = (DeleteOrderBean) data;
            Toast.makeText(getActivity(),deleteOrderBean.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void getDataFail(String error) {
        Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
    }
}
