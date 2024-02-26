package com.example.myapplication;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.database.ShoppingDBHelper;
import com.example.myapplication.enity.Userinfo;

import java.util.Objects;
import java.util.Random;

import Util.ViewUtil;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener, View.OnFocusChangeListener {

    private Button btn_login;

    private TextView tv_password;
    private EditText et_password;
    private TextView tv_user;
    private EditText et_user;
    private String mPassword = "0000";
    private RadioButton rb_password;
    private RadioButton rb_verifycode;
    private String mVerifyCode;
    private CheckBox ck_remember;
    private Button btn_forget;
    private ShoppingDBHelper mDBHelper;
    private ActivityResultLauncher<Intent> register;
    private Button btn_sign;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();//这种方式默认式亮色主题
        setContentView(R.layout.activity_login);
        findViewById(R.id.btn_login).setOnClickListener(this);
        btn_forget = findViewById(R.id.btn_forget);
        btn_login = findViewById(R.id.btn_login);
        ck_remember = findViewById(R.id.ck_remember);
        RadioGroup rb_login = findViewById(R.id.rg_login);
        rb_login.setOnCheckedChangeListener(this);
        tv_password = findViewById(R.id.tv_password);
        tv_user = findViewById(R.id.tv_user);
        et_user = findViewById(R.id.et_user);
        et_password = findViewById(R.id.et_password);
        rb_password = findViewById(R.id.rb_password);
        rb_verifycode = findViewById(R.id.rb_verifycode);
        btn_sign = findViewById(R.id.btn_sign);

        // 添加文本变更监听器
        et_user.addTextChangedListener(new HideTextWatcher(et_user, 11));

        et_password.addTextChangedListener(new HideTextWatcher(et_password, 6));

        btn_sign.setOnClickListener(this);
        btn_forget.setOnClickListener(this);
        btn_login.setOnClickListener(this);

        register = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                Intent intent = result.getData();
                if (intent != null && result.getResultCode() == Activity.RESULT_OK) {
                    // 用户密码已改为新密码，故更新密码变量
                    mPassword = intent.getStringExtra("new_password");
                    Log.d("ning", "传过来了");
                }
            }
        });
        et_password.setOnFocusChangeListener(this);

    }

    private void reload() {
        Userinfo info = mDBHelper.queryTop();
        if (info != null && info.remember) {
            et_user.setText(info.phone);
            et_password.setText(info.password);
            ck_remember.setChecked(true);
            MyShoppingApplication.getInstance().money = info.money;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mDBHelper = ShoppingDBHelper.getInstance(this);
        mDBHelper.openReadLink();
        mDBHelper.openWriteLink();

        reload();
    }

    @Override
    public void onClick(View view) {
        String phone = et_user.getText().toString();
        switch (view.getId()) {
            case R.id.btn_forget:

                if (rb_verifycode.isChecked()) {
                    mVerifyCode = String.format("%06d", new Random().nextInt(999999));
                    // 以下弹出提醒对话框，提示用户记住六位验证码数字
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("请记住验证码");
                    builder.setMessage("手机号" + phone + ",本次验证码是" + mVerifyCode + ",请输入验证码");
                    builder.setPositiveButton("好的", null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                if (rb_password.isChecked()) {
                    Intent intent = new Intent(this, LoginForgetActivity.class);
                    register.launch(intent);

                }
                break;
            case R.id.btn_login:
                if (rb_password.isChecked()) {
                        if (!et_password.getText().toString().equals(passwordCheck(et_user.getText().toString()))) {
                            Toast.makeText(this, "请输入正确密码", Toast.LENGTH_LONG).show();
                            return;
                        }
                        loginsuccess();

                } else {
                        Log.d("fu", "修改失败");
                        Toast.makeText(this, "请输入正确密码", Toast.LENGTH_LONG).show();
                        return;
                    }

                if (rb_verifycode.isChecked()) {
                    if (!mVerifyCode.equals(et_password.getText().toString())) {
                        Toast.makeText(this, "请输入正确的验证码", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        loginsuccess();
                    }
                }
                break;
            case R.id.btn_sign:
                Intent intent = new Intent(this, LoginForgetActivity.class);
                MyShoppingApplication.getInstance().flag = true;
                register.launch(intent);
                break;
        }
    }

    private String passwordCheck(String s) {
        Userinfo info = mDBHelper.queryByPhone(s);
        // 如果根据电话号码，查询出了密码
        if (info != null) {
            Log.d("fu","查到了");
            return info.password;

        }
        Log.d("fu","没查到");
        return null;

    }


    private void loginsuccess() {
        Toast.makeText(this, "登陆成功", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, MainDeskActivity.class);
        // 设置启动标志：跳转到新页面时，栈中的原有实例都被清空，同时开辟新任务的活动栈
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);//登录成功后不返回登录界面
        register.launch(intent);
        MyShoppingApplication.getInstance().user_phone = et_user.getText().toString();

        // 保存到数据库
       /* Userinfo info = new Userinfo();
        info.phone = et_user.getText().toString();
        MyShoppingApplication.getInstance().user_phone = et_user.getText().toString();
        Log.d("fu", MyShoppingApplication.getInstance().user_phone);
        info.password = et_password.getText().toString();
        info.remember = ck_remember.isChecked();
        //info.money = MyShoppingApplication.getInstance().money;

        mDBHelper.save(info);*/

    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {

        switch (i) {
            case R.id.rb_password:
                tv_password.setText(getString(R.string.password));
                et_password.setHint(getString(R.string.input_password));
                btn_forget.setText(getString(R.string.forget_password));
                ck_remember.setVisibility(View.VISIBLE);
                break;

            case R.id.rb_verifycode:
                tv_password.setText(getString(R.string.varify_code));
                et_password.setHint(getString(R.string.pvarify_code));
                btn_forget.setText(getString(R.string.apply_varify_code));
                ck_remember.setVisibility(View.GONE);
                break;


        }
    }

    @Override
    public void onFocusChange(View view, boolean hasFocus) {

        if (view.getId() == R.id.et_password && hasFocus) {
            Userinfo info = mDBHelper.queryByPhone(et_user.getText().toString());
            // 如果根据电话号码，查询出了密码
            if (info != null) {
                if(info.remember) {
                    et_password.setText(info.password);
                    ck_remember.setChecked(info.remember);
                }
            } else {
                // 没有查到，清空密码
                et_password.setText("");
                ck_remember.setChecked(false);
            }
        }

    }

    //输入最大位数后关闭键盘
    class HideTextWatcher implements TextWatcher {
        private EditText mView;
        private final int mMaxLength;

        public HideTextWatcher(EditText v, int maxLength) {
            this.mView = v;
            this.mMaxLength = maxLength;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.toString().length() == mMaxLength) {
                // 隐藏输入法软键盘
                ViewUtil.hideOneInputMethod(LoginActivity.this, mView);
            }
        }
    }


}