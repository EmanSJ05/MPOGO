package com.emansj.mpogo.activity;

import android.support.annotation.NonNull;
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

import com.emansj.mpogo.R;
import com.emansj.mpogo.helper.AppGlobal;
import com.emansj.mpogo.helper.ExceptionHandler;
import com.emansj.mpogo.helper.Tools;
import com.mikhaellopez.circularimageview.CircularImageView;
import java.util.ArrayList;
import java.util.List;

public class DrawerActivity extends AppCompatActivity {

    //Standard vars
    private static final String TAG = DrawerActivity.class.getSimpleName();
    private Context m_Ctx;
    private AppGlobal m_Global;
    private AppGlobal.Data m_GlobalData;

    //Define views
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

    private void initToolbar(){
        toolbar = findViewById(R.id.toolbar);
        //toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Home");
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
                showDialogTahunRKA();
            }
        });
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

    private void initData(){
        tvTahunRKA.setText(String.valueOf(m_Global.getTahunRKA()));
        tvFullName.setText(m_Global.getUserFullName());
        tvEmail.setText(m_Global.getUserEmail());
        //civAvatar.setImageDrawable(m_Global.getUserImageDrawable());
        Tools.displayImage(m_Ctx, civAvatar, m_Global.getUserImageUrl());
    }

    private void loadNotifications() {
        Menu menu = navView.getMenu();
        ((TextView) menu.findItem(R.id.nav_notifications).getActionView().findViewById(R.id.text)).setText("75");

        TextView badgePrioNotif = menu.findItem(R.id.nav_notifications).getActionView().findViewById(R.id.text);
        badgePrioNotif.setText("3");
        badgePrioNotif.setTextColor(getResources().getColor(R.color.red_A700));
        //badgePrioNotif.setBackgroundColor(getResources().getColor(R.color.colorPrimaryLight));
    }

    private void showNavigation(@NonNull MenuItem item, int itemId) {

        if (itemId == R.id.nav_notifications){
            Intent i = new Intent(m_Ctx, NotificationActivity.class);
            startActivity(i);

        } else if (itemId == R.id.nav_home){
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frameLayout, new HomeFragment());
            ft.commit();

        } else if (itemId == R.id.nav_dashboard){
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frameLayout, new DashboardFragment());
            ft.commit();

        } else if (itemId == R.id.nav_rk){
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frameLayout, new RekeFragment());
            ft.commit();

        } else if (itemId == R.id.nav_rkf){
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frameLayout, new RekesikFragment());
            ft.commit();

        } else if (itemId == R.id.nav_fisik){
            Intent i = new Intent(m_Ctx, FisikActivity.class);
            startActivity(i);

        } else if (itemId == R.id.nav_user_rating){
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frameLayout, new RatingFragment());
            ft.commit();

        } else if (itemId == R.id.nav_user_profile){
            Intent i = new Intent(m_Ctx, ProfileActivity.class);
            startActivity(i);

        } else if (itemId == R.id.nav_settings){
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frameLayout, new HomeFragment());
            ft.commit();

        }
        drwLayout.closeDrawer(GravityCompat.START);
    }

    private void showDialogTahunRKA()
    {
        if (m_Global.getTahunRKA_List().isEmpty() == false)
        {
            String[] tahun = new String[m_ListTahunRKA.size()];
            tahun = m_ListTahunRKA.toArray(tahun);

            //init alert dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(m_Ctx);
            builder.setTitle("Pilih Tahun RKA");
            builder.setSingleChoiceItems(tahun, 0, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    m_SelectedTahunRKA = m_ListTahunRKA.get(i).toString();
                }
            });

            //init click OK
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialogInterface, int i)
                {
                    Snackbar.make(parent_view, "selected : " + m_SelectedTahunRKA, Snackbar.LENGTH_SHORT).show();
                }
            });

            //init click Batal
            builder.setNegativeButton("Batal", null);
            builder.show();
        }
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
}

