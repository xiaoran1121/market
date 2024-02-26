package Util;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.example.myapplication.R;

import java.util.Random;

public class ViewUtil {

    public static void hideOneInputMethod(Activity act, View v) {
        // 从系统服务中获取输入法管理器
        InputMethodManager imm = (InputMethodManager) act.getSystemService(Context.INPUT_METHOD_SERVICE);
        // 关闭屏幕上的输入法软键盘
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

}
/*

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
                break;
            case R.id.btn_login:
                if (rb_password.isChecked()) {
                    if (!mPassword.equals(et_password.getText().toString())) {
                        Toast.makeText(this, "请输入正确密码", Toast.LENGTH_LONG).show();
                        return;
                    } else {
                        loginsuccess();
                    }
                }
                if(rb_verifycode .isChecked()) {
                    if (!mVerifyCode.equals(et_password.getText().toString())) {
                        Toast.makeText(this, "请输入正确的验证码", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        loginsuccess();
                    }
                }
                break;


            public void onClick(View view) {
                String phone = et_user.getText().toString();
                if (view.getId() == R.id.btn_login) {
                    if (rb_password.isChecked()) {
                        if (!mPassword.equals(et_password.getText().toString())) {
                            Toast.makeText(this, "请输入正确密码", Toast.LENGTH_LONG).show();
                            return;
                        }
                    } else if (rb_verifycode.isChecked()) {
                        if (view.getId() == R.id.btn_forget) {
                            // 验证码方式校验
                            mVerifyCode = String.format("%06d", new Random().nextInt(999999));
                            // 以下弹出提醒对话框，提示用户记住六位验证码数字
                            AlertDialog.Builder builder = new AlertDialog.Builder(this);
                            builder.setTitle("请记住验证码");
                            builder.setMessage("手机号" + phone + ",本次验证码是" + mVerifyCode + ",请输入验证码");
                            builder.setPositiveButton("好的", null);
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }
                        if (!mVerifyCode.equals(et_password.getText().toString())) {
                            Toast.makeText(this, "请输入正确的验证码", Toast.LENGTH_SHORT).show();
                            return;
                        }


                    } else {
                        loginsuccess();
                    }

       */
/* if (view.getId() == R.id.btn_exit) {


        }*//*


                }
            }
*/
