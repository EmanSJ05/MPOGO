package com.emansj.mpogo.activity;

import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.emansj.mpogo.R;
import com.emansj.mpogo.helper.Tools;

public class ErrorRecoveryActivity extends AppCompatActivity {

    //Standard vars
    private static final String TAG = ErrorRecoveryActivity.class.getSimpleName();
    private Context m_Ctx;

    //View vars
    private TextView tvError;
    private ImageButton imbCopyToClipboard, imbClose;

    //Custom vars
    private String m_Error;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error);

        m_Ctx = ErrorRecoveryActivity.this;
        initToolbar();
        initComponent();

        //Get extras
        Bundle extras = getIntent().getExtras();
        if(extras != null)
        {
            String error = (String) extras.get("error");
            m_Error = error;
        }
        TextView tvError = findViewById(R.id.tvError);
        tvError.setText(m_Error);
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_close);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.grey_60), PorterDuff.Mode.SRC_ATOP);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Kesalahan terjadi");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Tools.setSystemBarColor(this, R.color.colorPrimary);
        Tools.setSystemBarLight(this);
    }

    private void initComponent() {
        tvError = findViewById(R.id.tvError);
        imbCopyToClipboard = findViewById(R.id.imbCopyToClipboard);
        imbClose = findViewById(R.id.imbClose);

        imbCopyToClipboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Tools.copyToClipboard(m_Ctx, tvError.getText().toString());
            }
        });

        imbClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
