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
import utility.imaginet.com.judgeme.models.TrendingClips;


/**
 * Created by IM0033 on 11/23/2015.
 */
public class TrendingClipsAdapter extends BaseAdapter {
    Context activity;
    ArrayList<TrendingClips> clipList;
    ViewHolder holder;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    private int[] colors = new int[]{0xAAd3d3d3, 0x00000000};


    public TrendingClipsAdapter(Context context, ArrayList<TrendingClips> list) {
        this.activity = context;
        this.clipList = list;
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
            v = inflater.inflate(R.layout.trending_clips_custom_listview, null);
            if (imageLoader == null)
                imageLoader = AppController.getInstance().getImageLoader();
            holder.profileImage = (NetworkImageView) v.findViewById(R.id.trendingclipProfile);
            holder.clip_title = (TextView) v.findViewById(R.id.trendingclipTitle);
            holder.cliper_Name = (TextView) v.findViewById(R.id.trendingAtristName);
            holder.trendingclipaddress = (TextView) v.findViewById(R.id.trendingclipaddress);
            holder.trendingclipindex = (TextView) v.findViewById(R.id.trendingclipindex);
            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }
        if (position % 2 == 0) {
            v.setBackgroundColor(colors[0]);
        } else {
            v.setBackgroundColor(colors[1]);
        }
        final TrendingClips TrendingClipList = clipList.get(position);
        holder.clip_title.setText("'" + TrendingClipList.getTitle() + "'");
        holder.cliper_Name.setText(TrendingClipList.getArtistname());
        holder.trendingclipaddress.setText(TrendingClipList.getArtistcity() + "," + " " + TrendingClipList.getArtistcountry());
        holder.trendingclipindex.setText("#" + TrendingClipList.getIndex());
        holder.profileImage.setImageUrl(TrendingClipList.getArtistphotourl(), imageLoader);
        //imageLoader(clipList.get(position).getArtistphotourl());
        return v;
    }
    static class ViewHolder {
        private NetworkImageView profileImage;
        private TextView clip_title;
        private TextView cliper_Name;
        private TextView trendingclipaddress;
        private TextView trendingclipindex;

    }
}
