package com.example.myapplication;

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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.database.ShoppingDBHelper;
import com.example.myapplication.enity.Userinfo;

import java.util.Objects;
import java.util.Random;

import Util.ViewUtil;

public class LoginForgetActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_login;
    private EditText et_new_password;
    private EditText et_confirm_password;
    private Button btn_apply;
    private String mVerifyCode;
    private EditText et_phone_number;
    private EditText et_fvarifycode;
    private ShoppingDBHelper mDBHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();//这种方式默认式亮色主题
        setContentView(R.layout.activity_login_forget);
        btn_login = findViewById(R.id.btn_login);
        et_new_password = findViewById(R.id.et_new_password);
        et_confirm_password = findViewById(R.id.et_confirm_password);
        et_phone_number = findViewById(R.id.et_phone_number);
        et_fvarifycode = findViewById(R.id.et_fvarifycode);
        btn_apply = findViewById(R.id.btn_apply);
        TextView tv_change1 = findViewById(R.id.tv_change1);
        TextView tv_change2 = findViewById(R.id.tv_change2);
        btn_login.setOnClickListener(this);
        btn_apply.setOnClickListener(this);
        et_phone_number.addTextChangedListener(new LoginForgetActivity.HideeTextWatcher(et_phone_number, 11));
        // 给et_password添加文本变更监听器
        et_new_password.addTextChangedListener(new LoginForgetActivity.HideeTextWatcher(et_new_password, 6));
        et_confirm_password.addTextChangedListener(new LoginForgetActivity.HideeTextWatcher(et_confirm_password, 6));
        et_fvarifycode.addTextChangedListener(new LoginForgetActivity.HideeTextWatcher(et_fvarifycode, 6));

        if (MyShoppingApplication.getInstance().flag) {
            tv_change1.setText("新用户注册界面");
            tv_change2.setText("输入密码");
        }

    }
    @Override
    protected void onStart() {
        super.onStart();
        mDBHelper = ShoppingDBHelper.getInstance(this);
        mDBHelper.openReadLink();
        mDBHelper.openWriteLink();

    }


    @Override
    public void onClick(View view) {
        String phone = et_phone_number.getText().toString();

        switch (view.getId()) {

            case R.id.btn_apply:
                mVerifyCode = String.format("%06d", new Random().nextInt(999999));
                // 以下弹出提醒对话框，提示用户记住六位验证码数字
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("请记住验证码");
                builder.setMessage("手机号" + phone + ",本次验证码是" + mVerifyCode + ",请输入验证码");
                builder.setPositiveButton("好的", null);
                AlertDialog dialog = builder.create();
                dialog.show();
                break;

            case R.id.btn_login:
                String password_first = et_new_password.getText().toString();
                String new_password = password_first;
                /*String password_second = et_confirm_password.getText().toString();*/
                if (!mVerifyCode.equals(et_fvarifycode.getText().toString())) {
                    Toast.makeText(this, "请输入正确验证码", Toast.LENGTH_SHORT).show();
                    if (!password_first.equals(et_confirm_password.getText().toString())) {
                        Toast.makeText(this, "新密码与旧密码应保持一致", Toast.LENGTH_SHORT).show();
                        if (password_first.length() < 6) {
                            Toast.makeText(this, "请输入正确的密码", Toast.LENGTH_SHORT).show();
                        }
                    }
                    return;
                }
                if (MyShoppingApplication.getInstance().flag){
                    Toast.makeText(this, "用户创建成功", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(this, "密码修改成功", Toast.LENGTH_SHORT).show();
                }
                Intent intent = new Intent();
                intent.putExtra("new_password", password_first);
                setResult(Activity.RESULT_OK, intent);
                Log.d("ning", password_first);
                //写入数据库
                Userinfo info = new Userinfo();
                info.phone = et_phone_number.getText().toString();
                MyShoppingApplication.getInstance().user_phone = et_phone_number.getText().toString();
                Log.d("fu", MyShoppingApplication.getInstance().user_phone);
                info.password = password_first;
                info.remember = true;
                info.money = Float.valueOf(0);
                info.tx_path =null;
                mDBHelper.save(info);

                finish();
                break;
        }
    }

    private class HideeTextWatcher implements TextWatcher {
        private EditText mView;
        private int mMaxLength;

        public HideeTextWatcher(EditText v, int maxLength) {
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
                ViewUtil.hideOneInputMethod(LoginForgetActivity.this, mView);
            }
        }
    }
}