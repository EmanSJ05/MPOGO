package com.emansj.mpogo.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.v7.widget.Toolbar;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.emansj.mpogo.BuildConfig;
import com.emansj.mpogo.R;
import com.emansj.mpogo.adapter.AdapterUserRating;
import com.emansj.mpogo.helper.AppGlobal;
import com.emansj.mpogo.helper.ExceptionHandler;
import com.emansj.mpogo.helper.Tools;
import com.emansj.mpogo.helper.VolleySingleton;
import com.emansj.mpogo.model.User;
import com.mikhaellopez.circularimageview.CircularImageView;


/*begin::upload file*/
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadNotificationConfig;
import net.gotev.uploadservice.UploadService;
import net.gotev.uploadservice.UploadStatusDelegate;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.net.ssl.HttpsURLConnection;
/*end:upload file*/

public class ProfileActivity extends AppCompatActivity {

    //Standard vars
    private static final String TAG = ProfileActivity.class.getSimpleName();
    private Context m_Ctx;
    private AppGlobal m_Global;
    private AppGlobal.Data m_GlobalData;

    //View vars
    private View parent_view;
    private Toolbar toolbar;
    private FloatingActionButton fabChangePhoto;
    private TextView    tvNickName, tvFullName, tvUserTelpNo, tvUserEmail,
            tvProvinsi, tvKabupaten, tvDinas, tvSatker;
    private CircularImageView civUserImage;

    //Custom vars
    private User m_User;
    private AdapterUserRating m_Adapter;
    public static final int DIALOG_QUEST_CODE = 300;
    int userid;

    // upload file
    FloatingActionButton GetImageFromGalleryButton;
    private CircularImageView ShowSelectedImage;

    Bitmap FixBitmap;
    ProgressDialog progressDialog ;
    private ProgressDialog m_PDialog;
    ByteArrayOutputStream byteArrayOutputStream ;
    boolean check = true;
    private int GALLERY = 1, CAMERA = 2;
    private Uri filePath;
    private static final int STORAGE_PERMISSION_CODE = 123;
    //  upload file


    //---------------------------------------OVERRIDE
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        setContentView(R.layout.activity_profile);

        //Activity settings
        parent_view = findViewById(android.R.id.content);
        m_Ctx = ProfileActivity.this;
        m_Global = AppGlobal.getInstance(m_Ctx);
        m_GlobalData = m_Global.new Data();
        m_User = new User();
        userid = m_User.UserId;

        m_PDialog = new ProgressDialog(m_Ctx);
        m_PDialog.setCancelable(false);

        initToolbar();
        initComponent();
        initData();
        handleUpload();
        requestStoragePermission();

        UploadService.NAMESPACE = BuildConfig.APPLICATION_ID;
    }

    /*begin::upload file*/
    @TargetApi(Build.VERSION_CODES.M) // handle call requires api level 23
    private void handleUpload(){
        GetImageFromGalleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //showPictureDialog();
                choosePhotoFromGallery();
            }
        });

        if (ContextCompat.checkSelfPermission(ProfileActivity.this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT != Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, 5);
            }
        }
        /*end:: upload file*/
    }

    /*private void showPictureDialog(){
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Photo Gallery",
                "Camera" };
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallary();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }*/
    public void choosePhotoFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, GALLERY);
    }

    /*private void takePhotoFromCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);
    }*/

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
                    ShowSelectedImage.setImageBitmap(FixBitmap);

                    filePath = data.getData();
                    uploadMultipart();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(ProfileActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == CAMERA) {
            FixBitmap = (Bitmap) data.getExtras().get("data");
            ShowSelectedImage.setImageBitmap(FixBitmap);
            filePath = data.getData();
            //uploadMultipart();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 5) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Now user should be able to use camera
            }
            else {
                Toast.makeText(ProfileActivity.this, "Unable to use Camera..Please Allow us to use Camera", Toast.LENGTH_LONG).show();
            }
        }

        if (requestCode == STORAGE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
                Toast.makeText(this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
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
            m_PDialog.setMessage("Upload...");
            showDialog();
            String uploadId =
                    new MultipartUploadRequest(m_Ctx, UploadUrl)
                            // starting from 3.1+, you can also use content:// URI string instead of absolute file
                            .addFileToUpload(path, "image")
                            .addParameter("userid", Integer.toString(m_Global.getUserLoginId()))
                            .setNotificationConfig(new UploadNotificationConfig())
                            .setMaxRetries(2)
                            .startUpload();
            Toast.makeText(ProfileActivity.this, "Update Profile berhasil!", Toast.LENGTH_SHORT).show();

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
    /*end::upload file*/

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
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else if(item.getItemId() == R.id.action_edit) {
            showEditProfile();
        } else if(item.getItemId() == R.id.action_change_password) {
            showEditPassword();
        }
        else {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

        //LOAD USER PROFILE
        Runnable r = new Runnable() {
            @Override
            public void run() {
                initData();
            }
        };
        m_Global.loadUserProfile(r);
    }

    //---------------------------------------INIT COMPONENTS & DATA
    private void initToolbar(){
        toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initComponent(){
        //Init view
        tvNickName = findViewById(R.id.tvNickName);
        tvFullName = findViewById(R.id.tvFullName);
        tvUserTelpNo = findViewById(R.id.tvUserTelpNo);
        tvUserEmail = findViewById(R.id.tvUserEmail);
        tvProvinsi = findViewById(R.id.tvProvinsi);
        tvKabupaten = findViewById(R.id.tvKabupaten);
        tvDinas = findViewById(R.id.tvDinas);
        tvSatker = findViewById(R.id.tvSatker);

        /*begin::upload file*/
        ShowSelectedImage = findViewById(R.id.civUserImage);
        GetImageFromGalleryButton = findViewById(R.id.fabChangePhoto);
        byteArrayOutputStream = new ByteArrayOutputStream();
        /*end::upload file*/

    }

    private void initData() {
        tvFullName.setText(m_Global.UserProfile.FullName);
        tvNickName.setText(m_Global.UserProfile.NickName);
        tvUserTelpNo.setText(m_Global.UserProfile.MobileNo);
        tvUserEmail.setText(m_Global.UserProfile.Email);
        tvProvinsi.setText(m_Global.UserProfile.NamaProvinsi);
        tvKabupaten.setText(m_Global.UserProfile.NamaKabupaten);
        tvDinas.setText(m_Global.UserProfile.NamaDinas);
        tvSatker.setText(m_Global.UserProfile.Satker);

        String imgUrl = m_Global.UserProfile.ImageUrl;
        if (imgUrl != null) {
            Tools.displayImage(m_Ctx, ShowSelectedImage, imgUrl, R.drawable.img_no_available_user_photo);
        }
    }


    //---------------------------------------ACTIONS
    private void showEditProfile() {
//        FragmentManager fm = getSupportFragmentManager();
//        DialogProfileEdit newFragment = new DialogProfileEdit();
//        newFragment.setRequestCode(DIALOG_QUEST_CODE);
//
//        FragmentTransaction ft = fm.beginTransaction();
//        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//        ft.add(android.R.id.content, newFragment, "DialogEditProfile").addToBackStack(null).commit();
//
//        newFragment.setOnCallbackResult(new DialogProfileEdit.CallbackResult() {
//            @Override
//            public void sendResult(int requestCode, User obj) {
//                if (requestCode == DIALOG_QUEST_CODE) {
//                    displayBackResult((User) obj);
//                }
//            }
//        });
        Intent i = new Intent(m_Ctx, ProfileEditActivity.class);
        startActivity(i);
    }

    private void showEditPassword() {
    }


    //---------------------------------------GET DATA


    /*begin::pick image*/

    /*end:pick image*/

    private void showDialog() {
        if (!m_PDialog.isShowing())
            m_PDialog.show();
    }

    private void hideDialog() {
        if (m_PDialog.isShowing())
            m_PDialog.dismiss();
    }
}
