package com.emansj.mpogo.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.emansj.mpogo.R;
import com.emansj.mpogo.adapter.AdapterNotification;
import com.emansj.mpogo.helper.AppGlobal;
import com.emansj.mpogo.helper.AppSharedPref;
import com.emansj.mpogo.helper.ExceptionHandler;
import com.emansj.mpogo.helper.Tools;
import com.emansj.mpogo.helper.VolleySingleton;
import com.emansj.mpogo.model.Notif;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NotificationActivity extends AppCompatActivity {

    //Standard vars
    private static final String TAG = NotificationActivity.class.getSimpleName();
    private Context m_Ctx;
    private SharedPreferences m_SharedPref;
    private AppGlobal m_Global;
    private AppGlobal.Data m_GlobalData;

    //View vars
    private View parent_view;
    private Toolbar toolbar;
    private RecyclerView rvList;
    private SwipeRefreshLayout refreshLayout;

    //Custom vars
    private List<Notif> m_ListItem;
    private AdapterNotification m_Adapter;
    private final static int REQUEST_CODE = 1;


    //---------------------------------------OVERRIDE
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        setContentView(R.layout.activity_notification);

        //Activity settings
        parent_view = findViewById(android.R.id.content);
        m_Ctx = NotificationActivity.this;
        m_Global = AppGlobal.getInstance(m_Ctx);
        m_SharedPref = m_Ctx.getSharedPreferences(AppGlobal.PREFS_NAME, MODE_PRIVATE);

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
        Intent i = new Intent(m_Ctx, DrawerActivity.class);
        i.putExtra("loadNotifications", true);
        setResult(RESULT_OK, i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_delete_all, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else if(item.getItemId() == R.id.action_delete_all) {
            deleteAll();
        }
        else {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    // This method is invoked when target activity return result data back.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent dataIntent) {
        super.onActivityResult(requestCode, resultCode, dataIntent);

        switch (requestCode)
        {
            case REQUEST_CODE:
                if(resultCode == RESULT_OK)
                {
                    if (dataIntent.getBooleanExtra("item_deleted", false)){
                        initData();
                    }
                }
        }
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

    private void initComponent(){
        refreshLayout = findViewById(R.id.swipeRefresh);

        //listener
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
                refreshLayout.setRefreshing(false);
            }
        });

        //LIST
        rvList = findViewById(R.id.rvData);
        rvList.setLayoutManager(new LinearLayoutManager(m_Ctx));
        rvList.setHasFixedSize(true);
        rvList.setNestedScrollingEnabled(false);
    }

    private void initData() {
        m_ListItem = new ArrayList<>();
        getDataNotification();

        if (m_ListItem != null) {
            //LIST - ADAPTER - LISTENER
            m_Adapter = new AdapterNotification(m_Ctx, m_ListItem);
            m_Adapter.setOnItemClickListener(new AdapterNotification.OnItemClickListener() {
                @Override
                public void onItemClick(View view, Notif obj, int position) {
                    final Notif item = m_ListItem.get(position);
                    showNotificationItem(item.NotificationId);
                }
            });
            rvList.setAdapter(m_Adapter);
        } else {
            Toast.makeText(m_Ctx, "Tidak ada data notifikasi", Toast.LENGTH_LONG).show();
        }
    }


    //---------------------------------------ACTIONS
    private void showNotificationItem(int notifId) {
        Intent i = new Intent(m_Ctx, NotificationItemActivity.class);
        i.putExtra("notificationid", notifId);
        startActivityForResult(i, REQUEST_CODE);
    }

    private void deleteAll() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Hapus semua notifikasi ?");
        builder.setMessage("Anda yakin ingin menghapus semua notifikasi ?");
        builder.setPositiveButton("HAPUS SEMUA", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //hapus semua notifikasi (simpan kosongan)
                Notif notif = new Notif();
                notif.removeNotifs(m_Ctx);
                initData();
            }
        });
        builder.setNegativeButton("BATAL", null);
        builder.show();
    }


    //---------------------------------------GET DATA
    private void getDataNotification() {
        try {
            Notif notif = new Notif();
            m_ListItem = new ArrayList<>();
            m_ListItem = notif.getNotifs(m_Ctx);

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
