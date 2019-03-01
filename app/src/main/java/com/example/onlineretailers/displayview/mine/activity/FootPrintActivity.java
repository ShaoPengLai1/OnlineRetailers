package com.example.onlineretailers.displayview.mine.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.onlineretailers.Online.adapter.mine.adapter.FootAdapter;
import com.example.onlineretailers.Online.entry.mine.bean.FootPrintBean;
import com.example.onlineretailers.Online.entry.shopcar.bean.GoodsDetailBean;
import com.example.onlineretailers.R;
import com.example.onlineretailers.displayview.details.DetailsActivity;
import com.example.onlineretailers.utils.EventBean.EventBean;
import com.example.onlineretailers.utils.api.Apis;
import com.example.onlineretailers.utils.mvp.ContractEntity;
import com.example.onlineretailers.utils.mvp.presenter.IPresenterImpl;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 我的足迹
 * @author Peng
 * @time 2019/3/1 20:06
 */

public class FootPrintActivity extends AppCompatActivity implements ContractEntity.IView {

    @BindView(R.id.aicon_back)
    ImageView aiconBack;
    private int mPage;
    @BindView(R.id.myFootView)
    XRecyclerView myFootView;
    private FootAdapter footAdapter;
    private IPresenterImpl iPresenter;
    private FootPrintBean footPrintBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foot_print);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        mPage=1;
        iPresenter=new IPresenterImpl(this);
        footAdapter=new FootAdapter(this);
        myFootView.setPullRefreshEnabled(true);
        myFootView.setLoadingMoreEnabled(true);
        myFootView.setAdapter(footAdapter);
        myFootView.setLayoutManager(new StaggeredGridLayoutManager(2, GridLayoutManager.VERTICAL));
        myFootView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                mPage=1;
                loadData();
            }

            @Override
            public void onLoadMore() {
                loadData();
            }
        });
        loadData();
        footAdapter.result(new FootAdapter.Cicklistener() {
            @Override
            public void setonclicklisener(int index) {
                int commodityId = footPrintBean.getResult().get(index).getCommodityId();
                getGoods(commodityId);
            }
        });
    }

    private void loadData() {
        iPresenter.startRequestGet(Apis.BROWSE_LIST_GET + "?page=" + mPage + "&count=10",
                null, FootPrintBean.class);
    }
    private void getGoods(int id) {
        iPresenter.startRequestGet(Apis.FIND_COMMODITY_DETAILS_BYID_GET
                + "?commodityId=" + id, null, GoodsDetailBean.class);
    }

    @OnClick({R.id.aicon_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.aicon_back:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void getDataSuccess(Object data) {
        if (data instanceof FootPrintBean){
            FootPrintBean printBean1= (FootPrintBean) data;
            if (printBean1 == null || !printBean1.isSuccess()) {
                Toast.makeText(FootPrintActivity.this,
                        printBean1.getMessage(), Toast.LENGTH_LONG).show();
            } else {
                if (mPage == 1) {
                    footPrintBean=printBean1;
                    footAdapter.setmList(printBean1.getResult());
                } else {
                    printBean1.getResult().addAll(printBean1.getResult());
                    footAdapter.addLists(printBean1.getResult());
                }
                mPage++;
                myFootView.refreshComplete();
                myFootView.loadMoreComplete();
            }
        }else if (data instanceof GoodsDetailBean) {
            EventBus.getDefault().postSticky(new EventBean("goods", data));
            startActivity(new Intent(this, DetailsActivity.class));
        }
    }

    @Override
    public void getDataFail(String error) {

    }
}
