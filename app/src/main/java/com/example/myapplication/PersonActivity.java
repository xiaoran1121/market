package com.example.myapplication;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.database.ShoppingDBHelper;
import com.example.myapplication.enity.Figureinfo;
import com.example.myapplication.enity.GoodsInfo;
import com.example.myapplication.enity.Userinfo;

import java.io.File;
import java.util.Objects;

public class PersonActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton ib_setting;
    private ImageButton ib_home1;
    private ImageButton ib_cart1;
    private Intent intent;
    private TextView p_tv_user;
    private TextView p_tv_gender;
    private TextView p_tv_age;

    private ShoppingDBHelper mDBHelper;
    private ImageView iv_into;
    private ImageView iv_tx;
    private ActivityResultLauncher<Intent> mResultLauncher;
    private Uri picUri;
    private Button my_order;
    private ImageView iv_dfh;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();//这种方式默认式亮色主题
        setContentView(R.layout.activity_person);

        ib_setting = findViewById(R.id.ib_setting);
        ib_home1 = findViewById(R.id.ib_home1);
        ib_cart1 = findViewById(R.id.ib_cart1);
        p_tv_user = findViewById(R.id.p_tv_user);
        p_tv_gender = findViewById(R.id.p_tv_gender);
        p_tv_age = findViewById(R.id.p_tv_age);
        iv_into = findViewById(R.id.iv_into);
        iv_tx = findViewById(R.id.iv_tx);
        my_order = findViewById(R.id.my_order);
        iv_dfh = findViewById(R.id.iv_dfh);


        ib_setting.setOnClickListener(this);
        iv_dfh.setOnClickListener(this);
        iv_into.setOnClickListener(this);
        ib_home1.setOnClickListener(this);
        ib_cart1.setOnClickListener(this);
        iv_tx.setOnClickListener(this);
        my_order.setOnClickListener(this);
        mDBHelper = ShoppingDBHelper.getInstance(this);
        mDBHelper.openReadLink();
        mDBHelper.openWriteLink();
        //改头像
        mResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == RESULT_OK) {
                    Intent intent = result.getData();
                    // 获得选中图片的路径对象
                    picUri = intent.getData();
                    if (picUri != null) {
                        // ImageView 显示刚刚选中的图片
                        iv_tx.setImageURI(picUri);
                        Userinfo info = mDBHelper.update_tx(MyShoppingApplication.getInstance().user_phone, picUri.toString());
                        mDBHelper.save(info);
                        MyShoppingApplication.getInstance().pic = picUri;
                        Log.d("1111",picUri.toString());
                        Toast.makeText(PersonActivity.this, "头像更改成功", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    //重载数据
    private void reload() {
        p_tv_user.setText(MyShoppingApplication.getInstance().user_phone);
        Figureinfo info = mDBHelper.queryByPhone_number(MyShoppingApplication.getInstance().user_phone);
        Userinfo info1 = mDBHelper.queryByPhone(MyShoppingApplication.getInstance().user_phone);

        if (info == null) {
            Log.d("fu", "未修改");
        }
        if (info != null) {
            p_tv_user.setText(info.phone);
            p_tv_gender.setText(info.gender);
            p_tv_age.setText(info.age + "岁");
        }
        if (info1.tx_path != null) {
           //iv_tx.setImageURI(null);
            Uri uri = Uri.parse(info1.tx_path);
            //MyShoppingApplication.getInstance().pic = Uri.parse(info1.tx_path);
            iv_tx.setImageURI(MyShoppingApplication.getInstance().pic);
            //iv_tx.setImageURI(Uri.fromFile(new File(info1.tx_path)));
           //iv_tx.setImageURI(uri);

        }

    }

    protected void onStart() {
        super.onStart();
        mDBHelper = ShoppingDBHelper.getInstance(this);
        mDBHelper.openReadLink();
        mDBHelper.openWriteLink();
        reload();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_setting:
                intent = new Intent(this, SettingActivity.class);
                startActivity(intent);
                break;
            case R.id.ib_home1:
                intent = new Intent(this, MainDeskActivity.class);
                startActivity(intent);
                break;
            case R.id.ib_cart1:
                intent = new Intent(this, CartActivity.class);
                startActivity(intent);
                break;
            case R.id.iv_into:
                intent = new Intent(this, ChargeActivity.class);
                startActivity(intent);
                break;
            case R.id.iv_tx:
                // 跳转到系统相册，选择图片，并返回
                Intent intent2 = new Intent(Intent.ACTION_GET_CONTENT);
                // 设置内容类型为图片类型
                intent2.setType("image/*");
                // 打开系统相册，并等待图片选择结果
                mResultLauncher.launch(intent2);
                break;
            case R.id.my_order:
            case R.id.iv_dfh:
                intent = new Intent(this, OrderActivity.class);
                startActivity(intent);
                break;

        }
    }
}