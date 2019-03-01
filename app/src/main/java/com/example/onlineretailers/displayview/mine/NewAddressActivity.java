package com.example.onlineretailers.displayview.mine;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.onlineretailers.Online.entry.mine.bean.AddAddressBean;
import com.example.onlineretailers.R;
import com.example.onlineretailers.utils.api.Apis;
import com.example.onlineretailers.utils.mvp.ContractEntity;
import com.example.onlineretailers.utils.mvp.presenter.IPresenterImpl;
import com.lljjcoder.citypickerview.widget.CityPicker;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewAddressActivity extends AppCompatActivity implements ContractEntity.IView {

    @BindView(R.id.recipients_edit)
    EditText recipientsEdit;
    @BindView(R.id.telephone_edit)
    EditText telephoneEdit;
    @BindView(R.id.area_edit)
    EditText areaEdit;
    @BindView(R.id.area_image)
    ImageView areaImage;
    @BindView(R.id.detailed_edit)
    EditText detailedEdit;
    @BindView(R.id.postcode_edit)
    EditText postcodeEdit;
    @BindView(R.id.save)
    TextView save;
    @BindView(R.id.aicon_back)
    ImageView aiconBack;
    private String realName;
    private String phone;
    private String area;
    private String detailed;
    private String postcode;
    private String province;
    private String city;
    private String district;
    private String code;
    private IPresenterImpl iPresenter;
    private CityPicker cityPicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_address);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        iPresenter = new IPresenterImpl(this);
    }

    @OnClick({R.id.save, R.id.area_image,R.id.aicon_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.aicon_back:
                finish();
                break;
            case R.id.save:
                getData();
                break;
            case R.id.area_image:
                //判断输入法的隐藏状态
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm.isActive()) {
                    imm.hideSoftInputFromWindow(view.getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                    //调用CityPicker选取区域
                    selectAddress();
                }
                selectAddress();
                cityPicker.show();
                break;
            default:
                break;
        }
    }

    private void selectAddress() {
        cityPicker = new CityPicker.Builder(NewAddressActivity.this)
                //滚轮文字的大小
                .textSize(20)
                .title("地址选择")
                .backgroundPop(0xa0000000)
                .titleBackgroundColor("#0CB6CA")
                .titleTextColor("#000000")
                .backgroundPop(0xa0000000)
                .confirTextColor("#000000")
                .cancelTextColor("#000000")
                .province("xx省")
                .city("xx市")
                .district("xx区")
                //滚轮文字的颜色
                .textColor(Color.parseColor("#000000"))
                //省份滚轮是否循环显示
                .provinceCyclic(true)
                //城市滚轮是否循环显示
                .cityCyclic(false)
                //地区（县）滚轮是否循环显示
                .districtCyclic(false)
                //滚轮显示的item个数
                .visibleItemsCount(7)
                //滚轮item间距
                .itemPadding(10)
                .onlyShowProvinceAndCity(false)
                .build();
        //监听方法，获取选择结果
        cityPicker.setOnCityItemClickListener(new CityPicker.OnCityItemClickListener() {
            @Override
            public void onSelected(String... citySelected) {
                //省份
                province = citySelected[0];
                //城市
                city = citySelected[1];
                //区县（如果设定了两级联动，那么该项返回空）
                district = citySelected[2];
                //邮编
                code = citySelected[3];
                areaEdit.setText(province + city + district);
                postcodeEdit.setText(code);
            }

            @Override
            public void onCancel() {


            }
        });

    }

    private void getData() {
//获取输入框的值
        realName = recipientsEdit.getText().toString();
        phone = telephoneEdit.getText().toString();
        area = areaEdit.getText().toString();
        detailed = detailedEdit.getText().toString();
        postcode = postcodeEdit.getText().toString();
        Map<String, String> map = new HashMap();
        map.put("realName", realName);
        map.put("phone", phone);
        map.put("address", area + " " + detailed);
        map.put("zipCode", postcode);
        iPresenter.startRequestPost(Apis.ADD_RECEIVE_ADDRESS_POST, map, AddAddressBean.class);
    }

    @Override
    public void getDataSuccess(Object data) {
        if (data instanceof AddAddressBean) {
            AddAddressBean addressBean = (AddAddressBean) data;
            Toast.makeText(NewAddressActivity.this, addressBean.getMessage(), Toast.LENGTH_SHORT).show();
            setResult(200);
            finish();
        }
    }

    @Override
    public void getDataFail(String error) {
        Toast.makeText(NewAddressActivity.this, error, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        iPresenter.onDetach();
    }
}
