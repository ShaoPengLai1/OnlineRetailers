package com.example.onlineretailers.Online.adapter.home;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.onlineretailers.Online.entry.home.ByNameSeachBean;
import com.example.onlineretailers.R;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

public class ByNameSeachAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ByNameSeachBean.ResultBean> mList;
    private Context mContext;

    public ByNameSeachAdapter(Context mContext) {
        this.mContext = mContext;
        mList=new ArrayList<>();
    }

    public void setmList(List<ByNameSeachBean.ResultBean> lists) {
        mList.clear();
        if (lists!=null){
            mList.addAll(lists);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(mContext).inflate(R.layout.item_pin,viewGroup,false);
        return new ByNameViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ByNameViewHolder holder= (ByNameViewHolder) viewHolder;
        Uri uri=Uri.parse(mList.get(i).getMasterPic());
        holder.draweeView.setImageURI(uri);
        holder.name.setText(mList.get(i).getCommodityName());
        holder.price.setText("￥"+mList.get(i).getPrice());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener!=null){
                    listener.setonclicklisener(i);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
    public static class ByNameViewHolder extends RecyclerView.ViewHolder{
        private SimpleDraweeView draweeView;
        private TextView name,price;
        public ByNameViewHolder(@NonNull View itemView) {
            super(itemView);
            draweeView=itemView.findViewById(R.id.imageView);
            name=itemView.findViewById(R.id.name);
            price=itemView.findViewById(R.id.price);
        }
    }
    private Cicklistener listener;

    public void onClickListener(Cicklistener listener) {
        this.listener = listener;
    }
    public interface Cicklistener {
        void setonclicklisener(int index);
    }
}
