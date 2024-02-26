package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Objects;

public class ChargeActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView iv_back_person;
    private ImageView iv_into_wallet;
    private TextView wallet_money;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();//这种方式默认式亮色主题
        setContentView(R.layout.activity_charge);
        iv_back_person = findViewById(R.id.iv_back_person);
        iv_into_wallet = findViewById(R.id.iv_into_wallet);
        wallet_money = findViewById(R.id.wallet_money);
        wallet_money.setText(String.format("¥%s", String.valueOf(MyShoppingApplication.getInstance().money)));
        Log.d("fu", String.valueOf(MyShoppingApplication.getInstance().money));

        iv_back_person.setOnClickListener(this);
        iv_into_wallet.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_into_wallet:
                Intent intent = new Intent(this, WalletActivity.class);
                startActivity(intent);
                break;
            case R.id.iv_back_person:
                intent = new Intent(this, PersonActivity.class);
                startActivity(intent);
                break;
        }

    }
}