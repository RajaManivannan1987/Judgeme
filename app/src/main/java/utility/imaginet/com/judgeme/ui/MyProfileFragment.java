package utility.imaginet.com.judgeme.ui;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import utility.imaginet.com.judgeme.R;
import utility.imaginet.com.judgeme.adapter.RecyclerListAdapter;
import utility.imaginet.com.judgeme.helper.AppController;
import utility.imaginet.com.judgeme.helper.OnStartDragListener;
import utility.imaginet.com.judgeme.helper.PrefManager;
import utility.imaginet.com.judgeme.helper.SimpleItemTouchHelperCallback;
import utility.imaginet.com.judgeme.helper.VolleyCommonClass;
import utility.imaginet.com.judgeme.helper.judgeMeUrls;
import utility.imaginet.com.judgeme.interfaceClass.interfaceSelector;
import utility.imaginet.com.judgeme.models.MyProfiles;

/**
 * Created by IM0033 on 11/6/2015.
 */
public class MyProfileFragment extends Fragment implements OnStartDragListener {
    private TextView myProfileUserNameTextView, myProfileFollowsTextView, myProfileAcounttypeTextView, youtubeTextView, noClipsTextView;
    private ImageView myProfilesetZone, accountTypeImageView, videoviewCapture;
    private CircleImageView myProfileUserImageView;
    private PrefManager prefManager;
    private String token, uid, userName;
    private ImageLoader imageLoader;
    private ArrayList<MyProfiles> list = new ArrayList<MyProfiles>();
    private RecyclerView recyclerView;
    private LinearLayout upgradLayout;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerListAdapter adapter;
    private ItemTouchHelper mItemTouchHelper;
    private ToggleButton youtubebutton;
    private String youtubeUrl = judgeMeUrls.YOUTUBE;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_my_profile, container, false);
        findViewById(v);
        prefManager = new PrefManager(getActivity());
        imageLoader = AppController.getInstance().getImageLoader();
        token = prefManager.getObjectId();
        uid = prefManager.getUID();
        LoadData(v);
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        commonVollyMethod1(judgeMeUrls.FETCH_ARTIST + "&token=" + token + "&uid=" + uid, 3);
        commonVollyMethod1(judgeMeUrls.UPDATEUSER + "&token=" + token + "&fbtoken=" + prefManager.getfbToken(), 6);
    }

    private void commonVollyMethod1(String url, final int type) {
        VolleyCommonClass.getDataFromServer(url, new interfaceSelector() {
            @Override
            public void volleyError(String error) {
            }

            @Override
            public void volleyResponse(JSONObject response) {
                //Log.d("userupdateresponse", response + "");
                if (type == 3) {
                    Type2method(response);
                }
            }

        });
    }

    private void LoadData(View v) {


        //commonVollyMethod(judgeMeUrls.FETCH_ARTIST + "&token=" + token + "&uid=" + uid, v, 2);
        onClick(v);
    }

    private void onClick(final View v) {

        /*youtubebutton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    commonVollyMethod(judgeMeUrls.YOUTUBE + "&token=" + token, v, 1);
                    youtubeTextView.setText("Auto share");
                    Log.d("youtube", "autoshare");
                } else if (!isChecked) {
                    commonVollyMethod(judgeMeUrls.YOUTUBE + "&token=" + token, v, 1);
                    youtubeTextView.setText("Disable");
                    Log.d("youtube", "disable");
                }
            }
        });*/
       /* myProfilesetZone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commonVollyMethod(judgeMeUrls.NEXTZONE + "&token=" + token + "&uid=" + uid, v, 2);
            }
        });*/
        youtubebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((ToggleButton) v).isChecked()) {
                    commonVollyMethod(judgeMeUrls.YOUTUBE + "&token=" + token, v, 1);
                    youtubeTextView.setText("Auto \nshare");
                } else {
                    commonVollyMethod(judgeMeUrls.YOUTUBE + "&token=" + token, v, 1);
                    youtubeTextView.setText("Disable");
                }
            }
        });
        upgradLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
                builder1.setMessage("Upgrade will be available in the next release, coming soon!");
                builder1.setCancelable(true);
                builder1.setPositiveButton(
                        "Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert11 = builder1.create();
                //Window window = alert11.getWindow();
                //window.setGravity(Gravity.BOTTOM);
                alert11.show();*/
                //-------later-----
                Intent i = new Intent(getActivity(), UpGradeActivity.class);
                startActivity(i);
                getActivity().overridePendingTransition(R.anim.activity_enter, R.anim.activity_leave);
            }
        });
        videoviewCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), TransprentCameraActivity.class);
                startActivity(i);
                getActivity().overridePendingTransition(R.anim.activity_enter, R.anim.activity_leave);
            }
        });

    }

    private void findViewById(View v) {
        recyclerView = (RecyclerView) v.findViewById(R.id.rv);
        myProfileUserNameTextView = (TextView) v.findViewById(R.id.myProfileUserNameTextView);
        myProfileFollowsTextView = (TextView) v.findViewById(R.id.myProfileFollowsTextView);
        myProfileAcounttypeTextView = (TextView) v.findViewById(R.id.myProfileAcounttypeTextView);
        //myProfilesetZone = (ImageView) v.findViewById(R.id.myProfilesetZone);
        myProfileUserImageView = (CircleImageView) v.findViewById(R.id.myProfileUserImageView);
        accountTypeImageView = (ImageView) v.findViewById(R.id.accountTypeImageView);
        videoviewCapture = (ImageView) v.findViewById(R.id.videoviewCapture);
        upgradLayout = (LinearLayout) v.findViewById(R.id.upgradLayout);
        youtubebutton = (ToggleButton) v.findViewById(R.id.youtubebutton);
        youtubeTextView = (TextView) v.findViewById(R.id.youtubeTextView);
        noClipsTextView = (TextView) v.findViewById(R.id.noClipsTextView);
    }

    public void imageLoader(String photoURL, final String imageview) {
        imageLoader.get(photoURL, new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer imageContainer, boolean b) {
                if (imageview.startsWith("UserPhoto")) {
                    myProfileUserImageView.setImageBitmap(imageContainer.getBitmap());
                } else {
                    try {
                        ((MainActivity) getActivity()).changeZone(imageContainer.getBitmap());
                    } catch (Exception e) {
                        ((MainActivity) getActivity()).changeZone(imageContainer.getBitmap());
                    }
                }
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                VolleyLog.d("ErrorProPic", volleyError.getMessage());
            }
        });
    }


    private void commonVollyMethod(String url, final View v, final int type) {
       // Log.d("responseurl", url);
        VolleyCommonClass.getDataFromServer(url, new interfaceSelector() {
            @Override
            public void volleyError(String error) {
                Toast.makeText(getActivity(), error, Toast.LENGTH_LONG).show();

            }

            @Override
            public void volleyResponse(JSONObject response) {
                if (type == 2) {
                    Type2method(response);
                } else if (type == 1) {
                    Typemethod(response);
                    // commonVollyMethod(judgeMeUrls.FETCH_ARTIST + "&token=" + token + "&uid=" + uid, v, 2);
                }
            }

            private void Typemethod(JSONObject response) {

                try {
                    JSONObject object = response.getJSONObject("data");
                    JSONObject userDetail = object.getJSONObject("user");
                    String youTube = userDetail.getString("youtubeOK");
                    if (youTube.startsWith("0")) {
                        youtubebutton.setChecked(false);
                        youtubeTextView.setText("Disable");
                    } else if (youTube.startsWith("1")) {
                        youtubebutton.setChecked(true);
                        youtubeTextView.setText("Auto \nshare");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void Type2method(JSONObject response) {
        //Log.d("response", response + "");
        MyProfiles setDetails = new MyProfiles();
        setDetails.setMyProfileDetails(response);
        userName = setDetails.getFirstname() + " " + setDetails.getLastname();
        myProfileUserNameTextView.setText(setDetails.getFirstname() + " " + setDetails.getLastname());
        myProfileAcounttypeTextView.setText(setDetails.getAccountType());
        myProfileFollowsTextView.setText("#" + setDetails.getRank() + " " + "in" + " " + setDetails.getZone());
        String youTube = setDetails.getYoutubeOk();
        if (youTube.startsWith("0")) {
            youtubebutton.setChecked(false);
            youtubeTextView.setText("Disable");
        } else if (youTube.startsWith("1")) {
            youtubebutton.setChecked(true);
            youtubeTextView.setText("Auto \nshare");
        }
        imageLoader(setDetails.getPhotoURL(), "UserPhoto");
        String ZoneSetting = setDetails.getZonesetting();
        if (ZoneSetting != null) {
            if (ZoneSetting.startsWith("World")) {
                imageLoader(setDetails.getWorldiconURLlowres(), "Zone");
                // myProfileFollowsTextView.setText("#" + setDetails.getNumberoflikes() + " " + "in" + " " + "World.");
            } else if (ZoneSetting.startsWith("Country")) {
                imageLoader(setDetails.getCountryiconURLlowres(), "Zone");
                // myProfileFollowsTextView.setText("#" + setDetails.getNumberoflikes() + " " + "in" + " " + setDetails.getCountry());
            } else if (ZoneSetting.startsWith("State")) {
                imageLoader(setDetails.getStateiconURLlowres(), "Zone");
                // myProfileFollowsTextView.setText("#" + setDetails.getNumberoflikes() + " " + "in" + " " + setDetails.getState());
            } else if (ZoneSetting.startsWith("City")) {
                imageLoader(setDetails.getCityiconURLlowres(), "Zone");
                // myProfileFollowsTextView.setText("#" + setDetails.getNumberoflikes() + " " + "in" + " " + setDetails.getCity());
            }
        }
        try {
            JSONObject object = response.getJSONObject("data");
            JSONObject userDetail = object.getJSONObject("user");
            JSONObject accounttype = userDetail.getJSONObject("accounttype");
            MyProfiles setactiveClip = new MyProfiles();
            String active = accounttype.optString("activeclips");
            setactiveClip.setCount(active);
            JSONArray array = userDetail.getJSONArray("clips");
            //setactiveClip.setArray(array.length());
            if (!list.isEmpty()) {
                list.clear();
            }
            if (array.length() == 0) {
                noClipsTextView.setVisibility(View.VISIBLE);
            } else {
                noClipsTextView.setVisibility(View.GONE);
            }
            for (int i = 0; i < array.length(); i++) {
                // for (int j = i; j < activeclips; j++) {
                MyProfiles setclipData = new MyProfiles();
                JSONObject clip_Object = array.getJSONObject(i);
                setclipData.setClipID(clip_Object.optString("clipID"));
                setclipData.setClip_title(clip_Object.optString("title"));
                setclipData.setClip_likescount(clip_Object.optString("likescount"));
                setclipData.setClip_dislikescount(clip_Object.optString("dislikescount"));
                setclipData.setClip_netlikes(clip_Object.optString("netlikes"));
                setclipData.setClip_uid(clip_Object.optString("uid"));
                setclipData.setSetClip_streamURL(clip_Object.optString("streamURL"));
                list.add(setclipData);
            }
            recyclerView.setHasFixedSize(true);
            mLayoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(mLayoutManager);
            adapter = new RecyclerListAdapter(getActivity(), list, this);
            recyclerView.setAdapter(adapter);
            //adapter.notifyDataSetChanged();

            ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
            mItemTouchHelper = new ItemTouchHelper(callback);
            mItemTouchHelper.attachToRecyclerView(recyclerView);


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
/*
    private void Type1method(JSONObject response, View v) {
        try {
            JSONObject object = response.getJSONObject("data");
            String resultcode = object.getString("resultcode");
            String token = object.getString("token");
            String resultmessage = object.getString("resultmessage");

            if (resultcode.startsWith("200")) {
                commonVollyMethod(judgeMeUrls.FETCH_ARTIST + "&token=" + token + "&uid=" + uid, v, 2);
            } else {
                //Toast.makeText(getActivity(), resultmessage, Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getActivity(), LoginActivity.class);
                startActivity(i);
                getActivity().overridePendingTransition(R.anim.animation_enter, R.anim.animation_leave);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {

    }
}
