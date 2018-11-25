package com.emansj.mpogo.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.emansj.mpogo.R;
import com.emansj.mpogo.activity.RekeActivity;
import com.emansj.mpogo.activity.RekesikActivity;
import com.emansj.mpogo.adapter.AdapterSatkerFilter;
import com.emansj.mpogo.helper.AppGlobal;
import com.emansj.mpogo.helper.ExceptionHandler;
import com.emansj.mpogo.helper.Tools;
import com.emansj.mpogo.helper.VolleyErrorHelper;
import com.emansj.mpogo.helper.VolleySingleton;
import com.emansj.mpogo.model.RealisasiKeuangan;
import com.emansj.mpogo.model.Satker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static android.media.CamcorderProfile.get;

public class DialogReportFilter extends DialogFragment {


    //Standard vars
    private static final String TAG = DialogReportFilter.class.getSimpleName();
    private Context m_Ctx;
    private AppGlobal m_Global;
    private AppGlobal.Data m_GlobalData;

    //View vars
    private View parent_view;
    private Toolbar toolbar;
    private TextView tvSatkerTitle, tvSemuaSatker, tvTahunRKA;
    private CheckBox chkSemuaSatker;
    private EditText etFilterText;
    private ImageButton imbFilterClear;
    private RecyclerView rvList;
    private ImageView imgClose;
    private View lyTahunRKA, lyListLock, lyCari;

    //Custom vars
    private List<String> m_ListTahunRKA = new ArrayList<>();
    private List<Satker> m_ListSatker;
    private AdapterSatkerFilter m_Adapter;
    private int m_Filter_TahunRKA;
    private boolean m_Filter_SemuaSatker;
    private String m_Filter_idSatkers_InString;


    //---------------------------------------OVERRIDE
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        m_Ctx = this.getContext();
        parent_view = inflater.inflate(R.layout.dialog_report_filter, container, false);
        setCancelable(false);
        m_Global = AppGlobal.getInstance(m_Ctx);
        m_GlobalData = m_Global.new Data();

        initComponent();
        initData();

        return parent_view;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }


    //---------------------------------------INIT COMPONENTS & DATA
    private void initComponent() {
        tvTahunRKA = parent_view.findViewById(R.id.tvTahunRKA);
        tvSatkerTitle = parent_view.findViewById(R.id.tvSatkerTitle);
        tvSemuaSatker = parent_view.findViewById(R.id.tvSemuaSatker );
        chkSemuaSatker = parent_view.findViewById(R.id.chkSemuaSatker);
        etFilterText = parent_view.findViewById(R.id.etFilterText);
        imbFilterClear = parent_view.findViewById(R.id.imbFilterClear);
        imgClose = parent_view.findViewById(R.id.imgClose);
        lyTahunRKA = parent_view.findViewById(R.id.lyTahunRKA);
        lyListLock = parent_view.findViewById(R.id.lyListLock);
        lyCari = parent_view.findViewById(R.id.lyCari);

        //LIST
        rvList = (RecyclerView) parent_view.findViewById(R.id.rvList);
        rvList.setLayoutManager(new LinearLayoutManager(m_Ctx));
        rvList.setHasFixedSize(true);
        rvList.setNestedScrollingEnabled(false);

        //LIST - ADAPTER - LISTENER
        m_ListSatker = new ArrayList<>();
        m_Adapter = new AdapterSatkerFilter(m_Ctx, m_ListSatker);
        m_Adapter.setOnItemClickListener(new AdapterSatkerFilter.OnItemClickListener() {
            @Override
            public void onItemClick(View view) {
                countSelectedItems();
            }
        });
        rvList.setAdapter(m_Adapter);

        //TAHUN RKA
        tvTahunRKA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogTahunRKA();
            }
        });

        //SEMUA SATKER
        tvSemuaSatker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chkSemuaSatker.setChecked(!chkSemuaSatker.isChecked());
            }
        });

        //FILTER TEXT - ON FOCUS
        etFilterText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    lyTahunRKA.setVisibility(View.GONE);
                }else{
                    lyTahunRKA.setVisibility(View.VISIBLE);
                }
            }
        });

        //FILTER TEXT - TEXT CHANGED
        etFilterText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                m_Adapter.getFilter().filter(s);
            }
        });

        //FILTER CLEAR
        imbFilterClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etFilterText.getText().toString().trim().length() > 0){
                    etFilterText.setText("");
                }else {
                    etFilterText.clearFocus();
                    if (m_Ctx instanceof RekeActivity){
                        ((RekeActivity)getActivity()).hideKeyboard(view);
                    }else if (m_Ctx instanceof RekesikActivity){
                        ((RekesikActivity)getActivity()).hideKeyboard(view);
                    }
                }
            }
        });

        //SEMUA SATKER
        chkSemuaSatker.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    lyListLock.setVisibility(View.VISIBLE);
                }else{
                    lyListLock.setVisibility(View.GONE);
                }
            }
        });

        //CARI
        lyCari.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                search();
            }
        });

        //CLOSE
        imgClose.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private void initData() {
        //get last selections
        tvTahunRKA.setText(String.valueOf(m_Global.getTahunRKA()));
        if (m_Global.getFilterRunFirst()) {
            chkSemuaSatker.setChecked(true);
        }

        //get fresh satkers from server
        if (m_ListSatker != null) m_ListSatker.clear();
        getSatker();
    }


    //---------------------------------------ACTIONS
    private void countSelectedItems() {
        long count = m_Adapter.getSelectedItemCount();

        if (count == 0) {
            tvSatkerTitle.setText("SATKER");
        } else {
            tvSatkerTitle.setText("SATKER (" + count + ")");
        }
    }

    private String selectedTahun;
    private void showDialogTahunRKA() {
        if (m_Global.getTahunRKAList().isEmpty() == false)
        {
            //get choice list
            m_ListTahunRKA = m_Global.getTahunRKAList();
            String[] tahun = new String[m_ListTahunRKA.size()];
            tahun = m_ListTahunRKA.toArray(tahun);

            //get default choice
            int defaultChoice = m_ListTahunRKA.indexOf(tvTahunRKA.getText());

            //init alert dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(m_Ctx);
            builder.setTitle("Pilih Tahun RKA");
            builder.setSingleChoiceItems(tahun, defaultChoice, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    selectedTahun = m_ListTahunRKA.get(i);
                }
            });

            //init click OK
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialogInterface, int i)
                {
                    tvTahunRKA.setText(selectedTahun);
                    m_Global.setTahunRKA(Integer.parseInt(selectedTahun));

                    getSatker();
                    m_Adapter.notifyDataSetChanged();
                }
            });

            //init click Batal
            builder.setNegativeButton("Batal", null);
            builder.show();
        }
    }

    private void search() {
        m_Filter_TahunRKA = Integer.parseInt(tvTahunRKA.getText().toString());
        m_Filter_SemuaSatker = chkSemuaSatker.isChecked();

        //id satkers in string
        List<Integer> selectedSatkers = m_Adapter.getSelectedItem();
        List<String> selectedSatkers_String = new ArrayList<String>();

        if (chkSemuaSatker.isChecked()){
            m_Filter_idSatkers_InString = null;
        }else{
            m_Filter_idSatkers_InString = TextUtils.join(",", selectedSatkers);

            if (selectedSatkers.size() > 0) {
                selectedSatkers_String = new ArrayList<String>(selectedSatkers.size());
                for (Integer item : selectedSatkers) {
                    selectedSatkers_String.add(String.valueOf(item));
                }
            }
        }

        //set to AppGlobal
        m_Global.setTahunRKA(m_Filter_TahunRKA);
        m_Global.setFilterIsAllSatker(m_Filter_SemuaSatker);
        m_Global.setFilterSelectedIdSatkers(m_Filter_idSatkers_InString);
        m_Global.setFilterRunFirst(false);
        m_Global.setFilterSelectedIdSatkers_List(selectedSatkers_String);

        //send back to activity
        sendDataResult();
        dismiss();
    }


    //---------------------------------------CALLBACK
    private int request_code = 0;
    public CallbackResult callbackResult;

    public void setOnCallbackResult(final CallbackResult callbackResult) {
        this.callbackResult = callbackResult;
    }

    public interface CallbackResult {
        void sendResult(int requestCode, int tahunRKA, boolean semuaSatker, String idSatkers);
    }

    private void sendDataResult() {
        if (callbackResult != null) {
            callbackResult.sendResult(request_code, m_Filter_TahunRKA, m_Filter_SemuaSatker, m_Filter_idSatkers_InString);
        }
    }

    public void setRequestCode(int request_code) {
        this.request_code = request_code;
    }


    //---------------------------------------GET DATA
    private void getSatker() {
        final ProgressDialog progressDialog = new ProgressDialog(m_Ctx);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        String api = "/AppGlobal/get_satker_filter";
        String params = String.format("?tahun=%1$d&userid=%2$d", m_Global.getTahunRKA(), m_Global.getUserLoginId());
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
                                    //auto check
                                    List<String> selectedSatker = m_Global.getFilterSelectedIdSatkers_List();
                                    int totalSelected = 0;

                                    for (int i = 0; i < data.length(); i++)
                                    {
                                        JSONObject row = data.getJSONObject(i);

                                        Satker obj = new Satker();
                                        obj.SatkerId = row.getInt("idsatker");
                                        obj.KodeSatker = Tools.parseString(row.getString("kdsatker"));
                                        obj.NamaSatker = Tools.parseString(row.getString("nmsatker"));

                                        //auto check
                                        if (m_Global.getFilterRunFirst() == false) {
//                                            if (String.valueOf(obj.idSatker) == "986" || String.valueOf(obj.idSatker) == "987"){
//                                                Toast.makeText(m_Ctx,   "found", Toast.LENGTH_SHORT).show();
//                                            }
                                            if(selectedSatker.indexOf(String.valueOf(obj.SatkerId)) != -1) {
                                                obj.IsSelected = true;
                                                totalSelected += 1;
                                                tvSatkerTitle.setText("SATKER (" + totalSelected + ")");
                                            }
                                        }

                                        m_ListSatker.add(obj);
                                    }
                                    progressDialog.dismiss();
                                    m_Adapter.notifyDataSetChanged();
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
