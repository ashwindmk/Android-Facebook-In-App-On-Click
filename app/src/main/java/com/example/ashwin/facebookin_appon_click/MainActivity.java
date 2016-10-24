package com.example.ashwin.facebookin_appon_click;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.notifications.NotificationCardResult;
import com.facebook.notifications.NotificationsManager;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.Charset;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getCanonicalName();
    private SharedPreferencesManager mSharedPreferencesManager;
    private String bundleString = "";
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        // register for GCM
        Intent intent = new Intent(this, RegistrationIntentService.class);
        startService(intent);

        NotificationsManager.presentCardFromNotification(this);

        mSharedPreferencesManager = new SharedPreferencesManager(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        NotificationsManager.presentCardFromNotification(this, intent);
    }

    /**
     * Mock an example push notification bundle from one of our local example JSON files.
     * @param exampleId The example id of the asset to load
     * @return a bundle with the contents of the specified example id
     */
    @NonNull
    private Bundle getBundle(int exampleId) {
        try {
            InputStream inputStream = getAssets().open("example" + exampleId + ".json");

            StringWriter output = new StringWriter();
            IOUtils.copy(inputStream, output, Charset.forName("UTF-8"));

            JSONObject json = new JSONObject(output.toString());

            Bundle bundle = new Bundle();
            bundle.putString("fb_push_card", json.getJSONObject("fb_push_card").toString());
            JSONObject pushPayload = json.optJSONObject("fb_push_payload");
            if (pushPayload != null) {
                bundle.putString("fb_push_payload", pushPayload.toString());
            }

            output.close();
            inputStream.close();

            return bundle;
        } catch (Exception ex) {
            Log.e(LOG_TAG, "Error while getting bundle", ex);
            return new Bundle();
        }
    }

    public void showInAppCard(View view)
    {
        AppEventsLogger logger = AppEventsLogger.newLogger(this);
        logger.logEvent("Test 1 button clicked");

        bundleString = mSharedPreferencesManager.getInAppCard();
        Log.i(LOG_TAG, bundleString);

        bundle = new Bundle();

        if(!bundleString.equals(""))
        {
            bundle.putString("fb_push_card", bundleString);
            NotificationsManager.presentCard(this, bundle);
            if(!BuildConfig.DEBUG)
            {
                mSharedPreferencesManager.setInAppCard("");
            }
        }

        //Bundle exampleBundle = getBundle(Integer.parseInt(view.getTag().toString()));

    }

    public void showNotification(View view) {
        Bundle exampleBundle = getBundle(Integer.parseInt(view.getTag().toString()));

        NotificationsManager.presentNotification(this, exampleBundle, getIntent());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        NotificationCardResult result = NotificationsManager.handleActivityResult(requestCode, resultCode, data);

        if (result != null) {
            Toast.makeText(this, "Result: " + result.getActionUri(), Toast.LENGTH_LONG).show();
        }
    }

}
