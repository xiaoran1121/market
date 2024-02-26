package com.example.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.database.ShoppingDBHelper;
import com.example.myapplication.enity.GoodsInfo;

import com.example.myapplication.Adapter.OrderBaseAdapter;
import com.example.myapplication.enity.OrderInfo;

import java.util.List;
import java.util.Objects;

public class OrderActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private ListView list_order;
    private ShoppingDBHelper mDBHelper;
    private ImageView iv_back_order;
    private Intent intent;
    private List<OrderInfo> infos;
    private OrderBaseAdapter orderBaseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();//这种方式默认式亮色主题
        setContentView(R.layout.activity_order);
        list_order = findViewById(R.id.list_order);
        iv_back_order = findViewById(R.id.iv_back_order);
        iv_back_order.setOnClickListener(this);
        mDBHelper = ShoppingDBHelper.getInstance(this);
        mDBHelper.openReadLink();
        mDBHelper.openWriteLink();

        if (mDBHelper.queryAllOrderInfo() != null) {
            infos = mDBHelper.queryAllOrderInfo();
            orderBaseAdapter = new OrderBaseAdapter(this, infos);
            list_order.setAdapter(orderBaseAdapter);
            Log.d("fu", "point2");
            if (infos != null) {
                Log.d("fu", "point3");
                list_order.setOnItemClickListener(this);
                list_order.setOnItemLongClickListener(this);

            }
        }  //Toast.makeText(this, "您还未创建订单", Toast.LENGTH_SHORT).show();
        ShowOrder();
    }

    private void ShowOrder() {
        orderBaseAdapter.notifyDataSetChanged();
    }

    //点击监听事件
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back_order:
                intent = new Intent(this, PersonActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        intent = new Intent(this, GoodsDetailActivity.class);
        intent.putExtra("goods_id", infos.get(i).goodsId);
        startActivity(intent);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        GoodsInfo info1 = mDBHelper.queryGoodsInfoById(infos.get(i).goodsId);
        AlertDialog.Builder builder = new AlertDialog.Builder(OrderActivity.this);
        builder.setMessage("是否从购物车删除" + info1.name + "？");
        builder.setPositiveButton("是", (dialog, which) -> {
            // 删除该商品
            mDBHelper.deleteOrderInfoByGoodsId(infos.get(i).goodsId);
            // 从集合中移除数据
            infos.remove(i);
            // 通知适配器发生了数据变化
            orderBaseAdapter.notifyDataSetChanged();
            Log.d("fu", "point1");


        });
        builder.setNegativeButton("否", null);
        builder.create().show();
        return true;

    }


}