package com.emansj.mpogo.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.v7.widget.Toolbar;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.emansj.mpogo.R;
import com.emansj.mpogo.adapter.AdapterUserRating;
import com.emansj.mpogo.fragment.DialogProfileEdit;
import com.emansj.mpogo.helper.AppGlobal;
import com.emansj.mpogo.helper.ExceptionHandler;
import com.emansj.mpogo.helper.Tools;
import com.emansj.mpogo.helper.VolleySingleton;
import com.emansj.mpogo.model.User;
import com.mikhaellopez.circularimageview.CircularImageView;


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
        civUserImage = findViewById(R.id.civUserImage);
        fabChangePhoto = findViewById(R.id.fabChangePhoto);
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
            Tools.displayImage(m_Ctx, civUserImage, imgUrl, R.drawable.img_no_available_user_photo);
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

    private void displayBackResult(User obj) {
        tvFullName.setText(obj.FullName);
        tvNickName.setText(obj.NickName);
        tvUserTelpNo.setText(obj.MobileNo);
        tvUserEmail.setText(obj.Email);
        tvProvinsi.setText(obj.NamaProvinsi);
        tvKabupaten.setText(obj.NamaKabupaten);
        tvDinas.setText(obj.NamaDinas);
        if (obj.SatkerId != 0){
            String namaSatker = obj.KodeSatker + " - " + obj.Satker;
            tvSatker.setText(namaSatker);
        }else{
            tvSatker.setText(obj.Satker);
        }

        String imgUrl = obj.ImageUrl;
        if (imgUrl != null) {
            Tools.displayImage(m_Ctx, civUserImage, imgUrl, R.drawable.img_no_available_user_photo);
        }
    }

    public void hideKeyboard(View view) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);

        if (view != null) {
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    //---------------------------------------GET DATA
}
