package com.example.onlineretailers.Online.adapter.home.bottom;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.onlineretailers.Online.adapter.home.top.TopHomeAdapter;
import com.example.onlineretailers.Online.entry.home.bottom.ByIdSeachBean;
import com.example.onlineretailers.R;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

public class ByIdSeachAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ByIdSeachBean.ResultBean> mList;
    private Context mContext;

    public ByIdSeachAdapter(Context mContext) {
        this.mContext = mContext;
        mList=new ArrayList<>();
    }

    public void setmList(List<ByIdSeachBean.ResultBean> lists) {
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
        return new ByIdViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ByIdViewHolder holder= (ByIdViewHolder) viewHolder;
        Uri uri=Uri.parse(mList.get(i).getMasterPic());
        holder.imageView.setImageURI(uri);
        holder.name.setText(mList.get(i).getCommodityName());
        holder.price.setText("￥"+mList.get(i).getPrice());
        holder.itemView.setOnClickListener(v -> {
            if (listener!=null){
                listener.setonclicklisener(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
    public static class ByIdViewHolder extends RecyclerView.ViewHolder{
        private SimpleDraweeView imageView;
        private TextView name,price;
        public ByIdViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.imageView);
            name=itemView.findViewById(R.id.name);
            price=itemView.findViewById(R.id.price);
        }
    }
    private Cicklistener listener;

    public void onClick(Cicklistener listener) {
        this.listener = listener;
    }
    public interface Cicklistener {
        void setonclicklisener(int index);
    }
}
