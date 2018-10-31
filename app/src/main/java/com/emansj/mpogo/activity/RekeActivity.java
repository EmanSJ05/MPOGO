package com.emansj.mpogo.activity;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.emansj.mpogo.R;
import com.emansj.mpogo.adapter.AdapterReke;
import com.emansj.mpogo.fragment.DialogRekeDetail;
import com.emansj.mpogo.fragment.DialogReportFilter;
import com.emansj.mpogo.helper.AppUtils;
import com.emansj.mpogo.helper.ExceptionHandler;
import com.emansj.mpogo.model.RealisasiKeuangan;

import java.util.List;

public class RekeActivity extends AppCompatActivity {

    //Standard vars
    private static final String TAG = RekeActivity.class.getSimpleName();
    private Context m_Ctx;
    private RequestQueue m_Queue;

    //Define views
    private View parent_view;
    private Toolbar toolbar;
    private RecyclerView rvList;
    private View lyBottom, lyBottomTotal;
    private TextView    tvTotalPagu, tvTotalPaguLong,
                        tvTotalSmartValue, tvTotalSmartPercent, tvTotalSmartLong,
                        tvTotalMpoValue, tvTotalMpoPercent, tvTotalMpoLong;
    private LinearLayout lyProgress;
    private ProgressBar pbProgress;

    //Custom vars
    private List<RealisasiKeuangan> m_ListItem;
    private AdapterReke m_Adapter;
    private String m_Title;
    private String m_ReportName;
    private int m_FilterTahunRKA = 0;
    private List<Integer> m_FilterListIdSatker;
    private boolean m_IsBottomHide = false;
    private  myTask m_Task = new myTask();
    public static final int DIALOG_QUEST_CODE = 300;
    private FragmentManager m_fragmentManager;
    private DialogReportFilter m_dialogReportFilter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        setContentView(R.layout.activity_realisasi_keuangan);

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
        m_Ctx = RekeActivity.this;

        initToolbar();
        initComponent();
    }

    //Override
    @Override
    protected void onStart() {
        super.onStart();
        m_Task = new myTask();
        m_Task.execute();
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (m_Task != null && m_Task.getStatus() != myTask.Status.FINISHED) {
            m_Task.cancel(true);
            m_Task = null;
        }
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (m_Task != null && m_Task.getStatus() != myTask.Status.FINISHED) {
            m_Task.cancel(true);
            m_Task = null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_report, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.home) {
            finish();
        }
        else if(item.getItemId() == R.id.action_search) {
            showFilter();
        }
        else if(item.getItemId() == R.id.action_filter) {
            showFilter();
        }
        else {
            finish();
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

    private void initComponent(){
        rvList = findViewById(R.id.rvData);
        lyBottom = findViewById(R.id.lyBottom);
        lyBottomTotal = findViewById(R.id.lyBottomTotal);
        lyProgress = findViewById(R.id.lyProgress);
        pbProgress = findViewById(R.id.pbProgress);

        //listener
        NestedScrollView nested_content = findViewById(R.id.nsvScroll);
        nested_content.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY < oldScrollY) { // up
                    animateBottom(false);
                }
                if (scrollY > oldScrollY) { // down
                    animateBottom(true);
                }
            }
        });
    }

    private void animateBottom(final boolean hide) {
        if (m_IsBottomHide && hide || !m_IsBottomHide && !hide) return;
        m_IsBottomHide = hide;
        int moveY = hide ? (lyBottomTotal.getHeight()) : 0;
        lyBottom.animate().translationY(moveY).setStartDelay(100).setDuration(300).start();
    }

    private void showList() {
        rvList.setLayoutManager(new LinearLayoutManager(m_Ctx));
        rvList.setHasFixedSize(true);
        rvList.setNestedScrollingEnabled(false);

        switch(m_ReportName) {
            case "Kewenangan":
                m_ListItem = RealisasiKeuangan.getKewenangan(m_Ctx);
                break;
            case "Kegiatan":
                m_ListItem = RealisasiKeuangan.getKegiatan(m_Ctx);
                break;
            case "KegiatanOutput":
                m_ListItem = RealisasiKeuangan.getKegiatanOutput(m_Ctx);
                break;
            case "KegiatanNProvinsi":
                m_ListItem = RealisasiKeuangan.getKegiatanOutput(m_Ctx);
                break;
            case "Satker":
                m_ListItem = RealisasiKeuangan.getSatker(m_Ctx);
                break;
            case "Output":
                m_ListItem = RealisasiKeuangan.getOutput(m_Ctx);
                break;
        }

        m_Adapter = new AdapterReke(m_Ctx, m_ListItem);
        m_Adapter.setOnItemClickListener(new AdapterReke.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RealisasiKeuangan obj, int position) {
                final RealisasiKeuangan rk = m_ListItem.get(position);
                showDialogDetail(rk);
            }
        });
        rvList.setAdapter(m_Adapter);

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

            //init
            tvTotalPagu = parent_view.findViewById(R.id.tvTotalPagu);
            tvTotalPaguLong = parent_view.findViewById(R.id.tvTotalPaguLong);
            tvTotalSmartValue = parent_view.findViewById(R.id.tvTotalSmartValue);
            tvTotalSmartPercent = parent_view.findViewById(R.id.tvTotalSmartPercent);
            tvTotalSmartLong = parent_view.findViewById(R.id.tvTotalSmartLong);
            tvTotalMpoValue = parent_view.findViewById(R.id.tvTotalMpoValue);
            tvTotalMpoPercent = parent_view.findViewById(R.id.tvTotalMpoPercent);
            tvTotalMpoLong = parent_view.findViewById(R.id.tvTotalMpoLong);

            //set
            tvTotalPagu.setText(AppUtils.shortCurrency(m_Ctx, String.format("%.0f", totalPagu)));
            tvTotalPaguLong.setText(String.format("%,.0f", totalPagu));
            tvTotalSmartValue.setText(AppUtils.shortCurrency(m_Ctx, String.format("%.0f", totalSmartValue)));
            tvTotalSmartPercent.setText(String.format("%,.2f", totalSmartPercent) + "%");
            tvTotalSmartLong.setText(String.format("%,.0f", totalSmartValue));
            tvTotalMpoValue.setText(AppUtils.shortCurrency(m_Ctx, String.format("%.0f", totalMpoValue)));
            tvTotalMpoPercent.setText(String.format("%,.2f", totalMpoPercent) + "%");
            tvTotalMpoLong.setText(String.format("%,.0f", totalMpoValue));
        }
    }

    private void showDialogDetail(RealisasiKeuangan obj) {
        FragmentManager fm = getSupportFragmentManager();
        DialogRekeDetail newFragment = new DialogRekeDetail();
        newFragment.setData(obj);

        FragmentTransaction ft = fm.beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.add(android.R.id.content, newFragment).addToBackStack(null).commit();
    }

    private class myTask extends AsyncTask<Void, Void, Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            lyProgress.setVisibility(View.VISIBLE);
            rvList.setVisibility(View.GONE);
            lyBottom.setVisibility(View.GONE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            showList();
            showTotal();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            lyProgress.setVisibility(View.GONE);
            rvList.setVisibility(View.VISIBLE);
            lyBottom.setVisibility(View.VISIBLE);
        }
    }

    private void showFilter() {
        FragmentManager fm = getSupportFragmentManager();
        DialogReportFilter newFragment = new DialogReportFilter();
        newFragment.setRequestCode(DIALOG_QUEST_CODE);

        FragmentTransaction ft = fm.beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.add(android.R.id.content, newFragment, "DialogReportFilter").addToBackStack(null).commit();

        newFragment.setOnCallbackResult(new DialogReportFilter.CallbackResult() {
            @Override
            public void sendResult(int requestCode, int tahunRKA, boolean semuaSatker, List<Integer> idSatkers) {
                if (requestCode == DIALOG_QUEST_CODE) {
                    displayDataResult((int) tahunRKA, (boolean) semuaSatker, (List<Integer>) idSatkers);
                }
            }
        });
    }

    private void displayDataResult(int tahunRKA, boolean semuaSatker, List<Integer> idSatkers) {
        Toast.makeText(m_Ctx,   "tahun rka : " + tahunRKA + "\n semua satker : " + semuaSatker + "\n idsatkers : " + idSatkers.toArray().toString(), Toast.LENGTH_SHORT).show();

        m_Adapter.notifyDataSetChanged();
        showList();
        showTotal();
    }

    public void hideKeyboard(View view) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);

        if (view != null) {
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag("DialogReportFilter");
        if (fragment != null) {
            if (fragment.isVisible()) {
                if (fragment instanceof DialogReportFilter) {
                    if (fragment.getView().findViewById(R.id.lyTahunRKA).getVisibility() == View.GONE) {
                        fragment.getView().findViewById(R.id.lyTahunRKA).setVisibility(View.VISIBLE);
                        fragment.getView().findViewById(R.id.lySatkerTop).setVisibility(View.VISIBLE);
                    } else {
                        super.onBackPressed();
                    }
                }
            }
        }else{
            super.onBackPressed();
        }
    }
}