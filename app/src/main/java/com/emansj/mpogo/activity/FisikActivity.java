package com.emansj.mpogo.activity;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.emansj.mpogo.adapter.AdapterReke;
import com.emansj.mpogo.helper.AppGlobal;
import com.emansj.mpogo.R;
import com.emansj.mpogo.model.RealisasiKeuangan;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class FisikActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    //Standard vars
    private static final String TAG = FisikActivity.class.getSimpleName();
    private AppGlobal m_Global;
    private AppGlobal.Data m_GlobalData;
    private Context m_Ctx;
    private View parent_view;

    //View vars
    private Toolbar toolbar;
    private WebView webView;
    private SwipeRefreshLayout refreshLayout;

    //Custom vars
    private ValueCallback<Uri> mUploadMessage;
    public ValueCallback<Uri[]> uploadMessage;
    public static final int REQUEST_SELECT_FILE = 100;
    private final static int FILECHOOSER_RESULTCODE = 1;

    // SS
    public int m_Tahun;
    public int m_UserId;


    //---------------------------------------OVERRIDE
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fisik);

        //Activity settings
        parent_view = findViewById(android.R.id.content);
        m_Ctx = FisikActivity.this;
        m_Global = AppGlobal.getInstance(m_Ctx);
        m_GlobalData = m_Global.new Data();

        initToolbar();
        initComponent();
        initData();
        initSettings();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_fisik, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.home) {
            finish();
        }
        else if(item.getItemId() == R.id.action_entri_fisik) {
            Uri url = Uri.parse(webView.getUrl());
            String strUrl = m_Global.URL_MPO +
                    "mInputFisik/index?userid=" + url.getQueryParameter("userid") +
                    "&tahun=" + url.getQueryParameter("tahun");
            webView.loadUrl(strUrl);
        }
        else {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    //---------------------------------------INIT COMPONENTS & DATA
    private void initToolbar(){
        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Fisik");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initComponent(){
        webView = (WebView) findViewById(R.id.webView);
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);

        //listener
        refreshLayout.setOnRefreshListener(this);
    }

    private void initData(){
        this.m_Tahun = m_Global.getTahunRKA();
        this.m_UserId = m_Global.getUserLoginId();
    }

    private void initSettings(){
        //web view settings
        WebSettings mWebSettings = webView.getSettings();
        mWebSettings.setJavaScriptEnabled(true);
        mWebSettings.setSupportZoom(false);
        mWebSettings.setAllowFileAccess(true);
        mWebSettings.setAllowFileAccess(true);
        mWebSettings.setAllowContentAccess(true);

        // entahlah kenapa harus ditambahin ini
        webView.setOnKeyListener(new View.OnKeyListener(){
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
                    return false;
                }
                return false;
            }
        });
        // entahlah kenapa harus ditambahin ini

        webView.setWebViewClient(new MyBrowser());

        webView.clearCache(true);
        webView.loadUrl(m_Global.URL_MPO + "mInputFisik/index?userid="+m_UserId+"&tahun="+m_Tahun);

        webView.setWebChromeClient(new WebChromeClient() {
            // For 3.0+ Devices (Start)
            // onActivityResult attached before constructor
            protected void openFileChooser(ValueCallback uploadMsg, String acceptType)
            {
                mUploadMessage = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("image/*");
                startActivityForResult(Intent.createChooser(i, "File Browser"), FILECHOOSER_RESULTCODE);
            }

            // For Lollipop 5.0+ Devices
            public boolean onShowFileChooser(WebView mWebView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams)
            {
                if (uploadMessage != null) {
                    uploadMessage.onReceiveValue(null);
                    uploadMessage = null;
                }

                uploadMessage = filePathCallback;
                Intent intent = fileChooserParams.createIntent();

                try{
                    startActivityForResult(intent, REQUEST_SELECT_FILE);

                } catch (ActivityNotFoundException e) {
                    uploadMessage = null;
                    Toast.makeText(FisikActivity.this.getApplicationContext(), "Cannot Open File Chooser", Toast.LENGTH_LONG).show();
                    return false;

                }
                return true;
            }

            //For Android 4.1 only
            protected void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture)
            {
                mUploadMessage = uploadMsg;
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "File Browser"), FILECHOOSER_RESULTCODE);
            }

            protected void openFileChooser(ValueCallback<Uri> uploadMsg)
            {
                mUploadMessage = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("image/*");
                startActivityForResult(Intent.createChooser(i, "File Chooser"), FILECHOOSER_RESULTCODE);
            }
        });
    }


    //---------------------------------------ACTIONS
    @Override
    public void onRefresh() {
        webView.reload();
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void onBackPressed() {
        System.out.println("cuyyy2");
        if(webView.canGoBack()) {
            Uri url = Uri.parse(webView.getUrl());
            System.out.println(url);
            Set<String> paramNames = url.getQueryParameterNames();
            for (String key: paramNames) {
                String value = url.getQueryParameter(key);
                System.out.println(value);
            }
            webView.goBack();
        } else {
            Uri url = Uri.parse(webView.getUrl());
            System.out.println(url);
            Set<String> paramNames = url.getQueryParameterNames();
            for (String key: paramNames) {
                String value = url.getQueryParameter(key);
                System.out.println(value);
            }
            super.onBackPressed();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()){
            Uri url = Uri.parse(webView.getUrl());
            String LastPath = url.getLastPathSegment();
            String param = "?1=1";
            Set<String> paramNames = url.getQueryParameterNames();
            for (String key: paramNames) {
                String value = url.getQueryParameter(key);
                param += "&" + key + "=" + value;
            }
            System.out.println(param);

            String backUrl = "";
            switch (LastPath){
                case "index":
                    super.onBackPressed();
                    break;
                case "kegiatan_list":
                    webView.loadUrl(m_Global.URL_MPO + "mInputFisik/index?userid="+url.getQueryParameter("userid")+"&tahun="+url.getQueryParameter("tahun"));
                    break;
                case "realisasi_list":
                    webView.loadUrl(m_Global.URL_MPO + "mInputFisik/kegiatan_list?idsatker="+url.getQueryParameter("idsatker")+"&userid="+m_UserId+"&tahun="+m_Tahun);
                    break;
                case "form":
                    webView.loadUrl(m_Global.URL_MPO + "mInputFisik/realisasi_list?idsatker="+url.getQueryParameter("idsatker")+"&iditem="+url.getQueryParameter("iditem"));
                    break;
                default: webView.goBack();
            }
            return true;
        }


        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            if (requestCode == REQUEST_SELECT_FILE)
            {
                if (uploadMessage == null)
                    return;
                uploadMessage.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(resultCode, intent));
                uploadMessage = null;
            }
        }
        else if (requestCode == FILECHOOSER_RESULTCODE)
        {
            if (null == mUploadMessage)
                return;
            // Use MainActivity.RESULT_OK if you're implementing WebView inside Fragment
            // Use RESULT_OK only if you're implementing WebView inside an Activity
            Uri result = intent == null || resultCode != FisikActivity.RESULT_OK ? null : intent.getData();
            mUploadMessage.onReceiveValue(result);
            mUploadMessage = null;
        }
        else
            Toast.makeText(FisikActivity.this.getApplicationContext(), "Failed to Upload Image", Toast.LENGTH_LONG).show();
    }


    //---------------------------------------CLASS
    private class MyBrowser extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            //view.loadUrl(url);
            return false;
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            view.loadData("Maaf Internet Anda tidak stabil", "text/html", "utf-8");
            super.onReceivedError(view, request, error);
        }

//        @Override
//        public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
//            view.loadData("Maaf Internet Anda tidak stabil", "text/html", "utf-8");
//            super.onReceivedHttpError(view, request, errorResponse);
//        }
    }
}
