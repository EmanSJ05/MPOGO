package com.emansj.mpogo.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.emansj.mpogo.R;
import com.emansj.mpogo.adapter.AdapterReke;
import com.emansj.mpogo.adapter.AdapterUserRating;
import com.emansj.mpogo.fragment.DialogRekeDetail;
import com.emansj.mpogo.helper.AppUtils;
import com.emansj.mpogo.helper.ExceptionHandler;
import com.emansj.mpogo.model.RealisasiKeuangan;
import com.emansj.mpogo.model.UserRating;

import java.util.List;

public class RatingActivity extends AppCompatActivity {

    //Standard vars
    private static final String TAG = RatingActivity.class.getSimpleName();
    private Context m_Ctx;
    private RequestQueue m_Queue;

    //Define views
    private View parent_view;
    private Toolbar toolbar;
    private RecyclerView rvList;
    private View lyTotal;

    //Custom vars
    private List<UserRating> m_ListItem;
    private AdapterUserRating m_Adapter;
    private String m_Title;
    private String m_ReportName;
    private boolean m_IsBottomHide = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        setContentView(R.layout.activity_user_rating);

        //Get extras
        Bundle extras = getIntent().getExtras();
        if(extras != null)
        {
            String reportName = (String) extras.get("report_name");
            m_ReportName = reportName;

            String title = (String) extras.get("title");
            m_Title = title;
        }

        //Activity settings
        parent_view = findViewById(android.R.id.content);
        m_Ctx = RatingActivity.this;

        initToolbar();
        //initComponent();
        showList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_report, menu);
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

    private void initToolbar(){
        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(m_Title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

//    private void initComponent(){
//        NestedScrollView nested_content = (NestedScrollView) findViewById(R.id.nsvScroll);
//        nested_content.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
//            @Override
//            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//                if (scrollY < oldScrollY) { // up
//                    animateBottom(false);
//                }
//                if (scrollY > oldScrollY) { // down
//                    animateBottom(true);
//                }
//            }
//        });
//    }
//
//    private void animateBottom(final boolean hide) {
//        FloatingActionButton fabSort = findViewById(R.id.fabSort);
//
//        if (m_IsBottomHide && hide || !m_IsBottomHide && !hide) return;
//        m_IsBottomHide = hide;
//        int moveY = hide ? (2 * fabSort.getHeight()) : 0;
//        fabSort.animate().translationY(moveY).setStartDelay(100).setDuration(300).start();
//    }

    private void showList() {
        rvList = findViewById(R.id.rvData);
        rvList.setLayoutManager(new LinearLayoutManager(m_Ctx));
        rvList.setHasFixedSize(true);
        rvList.setNestedScrollingEnabled(false);

        switch(m_ReportName) {
            case "Provinsi":
                m_ListItem = UserRating.getRatingProvinsi(m_Ctx);
                break;
            case "Kabupaten":
                m_ListItem = UserRating.getRatingKabupaten(m_Ctx);
                break;
            case "User":
                m_ListItem = UserRating.getRatingAllUser(m_Ctx);
                break;
        }

        m_Adapter = new AdapterUserRating(m_Ctx, m_ListItem);
//        m_Adapter.setOnItemClickListener(new AdapterUserRating.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, UserRating obj, int position) {
//                final UserRating item = m_ListItem.get(position);
//                showDialogDetail(item);
//            }
//        });
        rvList.setAdapter(m_Adapter);

    }

}
