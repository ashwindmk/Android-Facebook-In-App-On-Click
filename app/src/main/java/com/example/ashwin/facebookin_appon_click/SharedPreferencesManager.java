package com.example.ashwin.facebookin_appon_click;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by ashwin on 24/10/16.
 */

public class SharedPreferencesManager {

    private Context mContext;
    private static SharedPreferences mSharedPreferences;
    private static final String PREFERENCES = "my_preferences";

    public SharedPreferencesManager(Context context) {
        mSharedPreferences = context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        mContext = context;
    }

    //in app card data
    private static final String IN_APP_CARD = "In App Card";

    public void setInAppCard(String inAppCard)
    {
        mSharedPreferences.edit().putString(IN_APP_CARD, inAppCard).commit();
    }

    public String getInAppCard()
    {
        return mSharedPreferences.getString(IN_APP_CARD, "");
    }


}
