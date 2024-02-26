package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.myapplication.database.ShoppingDBHelper;
import com.example.myapplication.enity.GoodsInfo;

import java.util.List;
import java.util.Objects;

import Util.ToastUtil;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView iv_back;
    private ImageView iv_cart;
    private TextView tv_count;

    private ShoppingDBHelper mDBHelper;
    private LinearLayout linearLayout;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();//这种方式默认式亮色主题
        setContentView(R.layout.activity_search);
        iv_back = findViewById(R.id.iv_back);
        iv_cart = findViewById(R.id.iv_cart);
        tv_count = findViewById(R.id.tv_count);
        linearLayout = findViewById(R.id.linearlayout);
        iv_back.setOnClickListener(this);
        iv_cart.setOnClickListener(this);
        mDBHelper = ShoppingDBHelper.getInstance(this);
        mDBHelper.openReadLink();
        mDBHelper.openWriteLink();
        showGoods();
    }
    @Override
    protected void onResume() {
        super.onResume();
        // 查询购物车商品总数，并展示
        showCartInfoTotal();
    }
    // 查询购物车商品总数，并展示
    private void showCartInfoTotal() {
        int count = mDBHelper.countCartInfo();
        MyShoppingApplication.getInstance().goodsCount = count;
        tv_count.setText(String.valueOf(count));
    }



    private void showGoods() {

        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(screenWidth, LinearLayout.LayoutParams.WRAP_CONTENT);

        // 移除下面的所有子视图
        linearLayout.removeAllViews();
        //assert list != null;
        List<GoodsInfo> list = mDBHelper.queryAllGoodsInfo();
        for (GoodsInfo info : list) {
            // 获取布局文件item_goods.xml的根视图
            View view = LayoutInflater.from(this).inflate(R.layout.item_goods, null);
            ImageView iv_goods = view.findViewById(R.id.iv_goods);
            TextView tv_description = view.findViewById(R.id.tv_description);
            TextView tv_mouth_sales = view.findViewById(R.id.tv_mouth_sales);
            TextView tv_price = view.findViewById(R.id.tv_price);
            ImageButton ib_cart3 = view.findViewById(R.id.ib_cart3);

            // 给控件设置值
            iv_goods.setImageURI(Uri.parse(info.picPath));
            tv_description.setText(info.name);
            tv_price.setText(String.valueOf((int) info.price));

            // 添加到购物车
            ib_cart3.setOnClickListener(v -> {
                addToCart(info.id, info.name);
            });

            // 点击商品图片，跳转到商品详情页面
            iv_goods.setOnClickListener(v -> {
                Intent intent = new Intent(this, GoodsDetailActivity.class);
                intent.putExtra("goods_id", info.id);
                startActivity(intent);
            });

            // 把商品视图添加到布局中
            linearLayout.addView(view, params);
        }
    }

    private void addToCart ( int goodsId, String goodsName){
        // 购物车商品数量+1
        int count = ++MyShoppingApplication.getInstance().goodsCount;
        tv_count.setText(String.valueOf(count));
        mDBHelper.insertCartInfo(goodsId);
        //ToastUtil.show(this, "已添加" + goodsName + "到购物车");
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDBHelper.closeLink();
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                intent = new Intent(this,MainDeskActivity.class);
                startActivity(intent);
                break;
            case R.id.iv_cart:
                intent = new Intent(this,CartActivity.class);
                startActivity(intent);
                break;
        }
    }

}
