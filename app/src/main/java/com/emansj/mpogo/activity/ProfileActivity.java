package com.emansj.mpogo.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.emansj.mpogo.R;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

public class ProfileActivity extends AppCompatActivity {

    //Standard vars
    private static final String TAG = ProfileActivity.class.getSimpleName();
    private Context m_Ctx;
    private RequestQueue m_Queue;

    //Define views
    private View m_View;
    private ActionBar m_ActionBar;
    private Toolbar m_Toolbar;
    private FloatingActionButton fabChangePhoto;

    //Custom vars
    //write here ......


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //Activity settings
        m_View = findViewById(android.R.id.content);
        m_Ctx = ProfileActivity.this;

        //Init view
        fabChangePhoto = findViewById(R.id.fabChangePhoto);
        initToolbar();

        ImageView imgPhoto = findViewById(R.id.imgPhoto);
        Picasso.with(m_Ctx)
                .load("http://localhost:8080/newmpo/mpo_upload/6f7bd5622148ce0404ff009afba3bd69.jpg")
                .resize(150, 150)
                .into(imgPhoto);
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


}
