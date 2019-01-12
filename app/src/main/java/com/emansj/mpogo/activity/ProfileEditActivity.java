package com.emansj.mpogo.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.emansj.mpogo.BuildConfig;
import com.emansj.mpogo.R;
import com.emansj.mpogo.helper.AppGlobal;
import com.emansj.mpogo.helper.ExceptionHandler;
import com.emansj.mpogo.helper.Tools;
import com.emansj.mpogo.helper.VolleyErrorHelper;
import com.emansj.mpogo.helper.VolleySingleton;
import com.emansj.mpogo.model.Dinas;
import com.emansj.mpogo.model.Satker;
import com.emansj.mpogo.model.User;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;
import net.gotev.uploadservice.UploadService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ProfileEditActivity extends AppCompatActivity {


    //Standard vars
    private static final String TAG = ProfileEditActivity.class.getSimpleName();
    private Context m_Ctx;
    private AppGlobal m_Global;
    private AppGlobal.Data m_GlobalData;
    private ProgressDialog m_PDialog;

    //View vars
    private View parent_view;
    private Toolbar toolbar;
    private ImageView imvImage;
    private EditText editFullName, editNickName, editMobileNo, editEmail, editProvinsi, editKabupaten, editDinas, editSatker;
    private FloatingActionButton fabChangeImage;


    //Custom vars
    private User m_User;
    private List<Dinas> m_ListDinas = new ArrayList<>();
    private List<Satker> m_ListSatker = new ArrayList<>();


    // upload
    private int GALLERY = 1, CAMERA = 2;
    private Uri filePath;
    private static final int STORAGE_PERMISSION_CODE = 123;
    Bitmap FixBitmap;

    //---------------------------------------OVERRIDE
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Tools.darkenStatusBar(this, R.color.grey_10);
        setContentView(R.layout.activity_profile_edit);

        //Activity settings
        parent_view = findViewById(android.R.id.content);
        m_Ctx = ProfileEditActivity.this;
        m_Global = AppGlobal.getInstance(m_Ctx);
        m_GlobalData = m_Global.new Data();
        m_User = new User();
        m_PDialog = new ProgressDialog(m_Ctx);
        m_PDialog.setCancelable(false);

        initToolbar();
        initComponent();
        initData();

        UploadService.NAMESPACE = BuildConfig.APPLICATION_ID;
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
        getMenuInflater().inflate(R.menu.menu_save, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else if(item.getItemId() == R.id.action_save) {
            save();
        }else {
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
        ((TextView) parent_view.findViewById(R.id.tvToolbarTitle)).setText("Ubah Profil");
    }

    @TargetApi(Build.VERSION_CODES.M) // handle call requires api level 23
    private void initComponent() {
        imvImage = parent_view.findViewById(R.id.imvImage);
        editFullName = parent_view.findViewById(R.id.editFullName);
        editNickName = parent_view.findViewById(R.id.editNickName);
        editMobileNo = parent_view.findViewById(R.id.editMobileNo );
        editEmail = parent_view.findViewById(R.id.editEmail);
        editProvinsi = parent_view.findViewById(R.id.editProvinsi);
        editKabupaten = parent_view.findViewById(R.id.editKabupaten);
        editDinas = parent_view.findViewById(R.id.editDinas);
        editSatker = parent_view.findViewById(R.id.editSatker);
        fabChangeImage = parent_view.findViewById(R.id.fabChangeImage);

        //CHANGE IMAGE
        fabChangeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePhotoFromGallery();
                if (ContextCompat.checkSelfPermission(ProfileEditActivity.this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    if (Build.VERSION.SDK_INT != Build.VERSION_CODES.M) {
                        requestPermissions(new String[]{Manifest.permission.CAMERA}, 5);
                    }
                }
            }
        });

        //SPINNER DINAS
        editDinas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogDinas();
            }
        });

        //SPINNER SATKER
        editSatker.setOnClickListener(new View.OnClickListener() {
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
        editProvinsi.setText(m_User.NamaProvinsi);
        editKabupaten.setText(m_User.NamaKabupaten);
        editDinas.setText(m_User.NamaDinas);
        editSatker.setText(m_User.Satker);

        getDataDinas();
        getDataSatker();
    }


    //---------------------------------------ACTIONS
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
                    editDinas.setText(m_ListDinas.get(index).NamaDinas);
                    m_User.DinasId = m_ListDinas.get(index).DinasId;
                    m_User.NamaDinas = m_ListDinas.get(index).NamaDinas;
                    getDataSatker();
                }
            });

            //init click Batal
            builder.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    editDinas.setText("");
                    m_User.DinasId = 0;
                    m_User.NamaDinas = null;
                    getDataSatker();
                }
            });
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
            builder.setTitle("Pilih Satker");
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
                    editSatker.setText(m_ListSatker.get(index).NamaSatker);
                    m_User.SatkerId = m_ListSatker.get(index).SatkerId;
                    m_User.KodeSatker = m_ListSatker.get(index).KodeSatker;
                    m_User.Satker = m_ListSatker.get(index).NamaSatker;
                }
            });

            //init click Batal
            builder.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    editSatker.setText("");
                    m_User.SatkerId = 0;
                    m_User.KodeSatker = null;
                    m_User.Satker = null;
                }
            });
            builder.show();
        }
    }

    private void save() {
        m_PDialog.setMessage("Menyimpan data...");
        showDialog();

        String url = m_Global.URL_ROOT + "/User/save_user";
        StringRequest strReq = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        hideDialog();

                        try {
                            JSONObject jsonObj = new JSONObject(response);
                            int status = jsonObj.getInt("status");

                            if (status == 200) { //login succeed
                                Toast.makeText(getApplicationContext(), "Data tersimpan", Toast.LENGTH_LONG).show();
                                finish();

                            }else{
                                String errorMsg = jsonObj.getString("message");
                                Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyErrorHelper.showError(error, m_Ctx);
                        hideDialog();
                    }
                }
        )
        {
            // kirim parameter ke server
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                String fullname = editFullName.getText().toString();
                String nickname = editNickName.getText().toString();
                String userid = Integer.toString(m_Global.getUserLoginId());
                String usertelpno = editMobileNo.getText().toString();
                String useremail = editEmail.getText().toString();
                String dinasid = Integer.toString(m_User.DinasId);
                String idsatker = Integer.toString(m_User.SatkerId);

                Map<String, String> params = new HashMap<String, String>();
                params.put("userid", userid);
                params.put("nickname", nickname);
                params.put("fullname", fullname);
                params.put("usertelpno", usertelpno);
                params.put("useremail", useremail);
                params.put("dinasid", dinasid);
                params.put("idsatker", idsatker);
                return params;
            }
        };
        VolleySingleton.getInstance(m_Ctx).addToRequestQueue(strReq, TAG);
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
            api = "/AppGlobal/get_dinas_by_dinas";
            params = String.format("?id=%1$d", m_User.DinasId);
        }else if(m_User.ProvId != 0){
            api = "/AppGlobal/get_dinas_by_prov";
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
            api = "/AppGlobal/get_satker_by_dinas";
            params = String.format("?id=%1$d", m_User.DinasId);
        }else if(m_User.ProvId != 0){
            api = "/AppGlobal/get_satker_by_prov";
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

    private void showDialog() {
        if (!m_PDialog.isShowing())
            m_PDialog.show();
    }

    private void hideDialog() {
        if (m_PDialog.isShowing())
            m_PDialog.dismiss();
    }


    //--------------------------- UPLOAD
    public void choosePhotoFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, GALLERY);
    }

    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }

    public void uploadMultipart() {
        try {
            String path = getPath(filePath);
            String UploadUrl = m_Global.URL_MPO + "/ApiProfile/doUploadProfilePicture/";
            m_PDialog.setMessage("Authenticating...");
            showDialog();
            String uploadId =
                    new MultipartUploadRequest(m_Ctx, UploadUrl)
                            // starting from 3.1+, you can also use content:// URI string instead of absolute file
                            .addFileToUpload(path, "image")
                            .addParameter("userid", Integer.toString(m_Global.getUserLoginId()))
                            .setNotificationConfig(new UploadNotificationConfig())
                            .setMaxRetries(2)
                            .startUpload();
            Toast.makeText(ProfileEditActivity.this, "Update Profile berhasil!", Toast.LENGTH_SHORT).show();

            hideDialog();
        } catch (Exception exc) {
            Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public String getPath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == this.RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    FixBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    imvImage.setImageBitmap(FixBitmap);

                    filePath = data.getData();
                    uploadMultipart();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(ProfileEditActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == CAMERA) {
            FixBitmap = (Bitmap) data.getExtras().get("data");
            imvImage.setImageBitmap(FixBitmap);
            filePath = data.getData();
        }
    }
}
