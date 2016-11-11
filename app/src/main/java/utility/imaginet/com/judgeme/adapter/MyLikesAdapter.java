package utility.imaginet.com.judgeme.adapter;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.io.IOException;
import java.util.ArrayList;

import utility.imaginet.com.judgeme.R;
import utility.imaginet.com.judgeme.helper.AppController;
import utility.imaginet.com.judgeme.models.MyLikes;
import utility.imaginet.com.judgeme.ui.CommonNextUpActivity;

/**
 * Created by IM0033 on 11/23/2015.
 */
public class MyLikesAdapter extends BaseAdapter {
    private MediaPlayer mp = new MediaPlayer();
    Context activity;
    ArrayList<MyLikes> clipList;
    private ViewHolder holder;
    private ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    private int[] colors = new int[]{0x00000000, 0xAAd3d3d3};
    int isClickedPosition = -1;


    public MyLikesAdapter(Context context, ArrayList<MyLikes> list) {
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = convertView;
        if (v == null) {
            holder = new ViewHolder();
            v = inflater.inflate(R.layout.mylikes_custom_listview, null);
            if (imageLoader == null)
                imageLoader = AppController.getInstance().getImageLoader();
            holder.main_like_LinearLayout = (LinearLayout) v.findViewById(R.id.main_like_LinearLayout);
            holder.myLikesClipName = (TextView) v.findViewById(R.id.myLikesClipName);
            holder.myLikesArtistName = (TextView) v.findViewById(R.id.myLikesArtistName);
            holder.myLikesArtistCity = (TextView) v.findViewById(R.id.myLikesArtistCity);
            holder.likes_play_button = (ImageView) v.findViewById(R.id.likes_play_button);
            holder.trendingArtistProfile = (NetworkImageView) v.findViewById(R.id.myLikesProfile);
            //holder.like_sound_only = (VideoView) v.findViewById(R.id.like_sound_only);
            holder.play_button_pink = (ImageView) v.findViewById(R.id.play_button_pink);
            v.setTag(holder);

            /*int colorPosition = position % colors.length;
            v.setBackgroundColor(colors[colorPosition]);*/

        } else {
            holder = (ViewHolder) v.getTag();
        }

        if (position % 2 == 0) {
            v.setBackgroundColor(colors[0]);
        } else {
            v.setBackgroundColor(colors[1]);
        }
        final MyLikes MyLikes_List = clipList.get(position);
        holder.myLikesClipName.setText("'" + MyLikes_List.getTitle() + "'");
        holder.myLikesArtistName.setText(MyLikes_List.getArtistname());
        holder.myLikesArtistCity.setText(MyLikes_List.getArtistcity() + ", " + MyLikes_List.getArtistcountry());

        String imageUrl = MyLikes_List.getArtistphotourl();
        if (!imageUrl.startsWith("null")) {
           // imageLoader(imageUrl);
            holder.trendingArtistProfile.setImageUrl(imageUrl, imageLoader);
        }else {
            Log.d("imageUrl", imageUrl);
        }

        if (position == isClickedPosition) {
            holder.play_button_pink.setVisibility(View.VISIBLE);
            holder.likes_play_button.setVisibility(View.GONE);
        } else {
            holder.play_button_pink.setVisibility(View.GONE);
            holder.likes_play_button.setVisibility(View.VISIBLE);
        }
        holder.likes_play_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mp.reset();
                    mp.setDataSource(activity, Uri.parse(MyLikes_List.getStreamURL()));
                    mp.prepare();
                    mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        mp.start();
                    }
                });
                thread.start();
                isClickedPosition = position;
                notifyDataSetChanged();
            }
        });
        holder.play_button_pink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mp != null && mp.isPlaying()) {
                    mp.pause();
                    mp.stop();
                   // mp.release();
                }
                isClickedPosition = -1;
                notifyDataSetChanged();
            }
        });

        holder.main_like_LinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("clipID", clipList.get(position).getClipID());
                Intent i = new Intent(activity, CommonNextUpActivity.class);
                i.putExtras(bundle);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.startActivity(i);
            }
        });
        return v;
    }

/*    private void imageLoader(String photoURL) {
        imageLoader.get(photoURL, new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer imageContainer, boolean b) {
                holder.trendingArtistProfile.setImageBitmap(imageContainer.getBitmap());
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                VolleyLog.d("ErrorProPic", volleyError.getMessage());
            }
        });

    }*/

    static class ViewHolder {
        NetworkImageView trendingArtistProfile;
        private ImageView  likes_play_button, play_button_pink;
        private TextView myLikesClipName;
        private TextView myLikesArtistName;
        private TextView myLikesArtistCity;
        // private VideoView like_sound_only;
        private LinearLayout main_like_LinearLayout;
    }

}
