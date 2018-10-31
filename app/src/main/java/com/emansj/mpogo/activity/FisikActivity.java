package com.emansj.mpogo.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.emansj.mpogo.helper.AppGlobal;
import com.emansj.mpogo.R;

public class FisikActivity extends Activity {

    //Standard vars
    private static final String TAG = FisikActivity.class.getSimpleName();
    private AppGlobal m_Global;
    private AppGlobal.Data m_GlobalData;

    //Custom vars
    //write here ...
    private WebView m_WebView;


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fisik);

        m_WebView = (WebView) findViewById(R.id.webview);
        m_WebView.getSettings().setLoadsImagesAutomatically(true);
        m_WebView.getSettings().setJavaScriptEnabled(true);
        m_WebView.getSettings().setDomStorageEnabled(true);

        // Tiga baris di bawah ini agar laman yang dimuat dapat
        // melakukan zoom.
        m_WebView.getSettings().setSupportZoom(true);
        m_WebView.getSettings().setBuiltInZoomControls(true);
        m_WebView.getSettings().setDisplayZoomControls(false);

        // Baris di bawah untuk menambahkan scrollbar di dalam WebView-nya
        m_WebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        m_WebView.setWebViewClient(new WebViewClient());
        //webView.loadUrl("https://www.codepolitan.com");
        //m_WebView.loadUrl("http://beta.html5test.com/");
        m_WebView.loadUrl("http://mpo.psp.pertanian.go.id/realisasi_fisik?x=11&y=12");
    }

    @Override
    public void onBackPressed() {
        if(m_WebView.canGoBack()) {
            m_WebView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
