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
        editor.putString("Name", User.Name);
        editor.commit();
    }

    public static List<Bill> loadData() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        String json = sp.getString("Bills", null);
        User.Name = sp.getString("Name", "Gabriel");
        if(json !=null) {
            Type type = new TypeToken<List<Bill>>(){}.getType();
            List<Bill> bills = new Gson().fromJson(json, type);
            if(bills.size()==0){
                bills.add(new Bill("February", 310.4));
                bills.add(new Bill("January", 505.7));
                bills.add(new Bill("December", 289.2));
                bills.add(new Bill("November", 352.3));
                bills.add(new Bill("October", 378.5));
                bills.add(new Bill("September", 458.5));
            }
            return bills;
        }
        else {
            List<Bill> bills = new ArrayList<>();
            bills.add(new Bill("February", 310.4));
            bills.add(new Bill("January", 505.7));
            bills.add(new Bill("December", 289.2));
            bills.add(new Bill("November", 352.3));
            bills.add(new Bill("October", 378.5));
            bills.add(new Bill("September", 458.5));
            return bills;
        }
    }
}
