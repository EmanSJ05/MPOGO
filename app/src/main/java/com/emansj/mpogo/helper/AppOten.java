package com.emansj.mpogo.helper;

import android.app.ProgressDialog;
import android.content.Context;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AppOten {

    //Standard vars
    private static final String TAG = AppOten.class.getSimpleName();
    private Context m_Ctx;
    private AppGlobal m_Global;
    private String m_Token;

    //Custom vars
//    private int m_UserId;
//    private String m_DeviceId;
//    private String m_Token;
//    private String m_Topic;


    public AppOten(Context ctx) {
        this.m_Ctx = ctx ;
        this.m_Global = AppGlobal.getInstance(m_Ctx);
    }


//    public void getTokenAndLogin(){
//        FirebaseInstanceId.getInstance().getInstanceId()
//                .addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
//                    @Override
//                    public void onSuccess(InstanceIdResult instanceIdResult) {
//                        m_Token = instanceIdResult.getToken();
//
//                        //call success method
//                        if (logIn != null) {
//                            callMethodSuccess.run();
//                        }
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        m_Token = null;
//                    }
//                });
//    }


        public void logIn(final String userName, final String password,
                      final Runnable callMethodSuccess, final Runnable callMethodFail) {

        String url = AppGlobal.URL_ROOT + "/Oten/login" ;
        StringRequest strReq = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObj = new JSONObject(response);
                            int status = jsonObj.getInt("status");

                            if (status == 200) { //login succeed
                                final String userId = jsonObj.getString("userid");
                                boolean firstLogin = jsonObj.getBoolean("first_login");

                                //set to AppGlobal
                                m_Global.setUserLoginId(Tools.parseInt(userId));
                                m_Global.setUserLoginName(userName);
                                m_Global.setUserLoginPass(password);

                                if (firstLogin) {
                                    //register token
                                    FirebaseInstanceId.getInstance().getInstanceId()
                                        .addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
                                            @Override
                                            public void onSuccess(InstanceIdResult instanceIdResult) {
                                                //register token to API & subscribe topic to FCM
                                                String token = instanceIdResult.getToken();
                                                m_Global.setUserToken(token);

                                                String android_id = Settings.Secure.getString(m_Ctx.getContentResolver(),
                                                        Settings.Secure.ANDROID_ID);
                                                m_Global.setUserAndroidId(android_id);

                                                RegisterToken(userId, android_id, token, callMethodSuccess, callMethodFail);
                                            }
                                        });
                                } else {
                                    //update token
                                    FirebaseInstanceId.getInstance().getInstanceId()
                                        .addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
                                            @Override
                                            public void onSuccess(InstanceIdResult instanceIdResult) {
                                                //update token to API
                                                String token = instanceIdResult.getToken();
                                                m_Global.setUserToken(token);

                                                String android_id = Settings.Secure.getString(m_Ctx.getContentResolver(),
                                                        Settings.Secure.ANDROID_ID);
                                                m_Global.setUserAndroidId(android_id);

                                                UpdateToken(token, callMethodSuccess, callMethodFail);
                                            }
                                        });
                                }

                            } else {
                                String errorMsg = jsonObj.getString("message");
                                Toast.makeText(m_Ctx, errorMsg, Toast.LENGTH_LONG).show();
                                if (callMethodFail != null) {
                                    callMethodFail.run();
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(m_Ctx, "Login error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                            if (callMethodFail != null) {
                                callMethodFail.run();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyErrorHelper.showError(error, m_Ctx);
                        if (callMethodFail != null) {
                            callMethodFail.run();
                        }
                    }
                }
        ) {
            // kirim parameter ke server
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", userName);
                params.put("password", password);
                return params;
            }
        };
        VolleySingleton.getInstance(m_Ctx).addToRequestQueue(strReq, TAG);
    }

    public void RegisterToken(final String userId, final String androidId, final String token,
                              final Runnable callMethodSuccess, final Runnable callMethodFail) {
        String url = AppGlobal.URL_ROOT + "/Oten/register_token";
        StringRequest strReq = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObj = new JSONObject(response);
                            int status = jsonObj.getInt("status");

                            if (status == 200) {
                                String topic = Tools.parseString(jsonObj.getString("topic"));

                                if (!topic.equals("")) {
                                    //subscribe topic to FCM
                                    FirebaseMessaging.getInstance().subscribeToTopic(topic);
                                }

                                //set to AppGlobal (settings)
                                m_Global.setUserLoginId(Tools.parseInt(userId));
                                m_Global.setUserToken(token);
                                m_Global.setUserTopic(topic);

                                //run other method
                                if (callMethodSuccess!= null) {
                                    callMethodSuccess.run();
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            if (callMethodFail != null) {
                                callMethodFail.run();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyErrorHelper.showError(error, m_Ctx);
                        if (callMethodFail != null) {
                            callMethodFail.run();
                        }
                    }
                }
        )
        {
            // kirim parameter ke server
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userid", userId);
                params.put("deviceid", androidId);
                params.put("token", token);

                return params;
            }
        };
        VolleySingleton.getInstance(m_Ctx).addToRequestQueue(strReq, TAG);
    }

    public void UpdateToken(final String token,
                            final Runnable callMethodSuccess, final Runnable callMethodFail) {
        String url = AppGlobal.URL_ROOT + "/Oten/refresh_token";
        StringRequest strReq = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObj = new JSONObject(response);
                            int status = jsonObj.getInt("status");

                            if (status == 200) {
                                //set to AppGlobal (settings)
                                m_Global.setUserToken(token);

                                //run other method
                                if (callMethodSuccess != null) {
                                    callMethodSuccess.run();
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            if (callMethodFail != null) {
                                callMethodFail.run();
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyErrorHelper.showError(error, m_Ctx);
                        if (callMethodFail != null) {
                            callMethodFail.run();
                        }
                    }
                }
        )
        {
            // kirim parameter ke server
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userid", String.valueOf(m_Global.getUserLoginId()));
                params.put("deviceid", m_Global.getUserAndroidId());
                params.put("token", token);
                return params;
            }
        };
        VolleySingleton.getInstance(m_Ctx).addToRequestQueue(strReq, TAG);
    }

//Disactive - 20181201
//    public void logIn(final String userName, final String password,
//                      final Runnable callMethodSuccess, final Runnable callMethodFail) {
//
//        String url = AppGlobal.URL_ROOT + "/Oten/login" ;
//        StringRequest strReq = new StringRequest(Request.Method.POST, url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        try {
//                            JSONObject jsonObj = new JSONObject(response);
//                            int status = jsonObj.getInt("status");
//
//                            if (status == 200) { //login succeed
//                                final String userId = jsonObj.getString("userid");
//                                boolean firstLogin = jsonObj.getBoolean("first_login");
//
//                                //set to AppGlobal
//                                m_Global.setUserLoginId(Tools.parseInt(userId));
//                                m_Global.setUserLoginName(userName);
//                                m_Global.setUserLoginPass(password);
//
//                                if (firstLogin) {
//                                    //get token from FCM
//                                    FirebaseInstanceId.getInstance().getInstanceId()
//                                            .addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
//                                                @Override
//                                                public void onSuccess(InstanceIdResult instanceIdResult) {
//                                                    //register token to API & subscribe topic to FCM
//                                                    String token = instanceIdResult.getToken();
//                                                    m_Global.setUserToken(token);
//
//                                                    String android_id = Settings.Secure.getString(m_Ctx.getContentResolver(),
//                                                            Settings.Secure.ANDROID_ID);
//                                                    m_Global.setUserAndroidId(android_id);
//
//                                                    RegisterToken(userId, android_id, token, callMethodSuccess);
//                                                }
//                                            });
//                                } else {
//                                    if (callMethodSuccess != null) {
//                                        callMethodSuccess.run();
//                                    }
//                                }
//
//                            } else {
//                                String errorMsg = jsonObj.getString("message");
//                                Toast.makeText(m_Ctx, errorMsg, Toast.LENGTH_LONG).show();
//                                if (callMethodFail != null) {
//                                    callMethodFail.run();
//                                }
//                            }
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                            Toast.makeText(m_Ctx, "Login error: " + e.getMessage(), Toast.LENGTH_LONG).show();
//                            if (callMethodFail != null) {
//                                callMethodFail.run();
//                            }
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        VolleyErrorHelper.showError(error, m_Ctx);
//                    }
//                }
//        ) {
//            // kirim parameter ke server
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("username", userName);
//                params.put("password", password);
//                return params;
//            }
//        };
//        VolleySingleton.getInstance(m_Ctx).addToRequestQueue(strReq, TAG);
//    }


//Disactive - 20190107
//    public void logIn(final String userName, final String password,
//                      final Runnable callMethodSuccess, final Runnable callMethodFail) {
//
//        //get-set android_id
//        final String deviceId = Settings.Secure.getString(m_Ctx.getContentResolver(),Settings.Secure.ANDROID_ID);
//        m_Global.setUserAndroidId(deviceId);
//
//        //get-set token
//        FirebaseInstanceId.getInstance().getInstanceId()
//                .addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
//                    @Override
//                    public void onSuccess(InstanceIdResult instanceIdResult) {
//                        String token = instanceIdResult.getToken();
//                        m_Global.setUserToken(token);
//                    }
//                });
//
//        //log in
//        String url = AppGlobal.URL_ROOT + "/Oten/login2" ;
//        StringRequest strReq = new StringRequest(Request.Method.POST, url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        try {
//                            JSONObject jsonObj = new JSONObject(response);
//                            int status = jsonObj.getInt("status");
//
//                            if (status == 200) { //login succeed
//                                final String userId = jsonObj.getString("userid");
//                                boolean firstLogin = jsonObj.getBoolean("first_login");
//
//                                //set to AppGlobal
//                                m_Global.setUserLoginId(Tools.parseInt(userId));
//                                m_Global.setUserLoginName(userName);
//                                m_Global.setUserLoginPass(password);
//
//                                //call success method
//                                if (callMethodSuccess != null) {
//                                    callMethodSuccess.run();
//                                }
//
//                            } else {
//                                String errorMsg = jsonObj.getString("message");
//                                Toast.makeText(m_Ctx, errorMsg, Toast.LENGTH_LONG).show();
//                                if (callMethodFail != null) {
//                                    callMethodFail.run();
//                                }
//                            }
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                            Toast.makeText(m_Ctx, "Login error: " + e.getMessage(), Toast.LENGTH_LONG).show();
//                            if (callMethodFail != null) {
//                                callMethodFail.run();
//                            }
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        VolleyErrorHelper.showError(error, m_Ctx);
//                    }
//                }
//        ) {
//            // kirim parameter ke server
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String, String> params = new HashMap<String, String>();
//
//                final String token;
//                if (Tools.parseString(m_Global.getUserToken()) == null) {
//                    token = "";
//                } else {
//                    token = m_Global.getUserToken();
//                }
//
//                params.put("username", userName);
//                params.put("password", password);
//                params.put("deviceid", deviceId);
//                params.put("token", token);
//                return params;
//            }
//        };
//        VolleySingleton.getInstance(m_Ctx).addToRequestQueue(strReq, TAG);
//    }
}



