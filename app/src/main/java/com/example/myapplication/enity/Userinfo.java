package com.example.myapplication.enity;

import android.net.Uri;

public class Userinfo {

    public int id; // 序号
    public String phone; // 手机号
    public String password;//密码
    public boolean remember = false;
    public Float money;
    public String tx_path;


    public Userinfo(){

    }

    public Userinfo(String phone, String password, boolean remember, float money, String tx_path) {
        this.phone = phone;
        this.password = password;
        this.remember = remember;
        this.money = money;
        this.tx_path = tx_path;

    }


}
