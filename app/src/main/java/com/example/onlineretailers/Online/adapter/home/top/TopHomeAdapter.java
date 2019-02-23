package com.example.onlineretailers.Online.adapter.home.top;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.onlineretailers.Online.entry.home.top.TopHomeBean;
import com.example.onlineretailers.R;

import java.util.ArrayList;
import java.util.List;

public class TopHomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<TopHomeBean.ResultBean> mList;
    private Context mContext;

    public TopHomeAdapter(Context mContext) {
        this.mContext = mContext;
        mList=new ArrayList<>();
    }

    public void setmList(List<TopHomeBean.ResultBean> lists) {
        mList.clear();
        if (lists!=null){
            mList.addAll(lists);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(mContext).inflate(R.layout.popup_item_top,viewGroup,false);
        return new TopRecyclerView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        TopRecyclerView holder= (TopRecyclerView) viewHolder;
        holder.topView.setText(mList.get(i).getName());
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
    public static class TopRecyclerView extends RecyclerView.ViewHolder{
        public TextView topView;
        public TopRecyclerView(@NonNull View itemView) {
            super(itemView);
            topView=itemView.findViewById(R.id.recycle_top_item);
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
