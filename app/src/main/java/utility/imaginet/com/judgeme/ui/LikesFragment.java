package utility.imaginet.com.judgeme.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import utility.imaginet.com.judgeme.R;
import utility.imaginet.com.judgeme.adapter.MyLikesAdapter;
import utility.imaginet.com.judgeme.helper.PrefManager;
import utility.imaginet.com.judgeme.helper.VolleyCommonClass;
import utility.imaginet.com.judgeme.helper.judgeMeUrls;
import utility.imaginet.com.judgeme.interfaceClass.interfaceSelector;
import utility.imaginet.com.judgeme.models.MyLikes;

/**
 * Created by IM0033 on 11/6/2015.
 */
public class LikesFragment extends Fragment {
    // private ImageLoader imageLoader;
    private ListView likesListView;
    private PrefManager prefManager;
    private ArrayList<MyLikes> likesArrayList;
    private MyLikesAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_likes, container, false);
        fitchData(v);
        return v;
    }

    private void fitchData(View v) {
        likesArrayList = new ArrayList<MyLikes>();
        likesListView = (ListView) v.findViewById(R.id.likesListView);
        prefManager = new PrefManager(getActivity());

        //imageLoader = AppController.getInstance().getImageLoader();
        commonVollyMethod(judgeMeUrls.MY_LIKES + "&token=" + prefManager.getObjectId());
    }

    private void setResponse(JSONObject response) {
        try {
            JSONObject object = response.getJSONObject("data");
            JSONArray clipsArray = object.getJSONArray("likes");
            for (int i = 0; i < clipsArray.length(); i++) {
                MyLikes setLikesDetails = new MyLikes();
                JSONObject responseData = clipsArray.getJSONObject(i);
                JSONObject clipsDetail = responseData.getJSONObject("clip");
                setLikesDetails.setClipID(clipsDetail.optString("clipID"));
                setLikesDetails.setUid(clipsDetail.optString("uid"));
                setLikesDetails.setArtistname(clipsDetail.optString("artistname"));
                setLikesDetails.setArtistcity(clipsDetail.optString("artistcity"));
                setLikesDetails.setArtiststate(clipsDetail.optString("artiststate"));
                setLikesDetails.setArtistcountry(clipsDetail.optString("artistcountry"));
                setLikesDetails.setArtistphotourl(clipsDetail.optString("artistphotourl"));
                // Log.d("Artistphotourl",clipsDetail.optString("artistphotourl"));

                setLikesDetails.setStreamURL(clipsDetail.optString("streamURL"));
                setLikesDetails.setTitle(clipsDetail.optString("title"));
                likesArrayList.add(setLikesDetails);
            }
            adapter = new MyLikesAdapter(getActivity(), likesArrayList);
            likesListView.setAdapter(adapter);

        } catch (Exception e) {

        }
    }

    private void commonVollyMethod(String url) {
        VolleyCommonClass.getDataFromServer(url, new interfaceSelector() {
            @Override
            public void volleyError(String error) {
            }

            @Override
            public void volleyResponse(JSONObject response) {
                setResponse(response);
            }
        });
    }

}
