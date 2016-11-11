package utility.imaginet.com.judgeme.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import utility.imaginet.com.judgeme.R;
import utility.imaginet.com.judgeme.adapter.GenresAdapter1;
import utility.imaginet.com.judgeme.helper.PrefManager;
import utility.imaginet.com.judgeme.helper.SimpleGestureFilter;
import utility.imaginet.com.judgeme.helper.VolleyCommonClass;
import utility.imaginet.com.judgeme.helper.judgeMeUrls;
import utility.imaginet.com.judgeme.interfaceClass.interfaceSelector;
import utility.imaginet.com.judgeme.models.MyGenres;

public class MyGenresActivity extends Activity implements SimpleGestureFilter.SimpleGestureListener {
    private SimpleGestureFilter detector;
    private ArrayList<MyGenres> list;
    private PrefManager prefManager;
    private CheckBox checkbox;
    private GridView gridView;
    public static boolean checkValue = false;
    private GenresAdapter1 adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            setContentView(R.layout.my_genres);
            prefManager = new PrefManager(getApplicationContext());
            detector = new SimpleGestureFilter(this, this);
            list = new ArrayList<>();
            gridView = (GridView) findViewById(R.id.myGenresGridView);
            checkbox = (CheckBox) findViewById(R.id.seleceAllCheckBox);

            checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                                    @Override
                                                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                        if (isChecked) {
                                                            String checkboxUrl = judgeMeUrls.ADD_ALL_GENREPREF + "&token=" + prefManager.getObjectId();
                                                            jsonClass(checkboxUrl);
                                                            int count = gridView.getAdapter().getCount();
                                                            for (int i = 0; i < count; i++) {
                                                                TextView textview = (TextView) gridView.findViewById(i);
                                                                textview.setBackgroundColor(getResources().getColor(R.color.purple));
                        /*checkValue=true;
                    adapter.notifyDataSetChanged();*/
                                                            }
                                                        } else {
                                                            //c.setChecked(true);
                                                            String checkboxUrl = judgeMeUrls.REMOVE_ALL_GENREPREF + "&token=" + prefManager.getObjectId();
                                                            jsonClass(checkboxUrl);
                                                            int count = gridView.getAdapter().getCount();
                                                            for (int i = 0; i < count; i++) {
                                                                TextView textview = (TextView) gridView.findViewById(i);
                                                                textview.setBackgroundColor(Color.parseColor("#7e7e7e"));
                                                                adapter.notifyDataSetChanged();
                                                            }
                                                            checkValue = false;
                                                            adapter.notifyDataSetChanged();
                                                        }
                                                    }
                                                }

            );
            commonVollyMethod(judgeMeUrls.LISTGenresUrl, 1);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent me) {
        this.detector.onTouchEvent(me);
        return super.dispatchTouchEvent(me);
    }

    private void jsonClass(String Url) {
        commonVollyMethod(Url, 2);
    }

    @Override
    public void onSwipe(int direction) {
        String str = "";

        switch (direction) {

            case SimpleGestureFilter.SWIPE_RIGHT:
                str = "Swipe Right";
                break;
            case SimpleGestureFilter.SWIPE_LEFT:
                Intent i = new Intent(getApplicationContext(), HelperActivity.class);
                startActivity(i);
                str = "Swipe Left";
                break;
            case SimpleGestureFilter.SWIPE_DOWN:
                str = "Swipe Down";
                break;
            case SimpleGestureFilter.SWIPE_UP:
                str = "Swipe Up";
                break;

        }
        //Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.animation_enter, R.anim.animation_leave);
    }

    @Override
    public void onDoubleTap() {
    }

    private void commonVollyMethod(String url, final int type) {
        VolleyCommonClass.getDataFromServer(url,  new interfaceSelector() {
            @Override
            public void volleyError(String error) {
            }

            @Override
            public void volleyResponse(JSONObject response) {
                if (type == 1) {
                    Teye1method(response);
                }

            }
        });
    }

    private void Teye1method(JSONObject response) {
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
                //  adapter.notifyDataSetChanged();
                adapter = new GenresAdapter1(MyGenresActivity.this, list, "");
                gridView.setAdapter(adapter);
                gridView.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE_MODAL);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
