package com.example.onlineretailers.displayview.details;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.example.onlineretailers.Online.entry.shopcar.bean.CommentBean;

import java.util.ArrayList;
import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<CommentBean.ResultBean> mLists;
    private Context mContext;

    public CommentAdapter(Context mContext) {
        this.mContext = mContext;
        mLists=new ArrayList<>();
    }

    public void setmLists(List<CommentBean.ResultBean> lists) {
        mLists.clear();
        if (lists!=null){
            mLists.addAll(lists);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
