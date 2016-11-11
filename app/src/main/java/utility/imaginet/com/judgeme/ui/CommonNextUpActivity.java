package utility.imaginet.com.judgeme.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.millennialmedia.InterstitialAd;
import com.millennialmedia.MMException;
import com.millennialmedia.MMSDK;
import com.triggertrap.seekarc.SeekArc;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import utility.imaginet.com.judgeme.R;
import utility.imaginet.com.judgeme.helper.CircleOverlayView;
import utility.imaginet.com.judgeme.helper.PrefManager;
import utility.imaginet.com.judgeme.helper.SimpleGestureFilter;
import utility.imaginet.com.judgeme.helper.Utilities;
import utility.imaginet.com.judgeme.helper.Utils;
import utility.imaginet.com.judgeme.helper.VolleyCommonClass;
import utility.imaginet.com.judgeme.helper.judgeMeUrls;
import utility.imaginet.com.judgeme.interfaceClass.interfaceSelector;

/**
 * Created by IM0033 on 12/9/2015.
 */
public class CommonNextUpActivity extends AppCompatActivity implements SimpleGestureFilter.SimpleGestureListener, SurfaceHolder.Callback, InterstitialAd.InterstitialListener {
    private SimpleGestureFilter detector;
    private Button nextUpBackButton;
    private LinearLayout refreshLayout;
    private RelativeLayout mainRelativeLayout;
    private CircleOverlayView CircleOverlayView1;
    private FrameLayout frameLayout;
    private PrefManager prefManager;
    private String token, fbToken, clipId, uid, current_clipID, streamURL, likescount, dislikescount, title, islongplayerYN;
    private TextView nextUpArtistsTextView, nextUpTitleTextView, nextUpDislikeTextView, nextUpDislikeView,
            nextUpLikeTextView, nextUpLikeView, nextUpDislikeCount, nextUpLikeCount, commonNextUpShareTextView, playbutton, pausebutton;

    // mediaplayer veriable-------------//
    private MediaPlayer mMediaPlayer, samplePlayer;
    Handler handler = new Handler();
    private Runnable runnable;
    private ProgressBar commondialogProgressBar;
    private SurfaceView circleSurfaceView;
    private SurfaceHolder holder;
    private SeekArc mSeekArc;
    private TextView mSeekArcProgress;
    private Handler mHandler = new Handler();
    private Utilities utils;
    boolean flag = false;

    private int leftswipecount = 0;
    private int rightswipecount = 0;
    public static final String PLACEMENT_ID = "221998";
    private InterstitialAd interstitialAd;
    public static final String TAG = CommonNextUpActivity.class.getSimpleName();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MMSDK.initialize(this);
        try {
            interstitialAd = InterstitialAd.createInstance(PLACEMENT_ID);
            interstitialAd.setListener(this);
        } catch (MMException e) {
            e.printStackTrace();
        }

        if (!flag) {
            Bundle bundle = getIntent().getExtras();
            clipId = bundle.getString("clipID");
            onClick();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
            mMediaPlayer.stop();
            Log.d(TAG, "onPause");
            // mMediaPlayer.release();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
            mMediaPlayer.stop();
            Log.d(TAG, "onstop");
            // mMediaPlayer.release();
        }
    }


    private void onClick() {
        setContentView(R.layout.circle_video);
        findViewById();

        CircleOverlayView1.setBackground(getResources().getDrawable(R.drawable.app_logo));
        detector = new SimpleGestureFilter(CommonNextUpActivity.this, this);
        utils = new Utilities();
        if (commondialogProgressBar.getVisibility() == View.GONE) {
            commondialogProgressBar.setVisibility(View.VISIBLE);
        }
        mMediaPlayer = new MediaPlayer();
        prefManager = new PrefManager(getApplicationContext());
        token = prefManager.getObjectId();
        fbToken = prefManager.getfbToken();

        if (!flag) {
//            Log.i("clipIdTag", "clipId is from another activity and flag is not true");
            if (!clipId.matches("")) {
                getClips(judgeMeUrls.NEXTUP + "&token=" + token + "&clipID=" + clipId, 1);
            } else {
                getClips(judgeMeUrls.NEXTUP + "&token=" + token, 1);
            }

        } else {
//            Log.i("clipIdTag", "clipId is from another activity and flag is not false");
            getClips(judgeMeUrls.PREVCLIP + "&token=" + token, 1);
        }

        mSeekArc.setOnSeekArcChangeListener(new SeekArc.OnSeekArcChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekArc seekArc) {
                mHandler.removeCallbacks(mUpdateTimeTask);
            }

            @Override
            public void onStartTrackingTouch(SeekArc seekArc) {
                mHandler.removeCallbacks(mUpdateTimeTask);
                int totalDuration = mMediaPlayer.getDuration();
                int currentPosition = utils.progressToTimer(mSeekArc.getProgress(), totalDuration);
                mMediaPlayer.seekTo(currentPosition);
                updateProgressBar();
            }

            @Override
            public void onProgressChanged(SeekArc seekArc, int progress, boolean fromUser) {
            }
        });
        nextUpArtistsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), UserProfileActivity.class);
                i.putExtra("uid", uid);
                startActivity(i);
                overridePendingTransition(R.anim.activity_enter, R.anim.activity_leave);
            }
        });

        nextUpBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        nextUpTitleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!nextUpTitleTextView.getText().toString().matches("")) {
                    Bundle bundle = new Bundle();
                    bundle.putString("clipID", current_clipID);
                    Intent i = new Intent(getApplicationContext(), CommandActivity.class);
                    i.putExtras(bundle);
                    startActivity(i);
                    overridePendingTransition(R.anim.activity_enter, R.anim.activity_leave);
                }
            }
        });
        commonNextUpShareTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (streamURL != null) {
                    Intent i = new Intent(getApplicationContext(), ShareActivity.class);
                    i.putExtra("url", current_clipID);
                    i.putExtra("clipTitle", title);
                    startActivity(i);
                    overridePendingTransition(R.anim.activity_enter, R.anim.activity_leave);
                }

            }
        });
        nextUpLikeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == nextUpLikeTextView) {
                    nextUpLikeView.setVisibility(View.VISIBLE);
                    nextUpLikeTextView.setVisibility(View.GONE);
                    nextUpDislikeView.setVisibility(View.GONE);
                    nextUpDislikeTextView.setVisibility(View.VISIBLE);
                    //nextUpLikeCount.setVisibility(View.VISIBLE);
                    getClips(judgeMeUrls.like + "&token=" + token + "&clipID=" + current_clipID + "&fbtoken=" + fbToken, 2);
                }

            }
        });
        nextUpDislikeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == nextUpDislikeTextView) {
                    nextUpDislikeView.setVisibility(View.VISIBLE);
                    nextUpDislikeTextView.setVisibility(View.GONE);
                    nextUpLikeView.setVisibility(View.GONE);
                    nextUpLikeTextView.setVisibility(View.VISIBLE);
                    //nextUpDislikeCount.setVisibility(View.VISIBLE);
                    getClips(judgeMeUrls.DILIKE + "&token=" + token + "&clipID=" + current_clipID + "&fbtoken=" + fbToken, 2);
                }
            }
        });

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mSeekArc.setVisibility(View.INVISIBLE);
            CircleOverlayView1.setVisibility(View.GONE);
            refreshLayout.setVisibility(View.GONE);
            mainRelativeLayout.setVisibility(View.GONE);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 2);
            frameLayout.setLayoutParams(params);

        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1);
            params.weight = 1.2f;
            frameLayout.setLayoutParams(params);
            mSeekArc.setVisibility(View.VISIBLE);
            mainRelativeLayout.setVisibility(View.VISIBLE);
            CircleOverlayView1.setVisibility(View.VISIBLE);
            refreshLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent me) {
        this.detector.onTouchEvent(me);
        return super.dispatchTouchEvent(me);
    }

    private void getClips(String url, final int type) {
        VolleyCommonClass.getDataFromServer(url, new interfaceSelector() {
            @Override
            public void volleyError(String error) {
            }

            @Override
            public void volleyResponse(JSONObject response) {
                if (type == 1) {
                    set1Response(response);
                } else if (type == 2) {
                    Type2Response(response);
                } else if (type == 3) {
                    Type3Response(response);
                }

            }
        });

    }

    private void Type3Response(JSONObject response) {
        //Log.i("Type3Response",response+"");
        try {
            JSONObject object = response.getJSONObject("data");
            JSONObject clipDetail = object.getJSONObject("clip");
            likescount = clipDetail.optString("likescount");
            dislikescount = clipDetail.optString("dislikescount");
            nextUpLikeCount.setText(likescount);
            nextUpDislikeCount.setText(dislikescount);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void Type2Response(JSONObject response) {
        // Log.i("Type2Response",response+"");
        JSONObject object = null;
        try {
            object = response.getJSONObject("data");
            String resultcode = object.optString("resultcode");
            if (resultcode.startsWith("200")) {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        getClips(judgeMeUrls.FETCH_CLIPS + "&token=" + prefManager.getObjectId() + "&clipID=" + current_clipID, 3);
                    }
                });
                thread.start();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void set1Response(JSONObject response) {
        try {
            JSONObject mainObject = response.getJSONObject("data");
            String resultcode = mainObject.getString("resultcode");
            if (resultcode.startsWith("200")) {
                JSONObject ClipDetail = mainObject.getJSONObject("clip");
                uid = ClipDetail.optString("uid");
                likescount = ClipDetail.optString("likescount");
                nextUpLikeCount.setText(likescount);
                String netlikes = ClipDetail.optString("netlikes");
                title = ClipDetail.optString("title");
                streamURL = ClipDetail.optString("streamURL");
                current_clipID = ClipDetail.optString("clipID");
                String artistname = ClipDetail.optString("artistname");
                String artistcity = ClipDetail.optString("artistcity");
                dislikescount = ClipDetail.optString("dislikescount");
                nextUpDislikeCount.setText(dislikescount);
                islongplayerYN = ClipDetail.optString("islongplayerYN");
                JSONArray likesanddislikes = ClipDetail.getJSONArray("likesanddislikes");
                for (int j = 0; j < likesanddislikes.length(); j++) {
                    JSONObject likesanddislikesObject = likesanddislikes.getJSONObject(j);
                    String likeuserID = likesanddislikesObject.optString("likeuserID");
                    if (uid.equals(likeuserID)) {
                        String value = likesanddislikesObject.optString("value");
                        if (value.startsWith("-1")) {
                            nextUpDislikeView.setVisibility(View.VISIBLE);
                            nextUpDislikeTextView.setVisibility(View.GONE);
                        } else if (value.startsWith("1")) {
                            nextUpLikeView.setVisibility(View.VISIBLE);
                            nextUpLikeTextView.setVisibility(View.GONE);
                        }
                    }

                }
                nextUpArtistsTextView.setText(artistname);
                nextUpTitleTextView.setText(title);
                playVideo();
            } else if (resultcode.startsWith("500")) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(CommonNextUpActivity.this);
                builder1.setMessage(mainObject.getString("resultmessage"));
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert11 = builder1.create();
                Window window = alert11.getWindow();
                window.setGravity(Gravity.BOTTOM);
                alert11.show();
            }
        } catch (Exception e) {

        }

    }

    private void playVideo() {
       // Log.d(TAG, "Play From Common fragment");
        holder = circleSurfaceView.getHolder();
        holder.addCallback(this);
        holder.setFixedSize(176, 144);
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setDisplay(holder);
        try {
            mMediaPlayer.reset();
            mMediaPlayer.setDataSource(getApplicationContext(), Uri.parse(streamURL));
            mMediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        CircleOverlayView1.setBackgroundColor(Color.parseColor("#00000000"));
        setFixedSizeVideo();
        mMediaPlayer.start();

        mSeekArc.setProgress(0);
        updateProgressBar();
        if (commondialogProgressBar.getVisibility() == View.VISIBLE) {
            commondialogProgressBar.setVisibility(View.GONE);
        }
        if (islongplayerYN.startsWith("N")) {
            runnable = new Runnable() {
                @Override
                public void run() {
                    if (mMediaPlayer != null && mMediaPlayer.isPlaying())
                        mMediaPlayer.stop();
                    Log.d(TAG, "islongplayerNo");
                    flag = true;
                    onClick();
                }
            };
            handler.postDelayed(runnable, 60000);
        }
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                CircleOverlayView1.setBackground(getResources().getDrawable(R.drawable.app_logo));
                mp.stop();
                mp.reset();
                mMediaPlayer.seekTo(0);
                Animation animation = AnimationUtils.loadAnimation(CommonNextUpActivity.this, R.anim.blink_animation);
                nextUpLikeTextView.startAnimation(animation);
                nextUpDislikeTextView.startAnimation(animation);
                nextUpLikeView.startAnimation(animation);
                nextUpDislikeView.startAnimation(animation);
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        flag = true;
                        onClick();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
            }
        });

        circleSurfaceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMediaPlayer.isPlaying()) {
                    mMediaPlayer.pause();
                    handler.removeCallbacks(runnable);
                    pausebutton.setVisibility(View.VISIBLE);
                    playbutton.setVisibility(View.GONE);
                    mSeekArcProgress.setVisibility(View.VISIBLE);
                    pausebutton.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //pausebutton.setVisibility(View.INVISIBLE);
                            mSeekArcProgress.setVisibility(View.INVISIBLE);
                        }
                    }, 3000);
                } else {
                    playbutton.setVisibility(View.VISIBLE);
                    playbutton.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            playbutton.setVisibility(View.INVISIBLE);
                            mSeekArcProgress.setVisibility(View.INVISIBLE);
                        }
                    }, 3000);
                    pausebutton.setVisibility(View.GONE);
                    mMediaPlayer.start();
                }
            }
        });
    }

    private void setFixedSizeVideo() {
        int surfaceView_Width = circleSurfaceView.getWidth();
        int surfaceView_Height = circleSurfaceView.getHeight();

        float video_Width = mMediaPlayer.getVideoWidth();
        float video_Height = mMediaPlayer.getVideoHeight();

        float ratio_width = surfaceView_Width / video_Width;
        float ratio_height = surfaceView_Height / video_Height;
        float aspectratio = video_Width / video_Height;

        ViewGroup.LayoutParams layoutParams = circleSurfaceView.getLayoutParams();
        if (ratio_width > ratio_height) {
            layoutParams.width = (int) (surfaceView_Height * aspectratio);
            layoutParams.height = surfaceView_Height;
        } else {
            layoutParams.width = surfaceView_Width;
            layoutParams.height = (int) (surfaceView_Width / aspectratio);
        }
        circleSurfaceView.setLayoutParams(layoutParams);
    }

    private void updateProgressBar() {
        mHandler.postDelayed(mUpdateTimeTask, 100);
    }

    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            long totalDuration = mMediaPlayer.getDuration();
            long currentDuration = mMediaPlayer.getCurrentPosition();
            String progressValue = utils.milliSecondsToTimer(currentDuration);
            mSeekArcProgress.setText(progressValue);
            int progress = (int) (utils.getProgressPercentage(currentDuration, totalDuration));
            mSeekArc.setProgress(progress);
            mHandler.postDelayed(this, 100);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
            mMediaPlayer.stop();
            // mMediaPlayer.release();
        }
        // mMediaPlayer.release();
    }

    private void findViewById() {
        nextUpBackButton = (Button) findViewById(R.id.nextUpBackButton);
        nextUpArtistsTextView = (TextView) findViewById(R.id.nextUpArtistsTextView);
        nextUpTitleTextView = (TextView) findViewById(R.id.nextUpTitleTextView);
        nextUpDislikeTextView = (TextView) findViewById(R.id.nextUpDislikeTextView);
        nextUpDislikeView = (TextView) findViewById(R.id.nextUpDislikeView);
        nextUpLikeTextView = (TextView) findViewById(R.id.nextUpLikeTextView);
        nextUpLikeView = (TextView) findViewById(R.id.nextUpLikeView);
        nextUpDislikeCount = (TextView) findViewById(R.id.nextUpDislikeCount);
        nextUpLikeCount = (TextView) findViewById(R.id.nextUpLikeCount);
        commonNextUpShareTextView = (TextView) findViewById(R.id.commonNextUpShareTextView);
        playbutton = (TextView) findViewById(R.id.playbutton1);
        pausebutton = (TextView) findViewById(R.id.pausebutton1);
        commondialogProgressBar = (ProgressBar) findViewById(R.id.commondialogProgressBar);
        //circleSurfaceView = (CircleSurfaceView_demo) findViewById(R.id.circleVideoView11);
        circleSurfaceView = (SurfaceView) findViewById(R.id.circlesurfaceVideoView);
        mSeekArcProgress = (TextView) findViewById(R.id.seekArcProgress1);
        mSeekArc = (SeekArc) findViewById(R.id.seekArc1);
        refreshLayout = (LinearLayout) findViewById(R.id.refreshLayout);
        CircleOverlayView1 = (CircleOverlayView) findViewById(R.id.CircleOverlayView1);
        frameLayout = (FrameLayout) findViewById(R.id.frameLayout);
        mainRelativeLayout = (RelativeLayout) findViewById(R.id.mainRelativeLayout);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
            mMediaPlayer.stop();
            // mMediaPlayer.release();
        }
        overridePendingTransition(R.anim.animation_enter, R.anim.animation_leave);
    }

    @Override
    public void onSwipe(int direction) {
        switch (direction) {
            case SimpleGestureFilter.SWIPE_RIGHT:
                rightswipecount++;
                if (rightswipecount == 1) {
                    requestAd();
                    rightswipecount = 0;
                }
               /* nDialog = new Dialog(CommonNextUpActivity.this);
                nDialog.setContentView(R.layout.dialog_progress);
                nDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                nDialog.setCancelable(true);
                nDialog.show();*/
                if (samplePlayer != null) {
                    samplePlayer = null;
                }
                if (mMediaPlayer.isPlaying()) {
                    mMediaPlayer.stop();
                    clipId = "";
                    flag = true;
                    onClick();
                } else {
                    clipId = "";
                    flag = true;
                    onClick();
                }
                break;
            case SimpleGestureFilter.SWIPE_LEFT:
                leftswipecount++;
                if (leftswipecount == 1) {
                    requestAd();
                    leftswipecount = 0;
                }
                /*nDialog = new Dialog(CommonNextUpActivity.this);
                nDialog.setContentView(R.layout.dialog_progress);
                nDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                nDialog.setCancelable(true);
                nDialog.show();*/
                if (samplePlayer != null) {
                    samplePlayer = null;
                }
                if (mMediaPlayer.isPlaying()) {
                    mMediaPlayer.stop();
                    clipId = "";
                    flag = false;
                    onClick();
                } else {
                    clipId = "";
                    flag = false;
                    onClick();
                }
                break;
            case SimpleGestureFilter.SWIPE_DOWN:

                break;
            case SimpleGestureFilter.SWIPE_UP:

                break;
        }
    }

    private void requestAd() {
        if (interstitialAd != null) {
            InterstitialAd.InterstitialAdMetadata metadata = new InterstitialAd.InterstitialAdMetadata();
            interstitialAd.load(CommonNextUpActivity.this, metadata);
            SharedPreferences sharedPreferences =
                    PreferenceManager.getDefaultSharedPreferences(CommonNextUpActivity.this);

            InterstitialAd.DisplayOptions displayOptions = new InterstitialAd.DisplayOptions().setImmersive(
                    sharedPreferences.getBoolean(getResources().getString(R.string.app_name), false));
            try {
                if (interstitialAd.isReady()) {
                    interstitialAd.show(CommonNextUpActivity.this, displayOptions);
                }
            } catch (MMException e) {
                Log.i(TAG, "Unable to show interstitial ad content, exception occurred");
                e.printStackTrace();
            }
        } else {
            Log.i(TAG, "Unable to load interstitial ad content, InterstitialAd instance is null.");
        }
    }

    @Override
    public void onDoubleTap() {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }


    @Override
    public void onLoaded(InterstitialAd interstitialAd) {

        Utils.runOnUiThread(new Runnable() {
            @Override
            public void run() {

            }
        });
    }

    @Override
    public void onLoadFailed(InterstitialAd interstitialAd, InterstitialAd.InterstitialErrorStatus interstitialErrorStatus) {
        Log.i(TAG, "Interstitial Ad load failed.");
    }

    @Override
    public void onShown(InterstitialAd interstitialAd) {
        Log.i(TAG, "Interstitial Ad shown.");
    }

    @Override
    public void onShowFailed(InterstitialAd interstitialAd, InterstitialAd.InterstitialErrorStatus interstitialErrorStatus) {
        Log.i(TAG, "Interstitial Ad show failed.");
    }

    @Override
    public void onClosed(InterstitialAd interstitialAd) {
        Log.i(TAG, "Interstitial Ad closed.");
    }

    @Override
    public void onClicked(InterstitialAd interstitialAd) {
        Log.i(TAG, "Interstitial Ad clicked.");
    }

    @Override
    public void onAdLeftApplication(InterstitialAd interstitialAd) {
        Log.i(TAG, "Interstitial Ad left application.");
    }

    @Override
    public void onExpired(InterstitialAd interstitialAd) {
        Log.i(TAG, "Interstitial Ad expired.");
    }
}
