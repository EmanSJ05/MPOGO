package com.emansj.mpogo.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import com.balysv.materialripple.MaterialRippleLayout;
import com.emansj.mpogo.helper.AppGlobal;
import com.emansj.mpogo.helper.AppOten;
import com.emansj.mpogo.helper.Tools;
import com.emansj.mpogo.helper.VolleyErrorHelper;
import com.emansj.mpogo.R;
import com.emansj.mpogo.helper.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.android.volley.Request.*;

public class LoginActivity extends Activity {

    //Standard vars
    private static final String TAG = LoginActivity.class.getSimpleName();
    private Context m_Ctx;
    private AppGlobal m_Global;
    private AppGlobal.Data m_GlobalData;
    private ProgressDialog m_PDialog;

    //View vars
    private View parent_view;
    private EditText editUsername, editPassword;
    private AppCompatCheckBox chkRememberMe;
    private Button btnLogin;
    private MaterialRippleLayout mrlAppInfo;
    private View lyAppInfo;
    private TextView tvAppVersion;

    //Custom vars
    //....


    //---------------------------------------OVERRIDE
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Tools.darkenStatusBar(this, R.color.grey_10);
        setContentView(R.layout.activity_login);

        //Activity settings
        parent_view = findViewById(android.R.id.content);
        m_Ctx = LoginActivity.this;
        m_Global = AppGlobal.getInstance(m_Ctx);
        m_GlobalData = m_Global.new Data();
        m_PDialog = new ProgressDialog(m_Ctx);
        m_PDialog.setCancelable(false);

        initComponent();
        initData();
    }

    @Override
    protected void onStop () {
        super.onStop();
        VolleySingleton.getInstance(m_Ctx).cancelPendingRequests(TAG);
        m_GlobalData.cancelRequest(TAG);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        VolleySingleton.getInstance(m_Ctx).cancelPendingRequests(TAG);
        m_GlobalData.cancelRequest(TAG);
    }


    //---------------------------------------INIT COMPONENTS & DATA
    private void initComponent(){
        editUsername = findViewById(R.id.editUsername);
        editPassword = findViewById(R.id.editPassword);
        chkRememberMe = findViewById(R.id.chkRememberMe);
        btnLogin = findViewById(R.id.btnLogin);
        mrlAppInfo = findViewById(R.id.mrlAppInfo);
        lyAppInfo = findViewById(R.id.lyAppInfo);
        tvAppVersion = findViewById(R.id.tvAppVersion);

//        chkRememberMe.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                CheckBox c = (CheckBox) v;
//                if (c.isChecked()){
//                    editUsername.setText("distannaksambas@yahoo.co.id");
//                    editPassword.setText("Admin123");
//                }
//            }
//        });

        //APP. INFO
        try {
            PackageInfo pinfo = m_Ctx.getPackageManager().getPackageInfo(getPackageName(), 0);
            int versionNumber = pinfo.versionCode;
            String versionName = pinfo.versionName;
            tvAppVersion.setText("Versi " + versionName);
        } catch (Exception e){
            e.printStackTrace();
        }
        mrlAppInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(m_Ctx, AboutActivity.class);
                startActivity(i);
            }
        });

        //LOGIN
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //LOGIN
                final Runnable run_GotoActivityHome = new Runnable() {
                    @Override
                    public void run() {
                        gotoActivityHome();
                    }
                };

                final Runnable run_loadUserProfile = new Runnable() {
                    @Override
                    public void run() {
                        m_Global.loadUserProfile(run_GotoActivityHome, null);

                    }
                };

                final Runnable run_ProgressStart = new Runnable() {
                    @Override
                    public void run() {
                        m_PDialog.setMessage("Otentikasi...");
                        m_PDialog.show();
                    }
                };

                final Runnable run_ProgressStop = new Runnable() {
                    @Override
                    public void run() {
                        m_PDialog.dismiss();
                    }
                };

                final Runnable run_ProgressLoginStop = new Runnable() {
                    @Override
                    public void run() {
                        ProgressBar progressLogin = findViewById(R.id.progressLogin);
                        progressLogin.setVisibility(View.GONE);
                        btnLogin.setVisibility(View.VISIBLE);
                    }
                };

                //save is remember me to AppGlobal (settings)
                m_Global.setIsRememberMe(chkRememberMe.isChecked());

                //logIn();
                final String userName = editUsername.getText().toString().trim();
                final String password = editPassword.getText().toString().trim();
                if(userName.isEmpty() && password.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Harap masukkan Username dan Password!", Toast.LENGTH_LONG).show();

                } else {
                    ProgressBar progressLogin = findViewById(R.id.progressLogin);
                    progressLogin.setVisibility(View.VISIBLE);
                    btnLogin.setVisibility(View.GONE);

                    AppOten appOten = new AppOten(m_Ctx);
                    appOten.logIn(userName, password, run_loadUserProfile, run_ProgressLoginStop);
                }
            }
        });
    }

    private void initData(){
        //LOAD LAST LOGIN
        m_Global.loadLastLogin();

        boolean is_remember_me = m_Global.getIsRememberMe();
        if (is_remember_me)
        {
            editUsername.setText(m_Global.getUserLoginName());
            editPassword.setText(m_Global.getUserLoginPass());
            chkRememberMe.setChecked(true);
        }
    }


    //---------------------------------------ACTIONS
    private void gotoActivityHome() {
        Intent intent = new Intent(m_Ctx, DrawerActivity.class);
        startActivity(intent);
        finish();
    }

    private void showDialog() {
        if (!m_PDialog.isShowing())
            m_PDialog.show();
    }

    private void hideDialog() {
        if (m_PDialog.isShowing())
            m_PDialog.dismiss();
    }
}
