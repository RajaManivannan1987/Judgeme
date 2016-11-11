package utility.imaginet.com.judgeme.ui;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import utility.imaginet.com.judgeme.R;
import utility.imaginet.com.judgeme.adapter.TrendingClipsAdapter;
import utility.imaginet.com.judgeme.helper.AppController;
import utility.imaginet.com.judgeme.helper.PrefManager;
import utility.imaginet.com.judgeme.helper.VolleyCommonClass;
import utility.imaginet.com.judgeme.helper.judgeMeUrls;
import utility.imaginet.com.judgeme.interfaceClass.interfaceSelector;
import utility.imaginet.com.judgeme.models.TrendingClips;

/**
 * Created by IM0033 on 11/6/2015.
 */
public class TrendingClipsFragment extends Fragment {
    private String token;
    private ListView list;
    private RadioGroup radioGroup;
    private ImageView clipsZone;
    private PrefManager prefManager;
    private ImageLoader imageLoader;
    private ArrayList<TrendingClips> arrayList;
    private TrendingClipsAdapter adapter;
    private RadioButton allRadioButton, yearRadioButton, monthRadioButton, weekRadioButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_trending_clips, container, false);
        LoadData(v);
        return v;
    }

    private void LoadData(View v) {
        findViewById(v);
        arrayList = new ArrayList<TrendingClips>();
        imageLoader = AppController.getInstance().getImageLoader();
        prefManager = new PrefManager(getActivity());
        token = prefManager.getObjectId();
        commonVollyMethod(judgeMeUrls.TRENDINGCLIPS + "&token=" + token, v, 1);
        onClick(v);
    }

    private void onClick(final View v) {

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putString("clipID", arrayList.get(position).getClipID());
                Intent i = new Intent(getActivity(), CommonNextUpActivity.class);
                i.putExtras(bundle);
                startActivity(i);
                getActivity().overridePendingTransition(R.anim.activity_enter, R.anim.activity_leave);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private void findViewById(View v) {
        radioGroup = (RadioGroup) v.findViewById(R.id.radioGroup);
        weekRadioButton = (RadioButton) v.findViewById(R.id.weekRadioButton);
        yearRadioButton = (RadioButton) v.findViewById(R.id.yearRadioButton);
        monthRadioButton = (RadioButton) v.findViewById(R.id.monthRadioButton);
        allRadioButton = (RadioButton) v.findViewById(R.id.allRadioButton);
        clipsZone = (ImageView) v.findViewById(R.id.clipsZone);
        list = (ListView) v.findViewById(R.id.trndingClipsList);

    }

    private void RedioGroupMethod(final View v) {
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.weekRadioButton:
                        setDatePref(judgeMeUrls.SET_DATE_PREF + "&token=" + token + "&datecode=" + "This%20week", v, 2);
                        break;
                    case R.id.monthRadioButton:
                        setDatePref(judgeMeUrls.SET_DATE_PREF + "&token=" + token + "&datecode=" + "This%20month", v, 2);
                        break;
                    case R.id.yearRadioButton:
                        setDatePref(judgeMeUrls.SET_DATE_PREF + "&token=" + token + "&datecode=" + "This%20year", v, 2);
                        break;
                    case R.id.allRadioButton:
                        setDatePref(judgeMeUrls.SET_DATE_PREF + "&token=" + token + "&datecode=" + "Any", v, 2);
                        break;
                }
            }
        });
    }

    private void set1Response(JSONObject response, View v) {

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
                monthRadioButton.setChecked(true);
                RedioGroupMethod(v);
            } else if (datePref.startsWith("This year")) {
                yearRadioButton.setChecked(true);
                RedioGroupMethod(v);
            } else if (datePref.startsWith("This week")) {
                weekRadioButton.setChecked(true);
                RedioGroupMethod(v);
            } else if (datePref.startsWith("Any")) {
                allRadioButton.setChecked(true);
                RedioGroupMethod(v);
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

            JSONArray array = object.getJSONArray("clips");
            for (int i = 0; i < array.length(); i++) {
                TrendingClips setClips = new TrendingClips();
                JSONObject setting = array.getJSONObject(i);
                setClips.setIndex(setting.optString("index"));
                JSONObject getClips = setting.getJSONObject("clip");
                setClips.setClipID(getClips.optString("clipID"));
                setClips.setUid(getClips.optString("uid"));
                setClips.setArtistname(getClips.optString("artistname"));
                setClips.setArtistcity(getClips.optString("artistcity"));
                setClips.setArtiststate(getClips.optString("artiststate"));
                setClips.setArtistcountry(getClips.optString("artistcountry"));
                setClips.setArtistphotourl(getClips.optString("artistphotourl"));
                setClips.setStreamURL(getClips.optString("streamURL"));
                setClips.setTitle(getClips.optString("title"));
                arrayList.add(setClips);
            }
            adapter = new TrendingClipsAdapter(getActivity(), arrayList);
            list.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void imageLoader(String photoURL) {
        imageLoader.get(photoURL, new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer imageContainer, boolean b) {
                //clipsZone.setImageBitmap(imageContainer.getBitmap());
                ((MainActivity) getActivity()).changeZone(imageContainer.getBitmap());
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
    }

    private void setDatePref(String url, final View v, int type) {
        commonVollyMethod(url, v, type);
    }

    private void commonVollyMethod(String url, final View v, final int type) {
        VolleyCommonClass.getDataFromServer(url,  new interfaceSelector() {
            @Override
            public void volleyError(String error) {
            }

            @Override
            public void volleyResponse(JSONObject response) {
                if (type == 1) {
                    set1Response(response, v);
                } else if (type == 2) {
                    Type2method(response, v);
                }


            }
        });
    }

    private void Type2method(JSONObject response, final View v) {
        try {
            JSONObject object = response.getJSONObject("data");
            String resultCode = object.getString("resultcode");
            if (resultCode.startsWith("200")) {
                if (adapter != null) {
                    arrayList.clear();
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        commonVollyMethod(judgeMeUrls.TRENDINGCLIPS + "&token=" + token, v, 1);
                    }
                });

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
