package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.myapplication.database.ShoppingDBHelper;
import com.example.myapplication.enity.Figureinfo;

import java.util.Objects;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    private ImageButton ib_back1;
    private Button btn_change_password;
    private Button btn_exit;
    private Button btn_save;
    private Intent intent;
    private ShoppingDBHelper mDBHelper;
    private EditText et_age;
    private EditText et_height;
    private EditText et_weight;
    private RadioGroup rg_gender;
    private RadioButton rb_male;
    private RadioButton rb_female;
    private String gender;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();//这种方式默认式亮色主题
        setContentView(R.layout.activity_setting);
        ib_back1 = findViewById(R.id.ib_back1);
        btn_change_password = findViewById(R.id.btn_change_password);
        btn_exit = findViewById(R.id.btn_exit);
        btn_save = findViewById(R.id.btn_save);
        et_age = findViewById(R.id.et_age);
        et_height = findViewById(R.id.et_height);
        et_weight = findViewById(R.id.et_weight);
        rg_gender = findViewById(R.id.rg_gender);
        rb_male = findViewById(R.id.rb_male);
        rb_female = findViewById(R.id.rb_female);


        ib_back1.setOnClickListener(this);
        btn_change_password.setOnClickListener(this);
        btn_exit.setOnClickListener(this);
        btn_save.setOnClickListener(this);
        rg_gender.setOnCheckedChangeListener(this);
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
        /*String phone = MyShoppingApplication.getInstance().user_phone;
        String age = et_age.getText().toString();
        String height = et_height.getText().toString();
        String weight = et_weight.getText().toString();*/
       /* Figureinfo user = null;*/
        switch (view.getId()){
            case R.id.ib_back1:
                intent = new Intent(this,PersonActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_change_password:
                intent = new Intent(this, LoginForgetActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_exit:
                intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_save:
                Figureinfo info = new Figureinfo();
                info.phone = MyShoppingApplication.getInstance().user_phone;
                info.age = Integer.parseInt(et_age.getText().toString());
                info.height = Long.parseLong(et_height.getText().toString());
                info.weight = Float.parseFloat(et_weight.getText().toString());
                info.gender = gender;
                mDBHelper.save(info);
                Log.d("fu","caocaocao");
                Toast.makeText(this, "修改成功", Toast.LENGTH_SHORT).show();
                break;
        }

    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        gender = "null";
        switch (i){
            case R.id.rb_male:
                gender = "男";
                break;
            case R.id.rb_female:
                gender = "女";
                break;
        }
    }
}