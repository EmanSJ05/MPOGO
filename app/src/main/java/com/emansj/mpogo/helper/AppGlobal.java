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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static android.content.Context.MODE_PRIVATE;

public class AppGlobal {
    //private static final AppGlobal ourInstance = new AppGlobal();
    private static volatile AppGlobal ourInstance;
    private SharedPreferences m_SharedPref;
    private Context m_Ctx;
    private ImageLoader imageLoader;
    private JSONObject m_JsonObjTemp;

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
    //public static String URL_ROOT = "http://192.168.100.5:8080/mpogo";
    //public static String URL_ROOT = "http://192.168.80.176:8080/mpogo";
    //public static String URL_ROOT = "http://192.168.43.127:8080/mpogo";
    public static String URL_ROOT = "http://117.102.94.187:8081/mpoapi";
    public static String URL_LOGIN = URL_ROOT + "/user/signin";
    private static String TAG = "MPOGO.GLOBAL";


    //GLOBAL VARS
    //user
    private int UserId;
    private String UserName;
    private String UserPass;
    private String UserEmail;
    private String UserNickName;
    private String UserFullName;
    private String UserMobileNo;
    private String UserImageUrl;
    private Drawable UserImageDraw;
    private String UserType;
    private int UserProvId;
    private int UserKabId;
    private int UserDinasId;
    private int UserSatkerId;

    //app
    private int TahunRKA;                               //tahun rka sedang aktif [menu, ganti tahun rka, report filter]
    private List<String> TahunRKA_List;                 //semua tahun RKA [login]
    //private List<Integer> SatkerId_List;              //satker bawahan user [login, ganti tahun rka, report filter]
    private boolean IsRememberMe;                       //pengingat username [login]
    private boolean FilterRunFirst = true;              //runfirst untuk report, jika report dijalankan pertama kali, list satker tidak ada yang dipilih [report filter]
    private boolean FilterIsAllSatker = true;           //pilihan semua satker di laporan [report filter]
    private String FilterSelectedIdSatkers;             //satker yang terpilih di laporan [report filter]
    private List<String> FilterSelectedIdSatkers_List;  //satker yang terpilih di laporan [report filter]

    //METHODS
    public int getUserId() {return UserId;}
    public void setUserId(int userId) {UserId = userId;}

    public String getUserName() {return UserName;}
    public void setUserName(String userName) {UserName = userName;}

    public String getUserPass() {return UserPass;}
    public void setUserPass(String userPass) {UserPass = userPass;}

    public String getUserEmail() {return UserEmail;}
    public void setUserEmail(String userEmail) {UserEmail = userEmail;}

    public String getUserNickName() {return UserNickName;}
    public void setUserNickName(String userNickName) {UserNickName = userNickName;}

    public String getUserFullName() {return UserFullName;}
    public void setUserFullName(String userFullName) {UserFullName = userFullName;}

    public String getUserMobileNo() {return UserMobileNo;}
    public void setUserMobileNo(String userMobileNo) {UserMobileNo = userMobileNo;}

    public String getUserImageUrl() {return UserImageUrl;}
    public void setUserImageUrl(String userImageUrl) {UserImageUrl = userImageUrl;}

    public Drawable getUserImageDrawable() {
        return UserImageDraw;
    }

    public void setUserImageDrawable() {
        ImageView imgView = null;

        Tools.displayImage(m_Ctx, imgView, UserImageUrl);
        //imgView.invalidate();
        Drawable drw = imgView.getDrawable();
        UserImageDraw = drw;
    }

    public String getUserType() {return UserType;}
    public void setUserType(String userType) {UserType = userType;}

    public int getUserProvId() {return UserProvId;}
    public void setUserProvId(int userProvId) {UserProvId = userProvId;}

    public int getUserKabId() {return UserKabId;}
    public void setUserKabId(int userKabId) {UserKabId = userKabId;}

    public int getUserDinasId() {return UserDinasId;}
    public void setUserDinasId(int userDinasId) {UserDinasId = userDinasId;}

    public int getUserSatkerId() {return UserSatkerId;}
    public void setUserSatkerId(int userSatkerId) {UserSatkerId = userSatkerId;}

    public int getTahunRKA() {
        int tahun = m_SharedPref.getInt("tahunrka", 0);

        if (TahunRKA == 0){ //cek TahunRKA
            if (tahun == 0) { //cek Preferences
                if (TahunRKA_List != null) { //cek dari list
                    tahun = Integer.parseInt(TahunRKA_List.get(0));
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
        m_SharedPref.edit().putInt("tahunrka", tahunRKA);
        m_SharedPref.edit().commit();
    }

    public List<String> getTahunRKA_List() {return TahunRKA_List;}
    public void setTahunRKA_List(List<String> tahunRKA_List) {TahunRKA_List = tahunRKA_List;}

    //public List<Integer> getSatkerId_List() {return SatkerId_List;}
    //public void setSatkerId_List(List<Integer> satkerId_List) {SatkerId_List = satkerId_List;}

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

        public void loadTahunRKA_List(){
            String url = URL_ROOT + "/AppGlobal/tahunrka_list";

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
                                setTahunRKA_List(list_tahunrka);

                                //save to pref
                                SharedPreferences.Editor editor = m_SharedPref.edit();
                                Set<String> setStr = new HashSet<String>();
                                setStr.addAll(list_tahunrka);
                                m_SharedPref.edit().putStringSet("tahunrka_list", setStr);
                                m_SharedPref.edit().commit();

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

            //m_JsonObjTemp = VolleySingleton.getInstance(m_Ctx).getRequestJSONObject("xxx", Request.Method.GET, "data");
        }

        public void loadLastLogin(){
            //set global vars from settings
            IsRememberMe = m_SharedPref.getBoolean("is_remember_me", false);
            UserId = m_SharedPref.getInt("userid",0);
            UserName = m_SharedPref.getString("username",null);
            UserPass = m_SharedPref.getString("userpass",null);
            TahunRKA = m_SharedPref.getInt("tahunrka", getTahunRKA());
        }

        public void loadUserProfile(final Runnable r){
            String url = String.format(URL_ROOT + "/AppGlobal/userprofile?userid=%1$s", UserId);

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

                                            UserName = row.getString("username");
                                            UserEmail = row.getString("useremail");
                                            UserNickName = row.getString("nickname");
                                            UserFullName = row.getString("fullname");
                                            UserMobileNo = row.getString("usertelpno");
                                            UserImageUrl = row.getString("userimage");
                                            UserType = row.getString("usertype");
                                            UserProvId = row.optInt("provid");
                                            UserKabId = row.optInt("kabid");
                                            UserDinasId = row.optInt("dinasid");
                                            UserSatkerId = row.optInt("idsatker");

                                            //setUserImageDrawable();
                                        }
                                        r.run();
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
}
