package utility.imaginet.com.judgeme.adapter;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

import utility.imaginet.com.judgeme.R;
import utility.imaginet.com.judgeme.models.UserClipsDetails;
import utility.imaginet.com.judgeme.ui.CommonNextUpActivity;

/**
 * Created by IM0033 on 11/23/2015.
 */
public class ArtistClipAdapter extends BaseAdapter {
    private MediaPlayer mp = new MediaPlayer();
    Context activity;
    ArrayList<UserClipsDetails> clipList;
    ViewHolder holder;
    int isClickedPosition = -1;
    private int[] colors = new int[]{0xAAd3d3d3, 0x00000000};

    public ArtistClipAdapter(Context context, ArrayList<UserClipsDetails> list) {
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = convertView;
        if (v == null) {
            holder = new ViewHolder();
            v = inflater.inflate(R.layout.artist_clips_list, null);
            holder.clipsMainlinear = (LinearLayout) v.findViewById(R.id.clipsMainlinear);
            holder.clip_title = (TextView) v.findViewById(R.id.artist_clip_title);
            holder.clip_likes = (TextView) v.findViewById(R.id.artist_clips_like_textView);
            holder.play_button = (ImageView) v.findViewById(R.id.audio_playbutton);
            holder.play_button_pink = (ImageView) v.findViewById(R.id.artist_play_button_pink);
            // holder.soundOnlyPlay = (VideoView) v.findViewById(R.id.sound_only);
            //holder.soundOnlyPlay.setVisibility(View.INVISIBLE);
            v.setTag(holder);


        } else {
            holder = (ViewHolder) v.getTag();
        }
        if (position % 2 == 0) {
            v.setBackgroundColor(colors[0]);
        } else {
            v.setBackgroundColor(colors[1]);
        }
        final UserClipsDetails clip_List = clipList.get(position);
        holder.clip_title.setText("'" + clip_List.getClip_title() + "'");
        holder.clip_likes.setText(clip_List.getClip_likescount() + " " + "Likes");

        if (position == isClickedPosition) {
            holder.play_button_pink.setVisibility(View.VISIBLE);
            holder.play_button.setVisibility(View.GONE);
        } else {
            holder.play_button_pink.setVisibility(View.GONE);
            holder.play_button.setVisibility(View.VISIBLE);
        }
        holder.play_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mp.reset();
                    mp.setDataSource(activity, Uri.parse(clip_List.getClip_streamURL()));
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
                    //mp.release();
                }
                isClickedPosition = -1;
                notifyDataSetChanged();
            }
        });
        holder.clipsMainlinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("clipID", clip_List.getClipID());
                Intent i = new Intent(activity, CommonNextUpActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtras(bundle);
                activity.startActivity(i);
            }
        });
        return v;
    }

    static class ViewHolder {
        private LinearLayout clipsMainlinear;
        private ImageView play_button, play_button_pink;
        private TextView clip_title;
        private TextView clip_likes;
        // private VideoView soundOnlyPlay;


    }
}
