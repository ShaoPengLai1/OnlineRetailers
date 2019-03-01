package com.example.onlineretailers.displayview.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.onlineretailers.Online.adapter.circle.adapter.CircleAdaper;
import com.example.onlineretailers.Online.entry.circle.bean.CircleBean;
import com.example.onlineretailers.Online.entry.circle.bean.CircleCanclePraiseBean;
import com.example.onlineretailers.Online.entry.circle.bean.CircleGivePraiseBean;
import com.example.onlineretailers.Online.entry.login.bean.LoginBean;
import com.example.onlineretailers.R;
import com.example.onlineretailers.utils.api.Apis;
import com.example.onlineretailers.utils.mvp.ContractEntity;
import com.example.onlineretailers.utils.mvp.presenter.IPresenterImpl;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class MomentsFragment extends Fragment implements ContractEntity.IView {

    @BindView(R.id.xrecycleview)
    XRecyclerView xrecycleview;
    Unbinder unbinder;
    private IPresenterImpl presenter;
    private CircleAdaper adaper;
    private int mPage;
    private int count = 10;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_circle, container, false);

        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;

    }

    private void initView() {
        mPage = 1;
        presenter = new IPresenterImpl(this);
        //创建布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        xrecycleview.setLayoutManager(layoutManager);
        xrecycleview.setPullRefreshEnabled(true);
        xrecycleview.setLoadingMoreEnabled(true);
        //创建适配器
        adaper = new CircleAdaper(getActivity());
        xrecycleview.setAdapter(adaper);
        xrecycleview.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                mPage = 1;
                initData();
            }

            @Override
            public void onLoadMore() {
                initData();
            }
        });
        adaper.setClickCallBack(new CircleAdaper.ClickCallBack() {
            @Override
            public void callBack(int i, int position, int id) {
                if (i == 1) {
                    //取消点赞
                    presenter.sendMessageDelete(String.format(Apis.CANCLE_CIRCLE_GREAT_DELETE, id),null, CircleCanclePraiseBean.class);
                    adaper.getCancelPraise(position);
                } else if (i == 2) {
                    //点赞
                    Map<String, String> addMap = new HashMap<>();
                    addMap.put("circleId", String.valueOf(id));
                    presenter.startRequestPost(Apis.ADD_CIRCLE_GREAT_POST, addMap, CircleGivePraiseBean.class);
                    adaper.getGivePraise(position);
                }
            }
        });
        initData();
    }

    private void initData() {
        Intent intent = getActivity().getIntent();
        LoginBean.ResultBean result = (LoginBean.ResultBean) intent.getSerializableExtra("result");
        presenter.startRequestGet(Apis.FIND_CIRCLE_LIST_GET+"?page="+mPage+"&count=10", null, CircleBean.class);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void getDataSuccess(Object data) {
        //圈子列表
        if (data instanceof CircleBean) {
            CircleBean bean = (CircleBean) data;
            if (bean == null || !bean.isSuccess()) {
                Toast.makeText(getActivity(), bean.getMessage(), Toast.LENGTH_SHORT).show();
            } else {
                adaper.setmResult(bean.getResult());
                if (mPage == 1) {
                    adaper.setmResult(bean.getResult());
                } else {
                    adaper.addmResult(bean.getResult());
                }
                mPage++;
                xrecycleview.refreshComplete();
                xrecycleview.loadMoreComplete();
            }
            //点赞
        } else if (data instanceof CircleGivePraiseBean) {
            CircleGivePraiseBean givePraiseBean = (CircleGivePraiseBean) data;
            Toast.makeText(getActivity(), givePraiseBean.getMessage(), Toast.LENGTH_SHORT).show();
            //取消点赞
        } else if (data instanceof CircleCanclePraiseBean) {
            CircleCanclePraiseBean canclePraiseBean = (CircleCanclePraiseBean) data;
            Toast.makeText(getActivity(), canclePraiseBean.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void getDataFail(String error) {

    }
}
