package com.example.onlineretailers.Online.adapter.shopcar.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.onlineretailers.Online.entry.shopcar.bean.FindShoppingCartBean;
import com.example.onlineretailers.R;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CloseShoppAdaaper extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<FindShoppingCartBean.ResultBean> mList;
    private Context mContext;

    public CloseShoppAdaaper(Context mContext) {
        this.mContext = mContext;
        mList=new ArrayList<>();
    }

    public void setmList(List<FindShoppingCartBean.ResultBean> lists) {
        mList.clear();
        if (lists!=null){
            mList.addAll(lists);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(mContext).inflate(R.layout.close_shopping_item,viewGroup,false);
        return new ViewHolderClose(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

        ViewHolderClose holderClose = (ViewHolderClose) viewHolder;
        holderClose.cartImage.setImageURI(Uri.parse(mList.get(i).getPic()));
        holderClose.cartPrice.setText("￥"+mList.get(i).getPrice());
        holderClose.cartTitle.setText(mList.get(i).getCommodityName());
        holderClose.num.setText("共"+mList.get(i).getCount()+"件");
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
    class ViewHolderClose extends RecyclerView.ViewHolder {
        @BindView(R.id.cart_image)
        SimpleDraweeView cartImage;
        @BindView(R.id.cart_title)
        TextView cartTitle;
        @BindView(R.id.cart_price)
        TextView cartPrice;
        @BindView(R.id.text_num)
        TextView num;
        public ViewHolderClose(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
    //定义接口
    private CallBackClose callBackClose;
    public void setCallBackClose(CallBackClose callBackClose){
        this.callBackClose = callBackClose;
    }
    public interface CallBackClose{
        void callBack(ArrayList<FindShoppingCartBean.ResultBean> list);
    }
}
