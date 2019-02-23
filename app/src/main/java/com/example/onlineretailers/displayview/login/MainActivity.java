package com.example.onlineretailers.displayview.login;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.AppCompatCheckBox;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.onlineretailers.Online.entry.login.bean.LoginBean;
import com.example.onlineretailers.R;
import com.example.onlineretailers.displayview.regist.RegistActivity;
import com.example.onlineretailers.displayview.shop.ShoppingMallActivity;
import com.example.onlineretailers.utils.EventBean.EventBean;
import com.example.onlineretailers.utils.api.Apis;
import com.example.onlineretailers.utils.concrete.BaseActivity;
import com.example.onlineretailers.utils.network.custom.Constants;
import com.example.onlineretailers.utils.mvp.ContractEntity;
import com.example.onlineretailers.utils.mvp.presenter.IPresenterImpl;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author Peng
 * @time 2019/2/18 18:48
 */

public class MainActivity extends BaseActivity implements ContractEntity.IView {


    @BindView(R.id.login_phone)
    EditText loginPhone;
    @BindView(R.id.login_password)
    EditText loginPassword;
    @BindView(R.id.Rememberpwd)
    AppCompatCheckBox Rememberpwd;
    @BindView(R.id.loginSubmit)
    Button loginSubmit;
    @BindView(R.id.register)
    TextView register;
    @BindView(R.id.control)
    ImageButton control;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private IPresenterImpl iPresenter;

    @Override
    protected void initData() {

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void initView(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        iPresenter=new IPresenterImpl(this);
        preferences = getSharedPreferences("User", MODE_PRIVATE);
        editor = preferences.edit();
        //将记住密码的状态取出
        boolean r_check = preferences.getBoolean("r_check", false);
        if (r_check) {
            //取出值
            String phones = preferences.getString("phones", null);
            String pwds = preferences.getString("pwds", null);
            //设值
            loginPhone.setText(phones);
            loginPassword.setText(pwds);
            Rememberpwd.setChecked(true);
        }
        //密码显示与隐藏
        control.setOnTouchListener((v, event) -> {
            if (event.getAction()==MotionEvent.ACTION_DOWN){
                loginPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                control.setBackgroundResource(R.drawable.ic_action_eye);
            }else if (event.getAction()==MotionEvent.ACTION_UP){
                loginPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                control.setBackgroundResource(R.drawable.ic_action_name);
            }
            return false;
        });
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main;
    }



    @OnClick({R.id.Rememberpwd, R.id.loginSubmit, R.id.register})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.Rememberpwd:
                break;
            case R.id.loginSubmit:
                String phones = loginPhone.getText().toString();
                String pwds = loginPassword.getText().toString();
                //判断记住密码是否勾选
                if (Rememberpwd.isChecked()){
                    //将值存入
                    editor.putString("phones",phones);
                    editor.putString("pwds",pwds);
                    //存入一个勾选状态
                    editor.putBoolean("r_check",true);
                    //提交
                    editor.commit();
                }else {
                    //清空
                    editor.clear();
                    //提交
                    editor.commit();
                }
                loadData();
                break;
            case R.id.register:
                //跳转注册页面
                startActivity(new Intent(MainActivity.this, RegistActivity.class));
                break;
                default:
                    break;
        }
    }

    private void loadData() {
        Map<String,String> params=new HashMap<>();
        params.put(Constants.LOGIN_PHONE,loginPhone.getText().toString().trim());
        params.put(Constants.LOGIN_PASSWORD,loginPassword.getText().toString().trim());
        iPresenter.startRequestPost(Apis.ULOGIN_POST,params,LoginBean.class);
    }

    @Override
    public void getDataSuccess(Object data) {
        if (data instanceof LoginBean){
            LoginBean loginBean= (LoginBean) data;
            if (loginBean==null||!loginBean.isSuceess()){
                Toast.makeText(MainActivity.this,loginBean.getMessage(),Toast.LENGTH_LONG).show();
            }else {
                //存储请求头保存
                SharedPreferences sharedPreferences=getSharedPreferences("UserShao",MODE_PRIVATE);
                sharedPreferences.edit().putString("userId",loginBean.getResult().getUserId()+"").
                        putString("sessionId",loginBean.getResult().getSessionId()).commit();
                EventBus.getDefault().postSticky(new EventBean("main",data));
                startActivity(new Intent(MainActivity.this,ShoppingMallActivity.class));
                finish();
            }

        }
    }

    @Override
    public void getDataFail(String error) {
        Toast.makeText(MainActivity.this,error,Toast.LENGTH_LONG).show();
    }
}
