package com.example.onlineretailers.displayview.regist;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.onlineretailers.Online.entry.register.bean.RegisterBean;
import com.example.onlineretailers.R;
import com.example.onlineretailers.displayview.login.MainActivity;
import com.example.onlineretailers.utils.api.Apis;
import com.example.onlineretailers.utils.concrete.BaseActivity;
import com.example.onlineretailers.utils.mvp.ContractEntity;
import com.example.onlineretailers.utils.mvp.presenter.IPresenterImpl;
import com.example.onlineretailers.utils.network.custom.Constants;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.gui.RegisterPage;

/**
 * @author Peng
 * @time 2019/2/20 15:28
 */

public class RegistActivity extends BaseActivity implements ContractEntity.IView {


    @BindView(R.id.regist_phone)
    EditText registPhone;
    @BindView(R.id.register_verification)
    EditText registerVerification;
    @BindView(R.id.Sendverification)
    TextView Sendverification;
    @BindView(R.id.Passwordswitching)
    ImageButton Passwordswitching;
    @BindView(R.id.register)
    TextView register;
    @BindView(R.id.loginSubmit)
    Button loginSubmit;
    @BindView(R.id.registPassword)
    EditText registPassword;
    private IPresenterImpl iPresenter;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_regist;
    }

    @Override
    protected void initData() {

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void initView(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        iPresenter = new IPresenterImpl(this);
        /**
         * 密码显示与隐藏
         */
        Passwordswitching.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                registPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                Passwordswitching.setBackgroundResource(R.drawable.ic_action_eye);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                registPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                Passwordswitching.setBackgroundResource(R.drawable.ic_action_name);
            }
            return false;
        });
    }


    @OnClick({R.id.Passwordswitching, R.id.register, R.id.loginSubmit, R.id.Sendverification})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.Passwordswitching:
                break;
            case R.id.register:
                startActivity(new Intent(RegistActivity.this, MainActivity.class));
                finish();
                break;
            case R.id.loginSubmit:
                loadData();
                break;
            case R.id.Sendverification:
                sendCode(this);
                break;
        }
    }

    private void loadData() {
        Map<String,String> params=new HashMap<>();
        params.put(Constants.REGISTER_PHONE,registPhone.getText().toString().trim());
        params.put(Constants.REGISTER_PASSWORD,registPassword.getText().toString().trim());
        iPresenter.startRequestPost(Apis.REGISTER_POST,params,RegisterBean.class);
    }

    public void sendCode(Context context) {

        RegisterPage page = new RegisterPage();
        //如果使用我们的ui，没有申请模板编号的情况下需传null
        page.setTempCode(null);
        page.setRegisterCallback(new EventHandler() {
            public void afterEvent(int event, int result, Object data) {
                if (result == SMSSDK.RESULT_COMPLETE) {
                    // 处理成功的结果
                    HashMap<String, Object> phoneMap = (HashMap<String, Object>) data;
                    String country = (String) phoneMap.get("country"); // 国家代码，如“86”
                    String phone = (String) phoneMap.get("phone"); // 手机号码，如“13800138000”
                    // TODO 利用国家代码和手机号码进行后续的操作

                    Sendverification.setText("验证成功");
                } else {
                    // TODO 处理错误的结果
                    Sendverification.setText("验证失败");
                }
            }
        });
        page.show(context);
    }

    @Override
    public void getDataSuccess(Object data) {
        RegisterBean registerBean= (RegisterBean) data;
        if (registerBean==null||!registerBean.isSuceess()){
            Toast.makeText(RegistActivity.this,registerBean.getMessage(),Toast.LENGTH_LONG).show();
        }else {
            Intent intent = new Intent(RegistActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

    }

    @Override
    public void getDataFail(String error) {
        Toast.makeText(RegistActivity.this,error,Toast.LENGTH_LONG).show();
    }
}
