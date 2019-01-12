package com.emansj.mpogo.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.emansj.mpogo.R;
import com.emansj.mpogo.adapter.AdapterDashboardKegut;
import com.emansj.mpogo.adapter.AdapterUserRating;
import com.emansj.mpogo.helper.AppGlobal;
import com.emansj.mpogo.helper.AppUtils;
import com.emansj.mpogo.helper.ExceptionHandler;
import com.emansj.mpogo.helper.Tools;
import com.emansj.mpogo.helper.VolleyErrorHelper;
import com.emansj.mpogo.helper.VolleySingleton;
import com.emansj.mpogo.model.DashboardDonut;
import com.emansj.mpogo.model.DashboardKegut;
import com.emansj.mpogo.model.RealisasiKeuangan;
import com.emansj.mpogo.model.UserRating;
import com.github.lzyzsd.circleprogress.DonutProgress;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class HomeFragment extends Fragment {

    //Standard vars
    private static final String TAG = HomeFragment.class.getSimpleName();
    private Context m_Ctx;
    private AppGlobal m_Global;
    private AppGlobal.Data m_GlobalData;

    //View vars
    private View parent_view;
    private TextView    tvPieSmartUpdate, tvSmartPagu, tvSmartRealisasi, tvSmartSisa,
                        tvPieMpoUpdate, tvMpoPagu, tvMpoRealisasi, tvMpoSisa,
                        tvSmartUpdate, tvMpoUpdate;
    private DonutProgress dopSmartPercent, dopMpoPercent;
    private RecyclerView rvSMART;
    private RecyclerView rvMPO;

    //Custom vars
    private List<DashboardKegut> m_ListItemReke;
    private List<DashboardKegut> m_ListItemRefi;
    private AdapterDashboardKegut m_AdapterReke;
    private AdapterDashboardKegut m_AdapterRefi;

    public HomeFragment() {
        // Required empty public constructor
    }


    //---------------------------------------OVERRIDE
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        m_Ctx = this.getContext();
        parent_view = inflater.inflate(R.layout.fragment_home, container, false);
        m_Global = AppGlobal.getInstance(m_Ctx);
        m_GlobalData = m_Global.new Data();

        initComponent();
        initData();

        return parent_view;
    }

    @Override
    public void onStop () {
        super.onStop();
        VolleySingleton.getInstance(m_Ctx).cancelPendingRequests(TAG);
        m_GlobalData.cancelAllRequest();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        VolleySingleton.getInstance(m_Ctx).cancelPendingRequests(TAG);
        m_GlobalData.cancelAllRequest();
    }


    //---------------------------------------INIT COMPONENTS & DATA
    private void initComponent() {
        tvSmartPagu = parent_view.findViewById(R.id.tvSmartPagu);
        tvPieSmartUpdate = parent_view.findViewById(R.id.tvPieSmartUpdate);
        tvSmartRealisasi = parent_view.findViewById(R.id.tvSmartRealisasi);
        tvSmartSisa = parent_view.findViewById(R.id.tvSmartSisa);
        dopSmartPercent = parent_view.findViewById(R.id.dopSmartPercent);

        tvPieMpoUpdate = parent_view.findViewById(R.id.tvPieMpoUpdate);
        tvMpoPagu = parent_view.findViewById(R.id.tvMpoPagu);
        tvMpoRealisasi = parent_view.findViewById(R.id.tvMpoRealisasi);
        tvMpoSisa = parent_view.findViewById(R.id.tvMpoSisa);
        dopMpoPercent = parent_view.findViewById(R.id.dopMpoPercent);

        tvSmartUpdate = parent_view.findViewById(R.id.tvSmartUpdate);
        tvMpoUpdate = parent_view.findViewById(R.id.tvMpoUpdate);

        //LIST - SMART
        rvSMART = parent_view.findViewById(R.id.rvKegutSMART);
        rvSMART.setLayoutManager(new LinearLayoutManager(m_Ctx));
        rvSMART.setHasFixedSize(true);
        rvSMART.setNestedScrollingEnabled(false);

        m_ListItemReke = new ArrayList<>();
        m_AdapterReke = new AdapterDashboardKegut(m_Ctx, m_ListItemReke);
        rvSMART.setAdapter(m_AdapterReke);
        m_AdapterReke.setOnItemClickListener(new AdapterDashboardKegut.OnItemClickListener() {
            @Override
            public void onItemClick(View view, DashboardKegut obj, int position) {
                Snackbar.make(parent_view, "Item " + obj.categoryName + " clicked", Snackbar.LENGTH_SHORT).show();
            }
        });

        //LIST - MPO
        rvMPO = parent_view.findViewById(R.id.rvKegutMPO);
        rvMPO.setLayoutManager(new LinearLayoutManager(m_Ctx));
        rvMPO.setHasFixedSize(true);
        rvMPO.setNestedScrollingEnabled(false);

        m_ListItemRefi = new ArrayList<>();
        m_AdapterRefi = new AdapterDashboardKegut(m_Ctx, m_ListItemRefi);
        rvMPO.setAdapter(m_AdapterRefi);
        m_AdapterRefi.setOnItemClickListener(new AdapterDashboardKegut.OnItemClickListener() {
            @Override
            public void onItemClick(View view, DashboardKegut obj, int position) {
                Snackbar.make(parent_view, "Item " + obj.categoryName + " clicked", Snackbar.LENGTH_SHORT).show();
            }
        });

    }

    private void initData() {
        getDataDonut();
        getReke();
        getRefi();
    }


    //---------------------------------------ACTIONS
    public void refreshData() {
        if (m_ListItemReke != null) m_ListItemReke.clear();
        if (m_ListItemRefi != null) m_ListItemRefi.clear();

        getDataDonut();
        getReke();
        getRefi();
    }


    //---------------------------------------GET DATA
    private void getDataDonut(){
        final ProgressDialog progressDialog = new ProgressDialog(m_Ctx);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        String api = "/Dashboard/get_pusat_smart_and_sas";
        String params = String.format("?tahun=%1$d", m_Global.getTahunRKA());
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
                                    JSONObject row = data.getJSONObject(0);

                                    DashboardDonut obj = new DashboardDonut();
                                    obj.tahun = Tools.parseInt(row.getString("tahun"));
                                    obj.pagu = Tools.parseDouble(row.getString("pagu"));

                                    obj.smartLastUpdate = Tools.convertDateSTD(row.getString("smartLastUpdate"), "yyyy-MM-dd");
                                    obj.smartRealisasi = Tools.parseDouble(row.getString("smartRealisasi"));
                                    obj.smartSisa = Tools.parseDouble(row.getString("smartSisa"));
                                    obj.smartRealisasiPersen = Tools.parseDouble(row.getString("smartRealisasiPersen"));

                                    obj.mpoLastUpdate = Tools.convertDateSTD(row.getString("mpoLastUpdate"), "yyyy-MM-dd");
                                    obj.mpoRealisasi = Tools.parseDouble(row.getString("mpoRealisasi"));
                                    obj.mpoSisa = Tools.parseDouble(row.getString("mpoSisa"));
                                    obj.mpoRealisasiPersen = Tools.parseDouble(row.getString("mpoRealisasiPersen"));


                                    tvPieSmartUpdate.setText(Tools.convertDateDTS(obj.smartLastUpdate, "d MMM yyyy"));
                                    tvSmartPagu.setText(AppUtils.shortCurrency(m_Ctx, String.format("%.0f", obj.pagu)));
                                    tvSmartRealisasi.setText(AppUtils.shortCurrency(m_Ctx, String.format("%.0f", obj.smartRealisasi)));
                                    tvSmartSisa.setText(AppUtils.shortCurrency(m_Ctx, String.format("%.0f", obj.smartSisa)));
                                    dopSmartPercent.setProgress(Long.parseLong(String.format("%.0f", obj.smartRealisasiPersen)));

                                    tvPieMpoUpdate.setText(Tools.convertDateDTS(obj.mpoLastUpdate, "d MMM yyyy"));
                                    tvMpoPagu.setText(AppUtils.shortCurrency(m_Ctx, String.format("%.0f", obj.pagu)));
                                    tvMpoRealisasi.setText(AppUtils.shortCurrency(m_Ctx, String.format("%.0f", obj.mpoRealisasi)));
                                    tvMpoSisa.setText(AppUtils.shortCurrency(m_Ctx, String.format("%.0f", obj.mpoSisa)));
                                    dopMpoPercent.setProgress(Long.parseLong(String.format("%.0f", obj.mpoRealisasiPersen)));
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

    private void getReke() {
        final ProgressDialog progressDialog = new ProgressDialog(m_Ctx);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        String api = "/Dashboard/get_pusat_reke_kegut";
        String params = String.format("?tahun=%1$d", m_Global.getTahunRKA());
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

                                        DashboardKegut obj = new DashboardKegut();
                                        obj.categoryName = row.getString("categoryName");
                                        obj.lastUpdate = Tools.convertDateSTD(row.getString("lastUpdate"), "yyyy-MM-dd");
                                        obj.realPercent = Tools.parseDouble(row.getString("realPercent"));
                                        m_ListItemReke.add(obj);

                                        tvSmartUpdate.setText(Tools.convertDateDTS(obj.lastUpdate, "d MMM yyyy"));
                                    }
                                    m_AdapterReke.notifyDataSetChanged();
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

    private void getRefi() {
        final ProgressDialog progressDialog = new ProgressDialog(m_Ctx);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        String api = "/Dashboard/get_pusat_refi_kegut";
        String params = String.format("?tahun=%1$d", m_Global.getTahunRKA());
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

                                        DashboardKegut obj = new DashboardKegut();
                                        obj.categoryName = row.getString("categoryName");
                                        obj.lastUpdate = Tools.convertDateSTD(row.getString("lastUpdate"), "yyyy-MM-dd");
                                        obj.realPercent = Tools.parseDouble(row.getString("realPercent"));
                                        m_ListItemRefi.add(obj);

                                        tvMpoUpdate.setText(Tools.convertDateDTS(obj.lastUpdate, "d MMM yyyy"));
                                    }
                                    m_AdapterRefi.notifyDataSetChanged();
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

