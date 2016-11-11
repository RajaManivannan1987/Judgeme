package utility.imaginet.com.judgeme.ui;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;

import utility.imaginet.com.judgeme.R;

/**
 * Created by IM0033 on 11/13/2015.
 */
public class ShareActivity extends Activity {
    private Button shareCloseButton, facebook_Button, twitter_Button, email_Button, youtube_Button, reportButton;
    private String title, clipId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.share_activity);
        FindViewId();
        onClickMethod();
    }

    private void onClickMethod() {
        clipId = getIntent().getExtras().getString("url");
        title = getIntent().getExtras().getString("clipTitle");
        shareCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                onBackPressed();
            }
        });
        facebook_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.setType("text/plain");
                sendIntent.putExtra(Intent.EXTRA_TEXT, title);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "http://www.judgeme.net/web/play-clip.html?clipid=" + clipId);
                sendIntent.setPackage("com.facebook.katana");
                try {
                    startActivity(sendIntent);
                    finish();
                } catch (android.content.ActivityNotFoundException ex) {
                    // Toast.makeText(getApplicationContext(), "There are no twitter clients installed.", Toast.LENGTH_SHORT).show();
                }

            }
        });
        email_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                emailIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                emailIntent.setType("plain/text");
                emailIntent.setClassName("com.google.android.gm", "com.google.android.gm.ComposeActivityGmail");
                emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, "");
                emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, title);
                emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "http://www.judgeme.net/web/play-clip.html?clipid=" + clipId);
                try {
                    startActivity(emailIntent);
                    finish();
                } catch (android.content.ActivityNotFoundException ex) {
                    // Toast.makeText(getApplicationContext(), "There are no email clients installed.", Toast.LENGTH_SHORT).show();
                }

            }
        });
        twitter_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, "http://www.judgeme.net/web/play-clip.html?clipid=" + clipId);
                intent.setType("text/plain");
                //intent.putExtra(Intent.EXTRA_STREAM, videoLink);
                //intent.setType("image/jpeg");
                intent.setPackage("com.twitter.android");
                //intent.setPackage(" com.twitter.android.PostActivity");

                try {
                    startActivity(intent);
                    finish();
                } catch (android.content.ActivityNotFoundException ex) {
                }

            }
        });
        reportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ReportActivity.class);
                i.putExtra("clipTitle", title);
                i.putExtra("clipId", clipId);
                startActivity(i);
                overridePendingTransition(R.anim.activity_enter, R.anim.activity_leave);
            }
        });
        youtube_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues content = new ContentValues(4);
                content.put(MediaStore.Video.VideoColumns.TITLE, "Test");
                content.put(MediaStore.Video.VideoColumns.DATE_ADDED,
                        System.currentTimeMillis() / 1000);
                content.put(MediaStore.Video.Media.MIME_TYPE, "video/mp4");
                content.put(MediaStore.Video.Media.DATA, "/sdcard/myvideo.mp4");
                ContentResolver resolver = getApplicationContext().getContentResolver();
                Uri uri = resolver.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, content);
            }
        });
    }

    private void FindViewId() {
        shareCloseButton = (Button) findViewById(R.id.shareCloseButton);
        facebook_Button = (Button) findViewById(R.id.facebook_Button);
        twitter_Button = (Button) findViewById(R.id.twitter_Button);
        email_Button = (Button) findViewById(R.id.email_Button);
        reportButton = (Button) findViewById(R.id.reportButton);
        youtube_Button = (Button) findViewById(R.id.youtube_Button);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.animation_enter, R.anim.animation_leave);
    }
}
