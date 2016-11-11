package utility.imaginet.com.judgeme.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;

import utility.imaginet.com.judgeme.R;
import utility.imaginet.com.judgeme.helper.PrefManager;
import utility.imaginet.com.judgeme.helper.VolleyCommonClass;
import utility.imaginet.com.judgeme.helper.judgeMeUrls;
import utility.imaginet.com.judgeme.interfaceClass.interfaceSelector;
import utility.imaginet.com.judgeme.models.GenresSetting;
import utility.imaginet.com.judgeme.models.MyGenres;


/**
 * Created by IM0033 on 11/5/2015.
 */
public class PreferenceAdapter extends BaseAdapter {
    private static ArrayList<MyGenres> list;
    private static ArrayList<GenresSetting> MyGenerList;
    private Context context;
    PrefManager prefManager;
    boolean flag = false;


    // String genres;

    public PreferenceAdapter(Context context, ArrayList<MyGenres> mobileValues) {
        this.context = context;
        this.list = mobileValues;
    }


    public PreferenceAdapter(Context context, ArrayList<GenresSetting> GenerList, boolean flag) {
        this.context = context;
        this.MyGenerList = GenerList;
        this.flag = flag;
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position).getGenres();
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View gridView;


        if (convertView == null) {
            gridView = new View(context);

            gridView = inflater.inflate(R.layout.preference_textview, null);
            final TextView textView;

            textView = (TextView) gridView
                    .findViewById(R.id.preference_Textview);
            textView.setBackgroundColor(Color.parseColor("#7e7e7e"));

            final MyGenres myList = list.get(position);
            //  final GenresSetting list123 = MyGenerList.get(position);
            textView.setText(myList.getGenres());
            textView.setId(position);
            for (int i = 0; i < list.size(); i++) {

                for (int j = 0; j < MyGenerList.size(); j++) {

                    String listStr = list.get(position).getGenres();

                    String MyListStr = MyGenerList.get(j).getMyGenresSetting();
                    if (listStr.equalsIgnoreCase(MyListStr)) {
                        textView.setBackgroundColor(Color.parseColor("#834caf"));
                    }

                }
            }


            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    prefManager = new PrefManager(context);
                    String url;

                    ColorDrawable cd = (ColorDrawable) textView.getBackground();
                    String colorCode = String.valueOf(cd.getColor());

                    if (colorCode.startsWith("-8487298")) {
                        textView.setBackgroundColor(Color.parseColor("#834caf"));
                        url = judgeMeUrls.ADD_GENRE_PREF + "&token=" + prefManager.getObjectId() + "&genre=" + textView.getText().toString();
                        jsonClass(url);

                    } else if (colorCode.startsWith("-8172369")) {
                        textView.setBackgroundColor(Color.parseColor("#7e7e7e"));
                        url = judgeMeUrls.REMOVE_GENRE_PREF + "&token=" + prefManager.getObjectId() + "&genre=" + textView.getText().toString();
                        jsonClass(url);

                    }

                }
            });


        } else {
            gridView = (View) convertView;
        }

        return gridView;
    }

    public void jsonClass(String url) {
        VolleyCommonClass.getDataFromServer(url,  new interfaceSelector() {
            @Override
            public void volleyError(String error) {
            }

            @Override
            public void volleyResponse(JSONObject response) {
                notifyDataSetChanged();

            }
        });
    }

}
