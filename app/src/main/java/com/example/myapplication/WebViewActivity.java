package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import java.util.Objects;

public class WebViewActivity extends AppCompatActivity {

    private long exitTime = 0;
    private String platfrom;
    private WebView myWebView;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();//这种方式默认式亮色主题
        setContentView(R.layout.activity_web_view);
        myWebView = findViewById(R.id.webview);
        platfrom = getIntent().getStringExtra("platform");
        Log.d("fu",platfrom);
        //ShowWeb();
       // myWebView.loadUrl("http://m.baidu.com/?cip=123.147.245.147&baiduid=860D8A6EE71AAC98C2EA423180322762&from=844b&vit=fps?from=844b&vit=fps&index=&ssid=0&bd_page_type=1&logid=10822690054863328176&pu=sz%401321_480&t_noscript=jump");

        webView = new WebView(this);
        webView.setWebViewClient(new WebViewClient() {
            //设置在webView点击打开的新网页在当前界面显示,而不跳转到新的浏览器中
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        webView.getSettings().setJavaScriptEnabled(true);  //设置WebView属性,运行执行js脚本
        //webView.loadUrl("https://www.baidu.com/");          //调用loadUrl方法为WebView加入链接
        ShowWeb();
        setContentView(webView);                           //调用Activity提供的setContentView将webView显示出来
    }


    //我们需要重写回退按钮的时间,当用户点击回退按钮：
    //1.webView.canGoBack()判断网页是否能后退,可以则goback()
    //2.如果不可以连续点击两次退出App,否则弹出提示Toast
    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序",
                        Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                super.onBackPressed();
            }

        }
    }


    private void ShowWeb() {
        switch (platfrom){
            case "tb":
                myWebView.loadUrl("https://uland.taobao.com/");
                break;
            case "xy":
                myWebView.loadUrl("https://goofish.com/");
                break;
            case "jd":
                myWebView.loadUrl("https://www.jd.com/?cu=true");
                break;
            case "pdd":
                myWebView.loadUrl("https://www.pinduoduo.com");
                break;
            case "wph":
                myWebView.loadUrl("https://www.vip.com/index.php");
                break;
            case "albb":
                myWebView.loadUrl("https://ali-home.alibaba.com");
                break;

        }
    }
}
