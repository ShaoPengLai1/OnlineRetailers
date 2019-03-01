package com.example.onlineretailers.displayview.shop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.onlineretailers.Online.adapter.order.bean.CreateOrderBean;
import com.example.onlineretailers.Online.adapter.shopcar.adapter.CloseAddressAdaper;
import com.example.onlineretailers.Online.adapter.shopcar.adapter.CloseShoppAdaaper;
import com.example.onlineretailers.Online.entry.shopcar.bean.AddressBean;
import com.example.onlineretailers.Online.entry.shopcar.bean.CloseBean;
import com.example.onlineretailers.Online.entry.shopcar.bean.FindShoppingCartBean;
import com.example.onlineretailers.R;
import com.example.onlineretailers.displayview.mine.NewAddressActivity;
import com.example.onlineretailers.utils.api.Apis;
import com.example.onlineretailers.utils.mvp.ContractEntity;
import com.example.onlineretailers.utils.mvp.presenter.IPresenterImpl;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CloseActivity extends AppCompatActivity implements ContractEntity.IView {


    @BindView(R.id.realName)
    TextView realName;
    @BindView(R.id.phone)
    TextView phone;
    @BindView(R.id.address)
    TextView address;
    @BindView(R.id.but_obtain)
    ImageView butObtain;
    @BindView(R.id.add_layout_view)
    ConstraintLayout addLayoutView;
    @BindView(R.id.layout_add)
    RelativeLayout layoutAdd;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.address_recyclerview)
    RecyclerView addressRecyclerview;
    @BindView(R.id.total_price)
    TextView totalPrice;
    @BindView(R.id.settlement)
    TextView settlement;
    @BindView(R.id.relativelayout)
    RelativeLayout relativelayout;
    @BindView(R.id.icon_back)
    ImageView iconBack;
    @BindView(R.id.addAddr)
    Button addAddr;
    private int num = 0;
    private double totalPrice1 = 0;
    private CloseShoppAdaaper shoppAdaaper;
    private List<CloseBean> closeBeans;
    private CloseAddressAdaper addressAdaper;
    private int id;
    private IPresenterImpl iPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_close);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        iPresenter = new IPresenterImpl(this);
        iPresenter.startRequestGet(Apis.RECEIVE_ADDRESS_GET, null, AddressBean.class);
        closeBeans = new ArrayList<>();
        shoppAdaaper = new CloseShoppAdaaper(this);

        //接收值
        Intent intent = getIntent();
        ArrayList<FindShoppingCartBean.ResultBean> checkList = intent.getParcelableArrayListExtra("checkList");
        if (checkList != null) {
            for (FindShoppingCartBean.ResultBean re : checkList) {
                num += re.getCount();
                totalPrice1 += re.getCount() * re.getPrice();
                closeBeans.add(new CloseBean(re.getCommodityId(), re.getCount()));
            }
        }
        totalPrice.setText("共" + num + "件商品，需付款" + totalPrice1 + "元");
        recyclerview.setLayoutManager(new LinearLayoutManager(CloseActivity.this, LinearLayoutManager.VERTICAL, false));
        recyclerview.setAdapter(shoppAdaaper);
        //将集合传到适配器
        shoppAdaaper.setmList(checkList);
        setResult(200, intent);
        //加载地址的view
        getAddressView();

    }

    private void getAddressView() {
        //创建布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        addressRecyclerview.setLayoutManager(layoutManager);
        //创建适配器
        addressAdaper = new CloseAddressAdaper(this);
        addressRecyclerview.setAdapter(addressAdaper);
        //选择
        addressAdaper.setCallBackCloseAddress(new CloseAddressAdaper.CallBackCloseAddress() {
            @Override
            public void callBack(AddressBean.ResultBean resultBean) {
                realName.setText(resultBean.getRealName());
                phone.setText(resultBean.getPhone());
                address.setText(resultBean.getAddress());
                //隐藏
                addressRecyclerview.setVisibility(View.GONE);
                butObtain.setBackgroundResource(R.drawable.ic_down_name);
            }
        });
    }

    @Override
    public void getDataSuccess(Object data) {
        //查询地址
        if (data instanceof AddressBean) {
            AddressBean addressBean = (AddressBean) data;
            if (addressBean == null || !addressBean.isSuccess()) {
                Toast.makeText(CloseActivity.this, addressBean.getMessage(), Toast.LENGTH_SHORT).show();
            } else {

                List<AddressBean.ResultBean> result = addressBean.getResult();
                for (AddressBean.ResultBean re : result) {
                    if (re.getWhetherDefault() == 1) {
                        Toast.makeText(CloseActivity.this,"111",Toast.LENGTH_LONG).show();
                        addAddr.setVisibility(View.GONE);
                        addLayoutView.setVisibility(View.VISIBLE);
                        realName.setText(re.getRealName());
                        phone.setText(re.getPhone());
                        address.setText(re.getAddress());
                        id = re.getId();
                    } else {
                        Toast.makeText(CloseActivity.this,"222",Toast.LENGTH_LONG).show();
                        addAddr.setVisibility(View.VISIBLE);
                        addLayoutView.setVisibility(View.GONE);
                    }
                }
                addressAdaper.setmResult(result);
            }

            //创建订单
        } else if (data instanceof CreateOrderBean) {
            CreateOrderBean orderBean = (CreateOrderBean) data;
            Toast.makeText(CloseActivity.this, orderBean.getMessage(), Toast.LENGTH_SHORT).show();
        } else if (data instanceof String) {
            String str = (String) data;
            Toast.makeText(CloseActivity.this, str, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void getDataFail(String error) {
        Toast.makeText(CloseActivity.this, error, Toast.LENGTH_LONG).show();
    }

    /**
     * 点击事件
     */
    private boolean flag = true;

    @OnClick({R.id.addAddr, R.id.but_obtain, R.id.settlement, R.id.icon_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.addAddr:
                Intent intent = new Intent(CloseActivity.this, NewAddressActivity.class);
                startActivityForResult(intent, 100);
                break;
            case R.id.but_obtain:
                if (flag) {
                    addressRecyclerview.setVisibility(View.GONE);
                    butObtain.setBackgroundResource(R.drawable.ic_down_name);
                } else {
                    addressRecyclerview.setVisibility(View.VISIBLE);
                    butObtain.setBackgroundResource(R.drawable.ic_up_name);
                }
                flag = !flag;
                break;
            case R.id.settlement:
                Gson gson = new Gson();
                String json = gson.toJson(closeBeans);
                Map<String, String> map = new HashMap<>();
                map.put("orderInfo", json);
                map.put("totalPrice", String.valueOf(totalPrice1));
                map.put("addressId", String.valueOf(id));
                iPresenter.startRequestPost(Apis.CREATE_ORDER_POST, map, CreateOrderBean.class);
                finish();
                break;
            case R.id.icon_back:
                finish();
                break;

            default:
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        iPresenter.onDetach();
    }
}
