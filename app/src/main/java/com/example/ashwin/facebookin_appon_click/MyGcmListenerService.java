package com.example.ashwin.facebookin_appon_click;

/**
 * Created by ashwin on 20/10/16.
 */

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.facebook.notifications.NotificationsManager;
import com.google.android.gms.gcm.GcmListenerService;

public class MyGcmListenerService extends GcmListenerService {

    private static final String TAG = "MyGcmListenerService";
    private SharedPreferencesManager mSharedPreferencesManager;

    /**
     * Called when message is received.
     *
     * @param from SenderID of the sender.
     * @param data Data bundle containing message data as key/value pairs.
     *             For Set of keys use data.keySet().
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(String from, Bundle data) {

        Log.v(TAG, "onMessageReceived(" + from + ", " + data + ")");

        // [START_EXCLUDE]
        /**
         * Production applications would usually process the message here.
         * Eg: - Syncing with server.
         *     - Store message in local database.
         *     - Update UI.
         */

        /**
         * In some cases it may be useful to show a notification indicating to the user
         * that a message was received.
         */

        NotificationsManager.presentNotification(
                    this,
                    data,
                    new Intent(getApplicationContext(), MainActivity.class)
        );

        mSharedPreferencesManager = new SharedPreferencesManager(this);

        String bundleString = data.getString("fb_push_card");
        mSharedPreferencesManager.setInAppCard(bundleString);

        //JSONObject json = new JSONObject(data.toString());

        //Bundle bundle = new Bundle();
        //bundle.putString("fb_push_card", json.getJSONObject("fb_push_card").toString());

        // [END_EXCLUDE]
    }
    // [END receive_message]
}