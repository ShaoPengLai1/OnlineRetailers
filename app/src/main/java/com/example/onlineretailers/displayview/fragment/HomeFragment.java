package com.example.onlineretailers.displayview.fragment;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.onlineretailers.Online.adapter.home.ByNameSeachAdapter;
import com.example.onlineretailers.Online.adapter.home.HotAdapter;
import com.example.onlineretailers.Online.adapter.home.MoAdapter;
import com.example.onlineretailers.Online.adapter.home.PinAdapter;
import com.example.onlineretailers.Online.adapter.home.bottom.BottomHomeAdapter;
import com.example.onlineretailers.Online.adapter.home.bottom.ByIdSeachAdapter;
import com.example.onlineretailers.Online.adapter.home.top.TopHomeAdapter;
import com.example.onlineretailers.Online.entry.home.ByNameSeachBean;
import com.example.onlineretailers.Online.entry.home.HomeBean;
import com.example.onlineretailers.Online.entry.home.XBannerBeans;
import com.example.onlineretailers.Online.entry.home.bottom.BottomHomeBean;
import com.example.onlineretailers.Online.entry.home.bottom.ByIdSeachBean;
import com.example.onlineretailers.Online.entry.home.top.TopHomeBean;
import com.example.onlineretailers.Online.entry.shopcar.bean.GoodsDetailBean;
import com.example.onlineretailers.R;
import com.example.onlineretailers.displayview.details.DetailsActivity;
import com.example.onlineretailers.displayview.fragment.cousom.ItemDecoration;
import com.example.onlineretailers.utils.EventBean.EventBean;
import com.example.onlineretailers.utils.api.Apis;
import com.example.onlineretailers.utils.mvp.ContractEntity;
import com.example.onlineretailers.utils.mvp.presenter.IPresenterImpl;
import com.stx.xhb.xbanner.XBanner;
import com.stx.xhb.xbanner.transformers.Transformer;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class HomeFragment extends Fragment implements ContractEntity.IView {


    @BindView(R.id.home_icon)
    ImageView homeIcon;
    @BindView(R.id.home_ed)
    EditText homeEd;
    @BindView(R.id.home_search)
    ImageView homeSearch;
    @BindView(R.id.home_tv)
    TextView homeTv;
    @BindView(R.id.xbanner_home)
    XBanner xbannerHome;
    @BindView(R.id.con_tv)
    TextView conTv;
    @BindView(R.id.point_re)
    ImageView pointRe;
    @BindView(R.id.recy_re)
    RecyclerView recyRe;
    @BindView(R.id.con_tv2)
    TextView conTv2;
    @BindView(R.id.point_mo)
    ImageView pointMo;
    @BindView(R.id.recy_mo)
    RecyclerView recyMo;
    @BindView(R.id.con_tv3)
    TextView conTv3;
    @BindView(R.id.point_pin)
    ImageView pointPin;
    @BindView(R.id.recy_pin)
    RecyclerView recyPin;
    @BindView(R.id.scroll)
    ScrollView scroll;
    @BindView(R.id.res)
    TextView res;
    @BindView(R.id.mos)
    TextView mos;
    @BindView(R.id.pins)
    TextView pins;
    @BindView(R.id.byName)
    RecyclerView byName;
    Unbinder unbinder;
    @BindView(R.id.linearLayout)
    LinearLayout linearLayout;
    private PopupWindow popupWindow;
    private IPresenterImpl iPresenter;
    private TopHomeAdapter topAdapter;
    private BottomHomeAdapter bottomAdapter;
    private TopHomeBean topHomeBean;
    private BottomHomeBean bottomHomeBean;
    private List<String> mImgUrl;
    private MoAdapter moAdapter;
    private PinAdapter pinAdapter;
    private HotAdapter hotAdapter;
    private String id;
    private ByNameSeachAdapter byNameSeachAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, view);
        iPresenter = new IPresenterImpl(this);
        mImgUrl = new ArrayList<>();
        iPresenter.startRequestGet(Apis.BANNER_SHOW_GET, null, XBannerBeans.class);
        initRecycle();
        return view;
    }

    private void initRecycle() {
        GridLayoutManager manager = new GridLayoutManager(getActivity(), 2);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        byName.setLayoutManager(manager);
        byNameSeachAdapter = new ByNameSeachAdapter(getActivity());
        byName.setAdapter(byNameSeachAdapter);
        //热销
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyRe.setLayoutManager(gridLayoutManager);
        hotAdapter = new HotAdapter(getActivity());
        recyRe.setAdapter(hotAdapter);
        //魔力时尚
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyMo.setLayoutManager(layoutManager);
        moAdapter = new MoAdapter(getActivity());
        recyMo.setAdapter(moAdapter);
        //品质生活
        GridLayoutManager gridLayoutManagers = new GridLayoutManager(getActivity(), 2);
        gridLayoutManagers.setOrientation(LinearLayoutManager.VERTICAL);
        recyPin.setLayoutManager(gridLayoutManagers);
        pinAdapter = new PinAdapter(getActivity());
        recyPin.setAdapter(pinAdapter);

    }


    @OnClick({R.id.home_icon, R.id.home_search, R.id.home_tv, R.id.point_re, R.id.point_mo, R.id.point_pin})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.home_icon:
                initPopupWindow();
                break;
            case R.id.home_search:
                homeEd.setVisibility(View.VISIBLE);
                homeTv.setVisibility(View.VISIBLE);
                homeSearch.setVisibility(View.INVISIBLE);
                break;
            case R.id.home_tv:
                if (homeEd.getText().toString().equals("")) {
                    homeSearch.setVisibility(View.VISIBLE);
                    homeTv.setVisibility(View.GONE);
                    homeEd.setVisibility(View.INVISIBLE);
                } else {
                    iPresenter.startRequestGet(Apis.FIND_COMMODITY_BYKEYWORD_GET
                                    + "?keyword=" + homeEd.getText().toString() + "&page=" + "1" + "&count=10",
                            null, ByNameSeachBean.class);

                }
                break;
            case R.id.point_re:
                res.setVisibility(View.VISIBLE);
                id = "1002";
                getData(id);
                backPage();
                break;
            case R.id.point_mo:
                mos.setVisibility(View.VISIBLE);
                id = "1003";
                getData(id);
                backPage();
                break;
            case R.id.point_pin:
                pins.setVisibility(View.VISIBLE);
                id = "1004";
                getData(id);
                backPage();
                break;
        }
    }


    private void getData(String id) {
        iPresenter.startRequestGet(Apis.FIND_COMMODITY_LIST_BYLABEL_GET
                + "?labelId=" + id + "&page=1&count=10", null, ByNameSeachBean.class);
    }

    //popupWindow显示一级和二级类目
    private void initPopupWindow() {
        //加载布局
        View v = View.inflate(getActivity(), R.layout.popup_item_home, null);
        //获取一级条目
        RecyclerView topView = v.findViewById(R.id.recycle_top);
        topAdapter = new TopHomeAdapter(getActivity());
        //一级条目布局管理器
        topView.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.HORIZONTAL, false));
        topView.setAdapter(topAdapter);
        //设置条目之间的间距
        ItemDecoration decoration = new ItemDecoration();
        topView.addItemDecoration(decoration);
        //获取二级类目
        RecyclerView bottomView = v.findViewById(R.id.recycle_bottom);
        bottomAdapter = new BottomHomeAdapter(getActivity());
        bottomView.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.HORIZONTAL, false));
        bottomView.setAdapter(bottomAdapter);
        bottomView.addItemDecoration(decoration);
        //设置popupWindow
        popupWindow = new PopupWindow(v, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, true);
        //设置焦点
        popupWindow.setFocusable(true);
        //设置背景
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        //设置可触摸
        popupWindow.setTouchable(true);
        //设置位置
        popupWindow.showAsDropDown(v, 0, 0);
        loadLstData();
        //点击一级类目获取二级类目
        topAdapter.onClick(index -> {
            String id = topHomeBean.getResult().get(index).getId();
            iPresenter.startRequestGet(Apis.FIND_SECOND_CATEGORY_GET + id, null, BottomHomeBean.class);
            bottomAdapter.onClick(index1 -> {
                String id1 = bottomHomeBean.getResult().get(index).getId();
                iPresenter.startRequestGet(Apis.FIND_COMMODITY_BYCATEGORY_GET
                        + "?categoryId=" + id1 + "&page=1&count=10", null, ByIdSeachBean.class);
            });
        });

    }

    private void loadLstData() {
        iPresenter.startRequestGet(Apis.FIND_FIRST_CATEGORY_GET, null, TopHomeBean.class);
    }

    private void initImageData() {
        xbannerHome.setData(mImgUrl, null);
        xbannerHome.loadImage((banner, model, view, position) -> Glide.with(getActivity())
                .load(mImgUrl.get(position)).into((ImageView) view));
        xbannerHome.setPageTransformer(Transformer.Default);
        xbannerHome.setPageTransformer(Transformer.Alpha);
        xbannerHome.setPageTransformer(Transformer.ZoomFade);
        xbannerHome.setPageTransformer(Transformer.ZoomCenter);
        xbannerHome.setPageTransformer(Transformer.ZoomStack);
        xbannerHome.setPageTransformer(Transformer.Stack);
        xbannerHome.setPageTransformer(Transformer.Depth);
        xbannerHome.setPageTransformer(Transformer.Zoom);
        xbannerHome.setPageChangeDuration(0);
    }

    /**
     * 请求成功的方法
     *
     * @param data
     */
    @Override
    public void getDataSuccess(Object data) {
        //一级列表
        if (data instanceof TopHomeBean) {
            topHomeBean = (TopHomeBean) data;
            if (topHomeBean == null || !topHomeBean.isSucess()) {
                Toast.makeText(getActivity(), topHomeBean.getMessage(), Toast.LENGTH_LONG).show();
            } else {
                topAdapter.setmList(topHomeBean.getResult());
            }
            //二级列表
        } else if (data instanceof BottomHomeBean) {
            bottomHomeBean = (BottomHomeBean) data;
            if (bottomHomeBean == null || !bottomHomeBean.isSuccess()) {
                Toast.makeText(getActivity(), bottomHomeBean.getMessage(), Toast.LENGTH_LONG).show();
            } else {
                bottomAdapter.setmList(bottomHomeBean.getResult());
            }
        } else if (data instanceof ByIdSeachBean) {
            ByIdSeachBean byIdSeachBean = (ByIdSeachBean) data;
            scroll.setVisibility(View.GONE);
            byName.setVisibility(View.VISIBLE);
            ByIdSeachAdapter byIdSeachAdapter = new ByIdSeachAdapter(getActivity());
            byName.setAdapter(byIdSeachAdapter);
            byIdSeachAdapter.setmList(byIdSeachBean.getResult());
            byIdSeachAdapter.onClick(index -> {
                getGoods(byIdSeachBean.getResult().get(index).getCommodityId());
            });

        } else if (data instanceof XBannerBeans) {
            XBannerBeans xBannerBeans = (XBannerBeans) data;
            if (xBannerBeans == null || !xBannerBeans.isSuccess()) {
                Toast.makeText(getActivity(), xBannerBeans.getMessage(), Toast.LENGTH_LONG).show();
            } else {
                for (int i = 0; i < xBannerBeans.getResult().size(); i++) {
                    mImgUrl.add(xBannerBeans.getResult().get(i).getImageUrl());
                    //加载图片
                    initImageData();
                    iPresenter.startRequestGet(Apis.COMMODITY_LIST_GET, null, HomeBean.class);
                }
            }
        } else if (data instanceof HomeBean) {
            HomeBean homeBean = (HomeBean) data;
            hotAdapter.setmList(homeBean.getResult().getRxxp().getCommodityList());
            pinAdapter.setmList(homeBean.getResult().getPzsh().getCommodityList());
            moAdapter.setmList(homeBean.getResult().getMlss().getCommodityList());
            hotAdapter.onClickListener(index -> {
                int commodityId = homeBean.getResult().getRxxp().
                        getCommodityList().get(index).getCommodityId();
                getGoods(commodityId);
            });
            pinAdapter.onClickListener(index -> {
                int commodityId = homeBean.getResult().getPzsh().
                        getCommodityList().get(index).getCommodityId();
                getGoods(commodityId);
            });
            moAdapter.onClickListener(index -> {
                int commodityId = homeBean.getResult().getMlss().
                        getCommodityList().get(index).getCommodityId();
                getGoods(commodityId);
            });

        } else if (data instanceof ByNameSeachBean) {
            ByNameSeachBean byNameSeachBean = (ByNameSeachBean) data;
            scroll.setVisibility(View.GONE);
            byName.setVisibility(View.VISIBLE);
            if (byNameSeachBean.getResult().size() == 0) {
                linearLayout.setVisibility(View.VISIBLE);
            } else {
                linearLayout.setVisibility(View.GONE);
                byNameSeachAdapter.setmList(byNameSeachBean.getResult());
            }


            byNameSeachAdapter.onClickListener(index -> {
                getGoods(byNameSeachBean.getResult().get(index).getCommodityId());
            });
            if (id == "1002") {
                res.setVisibility(View.VISIBLE);
            } else if (id == "1003") {
                mos.setVisibility(View.VISIBLE);
            } else if (id == "1004") {
                pins.setVisibility(View.VISIBLE);
            }

            backPage();
        } else if (data instanceof GoodsDetailBean) {
            EventBus.getDefault().postSticky(new EventBean("goods", data));
            startActivity(new Intent(getActivity(), DetailsActivity.class));
        }
    }

    //商品请求详情
    private void getGoods(int commodityId) {
        iPresenter.startRequestGet(Apis.FIND_COMMODITY_DETAILS_BYID_GET
                + "?commodityId=" + commodityId, null, GoodsDetailBean.class);
    }

    /**
     * 请求失败的方法
     *
     * @param error
     */
    @Override
    public void getDataFail(String error) {
        Toast.makeText(getActivity(), error, Toast.LENGTH_LONG).show();
    }

    /**
     * 设置返回的监听
     */
    private long exitTime = 0;

    private void backPage() {
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener((view, i, keyEvent) -> {
            scroll.setVisibility(View.VISIBLE);
            byName.setVisibility(View.GONE);

            if (i == KeyEvent.KEYCODE_BACK && keyEvent.getAction() == KeyEvent.ACTION_UP) {
                scroll.setVisibility(View.VISIBLE);
                byName.setVisibility(View.GONE);
                res.setVisibility(View.GONE);
                mos.setVisibility(View.GONE);
                pins.setVisibility(View.GONE);
                if (System.currentTimeMillis() - exitTime > 2000) {
                    exitTime = System.currentTimeMillis();
                } else {
                    //启动一个意图,回到桌面
                    Intent backHome = new Intent(Intent.ACTION_MAIN);
                    backHome.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    backHome.addCategory(Intent.CATEGORY_HOME);
                    startActivity(backHome);
                }
                return true;
            } else {
                return false;
            }
        });
    }

    //解绑，防止内存泄漏
    @Override
    public void onDestroy() {
        super.onDestroy();
        iPresenter.onDetach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
