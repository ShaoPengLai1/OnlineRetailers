package com.example.onlineretailers.displayview.fragment;

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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.example.onlineretailers.Online.adapter.shopcar.adapter.ShopCarAdapter;
import com.example.onlineretailers.Online.entry.shopcar.bean.FindShoppingCartBean;
import com.example.onlineretailers.R;
import com.example.onlineretailers.displayview.shop.activity.CloseActivity;
import com.example.onlineretailers.utils.api.Apis;
import com.example.onlineretailers.utils.mvp.ContractEntity;
import com.example.onlineretailers.utils.mvp.presenter.IPresenterImpl;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class ShoppingFragment extends Fragment implements ContractEntity.IView {

    @BindView(R.id.btn_close)
    Button btnClose;
    @BindView(R.id.query)
    CheckBox query;
    @BindView(R.id.query_text)
    TextView queryText;
    @BindView(R.id.heji)
    TextView heji;
    @BindView(R.id.total)
    TextView total;
    @BindView(R.id.cart_recyycle)
    RecyclerView cartRecyycle;
    Unbinder unbinder;
    private IPresenterImpl iPresenter;
    private ShopCarAdapter carAdapter;
    private List<FindShoppingCartBean.ResultBean> result;
    private ArrayList<FindShoppingCartBean.ResultBean> checkList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shopping, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;

    }

    private void initView() {
        iPresenter = new IPresenterImpl(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        cartRecyycle.setLayoutManager(layoutManager);
        // 创建适配器
        carAdapter = new ShopCarAdapter(getActivity());
        cartRecyycle.setAdapter(carAdapter);
        iPresenter.startRequestGet(Apis.FIND_SHOPPING_CART_GET, null, FindShoppingCartBean.class);

        carAdapter.setCallBackCart(position -> {
            carAdapter.setDel(position);
            carAdapter.notifyDataSetChanged();
        });
        carAdapter.setCallBackList(mResult -> {
            double totalPrice = 0;
            int num = 0;
            //实例化集合存入勾选的商品
            checkList = new ArrayList<>();
            for (int i = 0; i < mResult.size(); i++) {
                //获取选中状态
                if (mResult.get(i).isChecked()) {
                    checkList.add(mResult.get(i));
                    totalPrice = totalPrice + (mResult.get(i).getCount() * mResult.get(i).getPrice());
                    num++;
                }
            }
            if (num < mResult.size()) {
                query.setChecked(false);
            } else {
                query.setChecked(true);
            }
            total.setText("￥" + totalPrice);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void getDataSuccess(Object data) {
        if (data instanceof FindShoppingCartBean) {
            FindShoppingCartBean cartBean = (FindShoppingCartBean) data;
            result = cartBean.getResult();
            if (cartBean == null || !cartBean.isSuccess()) {
                Toast.makeText(getActivity(), cartBean.getMessage(), Toast.LENGTH_SHORT).show();
            } else {
                carAdapter.setmList(cartBean.getResult());
            }
        }
    }

    @Override
    public void getDataFail(String error) {
        Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
    }

    private void getCheck(boolean checked) {
        double totalPrice = 0;
        for (int i = 0; i < result.size(); i++) {
            result.get(i).setChecked(checked);
            totalPrice = totalPrice + (result.get(i).getCount() * result.get(i).getPrice());
        }
        if (checked) {
            total.setText("￥" + totalPrice);
        } else {
            total.setText("￥0.00");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == 200) {
            iPresenter.startRequestGet(Apis.FIND_SHOPPING_CART_GET, null, FindShoppingCartBean.class);
        }
    }

    @OnClick({R.id.btn_close, R.id.query})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_close:
                if(checkList!=null && checkList.size()!=0){
                    Intent intent = new Intent(getActivity(),CloseActivity.class);
                    intent.putParcelableArrayListExtra("checkList",checkList);
                    //startActivity(intent);
                    startActivityForResult(intent,100);
                }else{
                    Toast.makeText(getActivity(), "请选择要提交的商品", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.query:
                getCheck(query.isChecked());
                carAdapter.notifyDataSetChanged();
                break;
        }
    }
}
