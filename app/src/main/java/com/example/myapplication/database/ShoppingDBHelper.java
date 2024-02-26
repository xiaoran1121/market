package com.example.myapplication.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import com.example.myapplication.MyShoppingApplication;
import com.example.myapplication.enity.CartInfo;
import com.example.myapplication.enity.GoodsInfo;
import com.example.myapplication.enity.OrderInfo;
import com.example.myapplication.enity.Userinfo;
import com.example.myapplication.enity.Figureinfo;

import java.util.ArrayList;
import java.util.List;

public class ShoppingDBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "shopping.db";
    // 商品信息表
    private static final String TABLE_GOODS_INFO = "goods_info";
    // 购物车信息表
    private static final String TABLE_CART_INFO = "cart_info";
    //用户信息表
    private static final String TABLE_USER_INFO = "user_info";
    private static final String TABLE_FIGURE_INFO = "figure_info";
    private static final String TABLE_ORDER_INFO = "order_info";
    private static final int DB_VERSION = 1;
    private static ShoppingDBHelper mHelper = null;
    private SQLiteDatabase mRDB = null;
    private SQLiteDatabase mWDB = null;

    private ShoppingDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    // 利用单例模式获取数据库帮助器的唯一实例
    public static ShoppingDBHelper getInstance(Context context) {
        if (mHelper == null) {
            mHelper = new ShoppingDBHelper(context);
        }
        return mHelper;
    }

    // 打开数据库的读连接
    public SQLiteDatabase openReadLink() {
        if (mRDB == null || !mRDB.isOpen()) {
            mRDB = mHelper.getReadableDatabase();
        }
        return mRDB;
    }

    // 打开数据库的写连接
    public SQLiteDatabase openWriteLink() {
        if (mWDB == null || !mWDB.isOpen()) {
            mWDB = mHelper.getWritableDatabase();
        }
        return mWDB;
    }

    // 关闭数据库连接
    public void closeLink() {
        if (mRDB != null && mRDB.isOpen()) {
            mRDB.close();
            mRDB = null;
        }

        if (mWDB != null && mWDB.isOpen()) {
            mWDB.close();
            mWDB = null;
        }
    }

    // 创建数据库，执行建表语句
    @Override
    public void onCreate(SQLiteDatabase db) {
        // 创建商品信息表
        String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_GOODS_INFO +
                "(_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                " type VARCHAR NOT NULL,"+
                " name VARCHAR NOT NULL," +
                " description VARCHAR NOT NULL," +
                " price FLOAT NOT NULL," +
                " mouth_sales VARCHAR NOT NULL,"+
                " pic_path VARCHAR NOT NULL);";
        db.execSQL(sql);

        // 创建购物车信息表
        sql = "CREATE TABLE IF NOT EXISTS " + TABLE_CART_INFO +
                "(_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                " goods_id INTEGER NOT NULL," +
                " count INTEGER NOT NULL);";
        db.execSQL(sql);

        // 用户信息表
        sql = "CREATE TABLE IF NOT EXISTS " + TABLE_USER_INFO +
                "(_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                " phone VARCHAR NOT NULL," +
                " password VARCHAR NOT NULL," +
                " tx_path VARCHAR ," +
                " money FLOAT ," +
                " remember INTEGER NOT NULL);";
        db.execSQL(sql);
        sql = "CREATE TABLE IF NOT EXISTS " + TABLE_FIGURE_INFO +
                "(_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                " phone VARCHAR  NOT NULL," +
                " age INTEGER NOT NULL," +
                " height LONG ," +
                " weight FLOAT ,"+
                " gender VARCHAR  );";
        db.execSQL(sql);
        // 创建购物车信息表
        sql = "CREATE TABLE IF NOT EXISTS " + TABLE_ORDER_INFO +
                "(_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                " goods_id INTEGER NOT NULL," +
                " count INTEGER NOT NULL);";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    // 添加多条商品信息
    public void insertGoodsInfos(List<GoodsInfo> list) {
        // 插入多条记录，要么全部成功，要么全部失败
        try {
            mWDB.beginTransaction();
            for (GoodsInfo info : list) {
                ContentValues values = new ContentValues();
                values.put("type",info.type);
                values.put("name", info.name);
                values.put("description", info.description);
                values.put("price", info.price);
                values.put("mouth_sales",info.mouth_sales);
                values.put("pic_path", info.picPath);
                mWDB.insert(TABLE_GOODS_INFO, null, values);
            }
            mWDB.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mWDB.endTransaction();
        }
    }

    // 查询所有的商品信息
    public List<GoodsInfo> queryAllGoodsInfo() {
        String sql = null;
        switch (MyShoppingApplication.getInstance().type) {
            case 0:
                sql = "select * from " + TABLE_GOODS_INFO;
                break;
            case 1:
                sql = "select * from "+TABLE_GOODS_INFO+" where type ='美妆'";
                break;
            case 2:
                sql = "select * from "+TABLE_GOODS_INFO+" where type ='数码'";
                break;
            case 3:
                sql = "select * from "+TABLE_GOODS_INFO+" where type ='服饰'";
                break;
            case 4:
                sql = "select * from "+TABLE_GOODS_INFO+" where type ='食品'";
                break;
        }

        List<GoodsInfo> list = new ArrayList<>();
        Cursor cursor = mRDB.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            GoodsInfo info = new GoodsInfo();
            info.id = cursor.getInt(0);
            info.type = cursor.getString(1);
            info.name = cursor.getString(2);
            info.description = cursor.getString(3);
            info.price = cursor.getFloat(4);
            info.mouth_sales = cursor.getString(5);
            info.picPath = cursor.getString(6);
            list.add(info);
        }
        cursor.close();
        return list;
    }

    // 添加商品到购物车
    public void insertCartInfo(int goodsId) {
        // 如果购物车中不存在该商品，添加一条信息
        CartInfo cartInfo = queryCartInfoByGoodsId(goodsId);
        ContentValues values = new ContentValues();
        values.put("goods_id", goodsId);
        if (cartInfo == null) {
            values.put("count", 1);
            mWDB.insert(TABLE_CART_INFO, null, values);
        } else {
            // 如果购物车中已经存在该商品，更新商品数量
            values.put("_id", cartInfo.id);
            values.put("count", ++cartInfo.count);
            mWDB.update(TABLE_CART_INFO, values, "_id=?", new String[]{String.valueOf(cartInfo.id)});
        }
    }

    // 根据商品信息ID查询购物车信息
    private CartInfo queryCartInfoByGoodsId(int goodsId) {
        Cursor cursor = mRDB.query(TABLE_CART_INFO, null, "goods_id=?", new String[]{String.valueOf(goodsId)}, null, null, null);
        CartInfo info = null;
        if (cursor.moveToNext()) {
            info = new CartInfo();
            info.id = cursor.getInt(0);
            info.goodsId = cursor.getInt(1);
            info.count = cursor.getInt(2);
        }
        return info;
    }

    // 统计购物车中商品的总数量
    public int countCartInfo() {
        int count = 0;
        String sql = "select sum(count) from " + TABLE_CART_INFO;
        Cursor cursor = mRDB.rawQuery(sql, null);
        if (cursor.moveToNext()) {
            count = cursor.getInt(0);
        }
        return count;
    }

    // 查询购物车中所有的信息列表
    public List<CartInfo> queryAllCartInfo() {
        List<CartInfo> list = new ArrayList<>();
        Cursor cursor = mRDB.query(TABLE_CART_INFO, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            CartInfo info = new CartInfo();
            info.id = cursor.getInt(0);
            info.goodsId = cursor.getInt(1);
            info.count = cursor.getInt(2);
            list.add(info);
        }
        return list;
    }

    // 根据商品ID查询商品信息
    public GoodsInfo queryGoodsInfoById(int goodsId) {
        GoodsInfo info = null;
        Cursor cursor = mRDB.query(TABLE_GOODS_INFO, null, "_id=?", new String[]{String.valueOf(goodsId)}, null, null, null);
        if (cursor.moveToNext()) {
            info = new GoodsInfo();
            info.id = cursor.getInt(0);
            info.type = cursor.getString(1);
            info.name = cursor.getString(2);
            info.description = cursor.getString(3);
            info.price = cursor.getFloat(4);
            info.mouth_sales = cursor.getString(5);
            info.picPath = cursor.getString(6);
        }
        return info;
    }
//根据商品名称查询
    public GoodsInfo queryGoodsInfoByName(String name) {
        GoodsInfo info = null;
        Cursor cursor = mRDB.query(TABLE_GOODS_INFO, null, "name=?", new String[]{String.valueOf(name)}, null, null, null);
        if (cursor.moveToNext()) {
            info = new GoodsInfo();
            info.id = cursor.getInt(0);
            info.type = cursor.getString(1);
            info.name = cursor.getString(2);
            info.description = cursor.getString(3);
            info.price = cursor.getFloat(4);
            info.mouth_sales = cursor.getString(5);
            info.picPath = cursor.getString(6);
        }
        return info;
    }

    // 根据商品ID删除购物车信息
    public void deleteCartInfoByGoodsId(int goodsId) {
        mWDB.delete(TABLE_CART_INFO, "goods_id=?", new String[]{String.valueOf(goodsId)});
    }


    // 删除所有购物车信息
    public void deleteAllCartInfo() {
        mWDB.delete(TABLE_CART_INFO, "1=1", null);
    }
    //删除用户表信息
    public long delete(Userinfo info) {
        return mWDB.delete(TABLE_USER_INFO, "phone=?", new String[]{info.phone});
    }

    //插入用户表信息
    public long insert(Userinfo info) {
        ContentValues values = new ContentValues();
        values.put("phone", info.phone);
        values.put("password", info.password);
        values.put("tx_path", info.tx_path);
        values.put("money", info.money);
        values.put("remember", info.remember);
        return mWDB.insert(TABLE_USER_INFO, null, values);
    }
    public void save(Userinfo info) {
        // 如果存在则先删除，再添加
        try {
            mWDB.beginTransaction();

            delete(info);
            insert(info);
            mWDB.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mWDB.endTransaction();
        }
    }
    //查询
    public Userinfo queryTop() {
        Userinfo info = null;
        String sql = "select * from " + TABLE_USER_INFO + " where remember = 1 ORDER BY _id DESC limit 1";
        // 执行记录查询动作，该语句返回结果集的游标
        Cursor cursor = mRDB.rawQuery(sql, null);
        if (cursor.moveToNext()) {
            info = new Userinfo();
            info.id = cursor.getInt(0);
            info.phone = cursor.getString(1);
            info.password = cursor.getString(2);
            info.tx_path = cursor.getString(3);
            info.money = cursor.getFloat(4);
            info.remember = (cursor.getInt(5) == 0) ? false : true;
        }

        return info;
    }
    public Userinfo update_money(String phone,float money){
        Userinfo info = mHelper.queryByPhone(phone);
        info.money = money;
        return info;
    }
    public Userinfo update_tx(String phone,String tx){
        Userinfo info = mHelper.queryByPhone(phone);
        info.tx_path = tx;
        return info;
    }
    public Userinfo queryByPhone(String phone) {
        Userinfo info = null;
        String sql = "select * from " + TABLE_USER_INFO;
        // 执行记录查询动作，该语句返回结果集的游标
        Cursor cursor = mRDB.query(TABLE_USER_INFO, null, "phone=? ", new String[]{phone}, null, null, null);
        if (cursor.moveToNext()) {
            info = new Userinfo();
            info.id = cursor.getInt(0);
            info.phone = cursor.getString(1);
            info.password = cursor.getString(2);
            info.tx_path = cursor.getString(3);
            info.money = cursor.getFloat(4);
            info.remember = (cursor.getInt(5) == 0) ? false : true;
        }
        return info;
    }
    //增
    public long insert(Figureinfo info) {
        ContentValues values = new ContentValues();
        values.put("phone", info.phone);
        values.put("age", info.age);
        values.put("height", info.height);
        values.put("weight", info.weight);
        values.put("gender",info.gender);
        Log.d("fu","3333333");
        return mWDB.insert(TABLE_FIGURE_INFO, null, values);
    }
    //删
    public long delete(Figureinfo info) {
        return mWDB.delete(TABLE_FIGURE_INFO, "phone=?", new String[]{info.phone});
    }
    //查
    public Figureinfo queryByPhone_number(String phone) {
        Figureinfo info = null;
        String sql = "select * from " + TABLE_FIGURE_INFO;
        // 执行记录查询动作，该语句返回结果集的游标
        Cursor cursor = mRDB.query(TABLE_FIGURE_INFO, null, "phone=? ", new String[]{phone}, null, null, null);
        if (cursor.moveToNext()) {
            info = new Figureinfo();
            info.id = cursor.getInt(0);
            info.phone = cursor.getString(1);
            info.age = cursor.getInt(2);
            info.height = cursor.getLong(3);
            info.weight = cursor.getFloat(4);
            info.gender = cursor.getString(5);
        }
        return info;
    }
    public Figureinfo queryTopTop() {
        Figureinfo info = null;
        String sql = "select * from " + TABLE_FIGURE_INFO + " ORDER BY _id DESC limit 1";
        // 执行记录查询动作，该语句返回结果集的游标
        Cursor cursor = mRDB.rawQuery(sql, null);
        if (cursor.moveToNext()) {
            info = new Figureinfo();
            info.id = cursor.getInt(0);
            info.phone = cursor.getString(1);
            info.age = cursor.getInt(2);
            info.height = cursor.getLong(3);
            info.weight = cursor.getFloat(4);
            info.gender = cursor.getString(5);
        }
        return info;
    }
    public void save(Figureinfo info) {
        // 如果存在则先删除，再添加
        try {
            mWDB.beginTransaction();
            delete(info);
            insert(info);
            mWDB.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mWDB.endTransaction();
        }
    }


    //订单表
    public void insertOrder(int goods_id, int count) {
        // 如果购物车中不存在该商品，添加一条信息
        String sql = "select goodsId from " + TABLE_ORDER_INFO +"where goodsId = " +goods_id ;
        Log.d("qqq",sql);
        OrderInfo orderInfo = queryOrderIfoByGoodsId(goods_id);
        ContentValues values = new ContentValues();

        //String sql = "select goodsId from " + TABLE_ORDER_INFO +"where goodsId = " +goods_id ;
        Log.d("qqq",sql);
        if (orderInfo == null) {
            values.put("goods_id", goods_id);
            values.put("count", count);
            mWDB.insert(TABLE_ORDER_INFO, null, values);
        } else {
            // 如果购物车中已经存在该商品，更新商品数量
            values.put("_id", orderInfo.id);
            values.put("count", orderInfo.count+=count);
            mWDB.update(TABLE_ORDER_INFO, values, "_id=?", new String[]{String.valueOf(orderInfo.id)});
        }
    }

    // 根据商品信息ID查询购物车信息
    private OrderInfo queryOrderIfoByGoodsId(int goodsId) {
        Cursor cursor = mRDB.query(TABLE_ORDER_INFO, null, "goods_id=?", new String[]{String.valueOf(goodsId)}, null, null, null);
        OrderInfo info = null;
        if (cursor.moveToNext()) {
            info = new OrderInfo();
            info.id = cursor.getInt(0);
            info.goodsId = cursor.getInt(1);
            info.count = cursor.getInt(2);
        }
        return info;
    }

    // 统计订单中商品的总数量
    public int countOrderInfo() {
        int count = 0;
        String sql = "select sum(count) from " + TABLE_ORDER_INFO;
        Cursor cursor = mRDB.rawQuery(sql, null);
        if (cursor.moveToNext()) {
            count = cursor.getInt(0);
        }
        return count;
    }

    // 查询订单中所有的信息列表
    public List<OrderInfo> queryAllOrderInfo() {
        List<OrderInfo> list = new ArrayList<>();
        Cursor cursor = mRDB.query(TABLE_ORDER_INFO, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            OrderInfo info = new OrderInfo();
            info.id = cursor.getInt(0);
            info.goodsId = cursor.getInt(1);
            info.count = cursor.getInt(2);
            list.add(info);
        }
        return list;
    }
    public void deleteOrderInfoByGoodsId(int goodsId) {
        OrderInfo info = queryOrderIfoByGoodsId(goodsId);
        mWDB.delete(TABLE_ORDER_INFO, "goods_id=?", new String[]{String.valueOf(info.goodsId)});
    }

}

