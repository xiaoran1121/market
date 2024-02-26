package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.database.ShoppingDBHelper;

import java.util.Objects;

public class WalletActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView iv_back_charge;
    private TextView account;
    private EditText et_money;
    private Button btn_charge;
    private ShoppingDBHelper mDBHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();//这种方式默认式亮色主题
        setContentView(R.layout.activity_wallet);
        iv_back_charge = findViewById(R.id.iv_back_charge);
        account = findViewById(R.id.account);
        et_money = findViewById(R.id.et_money);
        btn_charge = findViewById(R.id.btn_charge);

        account.setText(String.format("¥%s", String.valueOf(MyShoppingApplication.getInstance().money)));

        mDBHelper = ShoppingDBHelper.getInstance(this);
        mDBHelper.openReadLink();
        mDBHelper.openWriteLink();

        btn_charge.setOnClickListener(this);
        iv_back_charge.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_charge:
                if (TextUtils.isEmpty(et_money.getText())) {
                    Toast.makeText(this, "请输入正确的数字", Toast.LENGTH_SHORT).show();
                }
                MyShoppingApplication.getInstance().money += Float.parseFloat(et_money.getText().toString());
                account.setText(String.format("¥%s", String.valueOf(MyShoppingApplication.getInstance().money)));
                mDBHelper.update_money(MyShoppingApplication.getInstance().user_phone,MyShoppingApplication.getInstance().money);
                Toast.makeText(this, "充值成功", Toast.LENGTH_SHORT).show();
                et_money.getText().clear();
                break;
            case R.id.iv_back_charge:
                Intent intent = new Intent(this, ChargeActivity.class);
                startActivity(intent);
                break;
        }
    }

}