package com.emansj.mpogo.fragment;

import android.app.Dialog;
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

import com.emansj.mpogo.R;
import com.emansj.mpogo.activity.RekeActivity;
import com.emansj.mpogo.adapter.AdapterSatkerFilter;
import com.emansj.mpogo.helper.AppGlobal;
import com.emansj.mpogo.model.Satker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.media.CamcorderProfile.get;

public class DialogReportFilter extends DialogFragment {


    //Standard vars
    private static final String TAG = DialogReportFilter.class.getSimpleName();
    private Context m_Ctx;
    private AppGlobal m_Global;
    private AppGlobal.Data m_GlobalData;

    //Define views
    private View parent_view;
    private Toolbar toolbar;
    private TextView tvTahunRKA;
    private CheckBox chkSemuaSatker;
    private EditText etFilterText;
    private ImageButton imbFilterClear;
    private RecyclerView rvList;
    private ImageView imgClose;
    private View lyTahunRKA, lySatkerTop, lyListLock, lyCari;

    //Custom vars
    private List<String> m_ListTahunRKA = new ArrayList<>();
    private String m_SelectedTahunRKA;
    private List<Satker> m_ListSatker;
    private List<Satker> m_ListSatkerSelected;
    private AdapterSatkerFilter m_Adapter;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        m_Ctx = this.getContext();
        parent_view = inflater.inflate(R.layout.dialog_report_filter, container, false);
        setCancelable(false);
        m_Global = AppGlobal.getInstance(m_Ctx);
        m_GlobalData = m_Global.new Data();

        //initToolbar();
        initComponent();
        //initData();

        return parent_view;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    private void initComponent() {
        tvTahunRKA = parent_view.findViewById(R.id.tvTahunRKA);
        chkSemuaSatker = parent_view.findViewById(R.id.chkSemuaSatker);
        etFilterText = parent_view.findViewById(R.id.etFilterText);
        imbFilterClear = parent_view.findViewById(R.id.imbFilterClear);
        imgClose = parent_view.findViewById(R.id.imgClose);
        lyTahunRKA = parent_view.findViewById(R.id.lyTahunRKA);
        lySatkerTop = parent_view.findViewById(R.id.lySatkerTop);
        lyListLock = parent_view.findViewById(R.id.lyListLock);
        lyCari = parent_view.findViewById(R.id.lyCari);

        //LIST
        rvList = (RecyclerView) parent_view.findViewById(R.id.rvList);
        rvList.setLayoutManager(new LinearLayoutManager(m_Ctx));
        rvList.setHasFixedSize(true);
        rvList.setNestedScrollingEnabled(false);

        //LIST - ADAPTER - LISTENER
        m_ListSatker = Satker.getSatker(m_Ctx);
        m_Adapter = new AdapterSatkerFilter(m_Ctx, m_ListSatker);
        rvList.setAdapter(m_Adapter);

        //TAHUN RKA
        tvTahunRKA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogTahunRKA();
            }
        });

        //FILTER TEXT - FOCUS
        etFilterText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    lyTahunRKA.setVisibility(View.GONE);
                    lySatkerTop.setVisibility(View.GONE);
                }else{
                    lyTahunRKA.setVisibility(View.VISIBLE);
                    lySatkerTop.setVisibility(View.VISIBLE);
                }
            }
        });

        //FILTER TEXT - FOCUS
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
                    ((RekeActivity)getActivity()).hideKeyboard(view);
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
                Search();
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
        tvTahunRKA = parent_view.findViewById(R.id.tvTahunRKA);
        chkSemuaSatker = parent_view.findViewById(R.id.chkSemuaSatker);
        etFilterText = parent_view.findViewById(R.id.etFilterText);
        imbFilterClear = parent_view.findViewById(R.id.imbFilterClear);
        imgClose = parent_view.findViewById(R.id.imgClose);
        lyTahunRKA = parent_view.findViewById(R.id.lyTahunRKA);
        lySatkerTop = parent_view.findViewById(R.id.lySatkerTop);
        lyListLock = parent_view.findViewById(R.id.lyListLock);
        lyCari = parent_view.findViewById(R.id.lyCari);

    }

    private void showDialogTahunRKA() {
        if (m_Global.getTahunRKA_List().isEmpty() == false)
        {
            m_ListTahunRKA = m_Global.getTahunRKA_List();
            String[] tahun = new String[m_ListTahunRKA.size()];
            tahun = m_ListTahunRKA.toArray(tahun);

            String selectedTahunRKA = String.valueOf(m_Global.getTahunRKA());

            int selectedTahunRKAIndex = Arrays.binarySearch(m_ListTahunRKA.toArray(), selectedTahunRKA);


            //init alert dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(m_Ctx);
            builder.setTitle("Pilih Tahun RKA");
            builder.setSingleChoiceItems(tahun, selectedTahunRKAIndex, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    m_SelectedTahunRKA = m_ListTahunRKA.get(i);
                }
            });

            //init click OK
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialogInterface, int i)
                {
                    tvTahunRKA.setText(m_SelectedTahunRKA);
                    m_Global.setTahunRKA(Integer.parseInt(m_SelectedTahunRKA));
                    Snackbar.make(parent_view, "selected : " + m_Global.getTahunRKA(), Snackbar.LENGTH_SHORT).show();
                }
            });

            //init click Batal
            builder.setNegativeButton("Batal", null);
            builder.show();
        }
    }

    private void Search() {
//        Toast.makeText(m_Ctx, "Search", Toast.LENGTH_SHORT).show();
//
//        StringBuilder sb=null;
//        sb=new StringBuilder();
//
//        int i=0;
//        do {
//            Satker spiritualTeacher = m_Adapter.m_ItemsChecked.get(i);
//            sb.append(spiritualTeacher.kdSatker);
//            if(i != m_Adapter.m_ItemsChecked.size()-1){
//                sb.append("\n");
//            }
//            i++;
//
//        }while (i < m_Adapter.m_ItemsChecked.size());
//
//        if(m_Adapter.m_ItemsChecked.size()>0)
//        {
//            Toast.makeText(m_Ctx, sb.toString(),Toast.LENGTH_SHORT).show();
//        }else
//        {
//            Toast.makeText(m_Ctx,"Please Check An Item First", Toast.LENGTH_SHORT).show();
//        }

        sendDataResult();
        dismiss();
    }

    //CALLBACK ------------------------------------------------------------------------------------------------------
    private int request_code = 0;
    public CallbackResult callbackResult;

    public void setOnCallbackResult(final CallbackResult callbackResult) {
        this.callbackResult = callbackResult;
    }

    public interface CallbackResult {
        void sendResult(int requestCode, int tahunRKA, boolean semuaSatker, List<Integer> idSatkers);
    }

    private void sendDataResult() {
        int tahunRKA = 2018;
        boolean semuaSatker = chkSemuaSatker.isChecked();
        List<Integer> idSatkers = m_Adapter.getSelectedItem();

        if (callbackResult != null) {
            callbackResult.sendResult(request_code, tahunRKA, semuaSatker, idSatkers);
        }
    }

    public void setRequestCode(int request_code) {
        this.request_code = request_code;
    }
    //CALLBACK ------------------------------------------------------------------------------------------------------
}
