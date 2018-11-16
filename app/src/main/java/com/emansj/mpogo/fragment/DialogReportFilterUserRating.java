package com.emansj.mpogo.fragment;

import com.balysv.materialripple.MaterialRippleLayout;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Layout;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.emansj.mpogo.helper.Tools;
import com.emansj.mpogo.helper.VolleyErrorHelper;
import com.emansj.mpogo.helper.VolleySingleton;
import com.emansj.mpogo.model.Satker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DialogReportFilterUserRating extends DialogFragment {


    //Standard vars
    private static final String TAG = DialogReportFilterUserRating.class.getSimpleName();
    private Context m_Ctx;
    private AppGlobal m_Global;
    private AppGlobal.Data m_GlobalData;

    //View vars
    private View parent_view;
    private TextView tvTahun, tvDateFrom, tvDateTo;
    private MaterialRippleLayout btnCari;
    private ImageView imgClose;

    //Custom vars
    private List<String> m_ListTahunRKA = new ArrayList<>();
    private String m_SelectedTahunRKA;
    private String m_Tahun = null;
    private String m_DateFrom = null;
    private String m_DateTo = null;


    //---------------------------------------OVERRIDE
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        m_Ctx = this.getContext();
        parent_view = inflater.inflate(R.layout.dialog_report_filter_user_rating, container, false);
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
        tvTahun = parent_view.findViewById(R.id.tvTahun);
        tvDateFrom = parent_view.findViewById(R.id.tvDateFrom );
        tvDateTo = parent_view.findViewById(R.id.tvDateTo);
        imgClose = parent_view.findViewById(R.id.imgClose);
        btnCari = parent_view.findViewById(R.id.btnCari);

        //TAHUN
        tvTahun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogTahunRKA();
            }
        });

        //PERIODE DARI
        tvDateFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogDatePicker((TextView) v);
            }
        });

        //PERIODE SAMPAI
        tvDateTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogDatePicker((TextView) v);
            }
        });

        //CARI
        btnCari.setOnClickListener(new View.OnClickListener(){
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
        //tahun
        if (m_SelectedTahunRKA == null) {
            if (m_Global.getTahunRKA() != 0) {
                m_SelectedTahunRKA =  String.valueOf(m_Global.getTahunRKA());

            }else{
                m_SelectedTahunRKA = Tools.getYearCurrent();

            }
        }
        tvTahun.setText(m_SelectedTahunRKA);

        //periode
        if (m_DateFrom == null) {
            m_DateFrom = m_SelectedTahunRKA + "0101";
            m_DateTo = m_SelectedTahunRKA + "1231";
        }
        Date dF = Tools.convertDateSTD(m_DateFrom, "yyyyMMdd");
        tvDateFrom.setText(Tools.getFormattedDate(dF.getTime(), "d MMM yyyy"));
        Date dT = Tools.convertDateSTD(m_DateTo, "yyyyMMdd");
        tvDateTo.setText(Tools.getFormattedDate(dT.getTime(), "d MMM yyyy"));
    }

    public void setData(String sDateFrom, String sDateTo){
        m_DateFrom = sDateFrom;
        m_DateTo = sDateTo;
        m_SelectedTahunRKA = sDateFrom.substring(0,4);
    }


    //---------------------------------------ACTIONS
    private String selectedTahun;
    private void showDialogTahunRKA() {
        if (m_Global.getTahunRKAList().isEmpty() == false)
        {
            //get choice list
            m_ListTahunRKA = m_Global.getTahunRKAList();
            String[] tahun = new String[m_ListTahunRKA.size()];
            tahun = m_ListTahunRKA.toArray(tahun);

            //get default choice
            int defaultChoice = m_ListTahunRKA.indexOf(tvTahun.getText());

            //init alert dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(m_Ctx);
            builder.setTitle("Pilih Tahun");
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
                    m_SelectedTahunRKA = selectedTahun;
                    tvTahun.setText(selectedTahun);
                    m_DateFrom = selectedTahun + "0101";
                    m_DateTo = selectedTahun + "1231";
                    Date dF = Tools.convertDateSTD(m_DateFrom, "yyyyMMdd");
                    tvDateFrom.setText(Tools.getFormattedDate(dF.getTime(), "d MMM yyyy"));
                    Date dT = Tools.convertDateSTD(m_DateTo, "yyyyMMdd");
                    tvDateTo.setText(Tools.getFormattedDate(dT.getTime(), "d MMM yyyy"));
                }
            });

            //init click Batal
            builder.setNegativeButton("Batal", null);
            builder.show();
        }
    }

    private void search() {
        //validation
        if (Integer.parseInt(m_DateFrom) > Integer.parseInt(m_DateTo)){
            Toast.makeText(m_Ctx, "Tidak diperbolehkan, [Periode Dari] lebih besar dari [Periode Sampai].", Toast.LENGTH_LONG).show();
            tvDateFrom.requestFocus();
            return;
        }

        //send back to activity
        sendDataResult();
        dismiss();
    }

    private void showDialogDatePicker(final TextView v) {
        Calendar cur_calender = Calendar.getInstance();
        DatePickerDialog datePicker = DatePickerDialog.newInstance(
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, monthOfYear);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        long date_ship_millis = calendar.getTimeInMillis();
                        v.setText(Tools.getFormattedDate(date_ship_millis, "d MMM yyyy"));

                        if (v.getTag().equals("DateFrom")){
                            m_DateFrom = Tools.getFormattedDate(date_ship_millis, "yyyyMMdd");
                            tvTahun.setText(m_DateFrom.substring(0,4));

                        }else{
                            m_DateTo = Tools.getFormattedDate(date_ship_millis, "yyyyMMdd");

                        }
                    }
                },
                cur_calender.get(Calendar.YEAR),
                cur_calender.get(Calendar.MONTH),
                cur_calender.get(Calendar.DAY_OF_MONTH)
        );
        //set dark light
        datePicker.setThemeDark(false);
        datePicker.setAccentColor(getResources().getColor(R.color.colorPrimary));
        //datePicker.setMinDate(cur_calender);
        datePicker.show(getActivity().getFragmentManager(), "Datepickerdialog");
    }


    //---------------------------------------CALLBACK
    private int request_code = 0;
    public CallbackResult callbackResult;

    public void setOnCallbackResult(final CallbackResult callbackResult) {
        this.callbackResult = callbackResult;
    }

    public interface CallbackResult {
        void sendResult(int requestCode, String sDateFrom, String sDateTo);
    }

    private void sendDataResult() {
        if (callbackResult != null) {
            callbackResult.sendResult(request_code, m_DateFrom, m_DateTo);
        }
    }

    public void setRequestCode(int request_code) {
        this.request_code = request_code;
    }


    //---------------------------------------GET DATA

}
