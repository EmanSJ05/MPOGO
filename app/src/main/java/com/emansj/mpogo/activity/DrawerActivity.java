package com.emansj.mpogo.activity;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.emansj.mpogo.R;
import com.emansj.mpogo.helper.AppGlobal;
import com.emansj.mpogo.helper.ExceptionHandler;
import com.emansj.mpogo.helper.Tools;
import com.emansj.mpogo.helper.VolleyErrorHelper;
import com.emansj.mpogo.helper.VolleySingleton;
import com.emansj.mpogo.model.Notif;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DrawerActivity extends AppCompatActivity {

    //Standard vars
    private static final String TAG = DrawerActivity.class.getSimpleName();
    private Context m_Ctx;
    private AppGlobal m_Global;
    private AppGlobal.Data m_GlobalData;

    //View vars
    private View parent_view;
    private Toolbar toolbar;
    private DrawerLayout drwLayout;
    private NavigationView navView;
    private TextView tvTahunRKA;
    private TextView tvFullName;
    private TextView tvEmail;
    private CircularImageView civAvatar;

    //Custom vars
    private List<String> m_ListTahunRKA = new ArrayList<>();
    private String m_SelectedTahunRKA;
    private final static int REQUEST_CODE = 1;


    //---------------------------------------OVERRIDE
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        setContentView(R.layout.activity_drawer);

        //Activity settings
        parent_view = findViewById(android.R.id.content);
        m_Ctx = DrawerActivity.this;
        m_Global = AppGlobal.getInstance(m_Ctx);
        m_GlobalData = m_Global.new Data();

        initToolbar();
        initComponent();
        initNavigationMenu();
        initData();
    }

    @Override
    protected void onStop () {
        super.onStop();
        VolleySingleton.getInstance(m_Ctx).cancelPendingRequests(TAG);
        m_GlobalData.cancelAllRequest();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        VolleySingleton.getInstance(m_Ctx).cancelPendingRequests(TAG);
        m_GlobalData.cancelAllRequest();
    }

    private long exitTime = 0;
    @Override
    public void onBackPressed() {
        if (drwLayout.isDrawerOpen(GravityCompat.START)) {
            drwLayout.closeDrawer(GravityCompat.START);
        } else {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(this, "Tekan lagi untuk keluar aplikasi", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent dataIntent) {
        super.onActivityResult(requestCode, resultCode, dataIntent);

        switch (requestCode)
        {
            case REQUEST_CODE:
                if(resultCode == RESULT_OK)
                {
                    if (dataIntent.getBooleanExtra("loadNotifications", false)){
                        loadNotifications();
                    }
                }
        }
    }

    //---------------------------------------INIT COMPONENTS & DATA
    private void initToolbar(){
        toolbar = findViewById(R.id.toolbar);
        //toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        //Toolbar Title & SubTitle
        ((TextView) parent_view.findViewById(R.id.tvToolbarTitle)).setText("Home");
    }

    private void initComponent(){
        drwLayout = findViewById(R.id.drwLayout);
        navView = findViewById(R.id.navView);
        tvTahunRKA = findViewById(R.id.tvTahunRKA);

        View headerLayout = navView.getHeaderView(0);
        tvFullName = headerLayout.findViewById(R.id.tvFullName);
        tvEmail = headerLayout.findViewById(R.id.tvEmail);
        civAvatar = headerLayout.findViewById(R.id.civAvatar);

        //TAHUN RKA
        tvTahunRKA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //set showDialogTahunRKA
                final Runnable runShowTahunRKA = new Runnable() {
                    @Override
                    public void run() {
                        showDialogTahunRKA();
                    }
                };

                if (m_Global.getTahunRKAList() == null){
                    m_Global.loadTahunRKAList(runShowTahunRKA, null);
                }else {
                    showDialogTahunRKA();
                }
            }
        });
    }

    private void initData(){
        tvTahunRKA.setText(String.valueOf(m_Global.getTahunRKA()));
        tvFullName.setText(m_Global.UserProfile.FullName);
        tvEmail.setText(m_Global.UserProfile.Email);
        Tools.displayImage(m_Ctx, civAvatar, m_Global.UserProfile.ImageUrl, R.drawable.img_no_available_user_photo);
    }

    private void initNavigationMenu() {
        //drawer & update counter
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drwLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        {
            public void onDrawerOpened(View drawerView) {
                loadNotifications();
                super.onDrawerOpened(drawerView);
            }
        };
        drwLayout.addDrawerListener(toggle);
        toggle.syncState();

        //MENU ITEM
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(final MenuItem item) {
                showNavigation(item, item.getItemId());
                return true;
            }
        });

        showNavigation(navView.getMenu().getItem(1), R.id.nav_home);
    }

    public void loadNotifications() {
        Notif notif = new Notif();
        int totalUnread = notif.totalUnread(m_Ctx);

        if (totalUnread > 0) {
            Menu menu = navView.getMenu();
            TextView badgePrioNotif = menu.findItem(R.id.nav_notifications).getActionView().findViewById(R.id.text);
            badgePrioNotif.setText(String.valueOf(totalUnread));
            badgePrioNotif.setTextColor(getResources().getColor(R.color.red_A700));
            badgePrioNotif.setBackgroundColor(getResources().getColor(R.color.grey_10));
        }else {
            Menu menu = navView.getMenu();
            TextView badgePrioNotif = menu.findItem(R.id.nav_notifications).getActionView().findViewById(R.id.text);
            badgePrioNotif.setText("");
            badgePrioNotif.setBackgroundColor(getResources().getColor(R.color.white));
        }
    }


    //---------------------------------------ACTIONS
    private void showNavigation(@NonNull MenuItem item, int itemId) {

        if (itemId == R.id.nav_notifications){
            Intent i = new Intent(m_Ctx, NotificationActivity.class);
            startActivityForResult(i, REQUEST_CODE);
            drwLayout.closeDrawer(GravityCompat.START);

        } else if (itemId == R.id.nav_home){
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frameLayout, new HomeFragment());
            ft.commit();

            ((TextView) parent_view.findViewById(R.id.tvToolbarTitle)).setText("Home");

            drwLayout.closeDrawer(GravityCompat.START);

//        } else if (itemId == R.id.nav_dashboard){
//            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//            ft.replace(R.id.frameLayout, new DashboardFragment());
//            ft.commit();
//            drwLayout.closeDrawer(GravityCompat.START);

        } else if (itemId == R.id.nav_rk){
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frameLayout, new RekeFragment());
            ft.commit();

            ((TextView) parent_view.findViewById(R.id.tvToolbarTitle)).setText("Realisasi Keuangan");

            drwLayout.closeDrawer(GravityCompat.START);

        } else if (itemId == R.id.nav_rkf){
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frameLayout, new RekesikFragment());
            ft.commit();

            ((TextView) parent_view.findViewById(R.id.tvToolbarTitle)).setText("Realisasi Keuangan & Fisik");

            drwLayout.closeDrawer(GravityCompat.START);

        } else if (itemId == R.id.nav_user_rating){
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frameLayout, new UserRatingFragment());
            ft.commit();

            ((TextView) parent_view.findViewById(R.id.tvToolbarTitle)).setText("User Rating");

            drwLayout.closeDrawer(GravityCompat.START);

        } else if (itemId == R.id.nav_fisik){
            Intent i = new Intent(m_Ctx, FisikActivity.class);
            startActivity(i);

        } else if (itemId == R.id.nav_user_profile){
            Intent i = new Intent(m_Ctx, ProfileActivity.class);
            startActivity(i);

        } else if (itemId == R.id.nav_user_logout){
            logOut();

        } else if (itemId == R.id.nav_app_about){
            Intent i = new Intent(m_Ctx, AboutActivity.class);
            startActivity(i);

        } else if (itemId == R.id.nav_app_out){
            finish();

        }
    }

    private String selectedTahun;
    private void showDialogTahunRKA() {
        if (m_Global.getTahunRKAList() != null)
        {
            //get choice list
            m_ListTahunRKA = m_Global.getTahunRKAList();
            String[] tahun = new String[m_ListTahunRKA.size()];
            tahun = m_ListTahunRKA.toArray(tahun);

            //get default choice
            int defaultChoice = m_ListTahunRKA.indexOf(tvTahunRKA.getText());

            //init alert dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(m_Ctx);
            builder.setTitle("Pilih Tahun");
            builder.setSingleChoiceItems(tahun, defaultChoice, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    selectedTahun = m_ListTahunRKA.get(i);
                }
            });

            //init click OK
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialogInterface, int i)
                {
                    m_SelectedTahunRKA = selectedTahun;
                    tvTahunRKA.setText(selectedTahun);
                    m_Global.setTahunRKA(Tools.parseInt(selectedTahun));
                    refreshFragment();
                }
            });

            //init click Batal
            builder.setNegativeButton("Batal", null);
            builder.show();
        }
    }

    private void refreshFragment(){
        FragmentManager fm = getSupportFragmentManager();

        //if you added fragment via layout xml
        Fragment fragment = fm.findFragmentById(R.id.frameLayout);
        if (fragment instanceof HomeFragment){
            HomeFragment homeFragment = (HomeFragment) fm.findFragmentById(R.id.frameLayout);
            homeFragment.refreshData();
        }
    }

    private void logOut(){
        m_Global.setIsRememberMe(false);
        gotoActivityLogin();
    }

    private void gotoActivityLogin() {
        Intent intent = new Intent(m_Ctx, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void gotoOut() {
        finish();
    }

}

