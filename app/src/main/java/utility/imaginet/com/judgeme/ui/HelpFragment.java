package utility.imaginet.com.judgeme.ui;

import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import utility.imaginet.com.judgeme.R;

/**
 * Created by IM0033 on 12/10/2015.
 */
public class HelpFragment extends Fragment {
    private ListView helpListview;
    private TextView versionTextView;
    String[] values = new String[]{"Terms & Condition", "Help", "Privacy Policy"};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.help_fragment, container, false);
        LoadData(v);
        return v;
    }


    private void LoadData(View v) {
        helpListview = (ListView) v.findViewById(R.id.helpListview);
        versionTextView= (TextView) v.findViewById(R.id.versionTextView);
        PackageInfo pInfo = null;
        try {
            pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
            int verCode = pInfo.versionCode;
            String version = pInfo.versionName;
            versionTextView.setText("Current version :"+version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1, values);
        helpListview.setAdapter(adapter);
        helpListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String itemValue = (String) helpListview.getItemAtPosition(position);
                Intent i = new Intent(getActivity(), AboutHelperActivity.class);
                i.putExtra("help", itemValue);
                startActivity(i);
            }
        });
    }
}