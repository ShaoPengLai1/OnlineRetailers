package com.example.onlineretailers.displayview.mine.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.onlineretailers.Online.entry.mine.bean.ImageHeadBean;
import com.example.onlineretailers.Online.entry.mine.bean.InformationBean;
import com.example.onlineretailers.Online.entry.mine.bean.MineUpdateNameBean;
import com.example.onlineretailers.Online.entry.mine.bean.MineUpdatePasswordBean;
import com.example.onlineretailers.R;
import com.example.onlineretailers.utils.api.Apis;
import com.example.onlineretailers.utils.mvp.ContractEntity;
import com.example.onlineretailers.utils.mvp.presenter.IPresenterImpl;
import com.example.onlineretailers.utils.network.ImageFileUtil;
import com.facebook.drawee.view.SimpleDraweeView;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PresonalDataActivity extends AppCompatActivity implements ContractEntity.IView {


    @BindView(R.id.backBar)
    Toolbar backBar;
    @BindView(R.id.my_profile)
    TextView myProfile;
    @BindView(R.id.my_profile_text)
    TextView myProfileText;
    @BindView(R.id.my_profile_view)
    View myProfileView;
    @BindView(R.id.my_profile_simple)
    SimpleDraweeView myProfileSimple;
    @BindView(R.id.my_nivkname_text)
    TextView myNivknameText;
    @BindView(R.id.my_nivkname_view)
    View myNivknameView;
    @BindView(R.id.my_nickname)
    TextView myNickname;
    @BindView(R.id.my_password_text)
    TextView myPasswordText;
    @BindView(R.id.my_password_view)
    View myPasswordView;
    @BindView(R.id.my_password)
    TextView myPassword;
    @BindView(R.id.my_constrain)
    ConstraintLayout myConstrain;
    private AlertDialog dialog;
    private IPresenterImpl iPresenter;
    private TextView camera;
    private TextView pick;
    private TextView cancel;
    private PopupWindow window;
    private String path = Environment.getExternalStorageDirectory()
            + "/image.png";
    private String file = Environment.getExternalStorageDirectory()
            + "/file.png";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presonal_data);
        ButterKnife.bind(this);
        initView();

    }

    private void initData() {
        Intent intent = getIntent();
        InformationBean.ResultBean result = (InformationBean.ResultBean) intent.getSerializableExtra("result");
        String headPic = result.getHeadPic();
        String nickName = result.getNickName();
        String password = result.getPassword();
        myProfileSimple.setImageURI(Uri.parse(headPic));
        myNickname.setText(nickName);
        myPassword.setText(password);
        setResult(200, intent);
    }

    private void initView() {
        iPresenter=new IPresenterImpl(this);
        //加载视图
        View view_p = View.inflate(this, R.layout.popupwindow_item, null);
        camera = view_p.findViewById(R.id.text_camera);
        pick = view_p.findViewById(R.id.text_pick);
        cancel = view_p.findViewById(R.id.text_cancel);
        //创建PopupWindow
        window = new PopupWindow(view_p, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置焦点
        window.setFocusable(true);
        //设置背景
        window.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        //设置可触摸
        window.setTouchable(true);
        //打开相机
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 调取系统相机
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if(hasSdcard()){
                    // 存到sdcard中
                    intent.putExtra(MediaStore.EXTRA_OUTPUT,
                            Uri.fromFile(new File(path)));
                    //执行
                    startActivityForResult(intent, 100);
                    window.dismiss();
                }

            }
        });
        //打开相册
        pick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //加载相册
                Intent intent = new Intent(Intent.ACTION_PICK);
                if(hasSdcard()){
                    //设置图片格式
                    intent.setType("image/*");
                    //执行
                    startActivityForResult(intent, 300);
                    window.dismiss();
                }

            }
        });
        //取消
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
            }
        });
        initData();
    }

    /**
     * 判断sd卡是否挂载
     * */
    private boolean hasSdcard() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }
    @OnClick({R.id.backBar, R.id.my_profile_simple, R.id.my_nickname, R.id.my_password})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.backBar:
                finish();
                break;
            case R.id.my_profile_simple:
                window.showAtLocation(View.inflate(PresonalDataActivity.this, R.layout.activity_presonal_data, null),
                        Gravity.BOTTOM, 0, 0);
                break;
            case R.id.my_nickname:
                showNameWindow();
                break;
            case R.id.my_password:
                showPwdWindow();
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        iPresenter.startRequestGet(Apis.QUERYBYID_GET, null, InformationBean.class);
    }

    private void showPwdWindow() {
        final AlertDialog.Builder pbuilder = new AlertDialog.Builder(PresonalDataActivity.this);
        View viewpas = View.inflate(this, R.layout.presonal_alertdalog_password, null);
        pbuilder.setView(viewpas);
        final EditText former_edix = viewpas.findViewById(R.id.former_edix);
        final EditText new_edix = viewpas.findViewById(R.id.new_edix);
        Button updatep = viewpas.findViewById(R.id.updata_btn);
        Button cancelp = viewpas.findViewById(R.id.cancal_btn);
        //修改
        updatep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String formerPass = former_edix.getText().toString().trim();
                String newPass = new_edix.getText().toString().trim();
                if (formerPass.equals("") || newPass.equals("")) {
                    Toast.makeText(PresonalDataActivity.this, "输入不能为空", Toast.LENGTH_SHORT).show();
                }else if(formerPass.equals(newPass)){
                    Toast.makeText(PresonalDataActivity.this, "两次输入的密码一样", Toast.LENGTH_SHORT).show();
                } else {
                    Map<String, String> map = new HashMap<>();
                    map.put("oldPwd", formerPass);
                    map.put("newPwd", newPass);
                    iPresenter.startRequestPut(Apis.UPDATE_PASSWORD_POST, map, MineUpdatePasswordBean.class);
                    myPassword.setText(newPass);
                    dialog.dismiss();
                }

            }
        });
        //取消
        cancelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PresonalDataActivity.this, "取消", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        dialog = pbuilder.create();
        dialog.show();
    }

    private void showNameWindow() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(PresonalDataActivity.this);
        View viewname = View.inflate(this, R.layout.presonal_alertdalog_name, null);
        builder.setView(viewname);
        Button update = viewname.findViewById(R.id.updata_btn);
        Button cencal = viewname.findViewById(R.id.cancal_btn);
        final EditText updateedix = viewname.findViewById(R.id.updata_edix);
        //修改
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = updateedix.getText().toString().trim();
                if (name.equals("")) {
                    Toast.makeText(PresonalDataActivity.this, "昵称不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    Map<String, String> map = new HashMap<>();
                    map.put("nickName", name);
                    iPresenter.startRequestPut(Apis.UPDATE_NAME_POST, map, MineUpdateNameBean.class);
                    myNickname.setText(name);
                }
                dialog.dismiss();
            }
        });
        //取消
        cencal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PresonalDataActivity.this, "取消", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        dialog = builder.create();
        dialog.show();
    }

    @Override
    public void getDataSuccess(Object data) {
            //修改昵称
        if (data instanceof MineUpdateNameBean) {
            MineUpdateNameBean updateBean = (MineUpdateNameBean) data;
            Toast.makeText(PresonalDataActivity.this, updateBean.getMessage(), Toast.LENGTH_SHORT).show();
            //修改密码
        } else if (data instanceof MineUpdatePasswordBean) {
            MineUpdatePasswordBean passwordBean = (MineUpdatePasswordBean) data;
            Toast.makeText(PresonalDataActivity.this, passwordBean.getMessage(), Toast.LENGTH_SHORT).show();
        }else if(data instanceof ImageHeadBean){
            ImageHeadBean headBean = (ImageHeadBean) data;
            Toast.makeText(PresonalDataActivity.this,headBean.getMessage(),Toast.LENGTH_SHORT).show();
        }else if (data instanceof InformationBean){
            InformationBean informationBean= (InformationBean) data;
            String headPic = informationBean.getResult().getHeadPic();
            myProfileSimple.setImageURI(headPic);
        }
    }

    @Override
    public void getDataFail(String error) {
        Toast.makeText(PresonalDataActivity.this,error,Toast.LENGTH_LONG).show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //相机
        if(requestCode == 100 && resultCode == RESULT_OK){
            //调取裁剪功能
            Intent intent = new Intent("com.android.camera.action.CROP");
            //将图片设置给裁剪
            intent.setDataAndType(Uri.fromFile(new File(path)), "image/*");
            //设置是否支持裁剪
            intent.putExtra("CROP", true);
            //设置宽高比
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            //设置显示大小
            intent.putExtra("outputX", 50);
            intent.putExtra("outputY", 50);
            //将图片返回给data
            intent.putExtra("return-data", true);
            startActivityForResult(intent, 200);
        }
        //相册
        if(requestCode == 300 && resultCode == RESULT_OK){
            //获取相册路径
            Uri uri = data.getData();
            //调取裁剪功能
            Intent intent = new Intent("com.android.camera.action.CROP");
            //将图片设置给裁剪
            intent.setDataAndType(uri, "image/*");
            //设置是否支持裁剪
            intent.putExtra("CROP", true);
            //设置框高比
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            //设置显示大小
            intent.putExtra("outputX", 50);
            intent.putExtra("outputY", 50);
            //将图片返回给data
            intent.putExtra("return-data", true);
            startActivityForResult(intent, 200);
        }
        if(requestCode == 200 && resultCode == RESULT_OK){
            Bitmap bitmap = data.getParcelableExtra("data");
            try {
                ImageFileUtil.setBitmap(bitmap,file,50);
            }catch (Exception e){
                e.printStackTrace();
                Toast.makeText(PresonalDataActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
            }
            Map<String,String> map = new HashMap<>();
            map.put("image",file);
            iPresenter.imagePostRequest(Apis.UPDATE_HEADERIMAGE_POST,map, ImageHeadBean.class);
        }
    }
}
