package com.example.onlineretailers.displayview.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.onlineretailers.Online.entry.mine.bean.InformationBean;
import com.example.onlineretailers.R;
import com.example.onlineretailers.displayview.mine.activity.AddressActivity;
import com.example.onlineretailers.displayview.mine.activity.FootPrintActivity;
import com.example.onlineretailers.displayview.mine.activity.MyCircleActivity;
import com.example.onlineretailers.displayview.mine.activity.PresonalDataActivity;
import com.example.onlineretailers.displayview.mine.activity.WalletActivity;
import com.example.onlineretailers.utils.api.Apis;
import com.example.onlineretailers.utils.mvp.ContractEntity;
import com.example.onlineretailers.utils.mvp.presenter.IPresenterImpl;
import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class MineFragment extends Fragment implements ContractEntity.IView {

    @BindView(R.id.mine_background_image)
    ImageView mineBackgroundImage;
    @BindView(R.id.mine_text_name)
    TextView mineTextName;
    @BindView(R.id.data_image)
    ImageView dataImage;
    @BindView(R.id.data_view)
    View dataView;
    @BindView(R.id.presonal_text)
    TextView presonalText;
    @BindView(R.id.circle_image)
    ImageView circleImage;
    @BindView(R.id.circle_view)
    View circleView;
    @BindView(R.id.circle_text)
    TextView circleText;
    @BindView(R.id.footprint_image)
    ImageView footprintImage;
    @BindView(R.id.footprint_view)
    View footprintView;
    @BindView(R.id.footprint_text)
    TextView footprintText;
    @BindView(R.id.wallet_image)
    ImageView walletImage;
    @BindView(R.id.wallet_view)
    View walletView;
    @BindView(R.id.wallet_text)
    TextView walletText;
    @BindView(R.id.address_image)
    ImageView addressImage;
    @BindView(R.id.address_view)
    View addressView;
    @BindView(R.id.address_text)
    TextView addressText;
    @BindView(R.id.constrain)
    ConstraintLayout constrain;
    @BindView(R.id.mine_simple)
    SimpleDraweeView mineSimple;
    Unbinder unbinder;
    private IPresenterImpl iPresenter;
    private String headPic;
    private String nickName;
    private String password;
    private InformationBean.ResultBean result;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        iPresenter = new IPresenterImpl(this);
        iPresenter.startRequestGet(Apis.QUERYBYID_GET, null, InformationBean.class);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        iPresenter.onDetach();
    }

    @Override
    public void getDataSuccess(Object data) {
        if (data instanceof InformationBean) {
            InformationBean informationBean = (InformationBean) data;
            headPic = informationBean.getResult().getHeadPic();
            nickName = informationBean.getResult().getNickName();
            password = informationBean.getResult().getPassword();
            result = informationBean.getResult();
            if (informationBean == null || !informationBean.isSuccess()) {
                Toast.makeText(getActivity(), informationBean.getMessage(), Toast.LENGTH_SHORT).show();
            } else {
                mineSimple.setImageURI(Uri.parse(headPic));
                mineTextName.setText(nickName);
            }
        }
    }

    @Override
    public void getDataFail(String error) {
        Toast.makeText(getActivity(), error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == 200) {
            iPresenter.startRequestGet(Apis.QUERYBYID_GET, null, InformationBean.class);
        }
    }

    @OnClick({R.id.presonal_text, R.id.circle_text, R.id.footprint_text, R.id.wallet_text, R.id.address_text})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.presonal_text:
                Intent intent = new Intent(getActivity(),PresonalDataActivity.class);
                intent.putExtra("result",result);
                startActivityForResult(intent,100);
                break;
            case R.id.circle_text:
                startActivity(new Intent(getActivity(),MyCircleActivity.class));
                break;
            case R.id.footprint_text:
                startActivity(new Intent(getActivity(),FootPrintActivity.class));
                break;
            case R.id.wallet_text:
                startActivity(new Intent(getActivity(),WalletActivity.class));
                break;
            case R.id.address_text:
                startActivity(new Intent(getActivity(),AddressActivity.class));
                break;
                default:
                    break;
        }
    }
}
