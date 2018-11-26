package com.emansj.mpogo.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import com.emansj.mpogo.helper.AppGlobal;
import com.emansj.mpogo.helper.AppOten;
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

    //Custom vars
    //....


    //---------------------------------------OVERRIDE
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
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
                        m_Global.loadUserProfile(run_GotoActivityHome);

                    }
                };

                //save is remember me to AppGlobal (settings)
                m_Global.setIsRememberMe(chkRememberMe.isChecked());

                //logIn();
                final String userName = editUsername.getText().toString().trim();
                final String password = editPassword.getText().toString().trim();
                AppOten appOten = new AppOten(m_Ctx);
                appOten.logIn(userName, password, run_loadUserProfile, null);
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
            editPassword.setText("");
            chkRememberMe.setChecked(true);
            //btnLogin.requestFocus();
        }
    }


    //---------------------------------------ACTIONS
    private void logIn(){
        final String userName = editUsername.getText().toString().trim();
        final String userPass = editPassword.getText().toString().trim();

        if(userName.isEmpty() && userPass.isEmpty()){
            Toast.makeText(getApplicationContext(),"Harap masukkan Username dan Password!", Toast.LENGTH_LONG).show();
            return;
        }

        m_PDialog.setMessage("Authenticating...");
        showDialog();

        String api = "/Oten/login";
        String params = "";
        String url = AppGlobal.URL_ROOT + api + params;
        StringRequest strReq = new StringRequest(Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        hideDialog();

                        try {
                            JSONObject jsonObj = new JSONObject(response);
                            int status = jsonObj.getInt("status");

                            if (status == 200) { //login succeed
                                int userId = jsonObj.getInt("userid");

                                //set to AppGlobal
                                m_Global.setIsRememberMe(chkRememberMe.isChecked());
                                m_Global.setUserLoginId(userId);
                                m_Global.setUserLoginName(userName);
                                m_Global.setUserLoginPass(userPass);
                                //String androidId = Settings.Secure.getString(getContentResolver(),Settings.Secure.ANDROID_ID);
                                //m_Global.setUserAndoidId(androidId);

                                //LOAD USER PROFILE
                                Runnable r = new Runnable() {
                                    @Override
                                    public void run() {
                                        gotoActivityHome();
                                    }
                                };
                                m_Global.loadUserProfile(r);

                            }else{
                                String errorMsg = jsonObj.getString("message");
                                Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Login error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyErrorHelper.showError(error, m_Ctx);
                        hideDialog();
                    }
                }
        )
        {
            // kirim parameter ke server
            @Override
            protected Map<String, String> getParams() throws AuthFailureError  {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", userName);
                params.put("password", userPass);

                return params;
            }
        };
        VolleySingleton.getInstance(m_Ctx).addToRequestQueue(strReq, TAG);
    }

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
