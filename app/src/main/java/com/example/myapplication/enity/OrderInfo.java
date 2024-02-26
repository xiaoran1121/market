package com.example.myapplication.enity;

import java.util.ArrayList;
import java.util.List;

public class OrderInfo {
    public int id;
    // 商品编号
    public int goodsId;
    // 商品数量
    public int count;

    public OrderInfo() {
    }

    public OrderInfo(int id, int goodsId, int count) {
        this.id = id;
        this.goodsId = goodsId;
        this.count = count;
    }
}
