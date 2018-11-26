package com.emansj.mpogo.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import com.emansj.mpogo.adapter.AdapterUserRating;
import com.emansj.mpogo.fragment.DialogReportFilter;
import com.emansj.mpogo.fragment.DialogReportFilterUserRating;
import com.emansj.mpogo.helper.AppGlobal;
import com.emansj.mpogo.helper.ExceptionHandler;
import com.emansj.mpogo.helper.Tools;
import com.emansj.mpogo.helper.VolleyErrorHelper;
import com.emansj.mpogo.helper.VolleySingleton;
import com.emansj.mpogo.model.UserRating;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class UserRatingActivity extends AppCompatActivity {

    //Standard vars
    private static final String TAG = UserRatingActivity.class.getSimpleName();
    private Context m_Ctx;
    private AppGlobal m_Global;
    private AppGlobal.Data m_GlobalData;

    //View vars
    private View parent_view;
    private Toolbar toolbar;
    private RecyclerView rvList;

    //Custom vars
    private List<UserRating> m_ListItem;
    private AdapterUserRating m_Adapter;
    private String m_Title;
    private String m_ReportName;
    private String m_DateFrom;
    private String m_DateTo;
    public static final int DIALOG_QUEST_CODE = 300;


    //---------------------------------------OVERRIDE
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
        m_Ctx = UserRatingActivity.this;
        m_Global = AppGlobal.getInstance(m_Ctx);
        m_GlobalData = m_Global.new Data();

        //Get current date
        String sYear = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
        m_DateFrom = sYear + "0101" ;
        m_DateTo = sYear + "1231" ;

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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_report, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else if(item.getItemId() == R.id.action_filter) {
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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Toolbar Title & SubTitle
        ((TextView) parent_view.findViewById(R.id.tvToolbarTitle)).setText("User Rating");
        ((TextView) parent_view.findViewById(R.id.tvToolbarSubTitle)).setText(m_Title);
    }

    private void initComponent(){
        //LIST
        rvList = findViewById(R.id.rvData);
        rvList.setLayoutManager(new LinearLayoutManager(m_Ctx));
        rvList.setHasFixedSize(true);
        rvList.setNestedScrollingEnabled(false);

        //LIST - ADAPTER - LISTENER
        m_ListItem = new ArrayList<>();
        m_Adapter = new AdapterUserRating(m_Ctx, m_ListItem);
        rvList.setAdapter(m_Adapter);
    }

    private void initData() {
        if (m_ListItem != null) m_ListItem.clear();

        switch(m_ReportName) {
            case "Provinsi":
                m_Adapter.setHolderMode(AdapterUserRating.HolderMode.PROVINSI);
                getDataRatingProvinsi();
                break;
            case "Kabupaten":
                m_Adapter.setHolderMode(AdapterUserRating.HolderMode.KABUPATEN);
                getDataRatingKabupaten();
                break;
            case "User":
                m_Adapter.setHolderMode(AdapterUserRating.HolderMode.ALLUSER);
                getDataRatingAllUser();
                break;
        }
    }


    //---------------------------------------ACTIONS
    private void showFilter() {
        FragmentManager fm = getSupportFragmentManager();
        DialogReportFilterUserRating newFragment = new DialogReportFilterUserRating();
        newFragment.setRequestCode(DIALOG_QUEST_CODE);
        newFragment.setData(m_DateFrom, m_DateTo);

        FragmentTransaction ft = fm.beginTransaction();
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.add(android.R.id.content, newFragment, "DialogReportFilter").addToBackStack(null).commit();

        newFragment.setOnCallbackResult(new DialogReportFilterUserRating.CallbackResult() {
            @Override
            public void sendResult(int requestCode, String sDateFrom, String sDateTo) {
                if (requestCode == DIALOG_QUEST_CODE) {
                    displayBackResult((String) sDateFrom, (String) sDateTo);
                }
            }
        });
    }

    private void displayBackResult(String sDateFrom, String sDateTo) {
        m_DateFrom = sDateFrom;
        m_DateTo = sDateTo;

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
    private void getDataRatingProvinsi() {
        final ProgressDialog progressDialog = new ProgressDialog(m_Ctx);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        String api = "/Laporan/get_ur_provinsi";
        String params = String.format("?from=%1$s&to=%2$s", m_DateFrom, m_DateTo);
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

                                        UserRating obj = new UserRating();
                                        obj.id = Tools.parseInt(row.getString("id"));
                                        obj.name = Tools.parseString(row.getString("name"));
                                        obj.totalUser = Tools.parseInt(row.getString("total_user"));
                                        obj.totalAktifitas = Tools.parseInt(row.getString("total_aktifitas"));
                                        obj.totalPoin = Tools.parseInt(row.getString("total_poin"));
                                        m_ListItem.add(obj);
                                    }
                                }
                                m_Adapter.notifyDataSetChanged();
                            }
                            progressDialog.dismiss();

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

    private void getDataRatingKabupaten() {
        final ProgressDialog progressDialog = new ProgressDialog(m_Ctx);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        String api = "/Laporan/get_ur_kabupaten";
        String params = String.format("?from=%1$s&to=%2$s", m_DateFrom, m_DateTo);
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

                                        UserRating obj = new UserRating();
                                        obj.groupId = Tools.parseInt(row.getString("groupid"));
                                        obj.groupName = Tools.parseString(row.getString("groupname"));
                                        obj.id = Tools.parseInt(row.getString("id"));
                                        obj.name = Tools.parseString(row.getString("name"));
                                        obj.totalUser= Tools.parseInt(row.getString("total_user"));
                                        obj.totalAktifitas = Tools.parseInt(row.getString("total_aktifitas"));
                                        obj.totalPoin = Tools.parseInt(row.getString("total_poin"));
                                        m_ListItem.add(obj);
                                    }
                                    m_Adapter.notifyDataSetChanged();
                                }
                            }
                            progressDialog.dismiss();

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

    private void getDataRatingAllUser() {
        final ProgressDialog progressDialog = new ProgressDialog(m_Ctx);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        String api = "/Laporan/get_ur_semua_user";
        String params = String.format("?from=%1$s&to=%2$s", m_DateFrom, m_DateTo);
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

                                        UserRating obj = new UserRating();
                                        obj.groupId = Tools.parseInt(row.getString("groupid"));
                                        obj.groupName = Tools.parseString(row.getString("groupname"));
                                        obj.id = Tools.parseInt(row.getString("id"));
                                        obj.name = Tools.parseString(row.getString("name"));
                                        obj.subTitle = Tools.parseString(row.getString("subtitle"));
                                        obj.caption = Tools.parseString(row.getString("caption"));
                                        obj.imageUrl = Tools.parseString(row.getString("userimage"));
                                        obj.totalAktifitas = Tools.parseInt(row.getString("total_aktifitas"));
                                        obj.totalPoin = Tools.parseInt(row.getString("total_poin"));
                                        m_ListItem.add(obj);
                                    }
                                    m_Adapter.notifyDataSetChanged();
                                }
                            }

                            progressDialog.dismiss();
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
