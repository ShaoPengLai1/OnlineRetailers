package com.example.onlineretailers.displayview.fragment.cousom;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.onlineretailers.Online.adapter.shopcar.adapter.CloseShoppAdaaper;
import com.example.onlineretailers.Online.adapter.shopcar.adapter.ShopCarAdapter;
import com.example.onlineretailers.Online.entry.shopcar.bean.FindShoppingCartBean;
import com.example.onlineretailers.R;

import java.util.ArrayList;
import java.util.List;

public class CustomViewNum extends LinearLayout {

    private ImageView add;
    private ImageView cut;
    private EditText editText;
    private int num;
    private ShopCarAdapter shopCarAdapter;
    private CloseShoppAdaaper closeShoppAdaaper;
     private List<FindShoppingCartBean.ResultBean> list = new ArrayList<>();
    private int position;

    public CustomViewNum(Context context) {
        super(context);
        initView(context);
    }

    public CustomViewNum(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }
    private Context mContext;
    private void initView(Context context) {
        mContext = context;
        View view=View.inflate(context,R.layout.custom_view_num,null);
        add = view.findViewById(R.id.cart_add);
        cut = view.findViewById(R.id.cart_cut);
        editText = view.findViewById(R.id.cart_num);
        addView(view);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    num = Integer.valueOf(s.toString());
                    list.get(position).setCount(num);
                }catch (Exception e){
                    list.get(position).setCount(1);
                }
                if(callBackListener!=null){
                    callBackListener.callback();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        //点击加号
        add.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                num++;
                editText.setText(num + "");
                list.get(position).setCount(num);
                if(callBackListener!=null){
                    callBackListener.callback();
                }
                if(shopCarAdapter!=null && closeShoppAdaaper==null){
                    shopCarAdapter.notifyItemChanged(position);
                }else if(shopCarAdapter==null && closeShoppAdaaper!=null){
                    closeShoppAdaaper.notifyDataSetChanged();
                }

            }
        });
        //点击减号
        cut.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (num > 1) {
                    num--;
                } else {
                    Toast.makeText(mContext, "不能在减了", Toast.LENGTH_SHORT).show();
                }
                editText.setText(num + "");
                list.get(position).setCount(num);
                if(callBackListener!=null){
                    callBackListener.callback();
                }
                if(shopCarAdapter!=null && closeShoppAdaaper==null){
                    shopCarAdapter.notifyItemChanged(position);
                }else if(shopCarAdapter==null && closeShoppAdaaper!=null){
                    closeShoppAdaaper.notifyDataSetChanged();
                }
            }
        });
    }
    public void setData(Object o, List<FindShoppingCartBean.ResultBean> list, int i){
        this.list=list;
        if(o instanceof ShopCarAdapter){
            ShopCarAdapter shopCarAdapter = (ShopCarAdapter) o;
            this.shopCarAdapter=shopCarAdapter;
        }else if(o instanceof CloseShoppAdaaper){
            CloseShoppAdaaper closeShoppAdaaper = (CloseShoppAdaaper) o;
            this.closeShoppAdaaper = closeShoppAdaaper;
        }
        position = i;
        num = list.get(i).getCount();
        editText.setText(num + "");
    }

    private CallBackListener callBackListener;

    public void setOnCallBack(CallBackListener listener){
        callBackListener=listener;
    }
    public interface CallBackListener{
        void callback();
    }
}
