package utility.imaginet.com.judgeme.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;

import utility.imaginet.com.judgeme.R;
import utility.imaginet.com.judgeme.helper.AppController;
import utility.imaginet.com.judgeme.models.TrendingArtist;

/**
 * Created by IM0033 on 11/23/2015.
 */
public class ArtistArtistAdapter extends BaseAdapter {
    Context activity;
    ArrayList<TrendingArtist> clipList;
    // ImageView trendingArtistProfile;
    private ViewHolder holder;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    private int[] colors = new int[]{0xAAd3d3d3, 0x00000000};


    public ArtistArtistAdapter(Context context, ArrayList<TrendingArtist> list) {
        this.activity = context;
        this.clipList = list;

        //Log.d("ArtistClipAdapter", list.toString());
    }


    @Override
    public int getCount() {
        return clipList.size();

    }

    @Override
    public Object getItem(int position) {

        return clipList.get(position);


    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = convertView;
        if (v == null) {
            holder = new ViewHolder();
            v = inflater.inflate(R.layout.trending_artist_custom_listview, null);
            if (imageLoader == null)
                imageLoader = AppController.getInstance().getImageLoader();
            holder.trendingArtistName = (TextView) v.findViewById(R.id.trendingArtistName);
            holder.trendingArtistLikes = (TextView) v.findViewById(R.id.trendingArtistLikes);
            holder.trendingArtistIndex = (TextView) v.findViewById(R.id.trendingArtistIndex);
            holder.trendingArtistProfile = (NetworkImageView) v.findViewById(R.id.trendingArtistProfile);
            v.setTag(holder);

        } else {
            holder = (ViewHolder) v.getTag();
        }
        if (position % 2 == 0) {
            v.setBackgroundColor(colors[0]);
        } else {
            v.setBackgroundColor(colors[1]);
        }
        final TrendingArtist Artist_List = clipList.get(position);
        holder.trendingArtistName.setText(Artist_List.getFirstname() + " " + Artist_List.getLastname());
        holder.trendingArtistIndex.setText("#" + Artist_List.getIndex());
        holder.trendingArtistLikes.setText(Artist_List.getNumberoflikes() + " Likes in " + Artist_List.getCountry());
        holder.trendingArtistProfile.setImageUrl(Artist_List.getPhotoURL(), imageLoader);
        //imageLoader(Artist_List.getPhotoURL());
        return v;
    }
/*
    private void imageLoader(String photoURL) {
        imageLoader.get(photoURL, new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer imageContainer, boolean b) {
                holder.trendingArtistProfile.setImageBitmap(imageContainer.getBitmap());
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
    }*/

    static class ViewHolder {
        private NetworkImageView trendingArtistProfile;
        private TextView trendingArtistName;
        private TextView trendingArtistLikes;
        private TextView trendingArtistIndex;
    }
}
