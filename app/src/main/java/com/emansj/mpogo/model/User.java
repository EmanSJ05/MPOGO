package com.emansj.mpogo.model;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.emansj.mpogo.helper.AppGlobal;
import com.emansj.mpogo.helper.IDataCallback;
import com.emansj.mpogo.helper.Tools;
import com.emansj.mpogo.helper.VolleyErrorHelper;
import com.emansj.mpogo.helper.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class User {

    private Context m_Ctx;
    private AppGlobal m_Global;

    public int UserId;
    public String UserName;
    public String UserPass;
    public String UserType;
    public String Peran;
    public String Email;
    public String NickName;
    public String FullName;
    public String MobileNo;
    public String ImageUrl;
    public int ProvId;
    public String KodeProvinsi;
    public String NamaProvinsi;
    public int KabId;
    public String KodeKabupaten;
    public String NamaKabupaten;
    public int DinasId;
    public String NamaDinas;
    public int SatkerId;
    public String KodeSatker;
    public String NamaSatker;
    public String Satker;


//    private User() {
//        m_Global = AppGlobal.getInstance(m_Ctx);
//    }

    public User() {}

//    public User(Context ctx) {
//        this.m_Ctx = ctx;
//    }

    public void getUser(final Context ctx, final int userId, final IDataCallback methodOnSuccess){
        String api = "/User/get_user";
        String params = String.format("?userid=%1$d", userId);
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

                                    AppGlobal global = AppGlobal.getInstance(ctx);
                                    User user = new User();
                                    user.UserId = Tools.parseInt(row.getString("userid"));
                                    user.UserName = Tools.parseString(row.getString("username"));
                                    user.UserPass = global.getUserLoginPass();
                                    user.FullName = Tools.parseString(row.getString("fullname"));
                                    user.NickName = Tools.parseString(row.getString("nickname"));
                                    user.MobileNo = Tools.parseString(row.getString("usertelpno"));
                                    user.Email = Tools.parseString(row.getString("useremail"));
                                    user.ImageUrl = Tools.parseString(row.getString("userimage"));
                                    user.Peran = Tools.parseString(row.getString("peran"));
                                    user.UserType = Tools.parseString(row.getString("usertype"));
                                    user.ProvId = Tools.parseInt(row.getString("provid"));
                                    user.KodeProvinsi = Tools.parseString(row.getString("kdprov"));
                                    user.NamaProvinsi = Tools.parseString(row.getString("nmprov"));
                                    user.KabId = Tools.parseInt(row.getString("kabid"));
                                    user.KodeKabupaten = Tools.parseString(row.getString("kdkab"));
                                    user.NamaKabupaten = Tools.parseString(row.getString("nmkab"));
                                    user.DinasId = Tools.parseInt(row.getString("dinasid"));
                                    user.NamaDinas = Tools.parseString(row.getString("namadinas"));
                                    user.SatkerId = Tools.parseInt(row.getString("idsatker"));
                                    user.KodeSatker = Tools.parseString(row.getString("kdsatker"));
                                    user.NamaSatker = Tools.parseString(row.getString("nmsatker"));
                                    if (user.SatkerId != 0){
                                        String satker = user.KodeSatker + " - " + user.NamaSatker;
                                        user.Satker = satker;
                                    }

                                    if (methodOnSuccess != null) {
                                        methodOnSuccess.onSuccess((User) user);
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
                        VolleyErrorHelper.showError(error, ctx);
                    }
                }
        );
        VolleySingleton.getInstance(ctx).addToRequestQueue(request, AppGlobal.TAG);
    }
}

