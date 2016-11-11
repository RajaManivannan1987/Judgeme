package utility.imaginet.com.judgeme.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

import utility.imaginet.com.judgeme.R;
import utility.imaginet.com.judgeme.helper.PrefManager;
import utility.imaginet.com.judgeme.helper.VolleyCommonClass;
import utility.imaginet.com.judgeme.helper.judgeMeUrls;
import utility.imaginet.com.judgeme.interfaceClass.interfaceSelector;


/**
 * Created by IM0033 on 11/4/2015.
 */
public class SplashActivity extends Activity {
    private PrefManager pref;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        // setContentView(R.layout.sample_filter);
        pref = new PrefManager(getApplicationContext());
        token = pref.getObjectId();
        if (VolleyCommonClass.isNetworkAvailable(SplashActivity.this)) {
            Thread thread = new Thread() {
                public void run() {
                    try {
                        //sleep(3000);
                        getValidateUser(judgeMeUrls.VALIDATE_USER + "&token=" + token);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        // getValidateUser(judgeMeUrls.VALIDATE_USER + "&token=" + token);
                   /* if (pref.isLoggedIn()) {
                        Intent i = new Intent(SplashActivity.this, MyGenresActivity.class);
                        startActivity(i);
                    } else {
                        Intent i = new Intent(SplashActivity.this, LoginActivity.class);
                        startActivity(i);

                    }
*/
                    }
                }

            };
            thread.start();
        } else {
            VolleyCommonClass.getInternetAlert(getApplicationContext());
        }
    }

    private void getValidateUser(String url) {
        Log.d("Loginurl", url);
/*        RequestQueue queue= Volley.newRequestQueue(SplashActivity.this);
        JsonObjectRequest request = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject object = response.getJSONObject("data");
                            String resultcode = object.getString("resultcode");
                            String token = object.getString("token");
                            String resultmessage = object.getString("resultmessage");
                            if (resultcode.startsWith("200")) {
                                Intent i = new Intent(SplashActivity.this, MainActivity.class);
                                startActivity(i);
                                SplashActivity.this.overridePendingTransition(R.anim.activity_enter, R.anim.activity_leave);
                            } else if (resultcode.startsWith("401")) {
                                Toast.makeText(getApplicationContext(), resultmessage, Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                                startActivity(i);
                                SplashActivity.this.overridePendingTransition(R.anim.activity_enter, R.anim.activity_leave);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    //dialog.dismiss();
                    if (error instanceof NetworkError) {
                        Toast.makeText(getApplicationContext(),
                                "NetworkError", Toast.LENGTH_SHORT)
                                .show();
                    } else if (error instanceof ServerError) {
                        Toast.makeText(getApplicationContext(),
                                "ServerError", Toast.LENGTH_SHORT)
                                .show();

                    } else if (error instanceof NoConnectionError) {
                        Toast.makeText(getApplicationContext(),
                                "NoConnectionError", Toast.LENGTH_SHORT)
                                .show();
                    } else if (error instanceof TimeoutError) {
                        Toast.makeText(getApplicationContext(),
                                "TimeoutError", Toast.LENGTH_SHORT)
                                .show();
                    }
                } catch (Exception e) {
                    Log.d("Change_City", error.getMessage());
                }

            }

        });
        int MY_SOCKET_TIMEOUT_MS = 30000;
        request.setRetryPolicy(new DefaultRetryPolicy(MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }*/
        VolleyCommonClass.getDataFromServer(url, new interfaceSelector() {
            @Override
            public void volleyError(String error) {

            }

            @Override
            public void volleyResponse(JSONObject response) {
                Log.d("Loginurl", "" + response);
                try {
                    JSONObject object = response.getJSONObject("data");
                    String resultcode = object.getString("resultcode");
                    String token = object.getString("token");
                    String resultmessage = object.getString("resultmessage");
                    if (resultcode.startsWith("200")) {
                        Intent i = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(i);
                        SplashActivity.this.overridePendingTransition(R.anim.activity_enter, R.anim.activity_leave);
                    } else if (resultcode.startsWith("401")) {
                        Toast.makeText(getApplicationContext(), resultmessage, Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(i);
                        SplashActivity.this.overridePendingTransition(R.anim.activity_enter, R.anim.activity_leave);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}

