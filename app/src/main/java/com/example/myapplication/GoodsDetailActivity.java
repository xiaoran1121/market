package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.database.ShoppingDBHelper;
import com.example.myapplication.enity.GoodsInfo;

import java.util.Objects;

import Util.ToastUtil;


public class GoodsDetailActivity extends AppCompatActivity implements View.OnClickListener {
    private ShoppingDBHelper mDBHelper;
    private ImageView iv_goods_show;
    private TextView tv_goods_name;
    private TextView tv_goods_desc;
    private TextView tv_mouth_sales;
    private ImageButton ib_cart3;
    private ImageButton ib_back3;
    private int mGoodsId;
    private TextView tv_price;
    private TextView tv_count;
    private ImageView iv_cart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();//这种方式默认式亮色主题
        setContentView(R.layout.activity_goods_detail);
        iv_goods_show = findViewById(R.id.iv_goods_show);
        tv_goods_name = findViewById(R.id.tv_goods_name1);
        tv_goods_desc = findViewById(R.id.tv_goods_desc1);
        tv_mouth_sales = findViewById(R.id.tv_mouth_sales);
        tv_price = findViewById(R.id.tv_price1);
        ib_cart3 = findViewById(R.id.ib_cart3);
        ib_back3 = findViewById(R.id.ib_back4);
        tv_count = findViewById(R.id.tv_count1);
        iv_cart = findViewById(R.id.iv_cart5);
        ib_back3.setOnClickListener(this);
        ib_cart3.setOnClickListener(this);
        iv_cart.setOnClickListener(this);

        mDBHelper = mDBHelper.getInstance(this);
       // tv_count.setText(String.valueOf(MyShoppingApplication.getInstance().goodsCount));

    }
    @Override
    protected void onResume() {
        super.onResume();
        showDetail();
    }

    private void showDetail() {
        // 获取上一个页面传来的商品编号
        mGoodsId = getIntent().getIntExtra("goods_id", 0);
        if (mGoodsId > 0) {
            // 根据商品编号查询商品数据库中的商品记录
            GoodsInfo info = mDBHelper.queryGoodsInfoById(mGoodsId);
            tv_goods_name.setText(info.name);
            tv_goods_desc.setText(info.description);
            tv_price.setText(String.valueOf((int) info.price));
            tv_mouth_sales.setText((info.mouth_sales));
            iv_goods_show.setImageURI(Uri.parse(info.picPath));
        }
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ib_back4:
                finish();
                break;
            case R.id.ib_cart3:
                addToCart(mGoodsId);
                break;
            case R.id.iv_cart5:
                Intent intent = new Intent(this, CartActivity.class);
                startActivity(intent);
                break;
        }

    }
    private void addToCart(int goodsId) {
        // 购物车商品数量+1
        int count = ++MyShoppingApplication.getInstance().goodsCount;
        tv_count.setText(String.valueOf(count));
        mDBHelper.insertCartInfo(goodsId);
        ToastUtil.show(this, "成功添加至购物车");
    }
}