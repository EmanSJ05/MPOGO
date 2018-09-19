package com.emansj.mpogo.helper;

import android.content.Context;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import org.json.JSONObject;
import com.emansj.mpogo.R;

public class VolleyErrorHelper {

    public static String getMessage (Object error , Context context){
        if(error instanceof TimeoutError){
            return context.getResources().getString(R.string.timeout);

        }else if (isServerProblem(error)){
            return handleServerError(error ,context);

        }else if(isNetworkProblem(error)){
            return context.getResources().getString(R.string.no_internet);
        }
        return context.getResources().getString(R.string.generic_error);
    }

    public static void showError (Object error , Context context){
        if(error instanceof TimeoutError){
            Toast.makeText(
                    context,
                    context.getResources().getString(R.string.timeout),
                    Toast.LENGTH_LONG
            ).show();

        }else if (isServerProblem(error)){
            Toast.makeText(
                    context,
                    handleServerError(error ,context),
                    Toast.LENGTH_LONG
            ).show();

        }else if(isNetworkProblem(error)){
            Toast.makeText(
                    context,
                    context.getResources().getString(R.string.no_internet),
                    Toast.LENGTH_LONG
            ).show();
        }else{
            Toast.makeText(
                    context,
                    context.getResources().getString(R.string.generic_error),
                    Toast.LENGTH_LONG
            ).show();
        }
    }

    private static String handleServerError(Object error, Context context) {

        VolleyError er = (VolleyError)error;
        NetworkResponse response = er.networkResponse;
        String errorMessage = context.getResources().getString(R.string.generic_error);

        try{
            if(response != null){
                switch (response.statusCode){
                    case 404:
                    case 422:
                    case 401:
                        try {
                            String responseBody = new String(response.data,"UTF-8");
                            if (!responseBody.isEmpty()){
                                JSONObject jsonObj = new JSONObject(responseBody);
                                if (jsonObj.has("message")){
                                    errorMessage = jsonObj.getString("message");
                                }
                            }
                        } catch (Exception e) {
                            errorMessage = e.getMessage().toString();
                            e.printStackTrace();
                        }
                }
            }
        } catch (Exception e) {
            errorMessage = e.getMessage().toString();
            e.printStackTrace();
        } finally{
            return errorMessage;
        }
    }

    private static boolean isServerProblem(Object error) {
        return (error instanceof ServerError || error instanceof AuthFailureError);
    }

    private static boolean isNetworkProblem (Object error){
        return (error instanceof NetworkError || error instanceof NoConnectionError);
    }

}
