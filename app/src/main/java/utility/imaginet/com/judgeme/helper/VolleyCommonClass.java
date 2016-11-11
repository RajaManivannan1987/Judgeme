package utility.imaginet.com.judgeme.helper;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import utility.imaginet.com.judgeme.interfaceClass.interfaceSelector;

/**
 * Created by IM0033 on 11/5/2015.
 */
public class VolleyCommonClass {

    public static void getDataFromServer(String url, final interfaceSelector listSelector) {
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        //dialog.dismiss();
                        try {
                            listSelector.volleyResponse(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                listSelector.volleyError(error.getMessage());
                // dialog.dismiss();
            }


        });
        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        return activeNetworkInfo != null
                && activeNetworkInfo.isConnectedOrConnecting();
    }

    public static void getInternetAlert(Context context) {
        try {
            Toast.makeText(
                    context,
                    "Unable to connect to internet. Please check your connection and try again",
                    Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
