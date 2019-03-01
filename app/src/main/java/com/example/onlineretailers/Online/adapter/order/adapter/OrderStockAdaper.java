package com.example.onlineretailers.Online.adapter.order.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.onlineretailers.Online.adapter.order.bean.OrderBean;
import com.example.onlineretailers.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderStockAdaper extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<OrderBean.OrderListBean> mList;
    private Context mContext;

    public OrderStockAdaper(Context mContext) {
        this.mContext = mContext;
        mList=new ArrayList<>();
    }
    public void setmOrder(List<OrderBean.OrderListBean> orders) {
        mList.clear();
        if (orders != null) {
            mList.addAll(orders);
        }
        notifyDataSetChanged();
    }

    public void addmOrder(List<OrderBean.OrderListBean> orders) {
        if (orders != null) {
            mList.addAll(orders);
        }
        notifyDataSetChanged();
    }
    //删除
    public void setDel(int position){
        mList.remove(position);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.order_stocks_fragment_item, viewGroup, false);
        ViewHolderStock holderStock = new ViewHolderStock(view);
        return holderStock;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ViewHolderStock holderStock = (ViewHolderStock) viewHolder;
        holderStock.mark.setText(mList.get(i).getOrderId());
        //设置时间类型
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(
                new Date(mList.get(i).getOrderTime()));
        holderStock.time.setText(date);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        holderStock.recycleTitle.setLayoutManager(layoutManager);
        OrderStockItemAdaper stockItemAdaper = new OrderStockItemAdaper(mContext);
        holderStock.recycleTitle.setAdapter(stockItemAdaper);
        stockItemAdaper.setmData(mList.get(i).getDetailList());
        //删除
        holderStock.del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(callBackDel!=null){
                    callBackDel.callBack(mList.get(i).getOrderId(),i);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolderStock extends RecyclerView.ViewHolder {
        @BindView(R.id.dingdan)
        TextView dingdan;
        @BindView(R.id.mark)
        TextView mark;
        @BindView(R.id.del)
        TextView del;
        @BindView(R.id.recycle_title)
        RecyclerView recycleTitle;
        @BindView(R.id.time)
        TextView time;
        @BindView(R.id.order_constr)
        ConstraintLayout orderConstr;
        public ViewHolderStock(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    /**定义接口删除订单*/
    private CallBackDel callBackDel;
    public void setCallBackDel(CallBackDel callBackDel){
        this.callBackDel = callBackDel;
    }
    public interface CallBackDel{
        void callBack(String orderId,int position);
    }
}
