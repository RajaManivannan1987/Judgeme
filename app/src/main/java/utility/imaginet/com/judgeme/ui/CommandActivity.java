package utility.imaginet.com.judgeme.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;
import utility.imaginet.com.judgeme.R;
import utility.imaginet.com.judgeme.helper.AppController;
import utility.imaginet.com.judgeme.helper.PrefManager;
import utility.imaginet.com.judgeme.helper.VolleyCommonClass;
import utility.imaginet.com.judgeme.helper.judgeMeUrls;
import utility.imaginet.com.judgeme.interfaceClass.interfaceSelector;

/**
 * Created by IM0033 on 12/21/2015.
 */
public class CommandActivity extends Activity {
    private TextView commentTitleTextView, comments_update_Time, comments_artist_Name;
    private Button commentBackButton, submit_comment_Button;
    private EditText comment_EditText;
    private CircleImageView mycomment_UserImageView;
    private ImageLoader imageLoader;
    private NetworkImageView commenter_Image, sub_childImageView, subCmdImageView;
    private PrefManager prefManager;
    private String token, text, clipID;
    private LinearLayout comments_LinearLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.command_activity);
        FindViewId();
        LoadData();
    }

    private void LoadData() {
        prefManager = new PrefManager(getApplicationContext());
        token = prefManager.getObjectId();
        imageLoader = AppController.getInstance().getImageLoader();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            clipID = bundle.getString("clipID");
            FetchComments(judgeMeUrls.FETCH_CLIPS + "&clipID=" + clipID);
        }
        onClick();
    }

    private void FetchComments(String url) {
        VolleyCommonClass.getDataFromServer(url,  new interfaceSelector() {
            @Override
            public void volleyError(String error) {

            }

            @Override
            public void volleyResponse(JSONObject response) {
                setResponse(response);
            }
        });
    }

    private void setResponse(JSONObject response) {
        try {
            JSONObject responseObject = response.getJSONObject("data");
            String resultcode = responseObject.optString("resultcode");
            if (resultcode.startsWith("200")) {
                JSONObject clipDetails = responseObject.getJSONObject("clip");
                String artistname = clipDetails.optString("artistname");
                String artistcity = clipDetails.optString("artistcity");
                String artiststate = clipDetails.optString("artiststate");
                String artistcountry = clipDetails.optString("artistcountry");
                String artistphotourl = clipDetails.optString("artistphotourl");
                String datesubmit = clipDetails.optString("datemodified");
                String title = clipDetails.optString("title");
                imageLoader(artistphotourl, "mycomment_UserImageView");
                commentTitleTextView.setText(title);
                comments_artist_Name.setText(artistname + " from " + artistcountry);

                setDateFormatter(datesubmit);

                JSONArray comments = clipDetails.getJSONArray("comments");
                LayoutInflater parentInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                for (int i = 0; i < comments.length(); i++) {
                    View contentView = parentInflater.inflate(R.layout.custom_comment_listview, null);
                    JSONObject Object = comments.getJSONObject(i);
                    final String commentID = Object.optString("commentID");
                    final String clipID = Object.optString("clipID");
                    String text = Object.optString("text");
                    String commenterName = Object.optString("artistname");
                    String commenterPhotourl = Object.optString("artistphotourl");
                    String submittedDate = Object.optString("datesubmitted");
                    JSONArray subCommandArray = Object.getJSONArray("subcomments");

                    TextView name = (TextView) contentView.findViewById(R.id.parentNameTextView);
                    TextView message = (TextView) contentView.findViewById(R.id.parentMessageTextView);
                    TextView time = (TextView) contentView.findViewById(R.id.parentTimeTextView);
                    TextView replay = (TextView) contentView.findViewById(R.id.parentReplyTextView);
                    TextView viewedTextView = (TextView) contentView.findViewById(R.id.parentviewedTextView);
                    final LinearLayout parent_sub_command_layout = (LinearLayout) contentView.findViewById(R.id.parent_sub_command_layout);
                    commenter_Image = (NetworkImageView) contentView.findViewById(R.id.parentImageView);

                    commenter_Image.setImageUrl(commenterPhotourl, imageLoader);
                    name.setText(commenterName);
                    message.setText(text);
                    setSubDateFormatter(submittedDate, time);

                    viewedTextView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (parent_sub_command_layout.getVisibility() == View.VISIBLE) {
                                parent_sub_command_layout.setVisibility(View.GONE);
                                Animation animation= AnimationUtils.loadAnimation(CommandActivity.this, R.anim.slide_down);
                                parent_sub_command_layout.setAnimation(animation);
                            } else {
                                parent_sub_command_layout.setVisibility(View.VISIBLE);
                                Animation animation= AnimationUtils.loadAnimation(CommandActivity.this, R.anim.slide_up);
                                parent_sub_command_layout.setAnimation(animation);

                            }
                        }
                    });
                    replay.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Bundle bundle = new Bundle();
                            bundle.putString("clipID", clipID);
                            bundle.putString("commentID", commentID);
                            Intent i = new Intent(getApplicationContext(), ReplyComments.class);
                            i.putExtras(bundle);
                            startActivity(i);
                            overridePendingTransition(R.anim.slide_up, R.anim.slide_down);

                        }
                    });
                    LayoutInflater childInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    if (subCommandArray != null && subCommandArray.length() > 0) {
                        viewedTextView.setVisibility(View.VISIBLE);
                        int count = subCommandArray.length();
                        viewedTextView.setText("View" + " " + count + "replay");
                        SubCommandMethod(subCommandArray, parent_sub_command_layout, childInflater, 1);
                    }
                    comments_LinearLayout.addView(contentView);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setSubDateFormatter(String submittedDate, TextView textview) {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String fromDate = submittedDate;
        String toDate = df.format(c.getTime());
        long diff = 0;
        try {
            Date startDate = df.parse(fromDate);
            Calendar c1 = Calendar.getInstance();
            c1.setTime(startDate);

            Date endDate = df.parse(toDate);
            Calendar c2 = Calendar.getInstance();
            c2.setTime(endDate);

            long ms1 = c1.getTimeInMillis();
            long ms2 = c2.getTimeInMillis();
            diff = ms2 - ms1;
            int diffInDays = (int) (diff / (24 * 60 * 60 * 1000));
            int hours = (int) (diff / (1000 * 60 * 60));
            if (hours < 24) {
                textview.setText("today");
            } else {
                if (diffInDays == 1) {
                    textview.setText("Yesterday");
                } else if (diffInDays == 2) {
                    textview.setText("2 days ago");
                } else if (diffInDays == 3) {
                    textview.setText("3 days ago");
                } else if (diffInDays == 4) {
                    textview.setText("4 days ago");
                } else if (diffInDays == 5) {
                    textview.setText("5 days ago");
                } else if (diffInDays == 6) {
                    textview.setText("6 days ago");
                } else if (diffInDays <= 7) {
                    textview.setText("1 week ago");
                } else if (diffInDays <= 14) {
                    textview.setText("2 week ago");
                } else if (diffInDays <= 21) {
                    textview.setText("3 week ago");
                } else if (diffInDays <= 28) {
                    textview.setText("4 week ago");
                } else if (diffInDays <= 30) {
                    textview.setText("1 month ago");
                } else if (diffInDays <= 60) {
                    textview.setText("2 month ago");
                } else if (diffInDays <= 90) {
                    textview.setText("3 month ago");
                } else if (diffInDays <= 120) {
                    textview.setText("4 month ago");
                } else if (diffInDays <= 150) {
                    textview.setText("5 month ago");
                } else if (diffInDays <= 180) {
                    textview.setText("6 month ago");
                } else if (diffInDays <= 210) {
                    textview.setText("7 month ago");
                } else if (diffInDays <= 240) {
                    textview.setText("8 month ago");
                } else if (diffInDays <= 270) {
                    textview.setText("9 month ago");
                } else if (diffInDays <= 300) {
                    textview.setText("10 month ago");
                } else if (diffInDays <= 330) {
                    textview.setText("11 month ago");
                } else if (diffInDays <= 360) {
                    textview.setText("12 month ago");
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void setDateFormatter(String datesubmit) {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String fromDate = datesubmit;
        String toDate = df.format(c.getTime());
        long diff = 0;
        try {
            Date startDate = df.parse(fromDate);
            Calendar c1 = Calendar.getInstance();
            c1.setTime(startDate);

            Date endDate = df.parse(toDate);
            Calendar c2 = Calendar.getInstance();
            c2.setTime(endDate);

            long ms1 = c1.getTimeInMillis();
            long ms2 = c2.getTimeInMillis();
            diff = ms2 - ms1;
            int diffInDays = (int) (diff / (24 * 60 * 60 * 1000));
            int hours = (int) (diff / (1000 * 60 * 60));
            if (hours < 24) {
                comments_update_Time.setText("Updated on " + "today");
            } else {
                if (diffInDays == 1) {
                    comments_update_Time.setText("Updated on " + "Yesterday");
                } else if (diffInDays == 2) {
                    comments_update_Time.setText("Updated on " + "2 days ago");
                } else if (diffInDays == 3) {
                    comments_update_Time.setText("Updated on " + "3 days ago");
                } else if (diffInDays == 4) {
                    comments_update_Time.setText("Updated on " + "4 days ago");
                } else if (diffInDays == 5) {
                    comments_update_Time.setText("Updated on " + "5 days ago");
                } else if (diffInDays == 6) {
                    comments_update_Time.setText("Updated on " + "6 days ago");
                } else if (diffInDays <= 7) {
                    comments_update_Time.setText("Updated on " + "1 week ago");
                } else if (diffInDays <= 14) {
                    comments_update_Time.setText("Updated on " + "2 week ago");
                } else if (diffInDays <= 21) {
                    comments_update_Time.setText("Updated on " + "3 week ago");
                } else if (diffInDays <= 28) {
                    comments_update_Time.setText("Updated on " + "4 week ago");
                } else if (diffInDays <= 30) {
                    comments_update_Time.setText("Updated on " + "1 month ago");
                } else if (diffInDays <= 60) {
                    comments_update_Time.setText("Updated on " + "2 month ago");
                } else if (diffInDays <= 90) {
                    comments_update_Time.setText("Updated on " + "3 month ago");
                } else if (diffInDays <= 120) {
                    comments_update_Time.setText("Updated on " + "4 month ago");
                } else if (diffInDays <= 150) {
                    comments_update_Time.setText("Updated on " + "5 month ago");
                } else if (diffInDays <= 180) {
                    comments_update_Time.setText("Updated on " + "6 month ago");
                } else if (diffInDays <= 210) {
                    comments_update_Time.setText("Updated on " + "7 month ago");
                } else if (diffInDays <= 240) {
                    comments_update_Time.setText("Updated on " + "8 month ago");
                } else if (diffInDays <= 270) {
                    comments_update_Time.setText("Updated on " + "9 month ago");
                } else if (diffInDays <= 300) {
                    comments_update_Time.setText("Updated on " + "10 month ago");
                } else if (diffInDays <= 330) {
                    comments_update_Time.setText("Updated on " + "11 month ago");
                } else if (diffInDays <= 360) {
                    comments_update_Time.setText("Updated on " + "12 month ago");
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void SubCommandMethod(JSONArray subCommandArray, final LinearLayout parent_sub_command_layout, LayoutInflater childInflater, int type) throws JSONException {
        for (int j = 0; j < subCommandArray.length(); j++) {
            View subCmdcontentView = childInflater.inflate(R.layout.child_list_item, null);
            JSONObject subCmdObject = subCommandArray.getJSONObject(j);
            final String subCmdClipId = subCmdObject.optString("clipID");
            final String subCmdComandId = subCmdObject.optString("commentID");
            String subCmdText = subCmdObject.optString("text");
            String subCmdName = subCmdObject.optString("artistname");
            String subCmdPhotourl = subCmdObject.optString("artistphotourl");
            String subCmdDate = subCmdObject.optString("datesubmitted");
            JSONArray CommandArray = subCmdObject.getJSONArray("subcomments");

            TextView ch_name = (TextView) subCmdcontentView.findViewById(R.id.childNameTextView);
            TextView ch_message = (TextView) subCmdcontentView.findViewById(R.id.childMessageTextView);
            TextView ch_Reply = (TextView) subCmdcontentView.findViewById(R.id.childReplyTextView);
            TextView ch_ViewTextView = (TextView) subCmdcontentView.findViewById(R.id.childviewTextView);
            TextView ch_Time = (TextView) subCmdcontentView.findViewById(R.id.childTimeTextView);
            subCmdImageView = (NetworkImageView) subCmdcontentView.findViewById(R.id.childImageView);
            final LinearLayout add_sub_command_layout = (LinearLayout) subCmdcontentView.findViewById(R.id.add_sub_command_layout);
            ch_name.setText(subCmdName);
            ch_message.setText(subCmdText);
            subCmdImageView.setImageUrl(subCmdPhotourl, imageLoader);

            //set Time formatter
            setSubDateFormatter(subCmdDate, ch_Time);
            //ch_Time.setText(subCmdDate);

            //imageLoader(subCmdPhotourl, "subCmdImageView");
            ch_Reply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString("clipID", subCmdClipId);
                    bundle.putString("commentID", subCmdComandId);
                    Intent i = new Intent(getApplicationContext(), ReplyComments.class);
                    i.putExtras(bundle);
                    startActivity(i);
                    overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
                }
            });
            ch_ViewTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (add_sub_command_layout.getVisibility() == View.VISIBLE) {
                        add_sub_command_layout.setVisibility(View.GONE);
                        Animation animation= AnimationUtils.loadAnimation(CommandActivity.this, R.anim.slide_down);
                        add_sub_command_layout.setAnimation(animation);
                    } else {
                        add_sub_command_layout.setVisibility(View.VISIBLE);
                        Animation animation= AnimationUtils.loadAnimation(CommandActivity.this, R.anim.slide_up);
                        add_sub_command_layout.setAnimation(animation);
                    }
                }
            });
            if (CommandArray.length() > 0) {
                ch_ViewTextView.setVisibility(View.VISIBLE);
                int count = CommandArray.length();
                ch_ViewTextView.setText(count + "item view");
                AddSubCommand(CommandArray, add_sub_command_layout);
            }
            //add_sub_command_layout.addView(subCmdcontentView);
            if (type == 1) {
                parent_sub_command_layout.addView(subCmdcontentView);
            } else if (type == 2) {
                add_sub_command_layout.addView(subCmdcontentView);
            }

        }
    }

    private void AddSubCommand(JSONArray commandArray, LinearLayout add_sub_command_layout) throws JSONException {
        LayoutInflater SubChildInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        for (int j = 0; j < commandArray.length(); j++) {
            View subChildCmdcontentView = SubChildInflater.inflate(R.layout.sub_child_list_item, null);
            TextView sub_childNameTextView = (TextView) subChildCmdcontentView.findViewById(R.id.sub_childNameTextView);
            TextView sub_childMessageTextView = (TextView) subChildCmdcontentView.findViewById(R.id.sub_childMessageTextView);
            TextView sub_childReplyTextView = (TextView) subChildCmdcontentView.findViewById(R.id.sub_childReplyTextView);
            TextView sub_childviewTextView = (TextView) subChildCmdcontentView.findViewById(R.id.sub_childviewTextView);
            TextView sub_childTimeTextView = (TextView) subChildCmdcontentView.findViewById(R.id.sub_childTimeTextView);
            sub_childImageView = (NetworkImageView) subChildCmdcontentView.findViewById(R.id.sub_childImageView);
            final LinearLayout sub_add_sub_command_layout = (LinearLayout) subChildCmdcontentView.findViewById(R.id.sub_add_sub_command_layout);

            JSONObject subCmdObject = commandArray.getJSONObject(j);
            final String sub_CmdClipId = subCmdObject.optString("clipID");
            final String sub_CmdComandId = subCmdObject.optString("commentID");
            String subCmdText = subCmdObject.optString("text");
            String subCmdName = subCmdObject.optString("artistname");
            String subCmdPhotourl = subCmdObject.optString("artistphotourl");
            String subCmdDate = subCmdObject.optString("datesubmitted");
            JSONArray subCommandArray = subCmdObject.getJSONArray("subcomments");
            //sub_time_method
            setSubDateFormatter(subCmdDate, sub_childTimeTextView);
            //set_sub_DateFormatter(subCmdDate, sub_childTimeTextView);

            sub_childNameTextView.setText(subCmdName);
            sub_childMessageTextView.setText(subCmdText);
            //sub_childTimeTextView.setText(subCmdDate);
            //imageLoader(subCmdPhotourl, "sub_childImageView");
            sub_childImageView.setImageUrl(subCmdPhotourl, imageLoader);
            sub_childviewTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(getApplicationContext(),"hai",Toast.LENGTH_SHORT).show();
                    if (sub_add_sub_command_layout.getVisibility() == View.VISIBLE) {
                        sub_add_sub_command_layout.setVisibility(View.GONE);
                        Animation animation= AnimationUtils.loadAnimation(CommandActivity.this, R.anim.slide_down);
                        sub_add_sub_command_layout.setAnimation(animation);
                    } else {
                        sub_add_sub_command_layout.setVisibility(View.VISIBLE);
                        Animation animation= AnimationUtils.loadAnimation(CommandActivity.this, R.anim.slide_up);
                        sub_add_sub_command_layout.setAnimation(animation);
                    }
                }
            });
            sub_childReplyTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(getApplicationContext(),sub_CmdClipId+" /"+sub_CmdComandId,Toast.LENGTH_SHORT).show();
                    Bundle bundle = new Bundle();
                    bundle.putString("clipID", sub_CmdClipId);
                    bundle.putString("commentID", sub_CmdComandId);
                    Intent i = new Intent(getApplicationContext(), ReplyComments.class);
                    i.putExtras(bundle);
                    startActivity(i);
                    overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
                }
            });
            if (subCommandArray.length() > 0) {
                sub_childviewTextView.setVisibility(View.VISIBLE);
                int count = subCommandArray.length();
                sub_childviewTextView.setText(count + "item view");
                AddSubCommand(subCommandArray, sub_add_sub_command_layout);
            }
            add_sub_command_layout.addView(subChildCmdcontentView);
        }
    }

    private void imageLoader(String artistphotourl, final String ImageView) {
        imageLoader.get(artistphotourl, new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer imageContainer, boolean b) {
                if (ImageView.startsWith("mycomment_UserImageView")) {
                    mycomment_UserImageView.setImageBitmap(imageContainer.getBitmap());
                } else if (ImageView.startsWith("subCmdImageView")) {
                    subCmdImageView.setImageBitmap(imageContainer.getBitmap());
                } else if (ImageView.startsWith("commenter_Image")) {
                    commenter_Image.setImageBitmap(imageContainer.getBitmap());
                } else if (ImageView.startsWith("sub_childImageView")) {
                    sub_childImageView.setImageBitmap(imageContainer.getBitmap());
                }


            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                VolleyLog.d("ErrorProPic", volleyError.getMessage());
            }
        });

    }

    private void onClick() {
        commentBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        submit_comment_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text = comment_EditText.getText().toString();
                String message = text.replace(" ", "%20");
                if (!text.matches("")) {
                    submitComments(judgeMeUrls.SUBMIT_COMMENT + "&token=" + token + "&clipID=" + clipID + "&text=" + message);
                    comment_EditText.setText("");
                } else {
                    comment_EditText.setError("You did not enter a comments");
                }

            }
        });
        commentTitleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("clipID", clipID);
                Intent i = new Intent(getApplicationContext(), CommonNextUpActivity.class);
                i.putExtras(bundle);
                startActivity(i);
                overridePendingTransition(R.anim.activity_enter, R.anim.activity_leave);
            }
        });
    }

    private void submitComments(String urls) {

        VolleyCommonClass.getDataFromServer(urls,  new interfaceSelector() {
            @Override
            public void volleyError(String error) {

            }

            @Override
            public void volleyResponse(JSONObject response) {
                finish();
                startActivity(getIntent());
            }
        });
    }

    private void FindViewId() {
        commentTitleTextView = (TextView) findViewById(R.id.commentTitleTextView);
        commentBackButton = (Button) findViewById(R.id.commentBackButton);
        mycomment_UserImageView = (CircleImageView) findViewById(R.id.mycomment_UserImageView);
        comments_update_Time = (TextView) findViewById(R.id.comments_update_Time);
        comments_artist_Name = (TextView) findViewById(R.id.comments_artist_Name);
        submit_comment_Button = (Button) findViewById(R.id.submit_comment_Button);
        comment_EditText = (EditText) findViewById(R.id.comment_EditText);
        comments_LinearLayout = (LinearLayout) findViewById(R.id.comments_LinearLayout);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.animation_enter, R.anim.animation_leave);
    }
}
