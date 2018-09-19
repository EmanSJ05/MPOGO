package com.emansj.mpogo.activity;

import android.content.Context;
import android.os.Bundle;
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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.emansj.mpogo.R;
import com.emansj.mpogo.adapter.AdapterReke;
import com.emansj.mpogo.adapter.AdapterRekesik;
import com.emansj.mpogo.fragment.DialogRekeDetail;
import com.emansj.mpogo.fragment.DialogRekesikDetail;
import com.emansj.mpogo.helper.AppUtils;
import com.emansj.mpogo.helper.ExceptionHandler;
import com.emansj.mpogo.model.RealisasiKeuangan;
import com.emansj.mpogo.model.RealisasiKeuanganFisik;
import com.github.lzyzsd.circleprogress.DonutProgress;

import java.util.List;

public class RekesikActivity extends AppCompatActivity {

    //Standard vars
    private static final String TAG = RekesikActivity.class.getSimpleName();
    private Context m_Ctx;
    private RequestQueue m_Queue;

    //Define views
    private View parent_view;
    private Toolbar toolbar;
    private RecyclerView rvList;
    private View lyTotal;
    private TextView tvTotalPagu;
    private TextView tvTotalPaguLong;
    private TextView tvTotalSmartValue;
    private TextView tvTotalSmartPercent;
    private TextView tvTotalSmartLong;
    private TextView tvTotalMpoValue;
    private TextView tvTotalMpoPercent;
    private TextView tvTotalMpoLong;
    private TextView tvTotalFisikTarget;
    private TextView tvTotalFisikTargetLong;
    private TextView tvTotalFisikValue;
    private TextView tvTotalFisikValueLong;
    private TextView tvTotalFisikPercent;

    //Custom vars
    private List<RealisasiKeuanganFisik> m_ListItem;
    private AdapterRekesik m_Adapter;
    private String m_Title;
    private String m_ReportName;
    private boolean m_IsBottomHide = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        setContentView(R.layout.activity_realisasi_keuangan_fisik);

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
        m_Ctx = RekesikActivity.this;

        initToolbar();
        initComponent();
        showList();
        showTotal();
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

    private void initComponent(){
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
        lyTotal = findViewById(R.id.lyTotal);

        if (m_IsBottomHide && hide || !m_IsBottomHide && !hide) return;
        m_IsBottomHide = hide;
        int moveY = hide ? (2 * lyTotal.getHeight()) : 0;
        lyTotal.animate().translationY(moveY).setStartDelay(100).setDuration(300).start();
    }

    private void showList() {
        rvList = parent_view.findViewById(R.id.rvData);
        rvList.setLayoutManager(new LinearLayoutManager(m_Ctx));
        rvList.setHasFixedSize(true);
        rvList.setNestedScrollingEnabled(false);

        switch(m_ReportName) {
            case "Kegiatan":
                m_ListItem = RealisasiKeuanganFisik.getKegiatan(m_Ctx);
                break;
            case "KegiatanOutput":
                m_ListItem = RealisasiKeuanganFisik.getKegiatanOutput(m_Ctx);
                break;
            case "Output":
                m_ListItem = RealisasiKeuanganFisik.getOutput(m_Ctx);
                break;
            case "OutputNProvinsiSmart":
                m_ListItem = RealisasiKeuanganFisik.getOutputProvinsiSmartMpo(m_Ctx);
                break;
            case "OutputNProvinsiMpo":
                m_ListItem = RealisasiKeuanganFisik.getOutputProvinsiSmartMpo(m_Ctx);
                break;
        }

        m_Adapter = new AdapterRekesik(m_Ctx, m_ListItem);
        m_Adapter.setOnItemClickListener(new AdapterRekesik.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RealisasiKeuanganFisik obj, int position) {
                final RealisasiKeuanganFisik rk = m_ListItem.get(position);
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
        double totalFisikTarget = 0;
        double totalFisikValue = 0;
        double totalFisikPercent = 0;

        if (m_ListItem.isEmpty() == false) {

            for (int row = 0; row < m_ListItem.size(); row++) {
                totalPagu += m_ListItem.get(row).pagu;
                totalSmartValue += m_ListItem.get(row).smartValue;
                totalMpoValue += m_ListItem.get(row).mpoValue;

                totalFisikTarget += m_ListItem.get(row).fisikTarget;
                totalFisikValue += m_ListItem.get(row).fisikValue;
            }

            totalSmartPercent = totalSmartValue / totalPagu * 100;
            totalMpoPercent = totalMpoValue / totalPagu * 100;
            totalFisikPercent = totalFisikValue / totalFisikTarget * 100;

            //init
            tvTotalPagu = parent_view.findViewById(R.id.tvTotalPagu);
            tvTotalSmartValue = parent_view.findViewById(R.id.tvTotalSmartValue);
            tvTotalSmartPercent = parent_view.findViewById(R.id.tvTotalSmartPercent);
            tvTotalMpoValue = parent_view.findViewById(R.id.tvTotalMpoValue);
            tvTotalMpoPercent = parent_view.findViewById(R.id.tvTotalMpoPercent);
            tvTotalFisikTarget = parent_view.findViewById(R.id.tvTotalFisikTarget);
            tvTotalFisikValue = parent_view.findViewById(R.id.tvTotalFisikValue);
            tvTotalFisikPercent = parent_view.findViewById(R.id.tvTotalFisikPercent);

            tvTotalPaguLong = parent_view.findViewById(R.id.tvTotalPaguLong);
            tvTotalSmartLong = parent_view.findViewById(R.id.tvTotalSmartLong);
            tvTotalMpoLong = parent_view.findViewById(R.id.tvTotalMpoLong);
            tvTotalFisikTargetLong = parent_view.findViewById(R.id.tvTotalFisikTargetLong);
            tvTotalFisikValueLong = parent_view.findViewById(R.id.tvTotalFisikValueLong);


            //set
            tvTotalPagu.setText(AppUtils.shortCurrency(m_Ctx, String.format("%.0f", totalPagu)));
            tvTotalSmartValue.setText(AppUtils.shortCurrency(m_Ctx, String.format("%.0f", totalSmartValue)));
            tvTotalSmartPercent.setText(String.format("%,.2f", totalSmartPercent) + "%");
            tvTotalMpoValue.setText(AppUtils.shortCurrency(m_Ctx, String.format("%.0f", totalMpoValue)));
            tvTotalMpoPercent.setText(String.format("%,.2f", totalMpoPercent) + "%");
            tvTotalFisikTarget.setText(AppUtils.shortCurrency(m_Ctx, String.format("%.0f", totalFisikTarget)));
            tvTotalFisikValue.setText(AppUtils.shortCurrency(m_Ctx, String.format("%.0f", totalFisikValue)));
            tvTotalFisikPercent.setText(String.format("%,.2f", totalFisikPercent) + "%");

            tvTotalPaguLong.setText(String.format("%,.2f", totalPagu));
            tvTotalSmartLong.setText(String.format("%,.2f", totalSmartValue));
            tvTotalMpoLong.setText(String.format("%,.2f", totalMpoValue));
            tvTotalFisikTargetLong.setText(String.format("%,.2f", totalFisikTarget));
            tvTotalFisikValueLong.setText(String.format("%,.2f", totalFisikValue));

        }
    }

    private void showDialogDetail(RealisasiKeuanganFisik obj) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        DialogRekesikDetail newFragment = new DialogRekesikDetail();
        newFragment.setData(obj);
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.add(android.R.id.content, newFragment).addToBackStack(null).commit();
    }
}
