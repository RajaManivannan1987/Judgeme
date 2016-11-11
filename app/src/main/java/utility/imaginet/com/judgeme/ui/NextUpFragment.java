package utility.imaginet.com.judgeme.ui;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
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
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.millennialmedia.InterstitialAd;
import com.millennialmedia.MMException;
import com.triggertrap.seekarc.SeekArc;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import utility.imaginet.com.judgeme.R;
import utility.imaginet.com.judgeme.helper.CircleOverlayView;
import utility.imaginet.com.judgeme.helper.PrefManager;
import utility.imaginet.com.judgeme.helper.Utilities;
import utility.imaginet.com.judgeme.helper.VolleyCommonClass;
import utility.imaginet.com.judgeme.helper.judgeMeUrls;
import utility.imaginet.com.judgeme.interfaceClass.interfaceSelector;
import utility.imaginet.com.judgeme.models.NextUpClipDetails;

/**
 * Created by IM0033 on 11/6/2015.
 */
public class NextUpFragment extends Fragment implements SurfaceHolder.Callback, InterstitialAd.InterstitialListener {
    private PrefManager prefManager;
    private String tokenId, fbToken, clipId, likeCountValue, disLikeCountValue, uid, video, ArtistName, loginUserUid, islongplayerYN;
    private TextView likeTextView, dislikeTextView, shareTextView, ClipArtistsTextView, clipTitleTextView, dislikeCount, likeCount, likeView, dislikeView, playbutton, pausebutton;

    // mediaplayer veriable-------------//
    private MediaPlayer mMediaPlayer, samplePlayer;
    private SurfaceView circleSurfaceView;
    private SurfaceHolder holder;
    private SeekArc mSeekArc;
    private TextView mSeekArcProgress;
    private Handler mHandler = new Handler();
    private Utilities utils;
    //private Dialog nDialog;
    private LinearLayout refreshLayout;
    private CircleOverlayView CircleOverlayView1;
    private FrameLayout frameLayout;
    private String direction = "";
    private String title;
    private int leftswipecount = 0;
    private int rightswipecount = 0;
    private ProgressBar nextdialogProgressBar;
    public static final String PLACEMENT_ID = "221998";
    private InterstitialAd interstitialAd;
    public static final String TAG = NextUpFragment.class.getSimpleName();
    Handler handler = new Handler();
    private Runnable runnable;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_nextup, container, false);
        // MMSDK.initialize(getActivity());
        // UserData userData = new UserData().setAge(25).setGender(UserData.Gender.MALE);
        // MMSDK.setUserData(userData);
        try {
            interstitialAd = InterstitialAd.createInstance(PLACEMENT_ID);
            interstitialAd.setListener(this);
        } catch (MMException e) {
            e.printStackTrace();
        }
        LoadDate(v);
        return v;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void LoadDate(View v) {
        FindId(v);
        CircleOverlayView1.setBackground(getResources().getDrawable(R.drawable.app_logo));
        GestureDetectorMethod(v);
        utils = new Utilities();
        if (nextdialogProgressBar.getVisibility() == View.GONE) {
            nextdialogProgressBar.setVisibility(View.VISIBLE);
        }
        mMediaPlayer = new MediaPlayer();
        prefManager = new PrefManager(getActivity());
        tokenId = prefManager.getObjectId();
        fbToken = prefManager.getfbToken();
        loginUserUid = prefManager.getUID();
        ClipArtistsTextView.setText(ArtistName);
        if (direction.startsWith("preview")) {
            commonVollyMethod(judgeMeUrls.PREVCLIP + "&token=" + tokenId, v, 2);
        } else {
            commonVollyMethod(judgeMeUrls.NEXTUP + "&token=" + tokenId, v, 2);
        }
        onClick(v);

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
    }

    private void onClick(View v) {
        clipTitleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!clipTitleTextView.getText().toString().matches("")) {
                    Bundle bundle = new Bundle();
                    bundle.putString("clipID", clipId);
                    Intent i = new Intent(getActivity(), CommandActivity.class);
                    i.putExtras(bundle);
                    startActivity(i);
                    getActivity().overridePendingTransition(R.anim.activity_enter, R.anim.activity_leave);
                }

            }
        });

        ClipArtistsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), UserProfileActivity.class);
                i.putExtra("uid", uid);
                i.putExtra("tokenId", tokenId);
                i.putExtra("ClipId", clipId);
                startActivity(i);
                getActivity().overridePendingTransition(R.anim.activity_enter, R.anim.activity_leave);
            }
        });
        likeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == likeTextView) {
                    likeView.setVisibility(View.VISIBLE);
                    likeTextView.setVisibility(View.GONE);
                    dislikeView.setVisibility(View.GONE);
                    dislikeTextView.setVisibility(View.VISIBLE);
                    //likeCount.setVisibility(View.VISIBLE);
                    commonVollyMethod(judgeMeUrls.like + "&token=" + tokenId + "&clipID=" + clipId + "&fbtoken=" + fbToken, v, 3);
                }

                //likeCount.setText(likeCountValue);
            }
        });
        dislikeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == dislikeTextView) {
                    dislikeView.setVisibility(View.VISIBLE);
                    dislikeTextView.setVisibility(View.GONE);
                    likeView.setVisibility(View.GONE);
                    likeTextView.setVisibility(View.VISIBLE);
                    //dislikeCount.setVisibility(View.VISIBLE);
                    commonVollyMethod(judgeMeUrls.DILIKE + "&token=" + tokenId + "&clipID=" + clipId + "&fbtoken=" + fbToken, v, 3);
                }

                //dislikeCount.setText(disLikeCountValue);
            }
        });
        shareTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (video != null) {
                    // requestAd();
                    Intent i = new Intent(getActivity(), ShareActivity.class);
                    i.putExtra("url", clipId);
                    i.putExtra("clipTitle", title);
                    startActivity(i);
                    getActivity().overridePendingTransition(R.anim.activity_enter, R.anim.activity_leave);

                }

            }
        });
    }

    private void GestureDetectorMethod(final View v) {
        final GestureDetector gesture = new GestureDetector(getActivity(),
                new GestureDetector.SimpleOnGestureListener() {

                    @Override
                    public boolean onDown(MotionEvent e) {
                        return true;
                    }

                    @Override
                    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                                           float velocityY) {
                        //Log.i("", "onFling has been called!");
                        final int SWIPE_MIN_DISTANCE = 100;
                        final int SWIPE_MAX_OFF_PATH = 200;
                        final int SWIPE_THRESHOLD_VELOCITY = 100;
                        try {
                            if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
                                return false;
                            if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
                                    && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                                onStart();
                                if (samplePlayer != null) {
                                    samplePlayer = null;
                                }
                                if (rightswipecount == 2) {
                                    requestAd();
                                } else {
                                    direction = "nextup";
                                    if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
                                        mMediaPlayer.pause();
                                        mMediaPlayer.stop();
                                        RefreshFragment();
                                        //commonVollyMethod(judgeMeUrls.NEXTUP + "&token=" + tokenId, v, 2);
                                    } else {
                                        RefreshFragment();
                                        // commonVollyMethod(judgeMeUrls.NEXTUP + "&token=" + tokenId, v, 2);
                                    }
                                    rightswipecount++;
                                }
                            } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
                                    && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                                onStart();
                                if (samplePlayer != null) {
                                    samplePlayer = null;
                                }
                                if (leftswipecount == 2) {
                                    requestAd();
                                } else {
                                    direction = "preview";
                                    if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
                                        mMediaPlayer.pause();
                                        mMediaPlayer.stop();
                                        RefreshFragment();
                                        //commonVollyMethod(judgeMeUrls.PREVCLIP + "&token=" + tokenId, v, 2);
                                    } else {
                                        RefreshFragment();
                                        //commonVollyMethod(judgeMeUrls.PREVCLIP + "&token=" + tokenId, v, 2);
                                    }
                                    leftswipecount++;
                                }
                            }
                        } catch (Exception e) {
                        }
                        return super.onFling(e1, e2, velocityX, velocityY);
                    }
                });

        v.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gesture.onTouchEvent(event);
            }
        });
    }

    private void requestAd() {
        if (interstitialAd != null) {
            InterstitialAd.InterstitialAdMetadata metadata = new InterstitialAd.InterstitialAdMetadata();
            interstitialAd.load(getActivity(), metadata);
            SharedPreferences sharedPreferences =
                    PreferenceManager.getDefaultSharedPreferences(getActivity());

            InterstitialAd.DisplayOptions displayOptions = new InterstitialAd.DisplayOptions().setImmersive(
                    sharedPreferences.getBoolean(getResources().getString(R.string.app_name), false));
            try {
                interstitialAd.show(getActivity().getBaseContext(), displayOptions);
            } catch (MMException e) {
                //Log.i(TAG, "Unable to show interstitial ad content, exception occurred");
                e.printStackTrace();
            }
        } else {
            //Log.i(TAG, "Unable to load interstitial ad content, InterstitialAd instance is null.");
        }
    }


    private void RefreshFragment() {
        Fragment frg = null;
        frg = getFragmentManager().findFragmentByTag("NextUpFragment");
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(frg);
        ft.attach(frg).commit();
    }

    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
        Log.d(TAG, "onpause Call");
        if (mMediaPlayer != null) {
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.stop();
            }
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (mMediaPlayer != null) {
            if (mMediaPlayer.isPlaying()) {
                mMediaPlayer.stop();
                mMediaPlayer.reset();
                mMediaPlayer.release();
            }
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mSeekArc.setVisibility(View.INVISIBLE);
            CircleOverlayView1.setVisibility(View.GONE);
            refreshLayout.setVisibility(View.GONE);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 2);
            frameLayout.setLayoutParams(params);

        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            mSeekArc.setVisibility(View.VISIBLE);
            CircleOverlayView1.setVisibility(View.VISIBLE);
            refreshLayout.setVisibility(View.VISIBLE);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1);
            params.weight = 1.2f;
            frameLayout.setLayoutParams(params);
        }

    }

    private void FindId(View v) {
        likeView = (TextView) v.findViewById(R.id.likeView);
        ClipArtistsTextView = (TextView) v.findViewById(R.id.ClipArtistsTextView);
        likeTextView = (TextView) v.findViewById(R.id.likeTextView);
        shareTextView = (TextView) v.findViewById(R.id.shareTextView);
        dislikeTextView = (TextView) v.findViewById(R.id.dislikeTextView);
        dislikeView = (TextView) v.findViewById(R.id.dislikeView);
        clipTitleTextView = (TextView) v.findViewById(R.id.clipTitleTextView);
        dislikeCount = (TextView) v.findViewById(R.id.dislikeCount);
        likeCount = (TextView) v.findViewById(R.id.likeCount);
        nextdialogProgressBar = (ProgressBar) v.findViewById(R.id.nextdialogProgressBar);
        playbutton = (TextView) v.findViewById(R.id.playbutton_nextup);
        pausebutton = (TextView) v.findViewById(R.id.pausebutton_nextup);
        mSeekArc = (SeekArc) v.findViewById(R.id.seekArc_nextup);
        circleSurfaceView = (SurfaceView) v.findViewById(R.id.circleVideoView_nextup);
        mSeekArcProgress = (TextView) v.findViewById(R.id.seekArcProgress_nextup);
        refreshLayout = (LinearLayout) v.findViewById(R.id.refreshLayout_NextUp);
        CircleOverlayView1 = (CircleOverlayView) v.findViewById(R.id.CircleOverlayView_NextUp);
        frameLayout = (FrameLayout) v.findViewById(R.id.frameLayout_NextUp);


    }

    private void commonVollyMethod(String url, final View v, final int type) {
        VolleyCommonClass.getDataFromServer(url, new interfaceSelector() {
            @Override
            public void volleyError(String error) {
            }

            @Override
            public void volleyResponse(JSONObject response) throws JSONException {
                if (type == 2) {
                    Type2method(response, v);
                } else if (type == 3) {
                    Type3method(response, v);
                } else if (type == 4) {
                    Type4method(response, v);
                }

            }
        });
    }

    private void Type4method(JSONObject response, View v) {
        try {
            JSONObject object = response.getJSONObject("data");
            JSONObject clipDetail = object.getJSONObject("clip");
            String likescount = clipDetail.optString("likescount");
            String dislikescount = clipDetail.optString("dislikescount");
            likeCount.setText(likescount);
            dislikeCount.setText(dislikescount);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void Type3method(JSONObject response, final View v) {
        JSONObject object = null;
        try {
            object = response.getJSONObject("data");
            String resultcode = object.optString("resultcode");
            if (resultcode.startsWith("200")) {
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        commonVollyMethod(judgeMeUrls.FETCH_CLIPS + "&token=" + prefManager.getObjectId() + "&clipID=" + clipId, v, 4);
                    }
                });
                thread.start();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void Type2method(JSONObject response, final View v) {
        NextUpClipDetails setClipDetail = new NextUpClipDetails();
        setClipDetail.getClipDetails(response);
        if (setClipDetail.getResultcode().startsWith("200")) {
            likeCount.setText(setClipDetail.getLikescount());
            dislikeCount.setText(setClipDetail.getDislikescount());
            title = setClipDetail.getTitle();
            clipTitleTextView.setText(title);
            likeCountValue = setClipDetail.getLikescount();
            disLikeCountValue = setClipDetail.getDislikescount();
            ArtistName = setClipDetail.getArtistname();
            islongplayerYN = setClipDetail.getIslongplayerYN();
            //ClipArtistsTextView.setText(setClipDetail.getArtistname());
            clipId = setClipDetail.getClipID();
            uid = setClipDetail.getUid();
            ((MainActivity) getActivity()).setActionBarTitle(ArtistName, uid);
            video = setClipDetail.getStreamURL();
            JSONArray likes = setClipDetail.getLikesanddislikes();

            for (int i = 0; i < likes.length(); i++) {
                try {
                    JSONObject likesanddislikesObject = null;
                    likesanddislikesObject = likes.getJSONObject(i);
                    String likeuserID = likesanddislikesObject.optString("likeuserID");
                    if (loginUserUid.equals(likeuserID)) {
                        String value = likesanddislikesObject.optString("value");
                        if (value.startsWith("-1")) {
                            dislikeView.setVisibility(View.VISIBLE);
                            dislikeTextView.setVisibility(View.GONE);
                        } else if (value.startsWith("1")) {
                            likeView.setVisibility(View.VISIBLE);
                            likeTextView.setVisibility(View.GONE);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            playVideo(v);
        } else if (setClipDetail.getResultcode().startsWith("500")) {
            /*if (nDialog != null) {
                nDialog.dismiss();
            }*/
            AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
            builder1.setMessage(setClipDetail.getResultmessage());
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
    }

    private void playVideo(final View v) {
//        Log.d(TAG, "Play From nextup fragment");
        holder = circleSurfaceView.getHolder();
        holder.addCallback(this);
        holder.setFixedSize(176, 144);
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setDisplay(holder);
        try {
            mMediaPlayer.reset();
            mMediaPlayer.setDataSource(getActivity(), Uri.parse(video));
            mMediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        CircleOverlayView1.setBackgroundColor(Color.parseColor("#00000000"));
        setFixedSizeVideo();
        mMediaPlayer.start();
        mSeekArc.setProgress(0);
        updateProgressBar();
        if (nextdialogProgressBar.getVisibility() == View.VISIBLE) {
            nextdialogProgressBar.setVisibility(View.GONE);
        }
        if (islongplayerYN.startsWith("N")) {
            runnable = new Runnable() {
                @Override
                public void run() {
                    if (mMediaPlayer != null && mMediaPlayer.isPlaying())
                        mMediaPlayer.stop();
                    //Log.d(TAG, "islongplayerNo");
                    RefreshFragment();
                }
            };
            handler.postDelayed(runnable, 60000);
        }

        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                CircleOverlayView1.setBackground(getResources().getDrawable(R.drawable.app_logo));
                /*if (samplePlayer != null) {
                    samplePlayer = null;
                }*/
                mp.stop();
                mp.reset();
                // mMediaPlayer.reset();
                mMediaPlayer.seekTo(0);
                Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.blink_animation);
                likeTextView.setFocusable(true);
                likeTextView.startAnimation(animation);
                likeView.startAnimation(animation);
                dislikeTextView.startAnimation(animation);
                dislikeView.startAnimation(animation);
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        direction = "nextup";
                        RefreshFragment();
                        // commonVollyMethod(judgeMeUrls.NEXTUP + "&token=" + tokenId, v, 2);
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
            if (mMediaPlayer != null) {
                long totalDuration = mMediaPlayer.getDuration();
                long currentDuration = mMediaPlayer.getCurrentPosition();
                String progressValue = utils.milliSecondsToTimer(currentDuration);
                mSeekArcProgress.setText(progressValue);
                int progress = (int) (utils.getProgressPercentage(currentDuration, totalDuration));
                mSeekArc.setProgress(progress);
                mHandler.postDelayed(this, 100);
            }
        }
    };

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

    }

    @Override
    public void onLoadFailed(InterstitialAd interstitialAd, InterstitialAd.InterstitialErrorStatus interstitialErrorStatus) {

    }

    @Override
    public void onShown(InterstitialAd interstitialAd) {

    }

    @Override
    public void onShowFailed(InterstitialAd interstitialAd, InterstitialAd.InterstitialErrorStatus interstitialErrorStatus) {

    }

    @Override
    public void onClosed(InterstitialAd interstitialAd) {
        if (direction.startsWith("nextup")) {
            rightswipecount = 0;
            rightswipecount++;
        } else if (direction.startsWith("preview")) {
            leftswipecount = 0;
            leftswipecount++;
        }
    }

    @Override
    public void onClicked(InterstitialAd interstitialAd) {

    }

    @Override
    public void onAdLeftApplication(InterstitialAd interstitialAd) {

    }

    @Override
    public void onExpired(InterstitialAd interstitialAd) {

    }
}
