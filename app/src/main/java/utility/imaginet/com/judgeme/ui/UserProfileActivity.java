package utility.imaginet.com.judgeme.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import utility.imaginet.com.judgeme.R;
import utility.imaginet.com.judgeme.adapter.ArtistClipAdapter;
import utility.imaginet.com.judgeme.helper.AppController;
import utility.imaginet.com.judgeme.helper.PrefManager;
import utility.imaginet.com.judgeme.helper.VolleyCommonClass;
import utility.imaginet.com.judgeme.helper.judgeMeUrls;
import utility.imaginet.com.judgeme.interfaceClass.interfaceSelector;
import utility.imaginet.com.judgeme.models.UserClipsDetails;

/**
 * Created by IM0033 on 11/20/2015.
 */
public class UserProfileActivity extends AppCompatActivity {
    private ImageView artistZoneImage;
    private Button artist_backButton;
    private ImageLoader imageLoader;
    private ListView artist_clips_listView;
    private ArrayList<UserClipsDetails> list = new ArrayList<UserClipsDetails>();
    private ArtistClipAdapter adapter;
    private TextView acounttypeTextView, noClipsTextview, artistNameText, followsTextView;
    private String token;
    private CircleImageView UserImageViewl;
    private PrefManager prefManager;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);
        LoadData();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void LoadData() {
        findViewById();
        prefManager = new PrefManager(getApplicationContext());
        token = prefManager.getObjectId();
        String uid = getIntent().getExtras().getString("uid");
        fetchArtist(judgeMeUrls.FETCH_ARTIST + "&token=" + token + "&uid=" + uid);
        onClick();
    }

    private void onClick() {
        artist_backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void findViewById() {
        UserImageViewl = (CircleImageView) findViewById(R.id.UserImageView);
        artist_backButton = (Button) findViewById(R.id.artist_backButton);
        artistNameText = (TextView) findViewById(R.id.artistNameText);
        followsTextView = (TextView) findViewById(R.id.followsTextView);
        imageLoader = AppController.getInstance().getImageLoader();
        artist_clips_listView = (ListView) findViewById(R.id.artist_clips_listView);
        artistZoneImage = (ImageView) findViewById(R.id.artistZoneImage);
        noClipsTextview = (TextView) findViewById(R.id.noClipsTextview);
    }


    public void imageLoader(String photoURL, final String imageview) {
        if (!photoURL.matches("")) {
            imageLoader.get(photoURL, new ImageLoader.ImageListener() {
                @Override
                public void onResponse(ImageLoader.ImageContainer imageContainer, boolean b) {
                    if (imageview.startsWith("UserPhoto")) {
                        Log.d("UserPhotoImage", "" + imageContainer);
                        if (imageContainer.getBitmap() != null) {
                            UserImageViewl.setImageBitmap(imageContainer.getBitmap());
                        } else {
                          //  UserImageViewl.setBackground(getResources().getDrawable(R.drawable.no_user_icon));
                        }
                    } else {
                        artistZoneImage.setImageBitmap(imageContainer.getBitmap());
                    }
                }

                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    VolleyLog.d("ErrorProPic", volleyError.getMessage());
                }
            });
        } else {
            Log.d("photoURL", "photoURL is Empty");
        }

    }

    private void fetchArtist(String url) {
        VolleyCommonClass.getDataFromServer(url, new interfaceSelector() {
            @Override
            public void volleyError(String error) {

            }

            @Override
            public void volleyResponse(JSONObject response) {
                Log.d("response", response + "");
                UserClipsDetails setData = new UserClipsDetails();
                setData.setUserClipsDetails(response);

                artistNameText.setText(setData.getFirstname() + " " + setData.getLastname());
                followsTextView.setText("#" + setData.getRank() + " " + "in" + " " + setData.getZone());
                String ZoneSetting = setData.getZonesetting();
                if (ZoneSetting != null) {
                    if (ZoneSetting.startsWith("World")) {
                        imageLoader(setData.getWorldiconURLlowres(), "Zone");
                        // followsTextView.setText("#" + setData.getNumberoflikes() + " " + "in" + " " + "World.");
                    } else if (ZoneSetting.startsWith("Country")) {
                        imageLoader(setData.getCountryiconURLlowres(), "Zone");
                        //followsTextView.setText("#" + setData.getNumberoflikes() + " " + "in" + " " + setData.getCountry());
                    } else if (ZoneSetting.startsWith("State")) {
                        imageLoader(setData.getStateiconURLlowres(), "Zone");
                        // followsTextView.setText("#" + setData.getNumberoflikes() + " " + "in" + " " + setData.getState());
                    } else if (ZoneSetting.startsWith("City")) {
                        imageLoader(setData.getCityiconURLlowres(), "Zone");
                        // followsTextView.setText("#" + setData.getNumberoflikes() + " " + "in" + " " + setData.getCity());
                    }
                }
                imageLoader(setData.getPhotoURL(), "UserPhoto");

                JSONObject object = null;
                try {
                    object = response.getJSONObject("data");
                    JSONObject userDetail = object.getJSONObject("user");
                    JSONObject accounttype = userDetail.getJSONObject("accounttype");
                    int activeclips = Integer.parseInt(accounttype.getString("activeclips"));
                    JSONArray array = userDetail.getJSONArray("clips");

                    if (!list.isEmpty()) {
                        list.clear();
                    }
                    if (array.length() == 0) {
                        noClipsTextview.setVisibility(View.VISIBLE);

                    } else {
                        for (int i = 0; i < array.length(); i++) {
                            if (i < activeclips) {
                                //for (int i = 0; i < activeclips; i++) {
                                UserClipsDetails setclipData = new UserClipsDetails();
                                JSONObject clip_Object = array.getJSONObject(i);

                                setclipData.setClipID(clip_Object.optString("clipID"));
                                setclipData.setClip_title(clip_Object.optString("title"));
                                setclipData.setClip_likescount(clip_Object.optString("likescount"));
                                setclipData.setClip_dislikescount(clip_Object.optString("dislikescount"));
                                setclipData.setClip_netlikes(clip_Object.optString("netlikes"));
                                setclipData.setClip_uid(clip_Object.optString("uid"));
                                setclipData.setClip_streamURL(clip_Object.optString("streamURL"));
                                list.add(setclipData);
                                // break;
                            }
                        }
                    }
                    adapter = new ArtistClipAdapter(getApplicationContext(), list);
                    artist_clips_listView.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.animation_enter, R.anim.animation_leave);
    }


    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
      /*  client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "UserProfile Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://utility.imaginet.com.judgeme.ui/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);*/
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
      /*  Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "UserProfile Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://utility.imaginet.com.judgeme.ui/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();*/
    }
}

