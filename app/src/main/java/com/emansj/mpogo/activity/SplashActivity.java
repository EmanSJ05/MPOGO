package com.emansj.mpogo.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import com.emansj.mpogo.helper.AppGlobal;
import com.emansj.mpogo.helper.Tools;
import com.emansj.mpogo.helper.VolleySingletonHelper;
import com.emansj.mpogo.R;

import java.util.ArrayList;
import java.util.List;


public class SplashActivity extends Activity {

    //Standard vars
    private static final String TAG = "MPODebug"; //LoginActivity.class.getSimpleName();
    private Context m_Context;
    private SharedPreferences m_SharedPref;

    //Custom vars
    private int SLEEP_TIMER = 4;

    //Define views
    private LinearLayout m_Layout1, m_Layout2;
    private Animation m_Uptodown, m_Downtoup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Tools.darkenStatusBar(this, R.color.grey_10);
        setContentView(R.layout.activity_splash);
        //getSupportActionBar().hide();

        //Activity settings
        m_Context = SplashActivity.this;
        m_SharedPref = getSharedPreferences("mpo_settings", MODE_PRIVATE);

        //Load data
        loadTahunRka();

        //Init views
        m_Layout1 = findViewById(R.id.lay1);
        m_Layout2 = findViewById(R.id.lay2);
        m_Uptodown = AnimationUtils.loadAnimation(this,R.anim.uptodown);
        m_Downtoup = AnimationUtils.loadAnimation(this,R.anim.downtoup);
        m_Layout1.setAnimation(m_Uptodown);
        m_Layout2.setAnimation(m_Downtoup);

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
        VolleySingletonHelper.getInstance(m_Context).getRequestQueue().cancelAll(TAG);
    }

    private void gotoActivityLogin() {
        Intent intent = new Intent(m_Context, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void loadTahunRka() {

        List<String> list_tahunrka = new ArrayList<String>();
        list_tahunrka.add("2018");
        list_tahunrka.add("2017");
        AppGlobal g = AppGlobal.getInstance();
        g.TahunRKAs(list_tahunrka);

//        String url = AppGlobal.url+"/AppGlobal/tahunrka";
//        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
//                new Response.Listener<JSONObject>(){
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        try{
//                            JSONArray jsonArray = response.getJSONArray("data");
//                            List<String> list_tahunrka = new ArrayList<>();
//
//                            for (int i = 0; i < jsonArray.length(); i++)
//                            {
//                                JSONObject dt = jsonArray.getJSONObject(i);
//                                list_tahunrka.add(dt.getString("tahun"));
//                            }
//
//                            AppGlobal g = AppGlobal.getInstance();
//                            g.TahunRKAs(list_tahunrka);
//
//                        }catch (JSONException e){
//                            e.printStackTrace();
//                        }
//                    }
//                },
//                new Response.ErrorListener(){
//                    @Override
//                    public void onErrorResponse(VolleyError error){
//                        //error.printStackTrace();
//                        VolleyErrorHelper.showError(error, m_Context);
//                    }
//                }
//        );
//        VolleySingletonHelper.getInstance(m_Context).addToRequestQueue(request, TAG);
    }
    //endregion
}
