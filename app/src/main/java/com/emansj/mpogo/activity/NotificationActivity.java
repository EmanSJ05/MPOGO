package com.emansj.mpogo.activity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.emansj.mpogo.R;
import com.emansj.mpogo.adapter.AdapterReke;
import com.emansj.mpogo.helper.AppUtils;
import com.emansj.mpogo.model.RealisasiKeuangan;

import java.util.List;

public class NotificationActivity extends AppCompatActivity {

    //Standard vars
    private static final String TAG = NotificationActivity.class.getSimpleName();
    private Context m_Ctx;
    private RequestQueue m_Queue;

    //Define views
    private View m_View;
    private ActionBar m_ActionBar;
    private Toolbar m_Toolbar;
    private FloatingActionButton fabChangePhoto;

    //Custom vars
    //write here ......
    myTask m_Task = new myTask();
    private List<RealisasiKeuangan> m_ListItem;
    private AdapterReke m_Adapter;
    private LinearLayout lyProgress;
    private ProgressBar pbProgress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress_basic);

        //Activity settings
        m_View = findViewById(android.R.id.content);
        m_Ctx = NotificationActivity.this;

        //lyProgress = findViewById(R.id.lyProgress);
        pbProgress = findViewById(R.id.pbProgress);
        pbProgress.setVisibility(View.VISIBLE);
        //Init view
        initToolbar();
        loading();
    }

    private void initToolbar(){
        m_Toolbar = findViewById(R.id.toolbar);
        m_Toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        setSupportActionBar(m_Toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else {
            Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
    private class myTask extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            showList();
            showList();
            showList();
            showList();
            showList();
            showList();
            showList();
            showList();
            showList();
            showTotal();
            return "ok";
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pbProgress.setVisibility(View.GONE);
        }
    }

    private void loading() {
        m_Task = new myTask();
        m_Task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void showList() {
        m_ListItem = RealisasiKeuangan.getSatker(m_Ctx);
        m_Adapter = new AdapterReke(m_Ctx, m_ListItem);
    }

    private void showTotal() {
        double totalPagu = 0;
        double totalSmartValue = 0;
        double totalSmartPercent = 0;
        double totalMpoValue = 0;
        double totalMpoPercent = 0;

        if (m_ListItem.isEmpty() == false) {

            for (int row = 0; row < m_ListItem.size(); row++) {
                totalPagu += m_ListItem.get(row).pagu;
                totalSmartValue += m_ListItem.get(row).smartValue;
                totalMpoValue += m_ListItem.get(row).mpoValue;
            }
            totalSmartPercent = totalSmartValue / totalPagu * 100;
            totalMpoPercent = totalMpoValue / totalPagu * 100;
        }
    }
}
