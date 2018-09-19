package com.emansj.mpogo.activity;

import android.content.Context;
import android.os.Bundle;
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
import com.emansj.mpogo.model.RealisasiKeuangan;

import java.util.List;

public class RkKewenanganActivity extends AppCompatActivity {

    //Standard vars
    private static final String TAG = RkKewenanganActivity.class.getSimpleName();
    private Context m_Ctx;
    private RequestQueue m_Queue;

    //Define views
    private View parent_view;
    private Toolbar toolbar;
    private RecyclerView rvList;
    private TextView tvTotalPagu;
    private TextView tvTotalSmartValue;
    private TextView tvTotalSmartPercent;
    private TextView tvTotalMpoValue;
    private TextView tvTotalMpoPercent;

    //Custom vars
    private List<RealisasiKeuangan> m_ListItem;
    private AdapterReke m_Adapter;
    private String m_ReportName;

//    public void RkKewenanganActivity(String reportName){
//        m_ReportName = reportName;
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_realisasi_keuangan);

        //Activity settings
        parent_view = findViewById(android.R.id.content);
        m_Ctx = RkKewenanganActivity.this;

        initToolbar();
        showList();
        showTotal();
    }

    private void initToolbar(){
        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Kewenangan");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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

    private void showList() {
        rvList = parent_view.findViewById(R.id.rvData);
        rvList.setLayoutManager(new LinearLayoutManager(m_Ctx));
        rvList.setHasFixedSize(true);

//        switch(m_ReportName) {
//            case "Kewenangan":
//                m_ListItem = RealisasiKeuangan.getKewenangan(m_Ctx);
//                break;
//            case "Kegiatan":
//                m_ListItem = RealisasiKeuangan.getKegiatan(m_Ctx);
//                break;
//            case "KegiatanOutput":
//                m_ListItem = RealisasiKeuangan.getKegiatanOutput(m_Ctx);
//                break;
//            case "KegiatanNProvinsi":
//                m_ListItem = RealisasiKeuangan.getKegiatanOutput(m_Ctx);
//                break;
//            case "Satker":
//                m_ListItem = RealisasiKeuangan.getKegiatanOutput(m_Ctx);
//                break;
//            case "Output":
//                m_ListItem = RealisasiKeuangan.getKegiatanOutput(m_Ctx);
//                break;
//        }
        m_ListItem = RealisasiKeuangan.getKewenangan(m_Ctx);
        m_Adapter = new AdapterReke(m_Ctx, m_ListItem);
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
            tvTotalSmartPercent = parent_view.findViewById(R.id.tvTotalSmartPercent);
            tvTotalSmartValue = parent_view.findViewById(R.id.tvTotalSmartValue);
            tvTotalMpoPercent = parent_view.findViewById(R.id.tvTotalMpoPercent);
            tvTotalMpoValue = parent_view.findViewById(R.id.tvTotalMpoValue);

            //set
            tvTotalPagu.setText(String.format("%,.2f", totalPagu));
            tvTotalSmartValue.setText(String.format("%,.2f", totalSmartValue));
            tvTotalSmartPercent.setText(String.format("%,.2f", totalSmartPercent) + "%");
            tvTotalMpoValue.setText(String.format("%,.2f", totalMpoValue));
            tvTotalMpoPercent.setText(String.format("%,.2f", totalMpoPercent) + "%");

        }
    }

}
