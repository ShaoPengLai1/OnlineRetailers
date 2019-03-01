package com.example.onlineretailers.Online.adapter.mine.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.onlineretailers.Online.entry.mine.bean.FootPrintBean;
import com.example.onlineretailers.R;
import com.facebook.drawee.view.SimpleDraweeView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FootAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<FootPrintBean.ResultBean> mList;
    private Context mContext;

    public FootAdapter(Context mContext) {
        this.mContext = mContext;
        mList=new ArrayList<>();
    }

    public void setmList(List<FootPrintBean.ResultBean> lists) {
        mList.clear();
        if (lists!=null){
            mList.addAll(lists);
        }
        notifyDataSetChanged();
    }
    public void addLists(List<FootPrintBean.ResultBean> lists) {

        if (lists!=null){
            mList.addAll(lists);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.foot_recycle_item,viewGroup,false);
        return new MineFootViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        MineFootViewHolder footViewHolder= (MineFootViewHolder) viewHolder;
        Uri uri=Uri.parse(mList.get(i).getMasterPic());
        footViewHolder.imageView.setImageURI(uri);
        //Glide.with(mContext).load(mList.get(i).getMasterPic()).into(footViewHolder.imageView);
        footViewHolder.name.setText(mList.get(i).getCommodityName());
        footViewHolder.price.setText("￥"+mList.get(i).getPrice()+"");
        footViewHolder.llcs.setText("已浏览"+mList.get(i).getBrowseNum()+"次");
        footViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null){
                    listener.setonclicklisener(i);
                }
            }
        });
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(
                new Date(mList.get(i).getBrowseTime()));
        footViewHolder.llsj.setText(date);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class MineFootViewHolder extends RecyclerView.ViewHolder{
        SimpleDraweeView imageView;
        TextView name;
        TextView price;
        TextView llcs;
        TextView llsj;
        public MineFootViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.imageView);
            name=itemView.findViewById(R.id.name);
            price=itemView.findViewById(R.id.price);
            llcs=itemView.findViewById(R.id.llcs);
            llsj=itemView.findViewById(R.id.llsj);
        }
    }
    private FootAdapter.Cicklistener listener;

    public void result(FootAdapter.Cicklistener listener) {
        this.listener = listener;
    }
    public interface Cicklistener {
        void setonclicklisener(int index);
    }
}
