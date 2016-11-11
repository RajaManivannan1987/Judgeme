package utility.imaginet.com.judgeme.ui;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import utility.imaginet.com.judgeme.R;
import utility.imaginet.com.judgeme.adapter.ArtistArtistAdapter;
import utility.imaginet.com.judgeme.helper.AppController;
import utility.imaginet.com.judgeme.helper.PrefManager;
import utility.imaginet.com.judgeme.helper.VolleyCommonClass;
import utility.imaginet.com.judgeme.helper.judgeMeUrls;
import utility.imaginet.com.judgeme.interfaceClass.interfaceSelector;
import utility.imaginet.com.judgeme.models.TrendingArtist;

/**
 * Created by IM0033 on 11/6/2015.
 */
public class TrendingArtistsFragment extends Fragment {
    private RadioGroup trending_ArtistRradioGroup;
    private RadioButton trending_ArtistWeekRadioButton, trending_ArtistMonthRadioButton, trending_ArtistYearRadioButton, trending_ArtistAllRadioButton;
    private ImageView trending_ArtistclipsZone;
    private ListView trending_ArtistList;
    private PrefManager prefManager;
    private ImageLoader imageLoader;
    private String token;
    private ArrayList<TrendingArtist> arrayList;
    private ArtistArtistAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_trending_artists, container, false);
        loadData(v);
        return v;
    }

    private void loadData(View v) {
        FindViewById(v);
        prefManager = new PrefManager(getActivity());
        token = prefManager.getObjectId();
        imageLoader = AppController.getInstance().getImageLoader();
        arrayList = new ArrayList<TrendingArtist>();
        commonVollyMethod(judgeMeUrls.TRENDINGARTIST + "&token=" + token, v, 2);
        onClick(v);

    }

    private void onClick(final View v) {
        trending_ArtistclipsZone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commonVollyMethod(judgeMeUrls.NEXTZONE + "&token=" + token, v, 3);
            }
        });
        trending_ArtistList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String uid = arrayList.get(position).getUid();
                Intent i = new Intent(getActivity(), UserProfileActivity.class);
                i.putExtra("uid", uid);
                startActivity(i);
                getActivity().overridePendingTransition(R.anim.activity_enter, R.anim.activity_leave);
            }
        });
    }
    private void checkingRadioButton(final View v) {
        trending_ArtistRradioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.trending_ArtistWeekRadioButton:
                        commonVollyMethod(judgeMeUrls.SET_DATE_PREF + "&token=" + token + "&datecode=" + "This%20week", v, 3);
                        break;
                    case R.id.trending_ArtistMonthRadioButton:
                        commonVollyMethod(judgeMeUrls.SET_DATE_PREF + "&token=" + token + "&datecode=" + "This%20month", v, 3);
                        break;
                    case R.id.trending_ArtistYearRadioButton:
                        commonVollyMethod(judgeMeUrls.SET_DATE_PREF + "&token=" + token + "&datecode=" + "This%20year", v, 3);
                        break;
                    case R.id.trending_ArtistAllRadioButton:
                        commonVollyMethod(judgeMeUrls.SET_DATE_PREF + "&token=" + token + "&datecode=" + "Any", v, 3);
                        break;
                    default:
                        break;
                }
            }
        });
    }


    private void setResponse(JSONObject response, View v) {
        try {
            if (adapter != null) {
                arrayList.clear();
                adapter.notifyDataSetChanged();
            }
            JSONObject object = response.getJSONObject("data");
            JSONObject userObject = object.getJSONObject("user");
            String Zone = userObject.optString("zonesetting");
            String datePref = userObject.optString("dateprefsetting");

            if (datePref.startsWith("This month")) {
                trending_ArtistMonthRadioButton.setChecked(true);
                checkingRadioButton(v);
            } else if (datePref.startsWith("This year")) {
                trending_ArtistYearRadioButton.setChecked(true);
                checkingRadioButton(v);
            } else if (datePref.startsWith("This week")) {
                trending_ArtistWeekRadioButton.setChecked(true);
                checkingRadioButton(v);
            } else if (datePref.startsWith("Any")) {
                trending_ArtistAllRadioButton.setChecked(true);
                checkingRadioButton(v);
            }
            if (Zone.startsWith("World")) {
                imageLoader(userObject.optString("worldiconURLlowres"));

            } else if (Zone.startsWith("Country")) {
                imageLoader(userObject.optString("countryiconURLlowres"));

            } else if (Zone.startsWith("State")) {
                imageLoader(userObject.optString("stateiconURLlowres"));

            } else if (Zone.startsWith("City")) {
                imageLoader(userObject.optString("cityiconURLlowres"));

            }
            JSONArray array = object.getJSONArray("artists");
            for (int i = 0; i < array.length(); i++) {

                TrendingArtist setData = new TrendingArtist();
                JSONObject setting = array.getJSONObject(i);
                setData.setIndex(setting.optString("index"));
                setData.setNetlikes(setting.optString("netlikes"));
                JSONObject getClips = setting.getJSONObject("artist");
                setData.setUid(getClips.optString("uid"));
                setData.setFirstname(getClips.optString("firstname"));
                setData.setLastname(getClips.optString("lastname"));
                setData.setCity(getClips.optString("city"));
                setData.setState(getClips.optString("state"));
                setData.setCountry(getClips.optString("country"));
                setData.setPhotoURL(getClips.optString("photoURL"));
                setData.setNumberoflikes(getClips.optString("numberoflikes"));
                arrayList.add(setData);
            }
            adapter = new ArtistArtistAdapter(getActivity(), arrayList);
            trending_ArtistList.setAdapter(adapter);
        } catch (Exception e) {

        }


    }


    private void FindViewById(View v) {
        trending_ArtistRradioGroup = (RadioGroup) v.findViewById(R.id.trending_ArtistRradioGroup);
        trending_ArtistWeekRadioButton = (RadioButton) v.findViewById(R.id.trending_ArtistWeekRadioButton);
        trending_ArtistMonthRadioButton = (RadioButton) v.findViewById(R.id.trending_ArtistMonthRadioButton);
        trending_ArtistYearRadioButton = (RadioButton) v.findViewById(R.id.trending_ArtistYearRadioButton);
        trending_ArtistAllRadioButton = (RadioButton) v.findViewById(R.id.trending_ArtistAllRadioButton);
        trending_ArtistclipsZone = (ImageView) v.findViewById(R.id.trending_ArtistclipsZone);
        trending_ArtistList = (ListView) v.findViewById(R.id.trending_ArtistList);

    }

    public void imageLoader(String photoURL) {
        imageLoader.get(photoURL, new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer imageContainer, boolean b) {
                ((MainActivity) getActivity()).changeZone(imageContainer.getBitmap());
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
    }

    private void commonVollyMethod(String url, final View v, final int type) {
        VolleyCommonClass.getDataFromServer(url, new interfaceSelector() {
            @Override
            public void volleyError(String error) {
                Toast.makeText(getActivity(), error, Toast.LENGTH_LONG).show();
            }

            @Override
            public void volleyResponse(JSONObject response) {
                if (type == 2) {
                    setResponse(response, v);
                } else if (type == 3) {
                    try {
                        JSONObject object = response.getJSONObject("data");
                        String resultCode = object.getString("resultcode");
                        if (resultCode.startsWith("200")) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    commonVollyMethod(judgeMeUrls.TRENDINGARTIST + "&token=" + token, v, 2);
                                }
                            });

                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


            }
        });
    }
}
