package com.emansj.mpogo.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.emansj.mpogo.R;
import com.emansj.mpogo.helper.AppGlobal;
import com.emansj.mpogo.helper.ExceptionHandler;
import com.emansj.mpogo.helper.Tools;
import com.emansj.mpogo.helper.VolleyErrorHelper;
import com.emansj.mpogo.helper.VolleySingleton;
import com.emansj.mpogo.model.Notif;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class NotificationItemActivity extends AppCompatActivity {


    //Standard vars
    private static final String TAG = NotificationItemActivity.class.getSimpleName();
    private Context m_Ctx;
    private AppGlobal m_Global;

    //View vars
    private View parent_view;
    private Toolbar toolbar;
    private ProgressBar progress;
    private WebView wvContent;

    //Custom vars
    private Notif m_Notif;
    private int m_NotificationId;


    //---------------------------------------OVERRIDE
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        setContentView(R.layout.activity_notification_item);

        //Activity settings
        parent_view = findViewById(android.R.id.content);
        m_Ctx = NotificationItemActivity.this;
        m_Global = AppGlobal.getInstance(m_Ctx);

        //Get extras
        Bundle extras = getIntent().getExtras();
        if(extras != null)
        {
            m_NotificationId = (int) extras.get("notificationid");
        }

        initToolbar();
        initComponent();
        initData();
    }

    @Override
    protected void onStop () {
        super.onStop();
        VolleySingleton.getInstance(m_Ctx).cancelPendingRequests(TAG);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        VolleySingleton.getInstance(m_Ctx).cancelPendingRequests(TAG);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_delete, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else if(item.getItemId() == R.id.action_delete) {
            deleteNotification();
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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Toolbar Title & SubTitle
        ((TextView) parent_view.findViewById(R.id.tvToolbarTitle)).setText("Notifikasi");
    }

    private void initComponent() {
        wvContent = parent_view.findViewById(R.id.wvContent);
        wvContent.getSettings().setJavaScriptEnabled(true);
    }

    private void initData() {
        m_Notif = new Notif();
        getDataNotification();
    }


    //---------------------------------------ACTIONS
    private void deleteNotification(){
        m_Notif.removeNotif(m_Ctx, m_Notif);
        Intent intent = new Intent();
        intent.putExtra("item_deleted", true);
        setResult(RESULT_OK, intent);
        finish();
    }


    //---------------------------------------GET DATA
    private void getDataNotification() {
        final ProgressDialog progressDialog = new ProgressDialog(m_Ctx);
        progressDialog.setCancelable(true);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        String api = "/notification/get_notification";
        String params = String.format("?id=%1$d", m_NotificationId);
        String url = AppGlobal.URL_ROOT + api + params;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            int status = response.getInt("status");
                            if (status == 200 )
                            {
                                JSONArray data = response.getJSONArray("data");
                                if (data.length() > 0)
                                {
                                    JSONObject row = data.getJSONObject(0);

                                    final Notif notif = new Notif();
                                    notif.NotificationId = Tools.parseInt(row.getString("notificationid"));
                                    notif.Title = Tools.parseString(row.getString("title"));
                                    notif.Message = Tools.parseString(row.getString("message"));
                                    String content = Tools.parseString(row.getString("content"));
                                    if (content == null) {
                                        content = "<h3>" + notif.Title + "</h3> <br><br> <p>" + notif.Message + "</p> <br>";
                                    }
                                    wvContent.loadData(content, "text/html; charset=utf-8", "UTF-8");

                                    //set this notification has been read
                                    notif.setHasBeenRead(m_Ctx, m_Notif.NotificationId);
                                    m_Notif = notif;
                                }
                            }
                            progressDialog.dismiss();

                        }catch (JSONException e){
                            e.printStackTrace();
                            progressDialog.dismiss();
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        progressDialog.dismiss();
                        VolleyErrorHelper.showError(error, m_Ctx);
                    }
                }
        );
        VolleySingleton.getInstance(m_Ctx).addToRequestQueue(request, TAG);
    }

}
