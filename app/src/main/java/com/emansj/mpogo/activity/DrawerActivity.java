package com.emansj.mpogo.activity;

import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.emansj.mpogo.R;

import java.util.ArrayList;
import java.util.List;

public class DrawerActivity extends AppCompatActivity {

    //Standard vars
    private static final String TAG = DrawerActivity.class.getSimpleName();
    private Context m_Context;
    private SharedPreferences m_SharedPref;
    private RequestQueue m_queue;

    //Custom vars
    List<String> m_ListTahunRKA = new ArrayList<>();

    //Define views
    private ActionBar m_ActionBar;
    private Toolbar m_Toolbar;
    private Menu m_MenuNav;
    private DrawerLayout m_Drawer;
    private View m_View;
    private Button btnTahunRKA;
    private TextView txtTahunRKA;
    private boolean m_is_account_mode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);

        //Activity settings
        m_View = findViewById(android.R.id.content);
        m_Context = DrawerActivity.this;
        m_SharedPref = getSharedPreferences("drawer_settings", MODE_PRIVATE);

        //Load data
        loadTahunRka();

        //Init view
        initToolbar();
        initNavigationMenu();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            gotoLogin();
        }
    }

    //region INIT VIEW
    private void initToolbar() {
        m_Toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(m_Toolbar);

        m_ActionBar = getSupportActionBar();
        m_ActionBar.setDisplayHomeAsUpEnabled(true);
        m_ActionBar.setHomeButtonEnabled(true);
        m_ActionBar.setTitle("Home");

        txtTahunRKA = findViewById(R.id.txtTahunRKA);
        txtTahunRKA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClick_btnTahunRKA();
            }
        });
    }

    private void initNavigationMenu() {
        final NavigationView nav_view = findViewById(R.id.nav_view);

        //drawer & update counter
        m_Drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, m_Drawer, m_Toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        {
            public void onDrawerOpened(View drawerView) {
                loadNotifications(nav_view);
                super.onDrawerOpened(drawerView);
            }
        };
        m_Drawer.addDrawerListener(toggle);
        toggle.syncState();

        //menu navigation clicked listener
        nav_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(final MenuItem item) {
                //resetAllMenuItemsTextColor(nav_view);
                //setTextColorForMenuItem(item, R.color.colorPrimaryDark);
                showNavigation(item, item.getItemId());
                return true;
            }
        });

        showNavigation(nav_view.getMenu().getItem(1), R.id.nav_home);
    }
    //endregion

    //region ACTION [click, swipe]
    private String single_choice_selected;
    private void onClick_btnTahunRKA(){
        if (m_ListTahunRKA.isEmpty() == false)
        {
            String[] tahun = new String[m_ListTahunRKA.size()];
            tahun = m_ListTahunRKA.toArray(tahun);

            //init alert dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(m_Context);
            builder.setTitle("Pilih Tahun RKA");
            builder.setSingleChoiceItems(tahun, 0, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    single_choice_selected = m_ListTahunRKA.get(i).toString();
                }
            });

            //init click OK
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialogInterface, int i)
                {
                    Snackbar.make(m_View, "selected : " + single_choice_selected, Snackbar.LENGTH_SHORT).show();
                }
            });

            //init click Batal
            builder.setNegativeButton("Batal", null);
            builder.show();
        }
    }
    //endregion

    //region LOAD DATA
    private void loadNotifications(NavigationView nav) {
        if (m_is_account_mode) return;
        Menu m = nav.getMenu();
        ((TextView) m.findItem(R.id.nav_notifications).getActionView().findViewById(R.id.text)).setText("75");

        TextView badgePrioNotif = m.findItem(R.id.nav_notifications).getActionView().findViewById(R.id.text);
        badgePrioNotif.setText("3");
        badgePrioNotif.setTextColor(getResources().getColor(R.color.red_A700));
//        badgePrioNotif.setBackgroundColor(getResources().getColor(R.color.colorPrimaryLight));
    }

    private void loadTahunRka()
    {
        m_ListTahunRKA.add("2018");
        m_ListTahunRKA.add("2017");

//        m_queue = Volley.newRequestQueue(this);
//        String url = AppGlobal.url+"/AppGlobal/tahunrka";
//
//        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
//                new Response.Listener<JSONObject>(){
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        try{
//                            JSONArray jsonArray = response.getJSONArray("data");
//
//                            for (int i = 0; i < jsonArray.length(); i++)
//                            {
//                                JSONObject dt = jsonArray.getJSONObject(i);
//                                m_ListTahunRKA.add(dt.getString("tahun"));
//                            }
//                        }catch (JSONException e){
//                            e.printStackTrace();
//                        }
//                    }
//                },
//                new Response.ErrorListener(){
//                    @Override
//                    public void onErrorResponse(VolleyError error){
//                        error.printStackTrace();
//                    }
//                }
//        );
//        m_queue.add(request);
    }
    //endregion

    //region GOTO
    private void gotoLogin()
    {
        Intent intent = new Intent(m_Context, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void gotoHome()
    {
        Intent intent = new Intent(m_Context, DrawerActivity.class);
        startActivity(intent);
        finish();
    }
    //endregion

    private void showNavigation(@NonNull MenuItem item, int itemId) {

        if (itemId == R.id.nav_notifications){
//            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//            ft.replace(R.id.frameLayout, new DashboardFragment());
//            ft.commit();

            Intent i = new Intent(m_Context, RkKewenanganActivity.class);
            startActivity(i);
        } else if (itemId == R.id.nav_home){
            //setTextColorForMenuItem(item, R.color.colorPrimaryDark);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frameLayout, new HomeFragment());
            ft.commit();

        } else if (itemId == R.id.nav_dashboard){
            //setTextColorForMenuItem(item, R.color.colorPrimaryDark);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frameLayout, new DashboardFragment());
            ft.commit();

        } else if (itemId == R.id.nav_rk){
            //setTextColorForMenuItem(item, R.color.colorPrimaryDark);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frameLayout, new RekeFragment());
            ft.commit();

        } else if (itemId == R.id.nav_rkf){
            //setTextColorForMenuItem(item, R.color.colorPrimaryDark);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frameLayout, new RekesikFragment());
            ft.commit();

        } else if (itemId == R.id.nav_fisik){
            //setTextColorForMenuItem(item, R.color.colorPrimaryDark);
            Intent i = new Intent(m_Context, FisikActivity.class);
            startActivity(i);

        } else if (itemId == R.id.nav_user_rating){
            //setTextColorForMenuItem(item, R.color.colorPrimaryDark);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frameLayout, new RatingFragment());
            ft.commit();

        } else if (itemId == R.id.nav_user_profile){
            Intent i = new Intent(m_Context, ProfileActivity.class);
            startActivity(i);

        } else if (itemId == R.id.nav_settings){
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frameLayout, new HomeFragment());
            ft.commit();

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    private void setTextColorForMenuItem(MenuItem menuItem, @ColorRes int color) {
        SpannableString spanString = new SpannableString(menuItem.getTitle().toString());
        //spanString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, color)), 0, spanString.length(), 0);
        spanString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, R.color.grey_900)), 0, spanString.length(), 0);
        menuItem.setTitle(spanString);
    }
    private void resetAllMenuItemsTextColor(NavigationView navigationView) {
        for (int i = 0; i < navigationView.getMenu().size(); i++)
            setTextColorForMenuItem(navigationView.getMenu().getItem(i), R.color.grey_900);
    }
}

