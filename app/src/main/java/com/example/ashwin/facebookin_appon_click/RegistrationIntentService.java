package com.example.ashwin.facebookin_appon_click;

/**
 * Created by ashwin on 20/10/16.
 */

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.facebook.appevents.AppEventsLogger;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

public class RegistrationIntentService extends IntentService
{
        private static final String LOG_TAG = RegistrationIntentService.class.getCanonicalName();

        public RegistrationIntentService() {
            super(LOG_TAG);
        }

        @Override
        protected void onHandleIntent(Intent intent) {
            try {
                synchronized (this) {
                    InstanceID instanceID = InstanceID.getInstance(this);
                    String token = instanceID.getToken(getString(R.string.gcm_defaultSenderId), GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
                    Log.i(LOG_TAG, "GCM Registration Token: " + token);
                    AppEventsLogger.setPushNotificationsRegistrationId(token);
                }
            } catch (Exception e) {
                Log.e(LOG_TAG, "Failed to complete token refresh", e);
            }
        }

}
