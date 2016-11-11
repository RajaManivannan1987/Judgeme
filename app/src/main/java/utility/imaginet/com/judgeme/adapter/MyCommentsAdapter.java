package utility.imaginet.com.judgeme.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import utility.imaginet.com.judgeme.R;
import utility.imaginet.com.judgeme.models.MyComments;
import utility.imaginet.com.judgeme.ui.CommandActivity;

public class MyCommentsAdapter extends BaseAdapter {
    Context activity;
    ArrayList<MyComments> commentsList;
    private ViewHolder holder;
    private int[] colors = new int[]{0x00000000, 0xAAd3d3d3};


    public MyCommentsAdapter(Context context, ArrayList<MyComments> list) {
        this.activity = context;
        this.commentsList = list;
    }


    @Override
    public int getCount() {
        return commentsList.size();

    }

    @Override
    public Object getItem(int position) {
        return commentsList.get(position);
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
            v = inflater.inflate(R.layout.mycomments_custom_listview, null);

            holder.myCommentArtistName = (TextView) v.findViewById(R.id.myCommentArtistName);
            holder.myCommentTitle = (TextView) v.findViewById(R.id.myCommentTitle);
            holder.commentsReadImageView = (TextView) v.findViewById(R.id.commentsReadImageView);
            holder.myComment_LinearLayout = (LinearLayout) v.findViewById(R.id.myComment_LinearLayout);
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
        holder.myCommentArtistName.setText(commentsList.get(position).getArtistname());
        holder.myCommentTitle.setText(commentsList.get(position).getTitle());
        String Unreadcomments = commentsList.get(position).getUnreadcomments();
        if (Unreadcomments.startsWith("0")) {
            holder.commentsReadImageView.setTextColor(Color.BLUE);
            holder.commentsReadImageView.setText(Unreadcomments);
        } else {
            holder.commentsReadImageView.setText(Unreadcomments);
            holder.commentsReadImageView.setTextColor(Color.parseColor("#e53173"));
        }
        holder.myComment_LinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("clipID", commentsList.get(position).getClipID());
                Intent i = new Intent(activity, CommandActivity.class);
                // Intent i = new Intent(activity, TreeViewListView.class);
                i.putExtras(bundle);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.startActivity(i);
            }
        });
        return v;
    }

    static class ViewHolder {
        private TextView commentsReadImageView;
        private TextView myCommentArtistName;
        private TextView myCommentTitle;
        private LinearLayout myComment_LinearLayout;

    }

}
