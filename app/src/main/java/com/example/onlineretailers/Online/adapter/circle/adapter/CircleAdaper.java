package com.example.onlineretailers.Online.adapter.circle.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.onlineretailers.Online.adapter.mine.cousom.CustomMultiImageView;
import com.example.onlineretailers.Online.entry.circle.bean.CircleBean;
import com.example.onlineretailers.R;
import com.facebook.drawee.view.SimpleDraweeView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
/**
 *圈子的适配器
 * */
public class CircleAdaper extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<CircleBean.ResultBean> mResult;
    private Context mContext;

    public CircleAdaper(Context mContext) {
        this.mContext = mContext;
        mResult = new ArrayList<>();
    }

    public void setmResult(List<CircleBean.ResultBean> results) {
        mResult.clear();
        if (results != null) {
            mResult.addAll(results);
        }
        notifyDataSetChanged();
    }

    public void addmResult(List<CircleBean.ResultBean> results) {
        if (results != null) {
            mResult.addAll(results);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.circle_item_image, viewGroup, false);
        ViewHolderCircleImage holderCircleImage = new ViewHolderCircleImage(view);
        return holderCircleImage;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
        ViewHolderCircleImage holderCircleImage = (ViewHolderCircleImage) viewHolder;
        //设置头像
        holderCircleImage.topSimple.setImageURI(Uri.parse(mResult.get(i).getHeadPic()));
        //设置名字
        holderCircleImage.circleName.setText(mResult.get(i).getNickName());
        //设置内容
        holderCircleImage.circleTitle.setText(mResult.get(i).getContent());
        String[] image = mResult.get(i).getImage().split("\\,");
        List<String> sList = new ArrayList<>();
        for(int a = 0;a<image.length;a++){
            sList.add(image[a]);
        }
        holderCircleImage.centerSimple.setList(sList);
        //设置图片
        //Glide.with(mContext).load(mResult.get(i).getImage()).into(holderCircleImage.centerSimple);
        //设置时间类型


        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(
                new java.util.Date(mResult.get(i).getCreateTime()));
        holderCircleImage.circleDate.setText(date);
        //设置赞
        holderCircleImage.textNum.setText(mResult.get(i).getGreatNum() + "");
        if (mResult.get(i).getWhetherGreat() == 1) {
            holderCircleImage.buttonPraise.setImageResource(R.mipmap.common_btn_prise_s);
        }else {
            holderCircleImage.buttonPraise.setImageResource(R.mipmap.common_btn_prise_n);
        }

        holderCircleImage.buttonPraise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickCallBack != null) {
                    clickCallBack.callBack(mResult.get(i).getWhetherGreat(), i, mResult.get(i).getId());
                }
            }
        });
    }

    //点赞的方法
    public void getGivePraise(int position) {
        mResult.get(position).setWhetherGreat(1);
        mResult.get(position).setGreatNum(mResult.get(position).getGreatNum() + 1);
        notifyDataSetChanged();
    }
    //取消点赞的方法
    public void getCancelPraise(int position) {
        mResult.get(position).setWhetherGreat(2);
        mResult.get(position).setGreatNum(mResult.get(position).getGreatNum() - 1);
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return mResult.size();
    }

    /**
     * 创建ViewHolder
     */
    class ViewHolderCircleImage extends RecyclerView.ViewHolder {
        @BindView(R.id.top_simple)
        SimpleDraweeView topSimple;
        @BindView(R.id.circle_name)
        TextView circleName;
        @BindView(R.id.circle_date)
        TextView circleDate;
        @BindView(R.id.circle_title)
        TextView circleTitle;
        @BindView(R.id.center_simple)
        CustomMultiImageView centerSimple;
        @BindView(R.id.button_praise)
        ImageView buttonPraise;
        @BindView(R.id.text_num)
        TextView textNum;

        public ViewHolderCircleImage(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    //定义接口
    private ClickCallBack clickCallBack;

    public void setClickCallBack(ClickCallBack clickCallBack) {
        this.clickCallBack = clickCallBack;
    }

    public interface ClickCallBack {
        void callBack(int i, int position, int id);
    }
}
