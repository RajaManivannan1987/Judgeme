package utility.imaginet.com.judgeme.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.millennialmedia.InterstitialAd;
import com.millennialmedia.MMException;
import com.millennialmedia.MMSDK;
import com.millennialmedia.UserData;

import utility.imaginet.com.judgeme.R;
import utility.imaginet.com.judgeme.helper.Utils;

/**
 * Created by IM0033 on 3/31/2016.
 */
public class AddActivity extends AppCompatActivity implements InterstitialAd.InterstitialListener {
    public static final String PLACEMENT_ID = "221998";
    private InterstitialAd interstitialAd;
    public static final String TAG = AddActivity.class.getSimpleName();
    Button load;
    private static int SPLASH_TIME_OUT = 2000;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MMSDK.initialize(this);
        UserData userData = new UserData().setAge(25).setGender(UserData.Gender.MALE);
        MMSDK.setUserData(userData);
        try {
            interstitialAd = InterstitialAd.createInstance(PLACEMENT_ID);
            interstitialAd.setListener(this);
        } catch (MMException e) {
            e.printStackTrace();
        }
        setContentView(R.layout.add_activity);
        //
        load = (Button) findViewById(R.id.load);
        load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestAd();
            }
        });
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                if (interstitialAd != null) {
                    InterstitialAd.InterstitialAdMetadata metadata = new InterstitialAd.InterstitialAdMetadata();
                    interstitialAd.load(AddActivity.this, metadata);
                    SharedPreferences sharedPreferences =
                            PreferenceManager.getDefaultSharedPreferences(AddActivity.this);
                    InterstitialAd.DisplayOptions displayOptions = new InterstitialAd.DisplayOptions().setImmersive(
                            sharedPreferences.getBoolean(getResources().getString(R.string.app_name), false));
                    try {
                        interstitialAd.show(AddActivity.this);
                    } catch (MMException e) {
                        Log.i(TAG, "Unable to show interstitial ad content, exception occurred");
                        e.printStackTrace();
                    }
                } else {
                    Log.i(TAG, "Unable to load interstitial ad content, InterstitialAd instance is null.");
                }
            }
        }, SPLASH_TIME_OUT);

    }

    private void requestAd() {
        if (interstitialAd != null) {
            InterstitialAd.InterstitialAdMetadata metadata = new InterstitialAd.InterstitialAdMetadata();
            interstitialAd.load(AddActivity.this, metadata);
            SharedPreferences sharedPreferences =
                    PreferenceManager.getDefaultSharedPreferences(AddActivity.this);

            InterstitialAd.DisplayOptions displayOptions = new InterstitialAd.DisplayOptions().setImmersive(
                    sharedPreferences.getBoolean(getResources().getString(R.string.app_name), false));

            try {
                if (interstitialAd.isReady()) {

                    interstitialAd.show(AddActivity.this, displayOptions);
                }
            } catch (MMException e) {
                Log.i(TAG, "Unable to show interstitial ad content, exception occurred");
                e.printStackTrace();
            }
        } else {
            Log.i(TAG, "Unable to load interstitial ad content, InterstitialAd instance is null.");
        }
    }

    @Override
    public void onLoaded(InterstitialAd interstitialAd) {
        Utils.runOnUiThread(new Runnable() {
            @Override
            public void run() {

            }
        });
        Log.i(TAG, "Interstitial Ad loaded.");
    }

    @Override
    public void onLoadFailed(InterstitialAd interstitialAd, InterstitialAd.InterstitialErrorStatus interstitialErrorStatus) {
        Log.i(TAG, "Interstitial Ad load failed.");
    }

    @Override
    public void onShown(InterstitialAd interstitialAd) {
        Log.i(TAG, "Interstitial Ad shown.");
    }

    @Override
    public void onShowFailed(InterstitialAd interstitialAd, InterstitialAd.InterstitialErrorStatus interstitialErrorStatus) {
        Log.i(TAG, "Interstitial Ad show failed.");
    }

    @Override
    public void onClosed(InterstitialAd interstitialAd) {
        Log.i(TAG, "Interstitial Ad closed.");
    }

    @Override
    public void onClicked(InterstitialAd interstitialAd) {
        Log.i(TAG, "Interstitial Ad clicked.");
    }

    @Override
    public void onAdLeftApplication(InterstitialAd interstitialAd) {
        Log.i(TAG, "Interstitial Ad left application.");
    }

    @Override
    public void onExpired(InterstitialAd interstitialAd) {
        Log.i(TAG, "Interstitial Ad expired.");
    }
}
