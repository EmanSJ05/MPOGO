package com.emansj.mpogo.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.emansj.mpogo.R;
import com.emansj.mpogo.helper.ExceptionHandler;
import com.emansj.mpogo.helper.Tools;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import java.net.URLEncoder;

public class AboutActivity extends AppCompatActivity {

    //Standard vars
    private static final String TAG = AboutActivity.class.getSimpleName();
    private Context m_Ctx;

    //View vars
    private View parent_view;
    private Toolbar toolbar;
//    private Button btnContactUs;
    private FloatingActionButton btnContactUs;
    private TextView tvAppVersion;

    //Custom vars
    private GoogleMap mMap;
    private SupportMapFragment mapFragment;


    //---------------------------------------OVERRIDE
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        Tools.setSystemBarTransparent(this);
        //transparent statusbar
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        setContentView(R.layout.activity_about);

        //Activity settings
        parent_view = findViewById(android.R.id.content);
        m_Ctx = AboutActivity.this;

        initToolbar();
        initComponent();
        initMapFragment();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_about, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.home) {
            finish();
        }else if(item.getItemId() == R.id.action_contactus) {
            sendMessageToWhatsAppContact("+6281514065653");
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
        ((TextView) parent_view.findViewById(R.id.tvToolbarTitle)).setText("Tentang Kami");
    }

    private void initComponent(){
        tvAppVersion = findViewById(R.id.tvAppVersion);
        btnContactUs = findViewById(R.id.btnContactUs);

        //APP. INFO
        try {
            PackageInfo pinfo = m_Ctx.getPackageManager().getPackageInfo(getPackageName(), 0);
            int versionNumber = pinfo.versionCode;
            String versionName = pinfo.versionName;
            tvAppVersion.setText("Versi " + versionName);
        } catch (Exception e){
            e.printStackTrace();
        }

        //CONTACT US
        btnContactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessageToWhatsAppContact("+6281514065653");
            }
        });
    }

    private void initMapFragment() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = Tools.configActivityMaps(googleMap);
                MarkerOptions markerOptions = new MarkerOptions().position(new LatLng(-6.296475, 106.822980));
                mMap.addMarker(markerOptions);
                mMap.moveCamera(zoomingLocation());
                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        try {
                            mMap.animateCamera(zoomingLocation());
                        } catch (Exception e) {
                        }
                        return true;
                    }
                });
            }
        });
    }


    //---------------------------------------ACTIONS
    private CameraUpdate zoomingLocation() {
        return CameraUpdateFactory.newLatLngZoom(new LatLng(-6.296475, 106.822980), 13);
    }

    private void sendMessageToWhatsAppContact(String number) {
        PackageManager packageManager = m_Ctx.getPackageManager();
        Intent i = new Intent(Intent.ACTION_VIEW);
        try {
            String s = "Haloo pengembang MPO GO ";
            String url = "https://api.whatsapp.com/send?phone=" + number + "&text=" + URLEncoder.encode(s, "UTF-8");
            i.setPackage("com.whatsapp");
            i.setData(Uri.parse(url));
            if (i.resolveActivity(packageManager) != null) {
                m_Ctx.startActivity(i);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(m_Ctx, "Smartphone anda tidak didukung dengan aplikasi WhatsApp.",Toast.LENGTH_SHORT).show();
        }
    }

}
