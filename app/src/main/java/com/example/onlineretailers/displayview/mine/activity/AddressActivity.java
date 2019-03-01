package com.example.onlineretailers.displayview.mine.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.onlineretailers.Online.adapter.mine.adapter.AddressAdaper;
import com.example.onlineretailers.Online.entry.mine.bean.DefaultAddressBean;
import com.example.onlineretailers.Online.entry.shopcar.bean.AddressBean;
import com.example.onlineretailers.R;
import com.example.onlineretailers.displayview.mine.NewAddressActivity;
import com.example.onlineretailers.utils.api.Apis;
import com.example.onlineretailers.utils.mvp.ContractEntity;
import com.example.onlineretailers.utils.mvp.presenter.IPresenterImpl;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddressActivity extends AppCompatActivity implements ContractEntity.IView {


    @BindView(R.id.text_myaddre)
    TextView textMyaddre;
    @BindView(R.id.accomplish)
    TextView accomplish;
    @BindView(R.id.btu_newaddress)
    Button btuNewaddress;
    @BindView(R.id.address_recycle)
    RecyclerView addressRecycle;
    @BindView(R.id.aicon_back)
    ImageView aiconBack;

    private AddressAdaper addressAdaper;
    private IPresenterImpl iPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        iPresenter = new IPresenterImpl(this);
        iPresenter.startRequestGet(Apis.RECEIVE_ADDRESS_GET, null, AddressBean.class);
        //创建布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        addressRecycle.setLayoutManager(layoutManager);
        //创建适配器
        addressAdaper = new AddressAdaper(this);
        addressRecycle.setAdapter(addressAdaper);
        addressAdaper.setCallBackUpdata(new AddressAdaper.CallBackUpdata() {
            @Override
            public void callBack(AddressBean.ResultBean resultBean) {
                Intent intent = new Intent(AddressActivity.this, UpdateAddressActivity.class);
                intent.putExtra("resultBean", resultBean);
                startActivityForResult(intent, 100);
            }
        });
        //设置默认选中
        addressAdaper.setCallBackDefault(new AddressAdaper.CallBackDefault() {
            @Override
            public void callBack(int id) {
                Map<String, String> map = new HashMap<>();
                map.put("id", String.valueOf(id));
                iPresenter.startRequestPost(Apis.SET_DEFAULT_RECEIVE_ADDRESS_POST, map, DefaultAddressBean.class);
            }
        });
    }


    @Override
    public void getDataSuccess(Object data) {
        if (data instanceof AddressBean) {
            AddressBean addressBean = (AddressBean) data;
            if (addressBean == null || !addressBean.isSuccess()) {
                Toast.makeText(AddressActivity.this, addressBean.getMessage(), Toast.LENGTH_SHORT).show();
            } else {
                addressAdaper.setmList(addressBean.getResult());
            }
        } else if (data instanceof DefaultAddressBean) {
            DefaultAddressBean defaultAddressBean = (DefaultAddressBean) data;
            Toast.makeText(AddressActivity.this, defaultAddressBean.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void getDataFail(String error) {
        Toast.makeText(AddressActivity.this, error, Toast.LENGTH_LONG).show();
    }

    @OnClick({R.id.aicon_back, R.id.btu_newaddress})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.aicon_back:
                finish();
                break;
            case R.id.btu_newaddress:
                Intent intent = new Intent(AddressActivity.this, NewAddressActivity.class);
                startActivityForResult(intent, 100);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == 200) {
            iPresenter.startRequestGet(Apis.RECEIVE_ADDRESS_GET, null, AddressBean.class);
        }
    }
}
