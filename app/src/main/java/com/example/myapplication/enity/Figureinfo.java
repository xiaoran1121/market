package com.example.myapplication.enity;

public class Figureinfo {

        public int id; // 序号
    public  String phone;//电话
        public int age; // 年龄
        public long height; // 身高
        public float weight; // 体重
        public String gender;//



        public Figureinfo(){

        }

        public Figureinfo(String phone, int age, long height, float weight,String gender) {

            this.phone = phone;
            this.age = age;
            this.height = height;
            this.weight = weight;
            this.gender = gender;


        }




}
