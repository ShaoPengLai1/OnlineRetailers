package com.example.onlineretailers.Online.adapter.shopcar.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.onlineretailers.Online.entry.shopcar.bean.FindShoppingCartBean;
import com.example.onlineretailers.R;
import com.example.onlineretailers.displayview.fragment.cousom.CustomViewNum;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShopCarAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private List<FindShoppingCartBean.ResultBean> mList;
    private Context mContext;

    public ShopCarAdapter(Context mContext) {
        this.mContext = mContext;
        mList = new ArrayList<>();
    }

    public void setmList(List<FindShoppingCartBean.ResultBean> results) {
        mList.clear();
        if (results != null) {
            mList.addAll(results);
        }
        notifyDataSetChanged();
    }

    public void setDel(int position) {
        mList.remove(position);
        notifyItemRemoved(position);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.cart_shopcar_item, viewGroup, false);
        return new ViewHolderCart(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ViewHolderCart holderCart= (ViewHolderCart) viewHolder;
        holderCart.cartImage.setImageURI(Uri.parse(mList.get(i).getPic()));
        holderCart.cartPrice.setText("￥"+mList.get(i).getPrice());

        holderCart.cartTitle.setText(mList.get(i).getCommodityName());
        holderCart.viewNum.setData(this,mList,i);
        holderCart.viewNum.setOnCallBack(() -> {
            if(callBackList!=null){
                callBackList.callBack(mList);
            }
        });
        holderCart.cartRadio.setChecked(mList.get(i).isChecked());
        holderCart.cartRadio.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked){
                mList.get(i).setChecked(true);
            }else{
                mList.get(i).setChecked(false);
            }
            if(callBackList!=null){
                callBackList.callBack(mList);
            }
        });
        holderCart.buttonDel.setOnClickListener(v -> {
            if(callBackCart!=null){
                callBackCart.callBack(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolderCart extends RecyclerView.ViewHolder {

        @BindView(R.id.cart_radio)
        CheckBox cartRadio;
        @BindView(R.id.cart_image)
        SimpleDraweeView cartImage;
        @BindView(R.id.cart_title)
        TextView cartTitle;
        @BindView(R.id.cart_price)
        TextView cartPrice;
        @BindView(R.id.cart_custom_view_num)
        CustomViewNum viewNum;
        @BindView(R.id.cart_relative)
        RelativeLayout cartRelative;
        @BindView(R.id.shop_car_del)
        Button buttonDel;
        public ViewHolderCart(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
    //定义接口
    private CallBackCart callBackCart;
    public void setCallBackCart(CallBackCart callBackCart){
        this.callBackCart = callBackCart;
    }
    public interface CallBackCart{
        void callBack(int position);
    }


    private CallBackList callBackList;
    public void setCallBackList(CallBackList callBackList){
        this.callBackList = callBackList;
    }
    public interface CallBackList{
        void callBack(List<FindShoppingCartBean.ResultBean> mResult);
    }
}
