package com.emansj.mpogo.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.emansj.mpogo.helper.AppGlobal;
import com.emansj.mpogo.R;

import java.util.HashMap;
import java.util.Map;

import static com.android.volley.Request.Method;

public class TestActivity extends Activity {

    //Define views
    private Button btnLogin;
    private Context context;
    private ProgressDialog pDialog;
    private SharedPreferences pref;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        //Activity settings
        context = TestActivity.this;
        pref = getSharedPreferences("data", MODE_PRIVATE);
        pDialog = new ProgressDialog(context);
        pDialog.setCancelable(false);

        //Initial views
        btnLogin = findViewById(R.id.btnLogin);

        //Listener btnLogin
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logIn();
            }
        });

    }

    private void logIn(){
        pDialog.setMessage("Testing...");
        showDialog();

        //Create s string request
        StringRequest stringRequest = new StringRequest(Method.POST, AppGlobal.URL_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(context, response.toString(), Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hideDialog();
                        Toast.makeText(context, error.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                }
        )

        {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                // kirim parameter ke server

                return params;
            }
        };
        //Adding the string request to the queue
        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void gotoActivityHome() {
        Intent intent = new Intent(context, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

}
