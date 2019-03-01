package com.example.onlineretailers.Online.adapter.mine.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.onlineretailers.Online.entry.mine.bean.WalletBean;
import com.example.onlineretailers.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WalletAdaper extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<WalletBean.ResultBean.DetailListBean> mList;
    private Context mContext;
    public WalletAdaper(Context mContext) {
        this.mContext = mContext;
        mList = new ArrayList<>();
    }
    public void setmBean(List<WalletBean.ResultBean.DetailListBean> beans){
        mList.clear();
        if(beans!=null){
            mList.addAll(beans);
        }
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.wallet_item, viewGroup, false);
        ViewHolderWallet holderWallet = new ViewHolderWallet(view);
        return holderWallet;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ViewHolderWallet holderWallet = (ViewHolderWallet) viewHolder;
        String amount = mList.get(i).getAmount();
        int integer = Integer.valueOf(mList.get(i).getAmount());
        int i1 = integer * 1000;

        holderWallet.sum.setText("￥"+integer);
        //设置时间类型
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(
                new Date(mList.get(i).getCreateTime()));
        holderWallet.time.setText(date);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
    class ViewHolderWallet extends RecyclerView.ViewHolder {
        @BindView(R.id.sum)
        TextView sum;
        @BindView(R.id.time)
        TextView time;
        public ViewHolderWallet(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
