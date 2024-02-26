package com.example.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.database.ShoppingDBHelper;
import com.example.myapplication.enity.CartInfo;
import com.example.myapplication.enity.GoodsInfo;
import com.example.myapplication.enity.OrderInfo;
import com.example.myapplication.enity.Userinfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import Util.ToastUtil;

public class CartActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tv_count;
    private LinearLayout ll_cart;
    private ShoppingDBHelper mDBHelper;

    // 声明一个购物车中的商品信息列表
    private List<CartInfo> mCartList;
    // 声明一个根据商品编号查找商品信息的映射，把商品信息缓存起来，这样不用每一次都去查询数据库
    private Map<Integer, GoodsInfo> mGoodsMap = new HashMap<>();
    private TextView tv_total_price;
    private LinearLayout ll_empty;
    private LinearLayout ll_content;
    private ImageButton ib_back2;
    private int totalPrice = 0;
    private Button btn_check_order;
    private int flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();//这种方式默认式亮色主题
        setContentView(R.layout.activity_cart);
        ib_back2 = findViewById(R.id.ib_back2);
        ib_back2.setOnClickListener(this);
        TextView tv_title = findViewById(R.id.tv_title);
        tv_title.setText("购物车");
        ll_cart = findViewById(R.id.ll_cart);
        tv_total_price = findViewById(R.id.tv_total_price);
        btn_check_order = findViewById(R.id.btn_check_order);

        tv_count = findViewById(R.id.tv_count);
        tv_count.setText(String.valueOf(MyShoppingApplication.getInstance().goodsCount));

        mDBHelper = ShoppingDBHelper.getInstance(this);


        btn_check_order.setOnClickListener(this);
        findViewById(R.id.btn_shopping_channel).setOnClickListener(this);
        findViewById(R.id.btn_clear).setOnClickListener(this);
        findViewById(R.id.btn_settle).setOnClickListener(this);
        ll_empty = findViewById(R.id.ll_empty);
        ll_content = findViewById(R.id.ll_content);
    }

    @Override
    protected void onResume() {
        super.onResume();
        showCart();
    }


    private void showCart() {
        // 移除下面的所有子视图
        ll_cart.removeAllViews();
        // 查询购物车数据库中所有的商品记录
        mCartList = mDBHelper.queryAllCartInfo();
        if (mCartList.size() == 0) {
            return;
        }

        for (CartInfo info : mCartList) {
            // 根据商品编号查询商品数据库中的商品记录
            GoodsInfo goods = mDBHelper.queryGoodsInfoById(info.goodsId);
            mGoodsMap.put(info.goodsId, goods);

            // 获取布局文件item_cart.xml的根视图
            View view = LayoutInflater.from(this).inflate(R.layout.item_cart, null);
            ImageView iv_thumb = view.findViewById(R.id.iv_thumb);
            TextView tv_name = view.findViewById(R.id.tv_name);
            TextView tv_desc = view.findViewById(R.id.tv_desc);
            TextView tv_count = view.findViewById(R.id.tv_count);
            TextView tv_price = view.findViewById(R.id.tv_price);
            TextView tv_sum = view.findViewById(R.id.tv_sum);

            iv_thumb.setImageURI(Uri.parse(goods.picPath));
            tv_name.setText(goods.name);
            tv_desc.setText(goods.description);
            tv_count.setText(String.valueOf(info.count));
            tv_price.setText(String.valueOf((int) goods.price));
            // 设置商品总价
            tv_sum.setText(String.valueOf((int) (info.count * goods.price)));

            // 给商品行添加长按事件。长按商品行就删除该商品
            view.setOnLongClickListener(v -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
                builder.setMessage("是否从购物车删除" + goods.name + "？");
                builder.setPositiveButton("是", (dialog, which) -> {
                    // 移除当前视图
                    ll_cart.removeView(v);
                    // 删除该商品
                    deleteGoods(info);
                });
                builder.setNegativeButton("否", null);
                builder.create().show();
                return true;
            });

            // 给商品行添加点击事件。点击商品行跳到商品的详情页
            view.setOnClickListener(v -> {
                Intent intent = new Intent(CartActivity.this, GoodsDetailActivity.class);
                intent.putExtra("goods_id", goods.id);
                startActivity(intent);
            });

            // 往购物车列表添加该商品行
            ll_cart.addView(view);
        }

        // 重新计算购物车中的商品总金额
        refreshTotalPrice();
    }

    private void deleteGoods(CartInfo info) {
        MyShoppingApplication.getInstance().goodsCount -= info.count;

        // 从购物车的数据库中删除商品
        mDBHelper.deleteCartInfoByGoodsId(info.goodsId);
        // 从购物车的列表中删除商品
        CartInfo removed = null;
        for (CartInfo cartInfo : mCartList) {
            if (cartInfo.goodsId == info.goodsId) {
                removed = cartInfo;
                break;
            }
        }
        mCartList.remove(removed);
        // 显示最新的商品数量
        showCount();
        ToastUtil.show(this, "已从购物车删除" + mGoodsMap.get(info.goodsId).name);
        mGoodsMap.remove(info.goodsId);
        // 刷新购物车中所有商品的总金额
        refreshTotalPrice();
    }

    // 显示购物车图标中的商品数量
    private void showCount() {
        tv_count.setText(String.valueOf(MyShoppingApplication.getInstance().goodsCount));
        // 购物车中没有商品，显示“空空如也”
        if (MyShoppingApplication.getInstance().goodsCount == 0) {
            ll_empty.setVisibility(View.VISIBLE);
            ll_content.setVisibility(View.GONE);
            ll_cart.removeAllViews();
        } else {
            ll_content.setVisibility(View.VISIBLE);
            ll_empty.setVisibility(View.GONE);
        }
    }

    // 重新计算购物车中的商品总金额
    private void refreshTotalPrice() {
        for (CartInfo info : mCartList) {
            GoodsInfo goods = mGoodsMap.get(info.goodsId);
            if (goods != null) {
                totalPrice += goods.price * info.count;
            } else {
                totalPrice = 0;
            }

        }
        tv_total_price.setText(String.valueOf(totalPrice));
    }

    //点击监听事件
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_back2:
                finish();
                break;
            case R.id.btn_shopping_channel:
                // 从购物车页面跳到商场页面
                Intent intent = new Intent(this, MainDeskActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.btn_clear:
                // 清空购物车数据库
                mDBHelper.deleteAllCartInfo();
                MyShoppingApplication.getInstance().goodsCount = 0;
                // 显示最新的商品数量
                showCount();
               // ToastUtil.show(this, "购物车已清空");
                break;

            case R.id.btn_settle:
                // 点击了“结算”按钮
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("结算商品");
                builder.setMessage("是否确认支付" + totalPrice);
                builder.setPositiveButton("是", (dialog, which) -> {
                    if (MyShoppingApplication.getInstance().money >= totalPrice) {
                        for (CartInfo info : mCartList) {
                            mDBHelper.insertOrder(info.goodsId,info.count);
                            Log.d("fu", String.valueOf(info.count));
                        }
                        mDBHelper.deleteAllCartInfo();
                        MyShoppingApplication.getInstance().money -= totalPrice;
                        MyShoppingApplication.getInstance().order_count = MyShoppingApplication.getInstance().goodsCount;
                        Userinfo userinfo = mDBHelper.update_money(MyShoppingApplication.getInstance().user_phone,
                                MyShoppingApplication.getInstance().money);
                        Log.d("ffff", String.valueOf(userinfo.money));
                        mDBHelper.save(userinfo);
                        MyShoppingApplication.getInstance().goodsCount = 0;
                        // 显示最新的商品数量
                        showCount();
                        Toast.makeText(this, "支付成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "余额不足请充值", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("我再想想", (dialog, which) -> flag = 0);
                builder.create().show();


                break;

            case R.id.btn_check_order:
                intent = new Intent(this, OrderActivity.class);
                startActivity(intent);
                break;
        }
    }
}
