package com.example.onlineretailers.Online.adapter.shopcar.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.onlineretailers.Online.entry.shopcar.bean.AddressBean;
import com.example.onlineretailers.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CloseAddressAdaper extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<AddressBean.ResultBean> mList;
    private Context mContext;

    public CloseAddressAdaper(Context mContext) {
        this.mContext = mContext;
        mList=new ArrayList<>();
    }

    public void setmResult(List<AddressBean.ResultBean> result){
        mList.clear();
        if(result!=null){
            mList.addAll(result);
        }
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.close_shopping_address_item, viewGroup, false);
        ViewHolderCloseAddress holderCloseAddress = new ViewHolderCloseAddress(view);
        return holderCloseAddress;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ViewHolderCloseAddress holderCloseAddress = (ViewHolderCloseAddress) viewHolder;
        holderCloseAddress.address.setText(mList.get(i).getAddress());
        holderCloseAddress.phone.setText(mList.get(i).getPhone());
        holderCloseAddress.realName.setText(mList.get(i).getRealName());
        holderCloseAddress.butDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(callBackCloseAddress!=null){
                    callBackCloseAddress.callBack(mList.get(i));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
    class ViewHolderCloseAddress extends RecyclerView.ViewHolder {
        @BindView(R.id.realName)
        TextView realName;
        @BindView(R.id.phone)
        TextView phone;
        @BindView(R.id.address)
        TextView address;
        @BindView(R.id.but_delete)
        TextView butDelete;
        public ViewHolderCloseAddress(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
    //定义接口
    private CallBackCloseAddress callBackCloseAddress;
    public void setCallBackCloseAddress(CallBackCloseAddress callBackCloseAddress){
        this.callBackCloseAddress = callBackCloseAddress;
    }
    public interface CallBackCloseAddress{
        void callBack(AddressBean.ResultBean resultBean);
    }


}
