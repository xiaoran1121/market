package com.example.myapplication.Adapter;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.OrderActivity;
import com.example.myapplication.R;
import com.example.myapplication.database.ShoppingDBHelper;
import com.example.myapplication.enity.GoodsInfo;
import com.example.myapplication.enity.OrderInfo;

import java.util.List;

public class OrderBaseAdapter extends BaseAdapter {
    public Context mContext;
    public List<OrderInfo> mOrder;
    private ShoppingDBHelper mDBHelper;
    private int count=0;

    public OrderBaseAdapter(Context mContext, List<OrderInfo> mOrder) {
        this.mContext = mContext;
        this.mOrder = mOrder;
    }


    @Override
    public int getCount() {
        return mOrder.size();

    }

    @Override
    public Object getItem(int i) {

        return mOrder.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            mDBHelper = ShoppingDBHelper.getInstance(mContext);
            mDBHelper.openReadLink();
            mDBHelper.openWriteLink();
            if (mDBHelper.queryAllOrderInfo() != null) {
                view = LayoutInflater.from(mContext).inflate(R.layout.item_cart, null);
                holder = new ViewHolder();
                holder.iv_thumb = view.findViewById(R.id.iv_thumb);
                holder.tv_name = view.findViewById(R.id.tv_name);
                holder.tv_desc = view.findViewById(R.id.tv_desc);
                holder.tv_count = view.findViewById(R.id.tv_count);
                holder.tv_price = view.findViewById(R.id.tv_price);
                holder.tv_sum = view.findViewById(R.id.tv_sum);
                //实例化list
                List<OrderInfo> list = mDBHelper.queryAllOrderInfo();

                if (list != null) {
                    //Adapter控件自动遍历，因此传入的列表是一个二维列表。
                    // 通过i来确定正在遍历的行数
                    OrderInfo info1 = mOrder.get(i);
                    holder.tv_count.setText(String.valueOf(info1.count));
                    Log.d("ddddddd", String.valueOf(info1.count));
                    //通过获取OderInfo的goodsId读取GoodsInfo中对应的数据
                    GoodsInfo goods = mDBHelper.queryGoodsInfoById(info1.goodsId);
                    holder.iv_thumb.setImageURI(Uri.parse(goods.picPath));
                    holder.tv_name.setText(goods.name);
                    holder.tv_desc.setText(goods.description);
                    holder.tv_price.setText(String.valueOf(goods.price));
                    holder.tv_sum.setText(String.valueOf(goods.price * info1.count ));
                }

            }

        }

        return view;
    }

    public static final class ViewHolder {
        public ImageView iv_thumb;
        public TextView tv_name;
        public TextView tv_desc;
        public TextView tv_count;
        public TextView tv_price;
        public TextView tv_sum;
    }
}
