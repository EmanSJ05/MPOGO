package com.emansj.mpogo.helper;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.preference.Preference;
import android.preference.PreferenceManager;
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
import com.emansj.mpogo.model.UserOten;

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
        m_SharedPref = m_Ctx.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        AppSharedPref.init(m_Ctx);
    }

    //Static Global vars
    public static String URL_MPO = "http://mpo.psp.pertanian.go.id/";
    public static String URL_ROOT = "http://app2.psp.pertanian.go.id";
    public static String TAG = "MPOGO.GLOBAL";
    public static String PREFS_NAME = "MPOGO.SETTINGS";
    public static String SERVER_KEY =
            "AAAAwwwuA14:APA91bG1xTKNmt1lIWZLQH-lu4zKe6FzM-asEq8oVGkJ_fMn_ILzG7RKPHArJSxC6TLnt3HG6hPLkqKW1akFMogAK1yh0JcuX-Tv2jPhyLRmqHTjh99Gyv_WK0EySGG2d6ilutyAXUJq";


    //GLOBAL VARS
    //user
    private int UserLoginId;
    private String UserLoginName;
    private String UserLoginPass;
    private String UserAndroidId;
    private String UserToken;
    private String UserTopic;
    public UserOten UserLogin;
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
        AppSharedPref.write(AppSharedPref.USER_ID, userLoginId);
    }

    public String getUserLoginName() {return UserLoginName;}
    public void setUserLoginName(String userLoginName) {
        UserLoginName = userLoginName;
        AppSharedPref.write(AppSharedPref.USER_NAME, userLoginName);
    }

    public String getUserLoginPass() {return UserLoginPass;}
    public void setUserLoginPass(String userLoginPass) {
        UserLoginPass = userLoginPass;
        AppSharedPref.write(AppSharedPref.USER_PASS, userLoginPass);
    }

    public String getUserAndroidId() {return UserAndroidId;}
    public void setUserAndroidId(String userAndroidId) {
        UserAndroidId = userAndroidId;
        AppSharedPref.write(AppSharedPref.USER_ANDROID_ID, userAndroidId);
    }

    public String getUserToken() {return UserToken;}
    public void setUserToken(String userToken) {
        UserToken = userToken;
        AppSharedPref.write(AppSharedPref.USER_TOKEN, userToken);
    }

    public String getUserTopic() {return UserTopic;}
    public void setUserTopic(String userTopic) {
        UserTopic = userTopic;
        AppSharedPref.write(AppSharedPref.USER_TOPIC, userTopic);
    }

    public boolean getIsRememberMe(){
        boolean value = AppSharedPref.read(AppSharedPref.USER_REMEMBER_LOGIN, false);
        if (value == false){
            value = IsRememberMe;
        }
        return value;
    }
    public void setIsRememberMe(boolean isRememberMe){
        IsRememberMe = isRememberMe;
        AppSharedPref.write(AppSharedPref.USER_REMEMBER_LOGIN, isRememberMe);
    }

    public int getTahunRKA() {
        int tahun = AppSharedPref.read(AppSharedPref.APP_TAHUN_RKA, 0);

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
        AppSharedPref.write(AppSharedPref.APP_TAHUN_RKA, tahunRKA);
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
        AppSharedPref.write(AppSharedPref.APP_FILTER_SEMUA_SATKER, filterIsAllSatker);
    }

    public String getFilterSelectedIdSatkers() {
        String str = FilterSelectedIdSatkers;
        if (str == null){
            str = "";
        }
        return str;
    }
    public void setFilterSelectedIdSatkers(String filterSelectedIdSatkers) {
        FilterSelectedIdSatkers = filterSelectedIdSatkers;
        AppSharedPref.write(AppSharedPref.APP_FILTER_SELECTED_IDSATKERS, filterSelectedIdSatkers);
    }

    public List<String> getFilterSelectedIdSatkers_List() {return FilterSelectedIdSatkers_List;}
    public void setFilterSelectedIdSatkers_List(List<String> filterSelectedIdSatkers_List) {
        FilterSelectedIdSatkers_List = filterSelectedIdSatkers_List;

        Set<String> setStr = new HashSet<String>();
        setStr.addAll(filterSelectedIdSatkers_List);
        AppSharedPref.writeSet(AppSharedPref.APP_FILTER_SELECTED_IDSATKERS_LIST, setStr);
    }


    //-------------------------------------------------------------------------------DATA LOAD
    public void loadInitData(){
        UserProfile = new User();
    }

    public void loadLastLogin(){
        //set global vars from settings
        IsRememberMe = AppSharedPref.read(AppSharedPref.USER_REMEMBER_LOGIN, false);
        UserLoginId = AppSharedPref.read(AppSharedPref.USER_ID, 0);
        UserLoginName = AppSharedPref.read(AppSharedPref.USER_NAME, null);
        UserLoginPass = AppSharedPref.read(AppSharedPref.USER_PASS, null);
        TahunRKA = AppSharedPref.read(AppSharedPref.APP_TAHUN_RKA, getTahunRKA());
    }

    public void clearLastLogin(){
        setUserLoginId(0);
        setUserLoginName(null);
        setUserLoginPass(null);
        setUserAndroidId(null);
        setUserToken(null);
        setUserTopic(null);
        setIsRememberMe(false);
    }

    public void loadTahunRKAList(final Runnable methodOnSuccess){
        String url = AppGlobal.URL_ROOT + "/AppGlobal/get_tahun_rka";
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

                            //run
                            if (methodOnSuccess != null) {
                                methodOnSuccess.run();
                            }

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

    public void loadUserProfile(final Runnable r){
        String api = "/User/get_user";
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
    }
}
