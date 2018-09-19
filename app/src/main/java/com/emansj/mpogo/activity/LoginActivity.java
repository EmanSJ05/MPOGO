package com.emansj.mpogo.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.emansj.mpogo.helper.AppGlobal;
import com.emansj.mpogo.helper.VolleyErrorHelper;
import com.emansj.mpogo.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.android.volley.Request.*;

public class LoginActivity extends Activity {

    //Standard vars
    private static final String TAG = LoginActivity.class.getSimpleName();
    private Context m_Context;
    private ProgressDialog m_PDialog;
    private SharedPreferences m_SharedPref;
    private AppGlobal m_Global = AppGlobal.getInstance();

    //Custom vars
    int m_UserId;
    String m_UserName;
    String m_UserFullName;
    boolean m_Remember_Me;
    int m_TahunRKA;

    //Define views
    private EditText editUsername, editPassword;
    private CheckBox chkRememberMe;
    private Button btnLogin;
    private Button btnTahunRKA;


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        //Activity settings
        m_Context = LoginActivity.this;
        m_SharedPref = getSharedPreferences("mpo_settings", MODE_PRIVATE);
        m_PDialog = new ProgressDialog(m_Context);
        m_PDialog.setCancelable(false);

        //Initial views
        editUsername = findViewById(R.id.editUsername);
        editPassword = findViewById(R.id.editPassword);
        chkRememberMe = findViewById(R.id.chkRememberMe);
        btnLogin = findViewById(R.id.btnLogin);
//        btnTahunRKA = findViewById(R.id.btnTahunRKA);

        //Start process
        //load tahun rka
//        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, m_Global.TahunRKAs());
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        btnTahunRKA.setAdapter(adapter);

        //load last settings
        final boolean is_remember_me = m_SharedPref.getBoolean("remember_me", false);
        if (is_remember_me){
            m_UserId = m_SharedPref.getInt("user_id",0);
            m_UserName = m_SharedPref.getString("user_name",null);
            m_UserFullName = m_SharedPref.getString("user_full_name", null);

            chkRememberMe.setChecked(is_remember_me);
            editUsername.setText(m_UserName);
            //chkRememberMe.requestFocus();
        }

        //Listener btnLogin
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editUsername.getText().toString().trim();
                String password = editPassword.getText().toString().trim();

                if(!username.isEmpty() && !password.isEmpty()){
                    //logIn(username, password);
                    gotoActivityHome();
                }else {
                    Toast.makeText(getApplicationContext(),"Harap masukkan Username dan Password!", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void logIn(final String userName, final String userPass){
        String tag_string_req = "req_login";
        m_PDialog.setMessage("Authenticating...");
        showDialog();

        //Create s string request
        StringRequest strReq = new StringRequest(Method.POST, AppGlobal.URL_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "Login Response: " + response.toString());
                        hideDialog();

                        try {
                            JSONObject jsonObj = new JSONObject(response);
                            int status = jsonObj.getInt("status");

                            if (status == 200) { //login succeed
                                //get value & response
                                m_UserId = jsonObj.getInt("userid");
                                m_UserName = jsonObj.getString("username");
                                m_UserFullName = jsonObj.getString("fullname");
                                m_Remember_Me = chkRememberMe.isChecked();
                                m_TahunRKA  = 2018; //Integer.parseInt(btnTahunRKA.getSelectedItem().toString());

                                //save to pref
                                SharedPreferences.Editor editor = m_SharedPref.edit();
                                editor.putInt("user_id", m_UserId);
                                editor.putString("user_name", m_UserName);
                                editor.putString("user_full_name", m_UserFullName);
                                editor.putBoolean("remember_me", m_Remember_Me);
                                editor.commit();

                                //set to AppGlobal
                                AppGlobal g = AppGlobal.getInstance();
                                g.UserId(m_UserId);
                                g.UserName(m_UserName);
                                g.UserFullName(m_UserFullName);
                                g.TahunRKA(m_TahunRKA);

                                gotoActivityHome();

                            } else { //login failed
                                String errorMsg = jsonObj.getString("message");
                                Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            // JSON error
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Login Error: " + error.getMessage());
                        String msg = VolleyErrorHelper.getMessage(error, m_Context);
                        Toast.makeText(m_Context, msg, Toast.LENGTH_LONG).show();
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
        //Adding the string request to the queue
        Volley.newRequestQueue(this).add(strReq);
        //AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void gotoActivityMain() {
        Intent intent = new Intent(m_Context, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void gotoActivityHome() {
        Intent intent = new Intent(m_Context, DrawerActivity.class);
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
