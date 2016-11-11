package utility.imaginet.com.judgeme.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import utility.imaginet.com.judgeme.R;
import utility.imaginet.com.judgeme.adapter.MyCommentsAdapter;
import utility.imaginet.com.judgeme.helper.PrefManager;
import utility.imaginet.com.judgeme.helper.VolleyCommonClass;
import utility.imaginet.com.judgeme.helper.judgeMeUrls;
import utility.imaginet.com.judgeme.interfaceClass.interfaceSelector;
import utility.imaginet.com.judgeme.models.MyComments;

/**
 * Created by IM0033 on 11/6/2015.
 */
public class DiscussionFragment extends Fragment {
    private PrefManager prefManager;
    private ListView commentsListView;
    private ArrayList<MyComments> arrayList;
    private MyCommentsAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_discussion, container, false);
        commentsListView = (ListView) v.findViewById(R.id.commentsListView);
        onClick(v);
        return v;
    }


    private void onClick(View v) {
        prefManager = new PrefManager(getActivity());
        arrayList = new ArrayList<MyComments>();
        commonVollyMethod(judgeMeUrls.FETCH_COMMENT + "&token=" + prefManager.getObjectId());
    }

    private void setResponse(JSONObject response) {
        try {
            JSONObject object = response.getJSONObject("data");
            JSONArray array = object.getJSONArray("clips");
            for (int i = 0; i < array.length(); i++) {
                MyComments setData = new MyComments();
                JSONObject commentDetail = array.getJSONObject(i);
                setData.setClipID(commentDetail.optString("clipID"));
                setData.setUid(commentDetail.optString("uid"));
                setData.setArtistname(commentDetail.optString("artistname"));
                setData.setTitle(commentDetail.optString("title"));
                setData.setUnreadcomments(commentDetail.optString("unreadcomments"));
                arrayList.add(setData);
            }
            adapter = new MyCommentsAdapter(getActivity(), arrayList);
            commentsListView.setAdapter(adapter);

        } catch (Exception e) {

        }
    }

    private void commonVollyMethod(String url) {
        VolleyCommonClass.getDataFromServer(url, new interfaceSelector() {
            @Override
            public void volleyError(String error) {
                Toast.makeText(getActivity(), error, Toast.LENGTH_LONG).show();
            }

            @Override
            public void volleyResponse(JSONObject response) {
                setResponse(response);
            }
        });
    }
}
