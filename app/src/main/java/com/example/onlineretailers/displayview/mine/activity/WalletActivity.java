package com.example.onlineretailers.displayview.mine.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.onlineretailers.Online.adapter.mine.adapter.WalletAdaper;
import com.example.onlineretailers.Online.entry.mine.bean.WalletBean;
import com.example.onlineretailers.R;
import com.example.onlineretailers.utils.api.Apis;
import com.example.onlineretailers.utils.mvp.ContractEntity;
import com.example.onlineretailers.utils.mvp.presenter.IPresenterImpl;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 我的钱包
 * @author Peng
 * @time 2019/3/1 20:37
 */

public class WalletActivity extends AppCompatActivity implements ContractEntity.IView {

    @BindView(R.id.icon_back)
    ImageView iconBack;
    @BindView(R.id.wallet_back)
    ImageView walletBack;
    @BindView(R.id.balance)
    TextView balance;
    @BindView(R.id.money)
    TextView money;
    @BindView(R.id.sum)
    TextView sum;
    @BindView(R.id.time)
    TextView time;
    @BindView(R.id.view_line)
    View viewLine;
    @BindView(R.id.view_linev)
    View viewLinev;
    @BindView(R.id.wallet_recycle)
    RecyclerView walletRecycle;
    private WalletAdaper walletAdaper;
    private IPresenterImpl iPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        ButterKnife.bind(this);
        initView();

    }

    private void initView() {
        iPresenter=new IPresenterImpl(this);
        walletAdaper=new WalletAdaper(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        DividerItemDecoration decoration = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        walletRecycle.addItemDecoration(decoration);
        walletRecycle.setLayoutManager(layoutManager);
        walletRecycle.setAdapter(walletAdaper);
        iconBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initData();
    }

    private void initData() {
        iPresenter.startRequestGet(Apis.FIND_USER_WALLET_GET+"?page=1&count=20",null, WalletBean.class);
    }


    @Override
    public void getDataSuccess(Object data) {
        if(data instanceof WalletBean){
            WalletBean walletBean = (WalletBean) data;
            WalletBean.ResultBean result = walletBean.getResult();

            if(walletBean == null || !walletBean.isSuccess()){
                Toast.makeText(WalletActivity.this,walletBean.getMessage(),Toast.LENGTH_SHORT).show();
            }else{
                int balance = result.getBalance();

                int integer = Integer.valueOf(balance);
                int i1 = integer * 1000;
                money.setText(balance+"");
                walletAdaper.setmBean(walletBean.getResult().getDetailList());
            }
        }
    }

    @Override
    public void getDataFail(String error) {
        Toast.makeText(WalletActivity.this,error,Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        iPresenter.onDetach();
    }

}
