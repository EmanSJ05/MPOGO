package com.emansj.mpogo.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import com.emansj.mpogo.R;
import com.emansj.mpogo.helper.AppGlobal;
import com.emansj.mpogo.helper.AppOten;
import com.emansj.mpogo.helper.Tools;


public class Splash2Activity extends Activity {

    //Standard vars
    private static final String TAG = Splash2Activity.class.getSimpleName();
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
        m_Ctx = Splash2Activity.this.getApplicationContext();
        m_Global = AppGlobal.getInstance(m_Ctx);
        m_GlobalData = m_Global.new Data();

        initComponent();
        initData();

        final Runnable run_GotoActivityLogin = new Runnable() {
            @Override
            public void run() {
                gotoActivityLogin();
            }
        };

        runAnimation(run_GotoActivityLogin);
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

}
