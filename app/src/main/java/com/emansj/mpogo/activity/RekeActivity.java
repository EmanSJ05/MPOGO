package com.emansj.mpogo.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
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
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.emansj.mpogo.R;
import com.emansj.mpogo.adapter.AdapterReke;
import com.emansj.mpogo.fragment.DialogRekeDetail;
import com.emansj.mpogo.fragment.DialogReportFilter;
import com.emansj.mpogo.helper.AppGlobal;
import com.emansj.mpogo.helper.AppUtils;
import com.emansj.mpogo.helper.ExceptionHandler;
import com.emansj.mpogo.helper.Tools;
import com.emansj.mpogo.helper.VolleyErrorHelper;
import com.emansj.mpogo.helper.VolleySingleton;
import com.emansj.mpogo.model.RealisasiKeuangan;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;


public class RekeActivity extends AppCompatActivity {

    //Standard vars
    private static final String TAG = RekeActivity.class.getSimpleName();
    private Context m_Ctx;
    private AppGlobal m_Global;
    private AppGlobal.Data m_GlobalData;

    //View vars
    private View parent_view;
    private Toolbar toolbar;
    private RecyclerView rvList;
    private View lyBottom, lyBottomTotal;
    private TextView    tvTotalPagu, tvTotalPaguLong,
                        tvTotalSmartValue, tvTotalSmartPercent, tvTotalSmartLong,
                        tvTotalMpoValue, tvTotalMpoPercent, tvTotalMpoLong;

    //Custom vars
    private List<RealisasiKeuangan> m_ListItem;
    private AdapterReke m_Adapter;
    private String m_Title;
    private String m_ReportName;
    private boolean m_IsBottomHide = false;
    public static final int DIALOG_QUEST_CODE = 300;


    //---------------------------------------OVERRIDE
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
        m_Global = AppGlobal.getInstance(m_Ctx);
        m_GlobalData = m_Global.new Data();

        initToolbar();
        initComponent();
        initData();
    }

    @Override
    protected void onStop () {
        super.onStop();
        VolleySingleton.getInstance(m_Ctx).cancelPendingRequests(TAG);
        m_GlobalData.cancelRequest(TAG);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        VolleySingleton.getInstance(m_Ctx).cancelPendingRequests(TAG);
        m_GlobalData.cancelRequest(TAG);
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag("DialogReportFilter");
        if (fragment != null) {
            if (fragment.isVisible()) {
                if (fragment instanceof DialogReportFilter) {
                    if (fragment.getView().findViewById(R.id.lyTahunRKA).getVisibility() == View.GONE) {
                        fragment.getView().findViewById(R.id.lyTahunRKA).setVisibility(View.VISIBLE);
                    } else {
                        super.onBackPressed();
                    }
                }
            }
        }else{
            super.onBackPressed();
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
        else if(item.getItemId() == R.id.action_filter) {
            showFilter();
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
        getSupportActionBar().setTitle(m_Title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initComponent(){
        lyBottom = findViewById(R.id.lyBottom);
        lyBottomTotal = findViewById(R.id.lyBottomTotal);

        //LIST
        rvList = findViewById(R.id.rvData);
        rvList.setLayoutManager(new LinearLayoutManager(m_Ctx));
        rvList.setHasFixedSize(true);
        rvList.setNestedScrollingEnabled(false);

        //LIST - ADAPTER - LISTENER
        m_ListItem = new ArrayList<>();
        m_Adapter = new AdapterReke(m_Ctx, m_ListItem);
        m_Adapter.setOnItemClickListener(new AdapterReke.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RealisasiKeuangan obj, int position) {
                final RealisasiKeuangan rk = m_ListItem.get(position);
                showDialogDetail(rk);
            }
        });
        rvList.setAdapter(m_Adapter);

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

    private void initData() {
        if (m_ListItem != null) m_ListItem.clear();

        switch(m_ReportName) {
            case "Kewenangan":
                getDataKewenangan();
                break;
            case "Kegiatan":
                getDataKegiatan();
                break;
            case "KegiatanNProvinsi":
                getDataKegiatanProvinsi();
                break;
            case "KegiatanOutput":
                getDataKegiatanOutput();
                break;
            case "Satker":
                getDataSatker();
                break;
            case "Output":
                getDataOutput();
                break;
        }
    }

    private void getTotal() {
        double totalPagu = 0;
        double totalSmartValue = 0;
        double totalSmartPercent = 0;
        double totalMpoValue = 0;
        double totalMpoPercent = 0;

        if (m_ListItem != null) {

            for (int row = 0; row < m_ListItem.size(); row++) {
                if (m_ListItem.get(row).tag == null || m_ListItem.get(row).tag.equals("subtotal")) {
                    totalPagu += m_ListItem.get(row).pagu;
                    totalSmartValue += m_ListItem.get(row).smartValue;
                    totalMpoValue += m_ListItem.get(row).mpoValue;
                }
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


    //---------------------------------------ACTIONS
    private void animateBottom(final boolean hide) {
        if (m_IsBottomHide && hide || !m_IsBottomHide && !hide) return;
        m_IsBottomHide = hide;
        int moveY = hide ? (lyBottomTotal.getHeight()) : 0;
        lyBottom.animate().translationY(moveY).setStartDelay(100).setDuration(300).start();
    }

    private void showDialogDetail(RealisasiKeuangan obj) {
        FragmentManager fm = getSupportFragmentManager();
        DialogRekeDetail newFragment = new DialogRekeDetail();
        newFragment.setData(obj);

        FragmentTransaction ft = fm.beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.add(android.R.id.content, newFragment).addToBackStack(null).commit();
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
            public void sendResult(int requestCode, int tahunRKA, boolean semuaSatker, String idSatkers) {
                if (requestCode == DIALOG_QUEST_CODE) {
                    displayBackResult((int) tahunRKA, (boolean) semuaSatker, (String) idSatkers);
                }
            }
        });
    }

    private void displayBackResult(int tahunRKA, boolean semuaSatker, String idSatkers) {
        //Toast.makeText(m_Ctx,   "tahun rka : " + tahunRKA + "\n semua satker : " + semuaSatker + "\n idsatkers : " + idSatkers, Toast.LENGTH_SHORT).show();

        initData();
        m_Adapter.notifyDataSetChanged();
    }

    public void hideKeyboard(View view) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);

        if (view != null) {
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    //---------------------------------------GET DATA
    private void getDataKewenangan() {
        final ProgressDialog progressDialog = new ProgressDialog(m_Ctx);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        String api = "/Laporan/getRKPerJenisKewenangan";
        String params = String.format("?tahun=%1$d&idsatker=%2$s&userid=%3$d", m_Global.getTahunRKA(), m_Global.getFilterSelectedIdSatkers(), m_Global.getUserLoginId());
        String url = AppGlobal.URL_ROOT + api + params;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            int status = response.getInt("status");
                            if (status == 200 )
                            {
                                JSONArray data = response.getJSONArray("data");
                                if (data.length() > 0)
                                {
                                    for (int i = 0; i < data.length(); i++)
                                    {
                                        JSONObject row = data.getJSONObject(i);

                                        RealisasiKeuangan obj = new RealisasiKeuangan();
                                        obj.title = Tools.parseString(row.getString("title"));
                                        obj.pagu = Tools.parseDouble(row.getString("pagu"));
                                        obj.smartValue = Tools.parseDouble(row.getString("smartValue"));
                                        obj.smartPercent = Tools.parseDouble(row.getString("smartPercent"));
                                        obj.mpoValue = Tools.parseDouble(row.getString("mpoValue"));
                                        obj.mpoPercent = Tools.parseDouble(row.getString("mpoPercent"));
                                        m_ListItem.add(obj);
                                    }
                                    progressDialog.dismiss();
                                    m_Adapter.notifyDataSetChanged();
                                    getTotal();
                                }
                            }

                        }catch (JSONException e){
                            e.printStackTrace();
                            progressDialog.dismiss();
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        progressDialog.dismiss();
                        VolleyErrorHelper.showError(error, m_Ctx);
                    }
                }
        );
        VolleySingleton.getInstance(m_Ctx).addToRequestQueue(request, TAG);
    }

    private void getDataKegiatan() {
        final ProgressDialog progressDialog = new ProgressDialog(m_Ctx);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        String api = "/Laporan/getRKPerKegiatan";
        String params = String.format("?tahun=%1$d&idsatker=%2$s&userid=%3$d", m_Global.getTahunRKA(), m_Global.getFilterSelectedIdSatkers(), m_Global.getUserLoginId());
        String url = AppGlobal.URL_ROOT + api + params;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            int status = response.getInt("status");
                            if (status == 200 )
                            {
                                JSONArray data = response.getJSONArray("data");
                                if (data.length() > 0)
                                {
                                    for (int i = 0; i < data.length(); i++)
                                    {
                                        JSONObject row = data.getJSONObject(i);

                                        RealisasiKeuangan obj = new RealisasiKeuangan();
                                        obj.title = Tools.parseString(row.getString("title"));
                                        obj.pagu = Tools.parseDouble(row.getString("pagu"));
                                        obj.smartValue = Tools.parseDouble(row.getString("smartValue"));
                                        obj.smartPercent = Tools.parseDouble(row.getString("smartPercent"));
                                        obj.mpoValue = Tools.parseDouble(row.getString("mpoValue"));
                                        obj.mpoPercent = Tools.parseDouble(row.getString("mpoPercent"));
                                        m_ListItem.add(obj);
                                    }
                                    progressDialog.dismiss();
                                    m_Adapter.notifyDataSetChanged();
                                    getTotal();
                                }
                            }

                        }catch (JSONException e){
                            e.printStackTrace();
                            progressDialog.dismiss();
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        progressDialog.dismiss();
                        VolleyErrorHelper.showError(error, m_Ctx);
                    }
                }
        );
        VolleySingleton.getInstance(m_Ctx).addToRequestQueue(request, TAG);
    }

    private void getDataKegiatanProvinsi() {
        final ProgressDialog progressDialog = new ProgressDialog(m_Ctx);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        String api = "/Laporan/getRKPerKegiatanProvinsi";
        String params = String.format("?tahun=%1$d&idsatker=%2$s&userid=%3$d", m_Global.getTahunRKA(), m_Global.getFilterSelectedIdSatkers(), m_Global.getUserLoginId());
        String url = AppGlobal.URL_ROOT + api + params;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            int status = response.getInt("status");
                            if (status == 200 )
                            {
                                JSONArray data = response.getJSONArray("data");
                                if (data.length() > 0)
                                {
                                    for (int i = 0; i < data.length(); i++)
                                    {
                                        JSONObject row = data.getJSONObject(i);

                                        RealisasiKeuangan obj = new RealisasiKeuangan();
                                        obj.tag = Tools.parseString(row.getString("tag"));
                                        obj.title = Tools.parseString(row.getString("title"));
                                        obj.pagu = Tools.parseDouble(row.getString("pagu"));
                                        obj.smartValue = Tools.parseDouble(row.getString("smartValue"));
                                        obj.smartPercent = Tools.parseDouble(row.getString("smartPercent"));
                                        obj.mpoValue = Tools.parseDouble(row.getString("mpoValue"));
                                        obj.mpoPercent = Tools.parseDouble(row.getString("mpoPercent"));
                                        m_ListItem.add(obj);
                                    }
                                    progressDialog.dismiss();
                                    m_Adapter.notifyDataSetChanged();
                                    getTotal();
                                }
                            }

                        }catch (JSONException e){
                            e.printStackTrace();
                            progressDialog.dismiss();
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        progressDialog.dismiss();
                        VolleyErrorHelper.showError(error, m_Ctx);
                    }
                }
        );
        VolleySingleton.getInstance(m_Ctx).addToRequestQueue(request, TAG);
    }

    private void getDataKegiatanOutput() {
        final ProgressDialog progressDialog = new ProgressDialog(m_Ctx);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        String api = "/Laporan/getRKPerKegiatanOutput";
        String params = String.format("?tahun=%1$d&idsatker=%2$s&userid=%3$d", m_Global.getTahunRKA(), m_Global.getFilterSelectedIdSatkers(), m_Global.getUserLoginId());
        String url = AppGlobal.URL_ROOT + api + params;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            int status = response.getInt("status");
                            if (status == 200 )
                            {
                                JSONArray data = response.getJSONArray("data");
                                if (data.length() > 0)
                                {
                                    for (int i = 0; i < data.length(); i++)
                                    {
                                        JSONObject row = data.getJSONObject(i);

                                        RealisasiKeuangan obj = new RealisasiKeuangan();
                                        obj.title = Tools.parseString(row.getString("title"));
                                        obj.pagu = Tools.parseDouble(row.getString("pagu"));
                                        obj.smartValue = Tools.parseDouble(row.getString("smartValue"));
                                        obj.smartPercent = Tools.parseDouble(row.getString("smartPercent"));
                                        obj.mpoValue = Tools.parseDouble(row.getString("mpoValue"));
                                        obj.mpoPercent = Tools.parseDouble(row.getString("mpoPercent"));
                                        m_ListItem.add(obj);
                                    }
                                    progressDialog.dismiss();
                                    m_Adapter.notifyDataSetChanged();
                                    getTotal();
                                }
                            }

                        }catch (JSONException e){
                            e.printStackTrace();
                            progressDialog.dismiss();
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        progressDialog.dismiss();
                        VolleyErrorHelper.showError(error, m_Ctx);
                    }
                }
        );
        VolleySingleton.getInstance(m_Ctx).addToRequestQueue(request, TAG);
    }

    private void getDataSatker() {
        final ProgressDialog progressDialog = new ProgressDialog(m_Ctx);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        String api = "/Laporan/getRKPerSatker";
        String params = String.format("?tahun=%1$d&idsatker=%2$s&userid=%3$d", m_Global.getTahunRKA(), m_Global.getFilterSelectedIdSatkers(), m_Global.getUserLoginId());
        String url = AppGlobal.URL_ROOT + api + params;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            int status = response.getInt("status");
                            if (status == 200 )
                            {
                                JSONArray data = response.getJSONArray("data");
                                if (data.length() > 0)
                                {
                                    for (int i = 0; i < data.length(); i++)
                                    {
                                        JSONObject row = data.getJSONObject(i);

                                        RealisasiKeuangan obj = new RealisasiKeuangan();
                                        obj.title = Tools.parseString(row.getString("title"));
                                        obj.pagu = Tools.parseDouble(row.getString("pagu"));
                                        obj.smartValue = Tools.parseDouble(row.getString("smartValue"));
                                        obj.smartPercent = Tools.parseDouble(row.getString("smartPercent"));
                                        obj.mpoValue = Tools.parseDouble(row.getString("mpoValue"));
                                        obj.mpoPercent = Tools.parseDouble(row.getString("mpoPercent"));
                                        m_ListItem.add(obj);
                                    }
                                    progressDialog.dismiss();
                                    m_Adapter.notifyDataSetChanged();
                                    getTotal();
                                }
                            }

                        }catch (JSONException e){
                            e.printStackTrace();
                            progressDialog.dismiss();
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        progressDialog.dismiss();
                        VolleyErrorHelper.showError(error, m_Ctx);
                    }
                }
        );
        VolleySingleton.getInstance(m_Ctx).addToRequestQueue(request, TAG);
    }

    private void getDataOutput() {
        final ProgressDialog progressDialog = new ProgressDialog(m_Ctx);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        String api = "/Laporan/getRKPerOutput";
        String params = String.format("?tahun=%1$d&idsatker=%2$s&userid=%3$d", m_Global.getTahunRKA(), m_Global.getFilterSelectedIdSatkers(), m_Global.getUserLoginId());
        String url = AppGlobal.URL_ROOT + api + params;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            int status = response.getInt("status");
                            if (status == 200 )
                            {
                                JSONArray data = response.getJSONArray("data");
                                if (data.length() > 0)
                                {
                                    for (int i = 0; i < data.length(); i++)
                                    {
                                        JSONObject row = data.getJSONObject(i);

                                        RealisasiKeuangan obj = new RealisasiKeuangan();
                                        obj.title = Tools.parseString(row.getString("title"));
                                        obj.pagu = Tools.parseDouble(row.getString("pagu"));
                                        obj.smartValue = Tools.parseDouble(row.getString("smartValue"));
                                        obj.smartPercent = Tools.parseDouble(row.getString("smartPercent"));
                                        obj.mpoValue = Tools.parseDouble(row.getString("mpoValue"));
                                        obj.mpoPercent = Tools.parseDouble(row.getString("mpoPercent"));
                                        m_ListItem.add(obj);
                                    }
                                    progressDialog.dismiss();
                                    m_Adapter.notifyDataSetChanged();
                                    getTotal();
                                }
                            }

                        }catch (JSONException e){
                            e.printStackTrace();
                            progressDialog.dismiss();
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        progressDialog.dismiss();
                        VolleyErrorHelper.showError(error, m_Ctx);
                    }
                }
        );
        VolleySingleton.getInstance(m_Ctx).addToRequestQueue(request, TAG);
    }

}