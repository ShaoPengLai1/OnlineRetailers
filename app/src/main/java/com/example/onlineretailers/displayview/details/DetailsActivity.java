package com.example.onlineretailers.displayview.details;


import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.onlineretailers.Online.adapter.shopcar.adapter.DetailThingsBannerAdapter;
import com.example.onlineretailers.Online.entry.shopcar.bean.AddShopCarBean;
import com.example.onlineretailers.Online.entry.shopcar.bean.GoodsDetailBean;
import com.example.onlineretailers.Online.entry.shopcar.bean.ShopCarBean;
import com.example.onlineretailers.Online.entry.shopcar.bean.ShoppingCarBean;
import com.example.onlineretailers.R;
import com.example.onlineretailers.displayview.details.cousom.DetailThingsScrollView;
import com.example.onlineretailers.utils.EventBean.EventBean;
import com.example.onlineretailers.utils.api.Apis;
import com.example.onlineretailers.utils.mvp.ContractEntity;
import com.example.onlineretailers.utils.mvp.presenter.IPresenterImpl;
import com.google.gson.Gson;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class DetailsActivity extends AppCompatActivity implements ContractEntity.IView {


    @BindView(R.id.price)
    TextView price;
    @BindView(R.id.saleNum)
    TextView saleNum;
    @BindView(R.id.commodityName)
    TextView commodityName;
    @BindView(R.id.weight)
    TextView weight;
    @BindView(R.id.webview)
    WebView webview;
    @BindView(R.id.shopAdd)
    ImageView shopAdd;
    @BindView(R.id.shopBuy)
    ImageView shopBuy;
    @BindView(R.id.viewpager_icon_show)
    ViewPager viewpagerIconShow;
    @BindView(R.id.text_icon_showNum)
    TextView textIconShowNum;
    @BindView(R.id.detailScroll)
    DetailThingsScrollView detailScroll;
    @BindView(R.id.aicon_back)
    ImageView aiconBack;
    @BindView(R.id.things_title)
    TextView thingsTitle;
    @BindView(R.id.details_title)
    TextView detailsTitle;
    @BindView(R.id.comments_title)
    TextView commentsTitle;
    @BindView(R.id.text_things)
    TextView textThings;
    @BindView(R.id.text_details)
    TextView textDetails;
    @BindView(R.id.text_comments)
    TextView textComments;
    @BindView(R.id.change_title)
    RelativeLayout changeTitle;
    @BindView(R.id.relative_changeColor)
    RelativeLayout relativeChangeColor;
    private IPresenterImpl iPresenter;
    private GoodsDetailBean goodsBean;
    private int commodityId;
    private int iconCount;
    private int num=1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);
        iPresenter=new IPresenterImpl(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);

    }

    @Subscribe (threadMode = ThreadMode.POSTING, sticky = true)
    public void onEvent(EventBean evBean) {
        if (evBean.getName().equals("goods")) {
            goodsBean = (GoodsDetailBean) evBean.getClazz();
            commodityId = goodsBean.getResult().getCommodityId();
            loadData();
        }
    }

    @SuppressLint("JavascriptInterface")
    private void loadData() {

        //手动轮播
        viewpagerIconShow.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                textIconShowNum.setText((position + 1) + "/" + iconCount);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        String details = goodsBean.getResult().getDetails();
        String picture = goodsBean.getResult().getPicture();
        String[] split = picture.split(",");
        WebSettings settings = webview.getSettings();
        settings.setJavaScriptEnabled(true);//支持JS
        String js = "<script type=\"text/javascript\">" +
                "var imgs = document.getElementsByTagName('img');" + // 找到img标签
                "for(var i = 0; i<imgs.length; i++){" +  // 逐个改变
                "imgs[i].style.width = '100%';" +  // 宽度改为100%
                "imgs[i].style.height = 'auto';" +
                "}" +
                "</script>";
        webview.loadDataWithBaseURL(null, details + js, "text/html", "utf-8", null);

        price.setText("￥" + goodsBean.getResult().getPrice() + "");
        commodityName.setText(goodsBean.getResult().getCommodityName());
        weight.setText(goodsBean.getResult().getWeight() + "kg");
        DetailThingsBannerAdapter bannerAdapter = new DetailThingsBannerAdapter(split, this);
        iconCount = bannerAdapter.getCount();
        viewpagerIconShow.setAdapter(bannerAdapter);

        detailScroll.setScrollViewListener((scrollView, l, t, oldl, oldt) -> {
            if (t<=0){
                changeTitle.setVisibility(View.GONE);
                relativeChangeColor.setBackgroundColor(Color.argb(0, 0, 0, 0));
            } else if (t > 0 && t < 200) {
                changeTitle.setVisibility(View.VISIBLE);
                float scale = (float) t / 200;
                float alpha = 255 * scale;
                relativeChangeColor.setBackgroundColor(Color.parseColor("#505d5d5d"));
            }
            if (0 < t && t < 600) {
                textThings.setBackgroundColor(Color.RED);
                textDetails.setBackgroundColor(Color.parseColor("#505d5d5d"));
                textComments.setBackgroundColor(Color.parseColor("#505d5d5d"));
            } else if (601 < t && t < 1600) {
                textThings.setBackgroundColor(Color.parseColor("#505d5d5d"));
                textDetails.setBackgroundColor(Color.RED);
                textComments.setBackgroundColor(Color.parseColor("#505d5d5d"));
            } else if (t > 1601) {
                textThings.setBackgroundColor(Color.parseColor("#505d5d5d"));
                textDetails.setBackgroundColor(Color.parseColor("#505d5d5d"));
                textComments.setBackgroundColor(Color.RED);
            }
        });
        aiconBack.setOnClickListener(v -> finish());
    }

    @Override
    public void getDataSuccess(Object data) {
        if (data instanceof ShopCarBean){
            ShopCarBean shopCarBean= (ShopCarBean) data;
            if (shopCarBean.getMessage().equals("查询成功")){

                List<ShoppingCarBean> list = new ArrayList<>();
                List<ShopCarBean.ResultBean> result = shopCarBean.getResult();
                for (ShopCarBean.ResultBean results : result) {
                    list.add(new ShoppingCarBean(results.getCommodityId(), results.getCount()));
                }
                addShopCar(list);
            }
        }else if (data instanceof AddShopCarBean) {
            AddShopCarBean addShopping = (AddShopCarBean) data;
            Toast.makeText(DetailsActivity.this, addShopping.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    //添加购物车
    private void addShopCar(List<ShoppingCarBean> list) {
        if (list.size()==0){
            list.add(new ShoppingCarBean(Integer.valueOf(commodityId),num));
        }else {
            for (int i=0;i<list.size();i++){
                if(Integer.valueOf(commodityId)==list.get(i).getCommodityId()){
                    int count = list.get(i).getCount();
                    count+=num;
                    list.get(i).setCount(count);
                    break;
                }else if(i==list.size()-1){
                    list.add(new ShoppingCarBean(Integer.valueOf(commodityId),num));
                    break;
                }
            }
        }
        String s = new Gson().toJson(list);
        Map<String, String> map = new HashMap<>();
        map.put("data", s);
        iPresenter.startRequestPut(Apis.SYNC_SHOPPING_CART_PUT, map, AddShopCarBean.class);
    }

    @Override
    public void getDataFail(String error) {

    }

    @OnClick({R.id.shopAdd, R.id.shopBuy})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.shopAdd:

                selectorShopCar();
                break;
            case R.id.shopBuy:
                break;
        }
    }

    //查询购物车
    private void selectorShopCar() {

        iPresenter.startRequestGet(Apis.FIND_SHOPPING_CART_GET,null,ShopCarBean.class);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
