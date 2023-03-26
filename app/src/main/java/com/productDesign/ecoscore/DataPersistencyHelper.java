package com.productDesign.ecoscore;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class DataPersistencyHelper {

    public static Context context;

    public static void storeData(List<Bill> bills) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        String json = new Gson().toJson(bills);
        editor.putString("Bills", json);
        editor.commit();
    }

    public static List<Bill> loadData() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        String json = sp.getString("Bills", null);
        if(json !=null) {
            Type type = new TypeToken<List<Bill>>(){}.getType();
            return new Gson().fromJson(json, type);
        }
        else {
            List<Bill> bills = new ArrayList<>();
            bills.add(new Bill("December", 321));
            bills.add(new Bill("November", 489.5));
            return bills;
        }
    }
}
