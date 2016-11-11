package utility.imaginet.com.judgeme.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.millennialmedia.InlineAd;

import org.json.JSONException;
import org.json.JSONObject;

import utility.imaginet.com.judgeme.R;
import utility.imaginet.com.judgeme.helper.PrefManager;
import utility.imaginet.com.judgeme.helper.VolleyCommonClass;
import utility.imaginet.com.judgeme.helper.judgeMeUrls;
import utility.imaginet.com.judgeme.interfaceClass.interfaceSelector;
import utility.imaginet.com.judgeme.models.UserLoginDetails;

/**
 * Created by IM0033 on 11/4/2015.
 */
public class LoginActivity extends Activity {

    CallbackManager callbackManager;
    private LoginButton loginButton;
    private PrefManager pref;
    private LinearLayout termsLayout;
    // ImageView ads;
    private InlineAd inlineAd;
    public static final String PLACEMENT_ID = "221660";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(LoginActivity.this);
        callbackManager = CallbackManager.Factory.create();
        // MMSDK.initialize(this);
        // UserData userData = new UserData().setAge(37).setGender(UserData.Gender.MALE);
        // MMSDK.setUserData(userData);
        setContentView(R.layout.login);
       /* final View adContainer = findViewById(R.id.ad_container);
        //ads= (ImageView) findViewById(R.id.ads);
        try {
            inlineAd = InlineAd.createInstance(PLACEMENT_ID, (ViewGroup) adContainer);
        } catch (MMException e) {
            e.printStackTrace();
        }*/
        LoginManager.getInstance().logOut();
        pref = new PrefManager(getApplicationContext());
        loginButton = (LoginButton) findViewById(R.id.login_button);
        termsLayout = (LinearLayout) findViewById(R.id.termsLayout);
        termsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), TermActivity.class);
                startActivity(i);
                /*Intent i = new Intent(getApplicationContext(), InterstitialActivity.class);
                startActivity(i);*/
                LoginActivity.this.overridePendingTransition(R.anim.activity_enter, R.anim.activity_leave);
            }
        });
        loginButton.setReadPermissions("email");
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {
                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, final GraphResponse response) {
                        try {
                            String Uid = (String) object.get("id");
                            String emaiId = (String) object.get("email");
                            final String url = judgeMeUrls.BASEURL + "loginWithFB&emailorphone=" + emaiId + "&uid=fb" + Uid;

                            VolleyCommonClass.getDataFromServer(url, new interfaceSelector() {
                                @Override
                                public void volleyError(String error) {
                                    Log.d("fb_error", error);
                                }

                                @Override
                                public void volleyResponse(JSONObject response) {
                                    Log.d("loginresponse", response.toString());
                                    UserLoginDetails loginResponse = new UserLoginDetails();
                                    loginResponse.setLoginResponseValue(response, LoginActivity.this);
                                    String resultCode = loginResponse.getResultcode();
                                    String Emailorphone = loginResponse.getEmailorphone();
                                    String token = loginResponse.getLogin_token();
                                    String Name = loginResponse.getFirstname();
                                    String uid = loginResponse.getUid();
                                    pref.createLoginSession(Emailorphone, token, Name, loginResult.getAccessToken().getToken(), uid);
                                    if (loginResponse.getIsfirstlogin().startsWith("N")) {
                                        Intent i = new Intent(LoginActivity.this, MainActivity.class);
                                        startActivity(i);
                                        LoginActivity.this.overridePendingTransition(R.anim.activity_enter, R.anim.activity_leave);
                                    } else {
                                        Intent i = new Intent(LoginActivity.this, MyGenresActivity.class);
                                        startActivity(i);
                                        LoginActivity.this.overridePendingTransition(R.anim.activity_enter, R.anim.activity_leave);
                                    }
                                }
                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender, birthday");
                request.setParameters(parameters);
                request.executeAsync();

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {
                //Log.d("fb_error", "" + error);
                Toast.makeText(getApplicationContext(), "FB_Error:" + error.toString(), Toast.LENGTH_LONG).show();
            }
        });

//        ads.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                adContainer.setVisibility(View.VISIBLE);
//                requestAd();
//            }
//        });

    }

    private void requestAd() {
        if (inlineAd != null) {

            //The AdRequest instance is used to pass additional metadata to the server to improve ad selection
            final InlineAd.InlineAdMetadata inlineAdMetadata = new InlineAd.InlineAdMetadata().
                    setAdSize(InlineAd.AdSize.LARGE_BANNER);

            //Request ads from the server.  If automatic refresh is enabled for your placement new ads will be shown
            //automatically
            inlineAd.request(inlineAdMetadata);
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.animation_enter, R.anim.animation_leave);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

    }
}
