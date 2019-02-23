package com.example.onlineretailers.Online.adapter.home.bottom;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.onlineretailers.Online.adapter.home.top.TopHomeAdapter;
import com.example.onlineretailers.Online.entry.home.bottom.BottomHomeBean;
import com.example.onlineretailers.R;

import java.util.ArrayList;
import java.util.List;

public class BottomHomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<BottomHomeBean.ResultBean> mList;
    private Context mContext;

    public BottomHomeAdapter(Context mContext) {
        this.mContext = mContext;
        mList=new ArrayList<>();
    }

    public void setmList(List<BottomHomeBean.ResultBean> lists) {
        mList.clear();
        if (lists!=null){
            mList.addAll(lists);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(mContext).inflate(R.layout.popup_item_bottom,viewGroup,false);
        return new BottomVieholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        BottomVieholder holder= (BottomVieholder) viewHolder;
        holder.tv.setText(mList.get(i).getName());
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
    public static class BottomVieholder extends RecyclerView.ViewHolder{
        private TextView tv;
        public BottomVieholder(@NonNull View itemView) {
            super(itemView);
            tv=itemView.findViewById(R.id.bottomTv1);
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
