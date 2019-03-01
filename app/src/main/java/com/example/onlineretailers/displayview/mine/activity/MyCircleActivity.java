package com.example.onlineretailers.displayview.mine.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.onlineretailers.Online.adapter.circle.adapter.MyCircleAdaper;
import com.example.onlineretailers.Online.entry.circle.bean.MyCircleBean;
import com.example.onlineretailers.Online.entry.mine.bean.DeleteCircleBean;
import com.example.onlineretailers.R;
import com.example.onlineretailers.utils.api.Apis;
import com.example.onlineretailers.utils.mvp.ContractEntity;
import com.example.onlineretailers.utils.mvp.presenter.IPresenterImpl;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 我的圈子
 * @author Peng
 * @time 2019/3/1 20:05
 */

public class MyCircleActivity extends AppCompatActivity implements ContractEntity.IView {

    @BindView(R.id.my_cirlce_text)
    TextView myCirlceText;
    @BindView(R.id.delete)
    ImageView delete;
    @BindView(R.id.clrcle_recycle)
    XRecyclerView clrcleRecycle;
    @BindView(R.id.icon_back)
    ImageView iconBack;
    private IPresenterImpl iPresenter;
    private int mPage=1;
    private boolean falg=true;
    private MyCircleAdaper circleAdapter;
    private int mCount = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_circle);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        iPresenter=new IPresenterImpl(this);
        circleAdapter=new MyCircleAdaper(this);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        clrcleRecycle.setLayoutManager(linearLayoutManager);
        circleAdapter = new MyCircleAdaper(this);
        clrcleRecycle.setAdapter(circleAdapter);
        clrcleRecycle.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                mPage=1;
                initData();
            }

            @Override
            public void onLoadMore() {
                initData();
            }
        });
        initData();
    }

    private void initData() {
        iPresenter.startRequestGet(Apis.FIND_MYCIRCLE_BYID_GET+"?page=1&count=5",null,MyCircleBean.class);
//        iPresenter.startRequestGet(String.format(Apis.FIND_MYCIRCLE_BYID_GET,mPage,mCount),null, MyCircleBean.class);
    }


    @Override
    public void getDataSuccess(Object data) {
        if(data instanceof MyCircleBean){
            MyCircleBean circleBean = (MyCircleBean) data;
            if(circleBean == null || !circleBean.isSuccess()){
                Toast.makeText(MyCircleActivity.this,circleBean.getMessage(),Toast.LENGTH_SHORT).show();
            }else{
                if(mPage == 1){
                    circleAdapter.setList(circleBean.getResult());
                }else{
                    circleAdapter.addList(circleBean.getResult());
                }
                mPage++;
                clrcleRecycle.loadMoreComplete();
                clrcleRecycle.refreshComplete();
            }
        }else if(data instanceof DeleteCircleBean){
            DeleteCircleBean deleteCircleBean = (DeleteCircleBean) data;
            if(deleteCircleBean == null || !deleteCircleBean.isSuccess()){
                Toast.makeText(MyCircleActivity.this,deleteCircleBean.getMessage(),Toast.LENGTH_SHORT).show();
            }else{
                mPage=1;
                initData();
            }
        }
    }

    @Override
    public void getDataFail(String error) {
        Toast.makeText(MyCircleActivity.this,error,Toast.LENGTH_LONG).show();
    }

    @OnClick({R.id.icon_back, R.id.delete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.icon_back:
                finish();
                break;
            case R.id.delete:
                if (falg){
                    circleAdapter.setCheckbox(true);
                }else {
                    circleAdapter.setCheckbox(false);
                    List<MyCircleBean.ResultBean> list = circleAdapter.getList();
                    String string="";
                    for (int i =0;i<list.size();i++){
                        if (list.get(i).isCheck()){
                            string+=list.get(i).getId()+",";
                        }
                    }

                    //请求删除圈子数据
                    if (!string.equals("")){
                        String substring = string.substring(0, string.length() - 1);

                        iPresenter.sendMessageDelete(String.format(Apis.DELETE_CIRCLE_DELETE,substring),null,DeleteCircleBean.class);
                    }

                }
                falg=!falg;
                break;
        }
    }
}
