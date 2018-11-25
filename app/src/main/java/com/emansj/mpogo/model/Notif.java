package com.emansj.mpogo.model;


import android.content.Context;
import android.content.SharedPreferences;
import com.emansj.mpogo.helper.AppGlobal;
import com.emansj.mpogo.helper.AppSharedPref;
import com.emansj.mpogo.helper.Tools;
import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;


public class Notif {

    public static final String APP_NOTIIFICATIONS = "app_notifications";

    public int NotificationId;
    public String Title;
    public String Message;
    public String ImageUrl;
    public String LinkUrl;
    public boolean IsRead = false;
    public String ReceivedDate;

    public Notif(){super();}


    public void addNotif(Context ctx, Notif item) {
        List<Notif> listItem = getNotifs(ctx);

        if (listItem == null)
            listItem = new ArrayList<Notif>();

        listItem.add(item);
        saveNotifs(ctx, listItem);
    }

    public void removeNotif(Context ctx, Notif item) {
        ArrayList<Notif> listItem = getNotifs(ctx);

        if (listItem != null) {
            //listItem.remove(item);
            Iterator<Notif> it = listItem.iterator();
            while (it.hasNext()) {
                Notif notif = it.next();
                if (item.NotificationId == notif.NotificationId) {
                    it.remove();
                    break;
                }
            }
            saveNotifs(ctx, listItem);
        }
    }

    public void setHasBeenRead(Context ctx, int notificationId) {
        ArrayList<Notif> listItem = getNotifs(ctx);

        if (listItem != null) {
            for(int i = 0; i < listItem.size(); ++i) {
                if (listItem.get(i).NotificationId == notificationId) {
                    listItem.get(i).IsRead = true;
                    break;
                };
            }

            saveNotifs(ctx, listItem);
        }
    }

    public void saveNotifs(Context ctx, List<Notif> listItem) {
        Gson gson = new Gson();
        String jsonItems = gson.toJson(listItem);

        AppSharedPref.init(ctx);
        AppSharedPref.write(AppSharedPref.APP_NOTIIFICATIONS, jsonItems);
    }

    public void removeNotifs(Context ctx){
        AppSharedPref.init(ctx);
        AppSharedPref.write(AppSharedPref.APP_NOTIIFICATIONS, "");
    }

    public ArrayList<Notif> getNotifs(Context ctx) {
        List<Notif> listItem;

        AppSharedPref.init(ctx);
        String jsonItems = AppSharedPref.read(AppSharedPref.APP_NOTIIFICATIONS, null);

        if (jsonItems != null) {
            if (!jsonItems.equals("")) {
                Gson gson = new Gson();
                Notif[] items = gson.fromJson(jsonItems, Notif[].class);

                listItem = Arrays.asList(items);
                listItem = new ArrayList<>(listItem);
            } else return null;
        } else
            return null;

        return (ArrayList<Notif>) listItem;
    }

    public List<Notif> getNotifList(Context ctx) {
        List<Notif> listItem = new ArrayList<>();

        AppSharedPref.init(ctx);
        String json = AppSharedPref.read(AppSharedPref.APP_NOTIIFICATIONS, null);

        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray data = jsonObject.getJSONArray("data");
            if (data.length() > 0)
            {
                for (int i = 0; i < data.length(); i++)
                {
                    JSONObject row = data.getJSONObject(i);

                    Notif obj = new Notif();
                    obj.NotificationId = Tools.parseInt(row.getString("NotificationId"));
                    obj.Title = Tools.parseString(row.getString("Title"));
                    obj.Message = Tools.parseString(row.getString("Message"));
                    obj.ImageUrl = Tools.parseString(row.getString("IconUrl"));
                    obj.LinkUrl = Tools.parseString(row.getString("LinkUrl"));
                    obj.IsRead = Boolean.parseBoolean(row.getString("IsRead"));
                    listItem.add(obj);
                }
            }
        }catch (JSONException e){
            e.printStackTrace();
            return null;
        }
        return listItem;
    }

    public int totalUnread(Context ctx) {
        int total = 0;
        ArrayList<Notif> listItem = getNotifs(ctx);

        if (listItem != null) {
            for(int i = 0; i < listItem.size(); ++i) {
                if (listItem.get(i).IsRead == false) {
                    total += 1;
                };
            }
        }
        return total;
    }

}


