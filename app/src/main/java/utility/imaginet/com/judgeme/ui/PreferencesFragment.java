package utility.imaginet.com.judgeme.ui;

import android.app.Fragment;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import utility.imaginet.com.judgeme.R;
import utility.imaginet.com.judgeme.adapter.PreferenceAdapter;
import utility.imaginet.com.judgeme.helper.AppController;
import utility.imaginet.com.judgeme.helper.PrefManager;
import utility.imaginet.com.judgeme.helper.VolleyCommonClass;
import utility.imaginet.com.judgeme.helper.judgeMeUrls;
import utility.imaginet.com.judgeme.interfaceClass.interfaceSelector;
import utility.imaginet.com.judgeme.models.FitchArtists;
import utility.imaginet.com.judgeme.models.GenresSetting;
import utility.imaginet.com.judgeme.models.MyGenres;

/**
 * Created by IM0033 on 11/6/2015.
 */
public class PreferencesFragment extends Fragment {
    private GridView gridView;
    private CheckBox SelectCheckBox;
    private PreferenceAdapter adapter;
    private ImageView worldImageView, countryImageView, stateImageView, cityImageView;
    private LinearLayout worldLayout, countryLayout, statedLayout, cityLayout;
    private ArrayList<MyGenres> list;
    private ArrayList<GenresSetting> MyGenreslist;
    private PrefManager prefManager;
    private ImageLoader imageLoader;
    private String Gender, Age, Zone;
    private RadioGroup genderRadioGroup, ageRadioGroup;
    private RadioButton maleRadioButton, feMaleRadioButton, anyRadioButton, kidsRadioGroup, adultRadioGroup, anyageRadioGroup;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                genresList(judgeMeUrls.LISTGenresUrl);
            }
        });
        thread.start();

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_preference, container, false);
        getFindViewId(v);
        LoadData(v);
        return v;
    }

    private void LoadData(final View v) {
        prefManager = new PrefManager(getActivity());
        list = new ArrayList<MyGenres>();
        MyGenreslist = new ArrayList<GenresSetting>();
        imageLoader = AppController.getInstance().getImageLoader();
        commonVollyMethod(judgeMeUrls.FETCH_ARTIST + "&token=" + prefManager.getObjectId(), v, 2);

        worldLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                worldImageView.setBackgroundColor(Color.parseColor("#FFFFFF"));
                countryImageView.setBackgroundColor(Color.parseColor("#d3d3d3"));
                stateImageView.setBackgroundColor(Color.parseColor("#d3d3d3"));
                cityImageView.setBackgroundColor(Color.parseColor("#d3d3d3"));
                String Url = judgeMeUrls.SET_ZONE + "&token=" + prefManager.getObjectId() + "&zone=" + "World";
                jsonClass(Url, v, 4);

            }
        });
        countryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                worldImageView.setBackgroundColor(Color.parseColor("#d3d3d3"));
                countryImageView.setBackgroundColor(Color.parseColor("#FFFFFF"));
                stateImageView.setBackgroundColor(Color.parseColor("#d3d3d3"));
                cityImageView.setBackgroundColor(Color.parseColor("#d3d3d3"));
                String Url = judgeMeUrls.SET_ZONE + "&token=" + prefManager.getObjectId() + "&zone=" + "Country";
                jsonClass(Url, v, 4);

            }
        });
        statedLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                worldImageView.setBackgroundColor(Color.parseColor("#d3d3d3"));
                countryImageView.setBackgroundColor(Color.parseColor("#d3d3d3"));
                stateImageView.setBackgroundColor(Color.parseColor("#FFFFFF"));
                cityImageView.setBackgroundColor(Color.parseColor("#d3d3d3"));
                String Url = judgeMeUrls.SET_ZONE + "&token=" + prefManager.getObjectId() + "&zone=" + "State";
                jsonClass(Url, v, 4);

            }
        });
        cityLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                worldImageView.setBackgroundColor(Color.parseColor("#d3d3d3"));
                countryImageView.setBackgroundColor(Color.parseColor("#d3d3d3"));
                stateImageView.setBackgroundColor(Color.parseColor("#d3d3d3"));
                cityImageView.setBackgroundColor(Color.parseColor("#FFFFFF"));
                String Url = judgeMeUrls.SET_ZONE + "&token=" + prefManager.getObjectId() + "&zone=" + "City";
                jsonClass(Url, v, 4);

            }
        });
    }

    private void GenderRadioGroup(final View v) {
        genderRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                String genderUrl;
                switch (checkedId) {
                    case R.id.maleRadioButton:
                        genderUrl = judgeMeUrls.SETGENDER + "&token=" + prefManager.getObjectId() + "&gendercode=" + "M";
                        jsonClass(genderUrl, v, 3);
                        break;
                    case R.id.feMaleRadioButton:
                        genderUrl = judgeMeUrls.SETGENDER + "&token=" + prefManager.getObjectId() + "&gendercode=" + "F";
                        jsonClass(genderUrl, v, 3);
                        break;
                    case R.id.anyRadioButton:
                        genderUrl = judgeMeUrls.SETGENDER + "&token=" + prefManager.getObjectId() + "&gendercode=" + "Any";
                        jsonClass(genderUrl, v, 3);
                        break;

                }

            }
        });
    }

    private void AgeRadioGroup(final View v) {
        ageRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                String ageUrl;
                switch (checkedId) {
                    case R.id.kidsRadioGroup:
                        ageUrl = judgeMeUrls.SETAGEPRE + "&token=" + prefManager.getObjectId() + "&agecode=" + "Kid";
                        jsonClass(ageUrl, v, 3);
                        break;
                    case R.id.adultRadioGroup:
                        ageUrl = judgeMeUrls.SETAGEPRE + "&token=" + prefManager.getObjectId() + "&agecode=" + "Adult";
                        jsonClass(ageUrl, v, 3);
                        break;
                    case R.id.anyageRadioGroup:
                        ageUrl = judgeMeUrls.SETAGEPRE + "&token=" + prefManager.getObjectId() + "&agecode=" + "Any";
                        jsonClass(ageUrl, v, 3);
                        break;

                }

            }
        });
    }

    private void jsonClass(final String Url, final View v, final int type) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                commonVollyMethod(Url, v, type);
            }
        });
        thread.start();

    }

    private void genresList(String url) {
        VolleyCommonClass.getDataFromServer(url,  new interfaceSelector() {
            @Override
            public void volleyError(String error) {

            }

            @Override
            public void volleyResponse(JSONObject response) {
                try {
                    JSONObject obj = response.getJSONObject("data");
                    String resultcode = obj.optString("resultcode");
                    if (resultcode.startsWith("200")) {
                        String resultmessage = obj.optString("resultmessage");
                        JSONArray array = obj.getJSONArray("genres");

                        for (int i = 0; i < array.length(); i++) {
                            MyGenres setData = new MyGenres();
                            setData.setGenres(array.get(i).toString());
                            list.add(setData);
                        }

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        });

    }

    void imageLoader(String photoURL, final String ImageView) {
        imageLoader.get(photoURL, new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer imageContainer, boolean b) {
                if (ImageView.startsWith("worldImageView")) {
                    worldImageView.setImageBitmap(imageContainer.getBitmap());
                } else if (ImageView.startsWith("countryImageView")) {
                    countryImageView.setImageBitmap(imageContainer.getBitmap());
                } else if (ImageView.startsWith("stateImageView")) {
                    stateImageView.setImageBitmap(imageContainer.getBitmap());
                } else if (ImageView.startsWith("cityImageView")) {
                    cityImageView.setImageBitmap(imageContainer.getBitmap());
                }
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                VolleyLog.d("ErrorProPic", volleyError.getMessage());
            }
        });
    }

    void imageLoader1(String photoURL) {
        imageLoader.get(photoURL, new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer imageContainer, boolean b) {
                ((MainActivity) getActivity()).changeZone(imageContainer.getBitmap());
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                VolleyLog.d("ErrorProPic", volleyError.getMessage());
            }
        });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    private void getFindViewId(View v) {
        genderRadioGroup = (RadioGroup) v.findViewById(R.id.genderRadioGroup);
        ageRadioGroup = (RadioGroup) v.findViewById(R.id.ageRadioGroup);
        maleRadioButton = (RadioButton) v.findViewById(R.id.maleRadioButton);
        feMaleRadioButton = (RadioButton) v.findViewById(R.id.feMaleRadioButton);
        anyRadioButton = (RadioButton) v.findViewById(R.id.anyRadioButton);
        kidsRadioGroup = (RadioButton) v.findViewById(R.id.kidsRadioGroup);
        adultRadioGroup = (RadioButton) v.findViewById(R.id.adultRadioGroup);
        anyageRadioGroup = (RadioButton) v.findViewById(R.id.anyageRadioGroup);
        gridView = (GridView) v.findViewById(R.id.artistsGridView);
        SelectCheckBox = (CheckBox) v.findViewById(R.id.SelectCheckBox);
        if (SelectCheckBox.isChecked()) {
            SelectCheckBox.setText("Deselect All");
        } else {
            SelectCheckBox.setText("Select All");
        }
        worldLayout = (LinearLayout) v.findViewById(R.id.worldLayout);
        worldLayout.setPadding(2, 2, 2, 2);
        countryLayout = (LinearLayout) v.findViewById(R.id.countryLayout);
        countryLayout.setPadding(2, 2, 2, 2);
        statedLayout = (LinearLayout) v.findViewById(R.id.statedLayout);
        statedLayout.setPadding(2, 2, 2, 2);
        cityLayout = (LinearLayout) v.findViewById(R.id.cityLayout);
        cityLayout.setPadding(2, 2, 2, 2);
        worldImageView = (ImageView) v.findViewById(R.id.worldImageView);
        countryImageView = (ImageView) v.findViewById(R.id.countryImageView);
        stateImageView = (ImageView) v.findViewById(R.id.stateImageView);
        cityImageView = (ImageView) v.findViewById(R.id.cityImageView);
    }

    private void commonVollyMethod(String url, final View v, final int type) {
        VolleyCommonClass.getDataFromServer(url,new interfaceSelector() {
            @Override
            public void volleyError(String error) {
            }

            @Override
            public void volleyResponse(JSONObject response) {
                if (type == 2) {
                    Type2method(response, v);
                } else if (type == 4) {
                    Type4method(response, v);
                }


            }
        });
    }

    private void Type4method(JSONObject response, View v) {
        //Log.d("Type4method", response.toString());
        String zonesettings;
        JSONObject Object = null;
        try {
            Object = response.getJSONObject("data");
            JSONObject userObject = Object.getJSONObject("user");
            zonesettings = userObject.getString("zonesetting");
            if (zonesettings.startsWith("World")) {
                imageLoader1(userObject.getString("worldiconURLlowres"));
            } else if (Zone.startsWith("Country")) {
                imageLoader1(userObject.getString("countryiconURLlowres"));
            } else if (Zone.startsWith("State")) {
                imageLoader1(userObject.getString("stateiconURLlowres"));
            } else {
                imageLoader1(userObject.getString("cityiconURLlowres"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void Type2method(JSONObject response, final View v) {
        FitchArtists setUserData = new FitchArtists();
        setUserData.setArtistsResponseValue(response);
        try {
            JSONObject Object = response.getJSONObject("data");
            JSONObject userObject = Object.getJSONObject("user");
            JSONArray genresettingsArray = userObject.getJSONArray("genresettings");
            for (int i = 0; i < genresettingsArray.length(); i++) {
                GenresSetting setGenres = new GenresSetting();
                setGenres.setMyGenresSetting(genresettingsArray.get(i).toString());
                MyGenreslist.add(setGenres);

            }
            adapter = new PreferenceAdapter(getActivity(), MyGenreslist, true);
            //Log.d("MyGenreslist", MyGenreslist.toString());
            adapter = new PreferenceAdapter(getActivity(), list);
            gridView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            if (MyGenreslist.size() >= 20) {
                SelectCheckBox.setText("Deselect All");
                SelectCheckBox.setChecked(true);
            } else {
                SelectCheckBox.setText("Select All");
                SelectCheckBox.setChecked(false);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        SelectCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String checkboxUrl;
                if (isChecked) {
                    checkboxUrl = judgeMeUrls.ADD_ALL_GENREPREF + "&token=" + prefManager.getObjectId();
                    jsonClass(checkboxUrl, v, 3);
                    SelectCheckBox.setText("Deselect All");
                    int count = gridView.getAdapter().getCount();
                    for (int i = 0; i < count; i++) {
                        TextView textview = (TextView) gridView.findViewById(i);
                        textview.setBackgroundColor(getResources().getColor(R.color.purple));
                    }
                } else {
                    SelectCheckBox.setText("Select All");
                    checkboxUrl = judgeMeUrls.REMOVE_ALL_GENREPREF + "&token=" + prefManager.getObjectId();
                    jsonClass(checkboxUrl, v, 3);

                    int count = gridView.getAdapter().getCount();
                    for (int i = 0; i < count; i++) {
                        TextView textview = (TextView) gridView.findViewById(i);
                        textview.setBackgroundColor(Color.parseColor("#7e7e7e"));
                        adapter.notifyDataSetChanged();
                    }

                }
            }
        });

        if (setUserData.getResultcode().startsWith("200")) {
            Gender = setUserData.getGenderprefsetting();
            Age = setUserData.getAgesetting();
            Zone = setUserData.getZonesetting();

            imageLoader(setUserData.getWorldiconURLlowres(), "worldImageView");
            imageLoader(setUserData.getCountryiconURLlowres(), "countryImageView");
            imageLoader(setUserData.getStateiconURLlowres(), "stateImageView");
            imageLoader(setUserData.getCityiconURLlowres(), "cityImageView");

            if (Gender.startsWith("Any")) {
                anyRadioButton.setChecked(true);
                GenderRadioGroup(v);
            } else if (Gender.startsWith("M")) {
                maleRadioButton.setChecked(true);
                GenderRadioGroup(v);
            } else if (Gender.startsWith("F")) {
                feMaleRadioButton.setChecked(true);
                GenderRadioGroup(v);
            }

            if (Age.startsWith("Any")) {
                anyageRadioGroup.setChecked(true);
                AgeRadioGroup(v);
            } else if (Age.startsWith("Kid")) {
                kidsRadioGroup.setChecked(true);
                AgeRadioGroup(v);
            } else {
                adultRadioGroup.setChecked(true);
                AgeRadioGroup(v);
            }

            if (Zone.startsWith("World")) {
                worldImageView.setBackgroundColor(Color.parseColor("#FFFFFF"));
                countryImageView.setBackgroundColor(Color.parseColor("#d3d3d3"));
                stateImageView.setBackgroundColor(Color.parseColor("#d3d3d3"));
                cityImageView.setBackgroundColor(Color.parseColor("#d3d3d3"));
                //((MainActivity) getActivity()).changeZone();

            } else if (Zone.startsWith("Country")) {
                worldImageView.setBackgroundColor(Color.parseColor("#d3d3d3"));
                countryImageView.setBackgroundColor(Color.parseColor("#FFFFFF"));
                stateImageView.setBackgroundColor(Color.parseColor("#d3d3d3"));
                cityImageView.setBackgroundColor(Color.parseColor("#d3d3d3"));
                // ((MainActivity) getActivity()).changeZone();
            } else if (Zone.startsWith("State")) {
                worldImageView.setBackgroundColor(Color.parseColor("#d3d3d3"));
                countryImageView.setBackgroundColor(Color.parseColor("#d3d3d3"));
                stateImageView.setBackgroundColor(Color.parseColor("#FFFFFF"));
                cityImageView.setBackgroundColor(Color.parseColor("#d3d3d3"));
                //((MainActivity) getActivity()).changeZone();
            } else {
                worldImageView.setBackgroundColor(Color.parseColor("#d3d3d3"));
                countryImageView.setBackgroundColor(Color.parseColor("#d3d3d3"));
                stateImageView.setBackgroundColor(Color.parseColor("#d3d3d3"));
                cityImageView.setBackgroundColor(Color.parseColor("#FFFFFF"));
                //((MainActivity) getActivity()).changeZone();
            }
        }
    }

}
