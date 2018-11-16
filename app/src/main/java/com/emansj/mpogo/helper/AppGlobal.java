package com.emansj.mpogo.helper;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.emansj.mpogo.R;
import com.emansj.mpogo.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static android.content.Context.MODE_PRIVATE;

public class AppGlobal {
    //private static final AppGlobal ourInstance = new AppGlobal();
    private static volatile AppGlobal ourInstance;
    private SharedPreferences m_SharedPref;
    private Context m_Ctx;

    public static AppGlobal getInstance(Context context) {
        if (ourInstance == null)
        {
            synchronized (AppGlobal.class) {   //Check for the second time.
                //if there is no instance available... create new one
                if (ourInstance == null) ourInstance = new AppGlobal(context);
            }

            //ourInstance = new AppGlobal(context.getApplicationContext());
        }
        return ourInstance;
    }

    private AppGlobal(Context context){
        //Prevent form the reflection api.
        if (ourInstance != null){
            throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
        }
        m_Ctx = context;
        m_SharedPref = m_Ctx.getSharedPreferences("MPOGO_SETTINGS", MODE_PRIVATE);
    }

    //Static Global vars
    public static String URL_MPO = "http://mpo.psp.pertanian.go.id/";
    public static String URL_ROOT = "http://192.168.100.5:8080/mpoapi";
    //public static String URL_ROOT = "http://192.168.1.103:8080/mpoapi";
//    public static String URL_ROOT = "http://192.168.80.176:8080/mpoapi";
    //public static String URL_ROOT = "http://192.168.43.127:8080/mpoapi";
    //public static String URL_ROOT = "http://117.102.94.187:8081/mpoapi";
    private static String TAG = "MPOGO.GLOBAL";


    //GLOBAL VARS
    //user
    private int UserLoginId;
    private String UserLoginName;
    private String UserLoginPass;
    public User UserProfile;

    //app
    private int TahunRKA;                               //tahun rka sedang aktif [menu, ganti tahun rka, report filter]
    private List<String> TahunRKAList;                  //semua tahun RKA [login]
    private boolean IsRememberMe;                       //pengingat username [login]
    private boolean FilterRunFirst = true;              //runfirst untuk report, jika report dijalankan pertama kali, list satker tidak ada yang dipilih [report filter]
    private boolean FilterIsAllSatker = true;           //pilihan semua satker di laporan [report filter]
    private String FilterSelectedIdSatkers;             //satker yang terpilih di laporan [report filter]
    private List<String> FilterSelectedIdSatkers_List;  //satker yang terpilih di laporan [report filter]

    //METHODS
    public int getUserLoginId() {return UserLoginId;}
    public void setUserLoginId(int userLoginId) {
        UserLoginId = userLoginId;

        //save to pref
        SharedPreferences.Editor editor = m_SharedPref.edit();
        m_SharedPref.edit().putInt("user_login_id", userLoginId);
        m_SharedPref.edit().commit();
    }

    public String getUserLoginName() {return UserLoginName;}
    public void setUserLoginName(String userLoginName) {
        UserLoginName = userLoginName;

        //save to pref
        SharedPreferences.Editor editor = m_SharedPref.edit();
        m_SharedPref.edit().putString("user_login_name", userLoginName);
        m_SharedPref.edit().commit();
    }

    public String getUserLoginPass() {return UserLoginPass;}
    public void setUserLoginPass(String userLoginPass) {
        UserLoginPass = userLoginPass;

        //save to pref
        SharedPreferences.Editor editor = m_SharedPref.edit();
        m_SharedPref.edit().putString("user_login_pass", userLoginPass);
        m_SharedPref.edit().commit();
    }

    public boolean getIsRememberMe(){
        boolean value = m_SharedPref.getBoolean("is_remember_me", false);
        if (value == false){
            value = IsRememberMe;
        }
        return value;
    }
    public void setIsRememberMe(boolean isRememberMe){
        IsRememberMe = isRememberMe;

        //save to pref
        SharedPreferences.Editor editor = m_SharedPref.edit();
        m_SharedPref.edit().putBoolean("is_remember_me", isRememberMe);
        m_SharedPref.edit().commit();
    }

    public int getTahunRKA() {
        int tahun = m_SharedPref.getInt("tahun_rka", 0);

        if (TahunRKA == 0){ //cek TahunRKA
            if (tahun == 0) { //cek Preferences
                if (TahunRKAList != null) { //cek dari list
                    tahun = Integer.parseInt(TahunRKAList.get(0));
                    setTahunRKA(tahun);
                } else {
                    tahun = 0;
                }
            }
        }else {
            tahun = TahunRKA;
        }

        return tahun;
    }
    public void setTahunRKA(int tahunRKA) {
        TahunRKA = tahunRKA;

        //save to pref
        SharedPreferences.Editor editor = m_SharedPref.edit();
        m_SharedPref.edit().putInt("tahun_rka", tahunRKA);
        m_SharedPref.edit().commit();
    }

    public List<String> getTahunRKAList() {return TahunRKAList;}
    public void setTahunRKAList(List<String> tahunRKAList) {TahunRKAList = tahunRKAList;}

    public boolean getFilterRunFirst() {
        return FilterRunFirst;
    }
    public void setFilterRunFirst(boolean filterRunFirst) {
        FilterRunFirst = filterRunFirst;
    }

    public boolean getFilterIsAllSatker() {return FilterIsAllSatker;}
    public void setFilterIsAllSatker(boolean filterIsAllSatker) {
        FilterIsAllSatker = filterIsAllSatker;

        if (filterIsAllSatker){
            FilterSelectedIdSatkers_List = null;
        }

        //save to pref
        SharedPreferences.Editor editor = m_SharedPref.edit();
        m_SharedPref.edit().putBoolean("filter_semua_satker", filterIsAllSatker);
        m_SharedPref.edit().commit();
    }

    public String getFilterSelectedIdSatkers() {
        String str = FilterSelectedIdSatkers;
        //str = "986,987";

        if (str == null){
            str = "";
        }
        return str;
    }
    public void setFilterSelectedIdSatkers(String filterSelectedIdSatkers) {
        FilterSelectedIdSatkers = filterSelectedIdSatkers;

        //save to pref
        SharedPreferences.Editor editor = m_SharedPref.edit();
        m_SharedPref.edit().putString("filter_selected_idsatkers", filterSelectedIdSatkers);
        m_SharedPref.edit().commit();
    }

    public List<String> getFilterSelectedIdSatkers_List() {return FilterSelectedIdSatkers_List;}
    public void setFilterSelectedIdSatkers_List(List<String> filterSelectedIdSatkers_List) {
        FilterSelectedIdSatkers_List = filterSelectedIdSatkers_List;

        //save to pref
        SharedPreferences.Editor editor = m_SharedPref.edit();
        Set<String> setStr = new HashSet<String>();
        setStr.addAll(filterSelectedIdSatkers_List);
        m_SharedPref.edit().putStringSet("filter_selected_idsatkers_list", setStr);
        m_SharedPref.edit().commit();
    }

    
    //-------------------------------------------------------------------------------DATA LOAD
    public void loadInitData(){
        UserProfile = new User();
    }

    public void loadTahunRKAList(final Runnable r){
        String api = "/AppGlobal/tahunrka_list";
        String params = "";
        String url = AppGlobal.URL_ROOT + api + params;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            JSONArray jsonArray = response.getJSONArray("data");
                            List<String> list_tahunrka = new ArrayList<>();

                            for (int i = 0; i < jsonArray.length(); i++)
                            {
                                JSONObject dt = jsonArray.getJSONObject(i);
                                list_tahunrka.add(dt.getString("tahun"));
                            }

                            //save to AppGlobal
                            setTahunRKAList(list_tahunrka);

                            //save to pref
                            SharedPreferences.Editor editor = m_SharedPref.edit();
                            Set<String> setStr = new HashSet<String>();
                            setStr.addAll(list_tahunrka);
                            m_SharedPref.edit().putStringSet("tahun_rka_list", setStr);
                            m_SharedPref.edit().commit();

                            //run
                            r.run();

                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        //error.printStackTrace();
                        VolleyErrorHelper.showError(error, m_Ctx);
                    }
                }
        );
        VolleySingleton.getInstance(m_Ctx).addToRequestQueue(request, TAG);
    }

    public void loadLastLogin(){
        //set global vars from settings
        IsRememberMe = m_SharedPref.getBoolean("is_remember_me", false);
        UserLoginId = m_SharedPref.getInt("user_login_id",0);
        UserLoginName = m_SharedPref.getString("user_login_name",null);
        UserLoginPass = m_SharedPref.getString("user_login_pass",null);
        TahunRKA = m_SharedPref.getInt("tahun_rka", getTahunRKA());
    }

    public void loadUserProfile(final Runnable r){
        String api = "/AppGlobal/userprofile";
        String params = String.format("?userid=%1$d", UserLoginId);
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

                                    UserProfile = new User();
                                    UserProfile.UserId = Tools.parseInt(row.getString("userid"));
                                    UserProfile.UserName = Tools.parseString(row.getString("username"));
                                    UserProfile.UserPass = getUserLoginPass();
                                    UserProfile.FullName = Tools.parseString(row.getString("fullname"));
                                    UserProfile.NickName = Tools.parseString(row.getString("nickname"));
                                    UserProfile.MobileNo = Tools.parseString(row.getString("usertelpno"));
                                    UserProfile.Email = Tools.parseString(row.getString("useremail"));
                                    UserProfile.ImageUrl = Tools.parseString(row.getString("userimage"));
                                    UserProfile.Peran = Tools.parseString(row.getString("peran"));
                                    UserProfile.UserType = Tools.parseString(row.getString("usertype"));
                                    UserProfile.ProvId = Tools.parseInt(row.getString("provid"));
                                    UserProfile.KodeProvinsi = Tools.parseString(row.getString("kdprov"));
                                    UserProfile.NamaProvinsi = Tools.parseString(row.getString("nmprov"));
                                    UserProfile.KabId = Tools.parseInt(row.getString("kabid"));
                                    UserProfile.KodeKabupaten = Tools.parseString(row.getString("kdkab"));
                                    UserProfile.NamaKabupaten = Tools.parseString(row.getString("nmkab"));
                                    UserProfile.DinasId = Tools.parseInt(row.getString("dinasid"));
                                    UserProfile.NamaDinas = Tools.parseString(row.getString("namadinas"));
                                    UserProfile.SatkerId = Tools.parseInt(row.getString("idsatker"));
                                    UserProfile.KodeSatker = Tools.parseString(row.getString("kdsatker"));
                                    UserProfile.NamaSatker = Tools.parseString(row.getString("nmsatker"));
                                    if (UserProfile.SatkerId != 0){
                                        String satker = UserProfile.KodeSatker + " - " + UserProfile.NamaSatker;
                                        UserProfile.Satker = satker;
                                    }
                                    if (r != null) {
                                        r.run();
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


    //-------------------------------------------------------------------------------PUBLIC DATA (retrieve data from server)
    public class Data
    {

        public Data(){}

        public void cancelAllRequest(){
            VolleySingleton.getInstance(m_Ctx).getRequestQueue().cancelAll(TAG);
        }

        public void cancelRequest(String tag){
            VolleySingleton.getInstance(m_Ctx).getRequestQueue().cancelAll(tag);
        }

//        public void loadTahunRKAList(){
//            String api = "/AppGlobal/tahunrka_list";
//            String params = "";
//            String url = AppGlobal.URL_ROOT + api + params;
//
//            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
//                    new Response.Listener<JSONObject>(){
//                        @Override
//                        public void onResponse(JSONObject response) {
//                            try{
//                                JSONArray jsonArray = response.getJSONArray("data");
//                                List<String> list_tahunrka = new ArrayList<>();
//
//                                for (int i = 0; i < jsonArray.length(); i++)
//                                {
//                                    JSONObject dt = jsonArray.getJSONObject(i);
//                                    list_tahunrka.add(dt.getString("tahun"));
//                                }
//
//                                //save to AppGlobal
//                                setTahunRKAList(list_tahunrka);
//
//                                //save to pref
//                                SharedPreferences.Editor editor = m_SharedPref.edit();
//                                Set<String> setStr = new HashSet<String>();
//                                setStr.addAll(list_tahunrka);
//                                m_SharedPref.edit().putStringSet("tahun_rka_list", setStr);
//                                m_SharedPref.edit().commit();
//
//                            }catch (JSONException e){
//                                e.printStackTrace();
//                            }
//                        }
//                    },
//                    new Response.ErrorListener(){
//                        @Override
//                        public void onErrorResponse(VolleyError error){
//                            //error.printStackTrace();
//                            VolleyErrorHelper.showError(error, m_Ctx);
//                        }
//                    }
//            );
//            VolleySingleton.getInstance(m_Ctx).addToRequestQueue(request, TAG);
//
//            //m_JsonObjTemp = VolleySingleton.getInstance(m_Ctx).getRequestJSONObject("xxx", Request.Method.GET, "data");
//        }
//
//        public void loadLastLogin(){
//            //set global vars from settings
//            IsRememberMe = m_SharedPref.getBoolean("is_remember_me", false);
//            UserLoginId = m_SharedPref.getInt("user_login_id",0);
//            UserLoginName = m_SharedPref.getString("user_login_name",null);
//            UserLoginPass = m_SharedPref.getString("user_login_pass",null);
//            TahunRKA = m_SharedPref.getInt("tahun_rka", getTahunRKA());
//        }

//        public void loadUserProfile(final Runnable r){
//            String api = "/AppGlobal/userprofile";
//            String params = String.format("?userid=%1$d", UserLoginId);
//            String url = AppGlobal.URL_ROOT + api + params;
//
//            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
//                    new Response.Listener<JSONObject>(){
//                        @Override
//                        public void onResponse(JSONObject response) {
//                            try{
//                                int status = response.getInt("status");
//                                if (status == 200 )
//                                {
//                                    JSONArray data = response.getJSONArray("data");
//                                    if (data.length() > 0)
//                                    {
//                                        for (int i = 0; i < data.length(); i++)
//                                        {
//                                            JSONObject row = data.getJSONObject(i);
//
//                                            obj.UserId = Tools.parseInt(row.getString("userid"));
//                                            obj.UserName = Tools.parseString(row.getString("username"));
//                                            obj.UserPass = Tools.parseString(row.getString("userpass"));
//                                            obj.FullName = Tools.parseString(row.getString("fullname"));
//                                            obj.NickName = Tools.parseString(row.getString("nickname"));
//                                            obj.MobileNo = Tools.parseString(row.getString("usertelpno"));
//                                            obj.Email = Tools.parseString(row.getString("useremail"));
//                                            obj.ImageUrl = Tools.parseString(row.getString("userimage"));
//                                            obj.ProvId = Tools.parseInt(row.getString("provid"));
//                                            obj.Provinsi = Tools.parseString(row.getString("nmprov"));
//                                            obj.KabId = Tools.parseInt(row.getString("kabid"));
//                                            obj.Kabupaten = Tools.parseString(row.getString("nmkab"));
//                                            obj.DinasId = Tools.parseInt(row.getString("dinasid"));
//                                            obj.Dinas = Tools.parseString(row.getString("namadinas"));
//                                            obj.SatkerId = Tools.parseInt(row.getString("idsatker"));
//                                            obj.KodeSatker = Tools.parseString(row.getString("kdsatker"));
//                                            obj.Satker = Tools.parseString(row.getString("nmsatker"));
//
//                                            UserName = Tools.parseString(row.getString("username"));
//                                            UserEmail = Tools.parseString(row.getString("useremail"));
//                                            UserNickName = Tools.parseString(row.getString("nickname"));
//                                            UserFullName = Tools.parseString(row.getString("fullname"));
//                                            UserMobileNo = Tools.parseString(row.getString("usertelpno"));
//                                            UserImageUrl = Tools.parseString(row.getString("userimage"));
//                                            UserType = Tools.parseString(row.getString("usertype"));
//                                            UserProvId = row.optInt("provid");
//                                            UserKabId = row.optInt("kabid");
//                                            UserDinasId = row.optInt("dinasid");
//                                            UserSatkerId = row.optInt("idsatker");
//                                        }
//                                        r.run();
//                                    }
//                                }
//
//                            }catch (JSONException e){
//                                e.printStackTrace();
//                            }
//                        }
//                    },
//                    new Response.ErrorListener(){
//                        @Override
//                        public void onErrorResponse(VolleyError error){
//                            VolleyErrorHelper.showError(error, m_Ctx);
//                        }
//                    }
//            );
//
//            VolleySingleton.getInstance(m_Ctx).addToRequestQueue(request, TAG);
//        }

        

    }
}
