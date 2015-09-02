package net.gocourse.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.DownloadListener;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import net.gocourse.teacher.MyApplication;
import net.gocourse.teacher.R;

import java.io.Serializable;
import java.util.Map;


/**
 * 定义统一的基于WebView的浏览器
 * @author xc
 * @version 1.0
 */
public class WebViewActivity extends AppCompatActivity {
    private MyApplication app;
    private WebView webView;
    private String url;
    private Toolbar toolbar;
    private String title;
    private ProgressBar pb;
    private WebSettings settings;
    private boolean loadFinish=false;
    private Map<String,String> cookies;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app=(MyApplication)getApplication();
        if (app.getAppTheme()==1)
            setTheme(R.style.AppTheme_Base_dark);
        else
            setTheme(R.style.AppTheme_Base_Light);
        setContentView(R.layout.activity_web_view);
        initIntent();
        initView();
        initWebView();
    }


    void initIntent(){
        Intent it=getIntent();
        url=it.getStringExtra("url");
        MyCookies myCookies=(MyCookies)it.getSerializableExtra("cookies");
        if (myCookies!=null)
            cookies=myCookies.getCookies();

    }
    void initWebView(){
        webView=(WebView)super.findViewById(R.id.webView);
        webView.setWebChromeClient(new MWebChromeClient());
        webView.setWebViewClient(new MWebClient());
        webView.setDownloadListener(new MyWebViewDownLoadListener());

        settings=webView.getSettings();
        settings.setJavaScriptEnabled(true);        //启用javascript脚本
        settings.setSupportZoom(true);              //支持缩放
        settings.setUseWideViewPort(true);          //任意比例缩放
        settings.setLoadWithOverviewMode(true);     //缩放至屏幕大小
        settings.setPluginState(WebSettings.PluginState.ON);
        setCookies();
        webView.loadUrl(url);
    }
    void setCookies(){
        CookieSyncManager.createInstance(this);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        cookieManager.removeSessionCookie();//移除
        if (cookies!=null&&cookies.size()!=0){
            for (String key:cookies.keySet()){
                String cookie=key+"="+cookies.get(key);
                cookieManager.setCookie(url,cookie);//cookies是在HttpClient中获得的cookie
            }
        }
        CookieSyncManager.getInstance().sync();
    }
    void initView(){
        toolbar=(Toolbar)super.findViewById(R.id.web_view_toolbar);
        setSupportActionBar(toolbar);
        //页面加载进度条
        pb=(ProgressBar)super.findViewById(R.id.pb);
        pb.setMax(100);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_web_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent it;
        switch (item.getItemId()){
            case android.R.id.home:             //返回
                WebViewActivity.this.finish();
                return true;
            case R.id.action_open_browser:      //用系统默认浏览器打开
                it=new Intent();
                it.setAction("android.intent.action.VIEW");
                it.setData(Uri.parse(url));
                startActivity(it);
                break;
            case R.id.action_clear_cache:       //清理缓存
                webView.clearCache(true);
                break;
            case R.id.action_reload:
                if(loadFinish)
                    webView.reload();
                else
                    webView.stopLoading();
                break;

        }

        return super.onOptionsItemSelected(item);
    }
    class MWebChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            Log.i("newProgress",newProgress+"");
            //动态在标题栏显示进度条
            if (pb.getVisibility()==View.GONE){
                pb.setVisibility(View.VISIBLE);
                Log.i("reload",newProgress+"");
            }
            pb.setProgress(newProgress);
            if (newProgress==100){
                pb.setVisibility(View.GONE);
                Log.i("finish",newProgress+"");
            }
            super.onProgressChanged(view, newProgress);
        }
        @Override
        public void onReceivedTitle(WebView view, String title) {
            //设置当前activity的标题栏
            WebViewActivity.this.title=title;
            WebViewActivity.this.setTitle(title);
            super.onReceivedTitle(view, title);
        }

        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            return super.onJsAlert(view, url, message, result);
        }

    }
    class MWebClient extends WebViewClient{
        //页面加载完成
        @Override
        public void onPageFinished(WebView view, String url) {
            loadFinish=true;
            super.onPageFinished(view, url);
        }
        //页面开始加载
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            loadFinish=false;
            super.onPageStarted(view, url, favicon);
        }
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            WebViewActivity.this.url=url;
            view.loadUrl(url);
            return true;
        }
        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl)
        {
            final String errorUrl=failingUrl;
            AlertDialog.Builder builder = new AlertDialog.Builder(WebViewActivity.this);
            builder.setTitle("页面发生错误");
            builder.setMessage(description);
            builder.setPositiveButton("重新加载", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    webView.loadUrl(errorUrl);
                }
            });
            builder.setNegativeButton("返回", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    webView.goBack();
                }
            });
            builder.show();
        }
    }
    //屏蔽返回键 使其控制网页的后退
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private class MyWebViewDownLoadListener implements DownloadListener {
        //文件下载
        @Override
        public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype,
                                    long contentLength) {
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
    }
    public static class MyCookies implements Serializable {
        private Map<String,String> cookies;
        public void setCookies(Map<String,String> cookies){
            this.cookies=cookies;
        }
        public Map<String,String> getCookies(){
            return cookies;
        }

    }

}
