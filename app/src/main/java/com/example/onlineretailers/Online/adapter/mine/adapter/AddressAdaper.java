package com.example.onlineretailers.Online.adapter.mine.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.onlineretailers.Online.entry.shopcar.bean.AddressBean;
import com.example.onlineretailers.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddressAdaper extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<AddressBean.ResultBean> mList;
    private Context mContext;
    private int position;

    public AddressAdaper(Context mContext) {
        this.mContext = mContext;
        mList=new ArrayList<>();
    }

    public void setmList(List<AddressBean.ResultBean> lists) {
        mList.clear();
        if (lists!=null){
            mList.addAll(lists);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.address_item, viewGroup, false);
        ViewHolderAddress holderAddress = new ViewHolderAddress(view);
        return holderAddress;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        final ViewHolderAddress holderAddress = (ViewHolderAddress) viewHolder;
        holderAddress.name.setText(mList.get(i).getRealName());
        holderAddress.telephone.setText(mList.get(i).getPhone());
        holderAddress.address.setText(mList.get(i).getAddress());
        holderAddress.updateAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(callBackUpdata!=null){
                    callBackUpdata.callBack(mList.get(i));
                }
            }
        });
        if(mList.get(i).getWhetherDefault() == 1){
            holderAddress.radio.setChecked(true);
        }else{
            holderAddress.radio.setChecked(false);
        }
        holderAddress.radio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //设置默认值
                position=i;
                setAllunCheck();
                if(callBackDefault!=null){
                    callBackDefault.callBack(mList.get(i).getId());
                }
            }
        });
    }

    private void setAllunCheck() {
        int size = mList.size();
        for (int i=0;i<size;i++){
            if(i==position){
                mList.get(i).setWhetherDefault(1);
            }else{
                mList.get(i).setWhetherDefault(2);
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
    class ViewHolderAddress extends RecyclerView.ViewHolder {
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.telephone)
        TextView telephone;
        @BindView(R.id.address)
        TextView address;
        @BindView(R.id.radio)
        RadioButton radio;
        @BindView(R.id.default_address)
        TextView defaultAddress;
        @BindView(R.id.update_address)
        Button updateAddress;
        @BindView(R.id.del_address)
        Button delAddress;
        public ViewHolderAddress(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
    //定义接口
    private CallBackUpdata callBackUpdata;
    public void setCallBackUpdata(CallBackUpdata callBackUpdata){
        this.callBackUpdata = callBackUpdata;
    }
    public interface CallBackUpdata{
        void callBack(AddressBean.ResultBean resultBean);
    }
    //定义默认选中的接口
    private CallBackDefault callBackDefault;
    public void setCallBackDefault(CallBackDefault callBackDefault){
        this.callBackDefault = callBackDefault;
    }
    public interface CallBackDefault{
        void callBack(int id);
    }
}
