package com.example.onlineretailers.Online.adapter.shopcar.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.onlineretailers.Online.entry.shopcar.bean.CommentBean;
import com.example.onlineretailers.Online.entry.shopcar.bean.GoodsDetailBean;
import com.example.onlineretailers.R;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CommentListAdapter extends RecyclerView.Adapter<CommentListAdapter.CommentViewHolder> {

    private List<CommentBean.ResultBean> mList;
    private Context mContext;

    public CommentListAdapter(Context mContext) {
        this.mContext = mContext;
        mList=new ArrayList<>();

    }

    public void setmList(List<CommentBean.ResultBean> lists) {
        mList.clear();
        if (lists!=null){
            mList.addAll(lists);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CommentListAdapter.CommentViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view=LayoutInflater.from(mContext).inflate(R.layout.adapter_commentlist_item,viewGroup,false);
        CommentViewHolder commentViewHolder=new CommentViewHolder(view);
        return commentViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CommentListAdapter.CommentViewHolder commentViewHolder, int i) {
        DraweeController controller=Fresco.newDraweeControllerBuilder()
                .setUri(mList.get(i).getHeadPic())
                .build();
        commentViewHolder.icon_head.setController(controller);
        commentViewHolder.icon_head.setScaleType(ImageView.ScaleType.FIT_XY);
        if (mList.get(i).getImage().equals("")) {
            commentViewHolder.icon_artcile.setVisibility(View.GONE);
        } else {
            String[] split = mList.get(i).getImage().split(",");
            DraweeController controller1 = Fresco.newDraweeControllerBuilder()
                    .setUri(split[0])
                    .build();
            commentViewHolder.icon_artcile.setController(controller1);
            commentViewHolder.icon_artcile.setScaleType(ImageView.ScaleType.FIT_XY);
        }

        commentViewHolder.text_name.setText(mList.get(i).getNickName());
        String dateTime = new SimpleDateFormat("yyyy-MM-dd").format(new Date(mList.get(i).getCreateTime()));
        commentViewHolder.text_time.setText(dateTime);
        commentViewHolder.text_artcile.setText(mList.get(i).getContent());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
    public class CommentViewHolder extends RecyclerView.ViewHolder {
        SimpleDraweeView icon_head, icon_artcile;
        TextView text_name, text_time, text_artcile;
        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            icon_head = itemView.findViewById(R.id.commentlist_iconhead);
            icon_artcile = itemView.findViewById(R.id.commentlist_icon_artcile);
            text_name = itemView.findViewById(R.id.commentlist_text_name);
            text_time = itemView.findViewById(R.id.commentlist_text_time);
            text_artcile = itemView.findViewById(R.id.commentlist_text_artcile);
        }
    }
}
