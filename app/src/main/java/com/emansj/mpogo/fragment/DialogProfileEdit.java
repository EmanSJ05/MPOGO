package com.emansj.mpogo.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.emansj.mpogo.R;
import com.emansj.mpogo.helper.AppGlobal;
import com.emansj.mpogo.helper.Tools;
import com.emansj.mpogo.helper.VolleyErrorHelper;
import com.emansj.mpogo.helper.VolleySingleton;
import com.emansj.mpogo.model.Dinas;
import com.emansj.mpogo.model.Satker;
import com.emansj.mpogo.model.User;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;


public class DialogProfileEdit extends DialogFragment {


    //Standard vars
    private static final String TAG = DialogProfileEdit.class.getSimpleName();
    private Context m_Ctx;
    private AppGlobal m_Global;

    //View vars
    private View parent_view;
    private Toolbar toolbar;
    private ImageView imvImage;
    private EditText editFullName, editNickName, editMobileNo, editEmail;
    private TextView tvProvinsi, tvKabupaten;
    private Button btnSpinnerDinas, btnSpinnerSatker;
    private FloatingActionButton fabChangeImage;


    //Custom vars
    private User m_User;
    private List<Dinas> m_ListDinas = new ArrayList<>();
    private List<Satker> m_ListSatker = new ArrayList<>();


    //---------------------------------------OVERRIDE
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        m_Ctx = this.getContext();
        parent_view = inflater.inflate(R.layout.activity_profile_edit, container, false);
        setCancelable(false);
        m_Global = AppGlobal.getInstance(m_Ctx);

        //setHasOptionsMenu(true);

        initToolbar();
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_save, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    //---------------------------------------INIT COMPONENTS & DATA
    private void initToolbar(){
        toolbar = parent_view.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Ubah Profile");
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initComponent() {
        imvImage = parent_view.findViewById(R.id.imvImage);
        editFullName = parent_view.findViewById(R.id.editFullName);
        editNickName = parent_view.findViewById(R.id.editNickName);
        editMobileNo = parent_view.findViewById(R.id.editMobileNo );
        editEmail = parent_view.findViewById(R.id.editEmail);
        tvProvinsi = parent_view.findViewById(R.id.editProvinsi);
        tvKabupaten = parent_view.findViewById(R.id.editKabupaten);
        btnSpinnerDinas = parent_view.findViewById(R.id.btnSpinnerDinas);
        btnSpinnerSatker = parent_view.findViewById(R.id.btnSpinnerSatker);
        fabChangeImage = parent_view.findViewById(R.id.fabChangeImage);

        //CHANGE IMAGE
        fabChangeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChangeImage();
            }
        });

        //SPINNER DINAS
        btnSpinnerDinas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogDinas();
            }
        });

        //SPINNER SATKER
        btnSpinnerSatker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogSatker();
            }
        });
    }

    private void initData() {
        m_User = new User();
        m_User = m_Global.UserProfile;

        Tools.displayImage(m_Ctx, imvImage, m_User.ImageUrl, R.drawable.ic_user_image_edit);
        editFullName.setText(m_User.FullName);
        editNickName.setText(m_User.NickName);
        editMobileNo.setText(m_User.MobileNo);
        editEmail.setText(m_User.Email);
        tvProvinsi.setText(m_User.NamaProvinsi);
        tvKabupaten.setText(m_User.NamaKabupaten);
        btnSpinnerDinas.setText(m_User.NamaDinas);
        btnSpinnerSatker.setText(m_User.Satker);

        getDataDinas();
        getDataSatker();
    }


    //---------------------------------------ACTIONS
    private void showChangeImage() {}

    private int index;
    private void showDialogDinas() {
        if (m_ListDinas.isEmpty() == false)
        {
            //get choice list
            ArrayList<String> x = new ArrayList<>();
            for(Dinas s : m_ListDinas){
                x.add(s.NamaDinas);
            }
            String[] namaDinas = new String[x.size()];
            namaDinas = x.toArray(namaDinas);

            //get default choice
            int defaultChoice = m_ListDinas.indexOf(m_User.DinasId);

            //init alert dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(m_Ctx);
            builder.setTitle("Pilih Dinas");
            builder.setSingleChoiceItems(namaDinas, defaultChoice, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    index = i;
                }
            });

            //init click OK
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialogInterface, int i)
                {
                    btnSpinnerDinas.setText(m_ListDinas.get(index).NamaDinas);
                    m_User.DinasId = m_ListDinas.get(index).DinasId;
                    m_User.NamaDinas = m_ListDinas.get(index).NamaDinas;
                    getDataSatker();
                }
            });

            //init click Batal
            builder.setNegativeButton("Batal", null);
            builder.show();
        }
    }

    private void showDialogSatker() {
        if (m_ListSatker.isEmpty() == false)
        {
            //get choice list
            ArrayList<String> x = new ArrayList<>();
            for(Satker s : m_ListSatker){
                x.add(s.Satker);
            }
            String[] satker = new String[x.size()];
            satker = x.toArray(satker);

            //get default choice
            int defaultChoice = m_ListSatker.indexOf(m_User.SatkerId);

            //init alert dialog
            AlertDialog.Builder builder = new AlertDialog.Builder(m_Ctx);
            builder.setTitle("Pilih Dinas");
            builder.setSingleChoiceItems(satker, defaultChoice, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    index = i;
                }
            });

            //init click OK
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialogInterface, int i)
                {
                    btnSpinnerSatker.setText(m_ListSatker.get(index).NamaSatker);
                    m_User.SatkerId = m_ListSatker.get(index).SatkerId;
                    m_User.KodeSatker = m_ListSatker.get(index).KodeSatker;
                    m_User.Satker = m_ListSatker.get(index).NamaSatker;
                }
            });

            //init click Batal
            builder.setNegativeButton("Batal", null);
            builder.show();
        }
    }

    private void save() {
    }


    //---------------------------------------CALLBACK
    private int request_code = 0;
    public CallbackResult callbackResult;

    public void setOnCallbackResult(final CallbackResult callbackResult) {
        this.callbackResult = callbackResult;
    }

    public interface CallbackResult {
        void sendResult(int requestCode, User obj);
    }

    private void sendDataResult() {
        if (callbackResult != null) {
            callbackResult.sendResult(request_code, m_User);
        }
    }

    public void setRequestCode(int request_code) {
        this.request_code = request_code;
    }


    //---------------------------------------GET DATA
    private void getDataDinas() {
        String api = "";
        String params = "";
        if (m_User.DinasId != 0) {
            api = "/appglobal/get_dinas_by_dinas";
            params = String.format("?id=%1$d", m_User.DinasId);
        }else if(m_User.ProvId != 0){
            api = "/appglobal/get_dinas_by_prov";
            params = String.format("?id=%1$d", m_User.ProvId);
        }else{
            return;
        }
        String url = AppGlobal.URL_ROOT + api + params;

        m_ListDinas = new ArrayList<Dinas>();
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

                                        Dinas dinas = new Dinas();
                                        dinas.DinasId = Tools.parseInt(row.getString("dinasid"));
                                        dinas.NamaDinas = Tools.parseString(row.getString("namadinas"));
                                        m_ListDinas.add(dinas);
                                    }
                                }
                            }

                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        VolleyErrorHelper.showError(error, m_Ctx);
                    }
                }
        );
        VolleySingleton.getInstance(m_Ctx).addToRequestQueue(request, TAG);
    }

    private void getDataSatker() {
        String api = "";
        String params = "";
        if (m_User.DinasId != 0) {
            api = "/appglobal/get_satker_by_dinas";
            params = String.format("?id=%1$d", m_User.DinasId);
        }else if(m_User.ProvId != 0){
            api = "/appglobal/get_satker_by_prov";
            params = String.format("?id=%1$d", m_User.ProvId);
        }else{
            return;
        }
        String url = AppGlobal.URL_ROOT + api + params;

        m_ListSatker = new ArrayList<Satker>();
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

                                        Satker satker = new Satker();
                                        satker.SatkerId = Tools.parseInt(row.getString("idsatker"));
                                        satker.KodeSatker = Tools.parseString(row.getString("kdsatker"));
                                        satker.NamaSatker = Tools.parseString(row.getString("nmsatker"));
                                        satker.Satker = Tools.parseString(row.getString("satker"));
                                        m_ListSatker.add(satker);
                                    }
                                }
                            }

                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        VolleyErrorHelper.showError(error, m_Ctx);
                    }
                }
        );
        VolleySingleton.getInstance(m_Ctx).addToRequestQueue(request, TAG);
    }

}
