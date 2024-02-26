package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerTabStrip;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.myapplication.Adapter.ImagePagerAdapter;
import com.example.myapplication.database.ShoppingDBHelper;
import com.example.myapplication.enity.GoodsInfo;

import java.util.ArrayList;
import java.util.Objects;

import Util.ToastUtil;

public class MainDeskActivity extends AppCompatActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {

    private ImageButton ib_home;
    private ImageButton ib_person;
    private ImageButton ib_cart;
    private Intent intent;
    private Button btn_search;
    private Button btn_food;
    private Button btn_digital;
    private Button btn_makeup;
    private Button btn_clothes;
    private ImageButton ib_tb;
    private ArrayList<GoodsInfo> mGoodsList;
    private WebView webView;
    private ShoppingDBHelper mDBHelper;
    private EditText et_search;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();//这种方式默认式亮色主题
        setContentView(R.layout.activity_main_desk);
        ib_home = findViewById(R.id.ib_home);
        ib_person = findViewById(R.id.ib_person);
        ib_cart = findViewById(R.id.ib_cart);
        btn_search = findViewById(R.id.btn_search);
        ib_tb = findViewById(R.id.ib_tb);
        btn_makeup = findViewById(R.id.btn_makeup);
        btn_digital = findViewById(R.id.btn_digital);
        btn_clothes = findViewById(R.id.btn_clothes);
        btn_food = findViewById(R.id.btn_food);
        et_search = findViewById(R.id.et_search);
        mDBHelper = mDBHelper.getInstance(this);




        findViewById(R.id.ib_pdd).setOnClickListener(this);
        findViewById(R.id.ib_wph).setOnClickListener(this);

        ib_home.setOnClickListener(this);
        ib_person.setOnClickListener(this);
        ib_cart.setOnClickListener(this);
        btn_search.setOnClickListener(this);
        ib_tb.setOnClickListener(this);
        btn_makeup.setOnClickListener(this);
        btn_digital.setOnClickListener(this);
        btn_clothes.setOnClickListener(this);
        btn_food.setOnClickListener(this);
        initPagerStrip();
        initViewPager();

    }

    private void initViewPager() {
        PagerTabStrip pts_tab = findViewById(R.id.pts_tab);
        // 设置翻页标签栏的文本大小
        pts_tab.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        pts_tab.setTextColor(Color.RED);
    }

    private void initPagerStrip() {
        ViewPager vp_content = findViewById(R.id.vp_content);
        mGoodsList = GoodsInfo.getDefaultList();
        ImagePagerAdapter adapter = new ImagePagerAdapter(this, mGoodsList);
        vp_content.setAdapter(adapter);
        // 给翻页视图添加页面变更监听器
        vp_content.addOnPageChangeListener(this);
        vp_content.setCurrentItem(3);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_cart:
                intent = new Intent(this, CartActivity.class);
                startActivity(intent);
                break;
            case R.id.ib_person:
                intent = new Intent(this, PersonActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_search:
                //if (TextUtils.isEmpty(et_search.getText()))
                if (TextUtils.isEmpty(et_search.getText())){
                    intent = new Intent(this, SearchActivity.class);
                    MyShoppingApplication.getInstance().type=0;

                }else{
                    GoodsInfo info = mDBHelper.queryGoodsInfoByName(String.valueOf(et_search.getText()));
                    if (info!=null) {
                        intent = new Intent(this, GoodsDetailActivity.class);
                        intent.putExtra("goods_id", info.id);
                    }
                }
                startActivity(intent);

                break;
            case R.id.ib_tb:
                intent = new Intent(this,WebViewActivity.class);
                intent.putExtra("platform","tb");
                startActivity(intent);
                break;
            /*case R.id.ib_xy:
                intent = new Intent(this,WebViewActivity.class);
                intent.putExtra("platform","xy");
                startActivity(intent);
                break;
            case R.id.ib_albb:
                intent = new Intent(this,WebViewActivity.class);
                intent.putExtra("platform","albb");
                startActivity(intent);
                break;*/
            case R.id.ib_wph:
                intent = new Intent(this,WebViewActivity.class);
                intent.putExtra("platform","wph");
                startActivity(intent);
                break;
           /* case R.id.ib_jd:
                intent = new Intent(this,WebViewActivity.class);
                intent.putExtra("platform","jd");
                startActivity(intent);
                break;*/
            case R.id.ib_pdd:
                intent = new Intent(this,WebViewActivity.class);
                intent.putExtra("platform","pdd");
                startActivity(intent);
                break;

            case R.id.btn_makeup:
                intent = new Intent(this, SearchActivity.class);
                MyShoppingApplication.getInstance().type = 1;
                startActivity(intent);
                break;
            case R.id.btn_digital:
                intent = new Intent(this, SearchActivity.class);
                MyShoppingApplication.getInstance().type = 2;
                startActivity(intent);
                break;
            case R.id.btn_clothes:
                intent = new Intent(this, SearchActivity.class);
                MyShoppingApplication.getInstance().type = 3;
                startActivity(intent);
                break;
            case R.id.btn_food:
                intent = new Intent(this, SearchActivity.class);
                MyShoppingApplication.getInstance().type = 4;
                startActivity(intent);
                break;


        }

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
       // ToastUtil.show(this, "您翻到的是：" + mGoodsList.get(position).name);

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}