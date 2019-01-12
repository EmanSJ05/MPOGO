package com.emansj.mpogo.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.emansj.mpogo.helper.AppGlobal;
import com.emansj.mpogo.helper.AppOten;
import com.emansj.mpogo.helper.IDataCallback;
import com.emansj.mpogo.helper.Tools;
import com.emansj.mpogo.helper.VolleyErrorHelper;
import com.emansj.mpogo.helper.VolleySingleton;
import com.emansj.mpogo.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
    private boolean activityFinished = false;
    private int m_UserId;
    private String m_UserName;
    private String m_UserPassword;
    private boolean m_IsRemember;


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
//        //Splash Animation
//        Thread thread = new Thread(){
//            public void run(){
//                try {
//                    sleep(1000 * SLEEP_TIMER);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        };
//        thread.start();

//        SharedPreferences.Editor editor = getSharedPreferences("MPOGO.SETTINGS", MODE_PRIVATE).edit();
//        editor.putBoolean("is_remember_me", true);
//        editor.putInt("user_login_id", 1179);
//        editor.putString("user_login_name", "distannaksambas@yahoo.co.id");
//        editor.putString("user_login_password", "Admin123");
//        editor.commit();
    }

    @Override
    protected void onStop () {
        super.onStop();
    }


    //---------------------------------------INIT COMPONENTS & DATA
    private void initComponent(){
        ly1 = findViewById(R.id.lay1);
        ly2 = findViewById(R.id.lay2);
    }

    private void initData(){
        //LOAD INIT DATA
        m_Global.loadInitData();

        //LOAD LAST LOGIN
        m_Global.loadLastLogin();


        //set goto Activity Login
        final Runnable gotoLogin = new Runnable() {
            @Override
            public void run() {
                gotoActivityLogin();
            }
        };

        //set goto Activity Home
        final Runnable gotoHome = new Runnable() {
            @Override
            public void run() {
                gotoActivityHome();
            }
        };

        //set Load Tahun RKA and Goto Activity Login
        final Runnable loadTahunAndGotoLogin = new Runnable() {
            @Override
            public void run() {
                m_Global.loadTahunRKAList(gotoLogin, null);
            }
        };

        //set Load Tahun RKA and Goto Activity Home
        final Runnable loadTahunAndGotoHome = new Runnable() {
            @Override
            public void run() {
                Runnable r = new Runnable() {
                    @Override
                    public void run() {
                        m_Global.loadTahunRKAList(gotoHome, null);
                    }
                };
                m_Global.loadUserProfile(r, null);
            }
        };

        //set Run Animation and Goto Login
        final Runnable runAnimationAndGotoLogin = new Runnable() {
            @Override
            public void run() {
                Runnable r = new Runnable() {
                    @Override
                    public void run() {
                        runAnimation(gotoLogin);
                    }
                };
                m_Global.loadUserProfile(r, null);
            }
        };


        //AUTO LOGIN OR GOTO LOGIN SCREEN
        m_IsRemember = m_Global.getIsRememberMe();
        if (m_IsRemember)
        {
            runAnimation(null);
            int userId = m_Global.getUserLoginId();
            String userName = m_Global.getUserLoginName();
            String password = m_Global.getUserLoginPass();
            if(userId == 0){
                m_Global.loadTahunRKAList(gotoLogin, null);
            } else {
                AppOten appOten = new AppOten(m_Ctx);
                appOten.logIn(userName, password, loadTahunAndGotoHome, loadTahunAndGotoLogin);
            }
        } else {
//            m_Global.loadTahunRKAList(null);
//            runAnimation(gotoLogin);
            runAnimation(loadTahunAndGotoLogin);
        }
    }


    //---------------------------------------ACTIONS
    private void runAnimation(final Runnable methodToFinish){
        aniUptodown = AnimationUtils.loadAnimation(this,R.anim.uptodown);
        aniDowntoup = AnimationUtils.loadAnimation(this,R.anim.downtoup);
        ly1.setAnimation(aniUptodown);
        ly2.setAnimation(aniDowntoup);

        Thread thread = new Thread(){
            public void run(){
                try {
                    sleep(1000 * SLEEP_TIMER);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    if (methodToFinish != null){
                        methodToFinish.run();
                    }
                }
            }
        };
        thread.start();
    }

    private void gotoActivityLogin() {
        Intent intent = new Intent(m_Ctx, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void gotoActivityHome() {
        Intent intent = new Intent(m_Ctx, DrawerActivity.class);
        startActivity(intent);
        finish();
    }

//    private void logIn(){
//        final ProgressDialog progressDialog = new ProgressDialog(m_Ctx);
//        progressDialog.setMessage("Authenticating...");
//        progressDialog.show();
//
//        String url = AppGlobal.URL_ROOT + "/Oten/login";
//        StringRequest strReq = new StringRequest(Request.Method.POST, url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        try {
//                            JSONObject jsonObj = new JSONObject(response);
//                            int status = jsonObj.getInt("status");
//
//                            if (status == 200) { //login succeed
//                                int userId = jsonObj.getInt("userid");
//
//                                //set to AppGlobal
//                                m_Global.setIsRememberMe(m_IsRemember);
//                                m_Global.setUserLoginId(userId);
//                                m_Global.setUserLoginName(m_UserName);
//                                m_Global.setUserLoginPass(m_UserPassword);
//                                progressDialog.dismiss();
//
//                                //LOAD USER PROFILE
//                                Runnable r = new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        gotoActivityHome();
//                                    }
//                                };
//                                m_Global.loadUserProfile(r);
//
//                            }else{
//                                String errorMsg = jsonObj.getString("message");
//                                Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
//                                progressDialog.dismiss();
//                                gotoActivityLogin();
//                            }
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                            Toast.makeText(getApplicationContext(), "Login error: " + e.getMessage(), Toast.LENGTH_LONG).show();
//                            progressDialog.dismiss();
//                            gotoActivityLogin();
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        VolleyErrorHelper.showError(error, m_Ctx);
//                        progressDialog.dismiss();
//                        gotoActivityLogin();
//                    }
//                }
//        )
//        {
//            // kirim parameter ke server
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("username", m_UserName);
//                params.put("password", m_UserPassword);
//
//                return params;
//            }
//        };
//        VolleySingleton.getInstance(m_Ctx).addToRequestQueue(strReq, TAG);
//    }

}
