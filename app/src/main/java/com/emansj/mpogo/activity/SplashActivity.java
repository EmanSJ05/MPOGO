package com.emansj.mpogo.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.emansj.mpogo.helper.AppGlobal;
import com.emansj.mpogo.helper.Tools;
import com.emansj.mpogo.helper.VolleyErrorHelper;
import com.emansj.mpogo.helper.VolleySingleton;
import com.emansj.mpogo.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class SplashActivity extends Activity {

    //Standard vars
    private static final String TAG = LoginActivity.class.getSimpleName();
    private Context m_Ctx;
    private AppGlobal m_Global;
    private AppGlobal.Data m_GlobalData;

    //View vars
    private LinearLayout ly1, ly2;
    private Animation aniUptodown, aniDowntoup;

    //Custom vars
    private int SLEEP_TIMER = 4;


    //---------------------------------------OVERRIDE
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Tools.darkenStatusBar(this, R.color.grey_10);
        setContentView(R.layout.activity_splash);
        //getSupportActionBar().hide();

        //Activity settings
        m_Ctx = SplashActivity.this.getApplicationContext();
        m_Global = AppGlobal.getInstance(m_Ctx);
        m_GlobalData = m_Global.new Data();

        initComponent();
        initData();

        //Splash Animation
        Thread thread = new Thread(){
            public void run(){
                try {
                    sleep(1000 * SLEEP_TIMER);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    gotoActivityLogin();
                }
            }
        };
        thread.start();
    }

    @Override
    protected void onStop () {
        super.onStop();
        m_GlobalData.cancelAllRequest();
    }


    //---------------------------------------INIT COMPONENTS & DATA
    private void initComponent(){
        ly1 = findViewById(R.id.lay1);
        ly2 = findViewById(R.id.lay2);
        aniUptodown = AnimationUtils.loadAnimation(this,R.anim.uptodown);
        aniDowntoup = AnimationUtils.loadAnimation(this,R.anim.downtoup);
        ly1.setAnimation(aniUptodown);
        ly2.setAnimation(aniDowntoup);
    }

    private void initData(){
        //LOAD INIT DATA
        m_Global.loadInitData();

        //LOAD ALL TAHUN RKA
        Runnable r = new Runnable() {
            @Override
            public void run() {
                gotoActivityLogin();
            }
        };
        m_Global.loadTahunRKAList(r);
    }


    //---------------------------------------ACTIONS
    private void gotoActivityLogin() {
        Intent intent = new Intent(m_Ctx, LoginActivity.class);
        startActivity(intent);
        finish();
    }


}
