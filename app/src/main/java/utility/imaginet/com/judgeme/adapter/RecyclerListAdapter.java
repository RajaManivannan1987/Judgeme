/*
 * Copyright (C) 2015 Paul Burke
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package utility.imaginet.com.judgeme.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

import utility.imaginet.com.judgeme.R;
import utility.imaginet.com.judgeme.helper.ItemTouchHelperAdapter;
import utility.imaginet.com.judgeme.helper.ItemTouchHelperViewHolder;
import utility.imaginet.com.judgeme.helper.OnStartDragListener;
import utility.imaginet.com.judgeme.helper.PrefManager;
import utility.imaginet.com.judgeme.helper.VolleyCommonClass;
import utility.imaginet.com.judgeme.helper.judgeMeUrls;
import utility.imaginet.com.judgeme.interfaceClass.interfaceSelector;
import utility.imaginet.com.judgeme.models.MyProfiles;
import utility.imaginet.com.judgeme.ui.UploadActivity;


public class RecyclerListAdapter extends RecyclerView.Adapter<RecyclerListAdapter.ItemViewHolder>
        implements ItemTouchHelperAdapter {
    ArrayList<MyProfiles> listValue;
    Context context;
    String clipId, token;
    PrefManager prefManager;
    private int[] colors = new int[]{0xAAd3d3d3, 0x00000000};


    // private final List<String> mItems = new ArrayList<>();

    private final OnStartDragListener mDragStartListener;
/*
    public RecyclerListAdapter(Context context, OnStartDragListener dragStartListener) {
        mDragStartListener = dragStartListener;
        //mItems.addAll(Arrays.asList(context.getResources().getStringArray(R.array.dummy_items)));
    }*/

    public RecyclerListAdapter(Activity activity, ArrayList<MyProfiles> list, OnStartDragListener dragStartListener) {
        this.listValue = list;
        this.context = activity;
        mDragStartListener = dragStartListener;
        prefManager = new PrefManager(context);
    }


    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.myprofile_custom_listitem, parent, false);
        ItemViewHolder itemViewHolder = new ItemViewHolder(view);
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, final int position) {
        MyProfiles clipCount = new MyProfiles();
        int Count =Integer.parseInt(clipCount.getCount());
       // Log.d("Count",""+Count);
        /*if (position % 2 == 0) {
            holder.backgroundLayout.setBackgroundColor(colors[0]);
        } else {
            holder.backgroundLayout.setBackgroundColor(colors[1]);
        }*/
        if (position >= Count) {
            holder.backgroundLayout.setBackground(context.getResources().getDrawable(R.drawable.stripe_disabled_icon));
        } else {
            holder.backgroundLayout.setBackgroundColor(Color.parseColor("#00000000"));
        }
        holder.clip_title.setText("'" + listValue.get(position).getClip_title() + "'");
        holder.cliper_likes.setText(listValue.get(position).getClip_likescount() + " " + "Likes");
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(v
                        .getRootView().getContext());
                alertDialog.setTitle("Are you sure?");
                alertDialog.setMessage("Do you want to delete this" + " '" + listValue.get(position).getClip_title() + "' " + "clip");
                alertDialog.setPositiveButton("yes Delete",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                token = prefManager.getObjectId();
                                clipId = listValue.get(position).getClipID();
                                DeleteClips(judgeMeUrls.DELETE_CLIPS + "&token=" + token + "&clipID=" + clipId, position);
                            }
                        });
                alertDialog
                        .setNegativeButton("Leave It",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                }).create().show();

            }

        });
        //onItemMove(position);
        holder.mainLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clipId = listValue.get(position).getClipID();
                Bundle bundle = new Bundle();
                bundle.putString("clipID", clipId);
                Intent i = new Intent(context, UploadActivity.class);
                i.putExtras(bundle);
                context.startActivity(i);
            }
        });
        // Start a drag whenever the handle view it touched
        holder.mainLinear.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                clipId = listValue.get(position).getClipID();
                if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                    mDragStartListener.onStartDrag(holder);
                }
                return false;
            }
        });
    }

    @Override
    public void onItemDismiss(int position) {
        Toast.makeText(context, ""+position, Toast.LENGTH_SHORT).show();
        listValue.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        token = prefManager.getObjectId();
        Collections.swap(listValue, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        String url = judgeMeUrls.CLIPINDEX + "&token=" + token + "&clipID=" + clipId + "&newindex=" + toPosition;
        ChangeClipIndex(url);
        return true;
    }

    private void ChangeClipIndex(String url) {
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

    @Override
    public int getItemCount() {
        return listValue.size();
    }

    /**
     * Simple example of a view holder that implements {@link ItemTouchHelperViewHolder} and has a
     * "handle" view that initiates a drag event when touched.
     */
    public static class ItemViewHolder extends RecyclerView.ViewHolder implements
            ItemTouchHelperViewHolder {

        private TextView clip_title;
        private TextView cliper_likes;
        private LinearLayout mainLinear,backgroundLayout;
        private ImageView imageView;


        public ItemViewHolder(View itemView) {
            super(itemView);
            mainLinear = (LinearLayout) itemView.findViewById(R.id.mainLinear);
            clip_title = (TextView) itemView.findViewById(R.id.myprofile_clip_title);
            cliper_likes = (TextView) itemView.findViewById(R.id.myprofile_clips_like_textView);
            cliper_likes = (TextView) itemView.findViewById(R.id.myprofile_clips_like_textView);
            imageView = (ImageView) itemView.findViewById(R.id.myprofile_clips_delete_button);
            backgroundLayout= (LinearLayout) itemView.findViewById(R.id.backgroundLayout);
        }

        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY);
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(0);
        }
    }

    private void DeleteClips(String url, final int position) {

        final ProgressDialog dialog = new ProgressDialog(context);
        dialog.setMessage("Deleting the clip...");
        dialog.show();
        dialog.setCancelable(false);
        VolleyCommonClass.getDataFromServer(url,  new interfaceSelector() {
            @Override
            public void volleyError(String error) {
                dialog.dismiss();
            }

            @Override
            public void volleyResponse(JSONObject response) {
                // Log.d("deleteUrl", response.toString());
                try {
                    JSONObject object = response.getJSONObject("data");
                    String resultcode = object.getString("resultcode");
                    String resultmessage = object.getString("resultmessage");
                    dialog.dismiss();
                    if (resultcode.startsWith("200")) {
                        listValue.remove(position);
                        notifyDataSetChanged();

                    } else {
                        //Toast.makeText(activity, resultmessage, Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
