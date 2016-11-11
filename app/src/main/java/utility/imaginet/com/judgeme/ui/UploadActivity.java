package utility.imaginet.com.judgeme.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import utility.imaginet.com.judgeme.R;
import utility.imaginet.com.judgeme.adapter.GenresAdapter;
import utility.imaginet.com.judgeme.helper.PrefManager;
import utility.imaginet.com.judgeme.helper.Upload_CircleSurfaceView;
import utility.imaginet.com.judgeme.helper.VolleyCommonClass;
import utility.imaginet.com.judgeme.helper.judgeMeUrls;
import utility.imaginet.com.judgeme.interfaceClass.interfaceSelector;
import utility.imaginet.com.judgeme.models.GenresSetting;
import utility.imaginet.com.judgeme.models.MyGenres;

/**
 * Created by IM0033 on 12/10/2015.
 */
public class UploadActivity extends AppCompatActivity implements SurfaceHolder.Callback {
    private static final String TAG = UploadActivity.class.getSimpleName();
    private TextView UploadclipArtistName, playbutton, pausebutton, LongplayerTextView;
    // private MediaController mCC;
//    private ProgressBar Upload_clips_Progressbar;
    // private VideoView vidPreview;
    private Button record_Button, Upload_clips_BackButton, makeLongplayButton, Longplayer;
    private EditText clip_title_Name_Edittext;
    private GridView Upload_clips_GenresGridView;
    private ArrayList<MyGenres> list;
    private GenresAdapter adapter;
    private String title, clipID, clipPath, token;
    private PrefManager prefManager;
    private ArrayList<GenresSetting> updateGenreslist;
    private MediaPlayer mMediaPlayer;
    private Upload_CircleSurfaceView circleSurfaceView;
    private SurfaceHolder holder;

    @Override
    protected void onResume() {
        super.onResume();
        genresList(judgeMeUrls.LISTGenresUrl);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_clip);
        LoadData();
    }

    private void LoadData() {
        findViewById();
        mMediaPlayer = new MediaPlayer();
        updateGenreslist = new ArrayList<GenresSetting>();
        list = new ArrayList<MyGenres>();
        prefManager = new PrefManager(getApplicationContext());
        token = prefManager.getObjectId();
        clip_title_Name_Edittext.requestFocus();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            clipID = bundle.getString("clipID");
            // Log.i("clipId", clipID);
            commonVollyMethod(judgeMeUrls.FETCH_CLIPS + "&clipID=" + clipID, 1);
            //Log.i("clipId", judgeMeUrls.FETCH_CLIPS + "&clipID=" + clipID);
        }
        onClickEvent();
    }

    private void genresList(String listGenresUrl) {
        VolleyCommonClass.getDataFromServer(listGenresUrl, new interfaceSelector() {
            @Override
            public void volleyError(String error) {
            }

            @Override
            public void volleyResponse(JSONObject response) {
                try {
                    JSONObject obj = response.getJSONObject("data");
                    String resultcode = obj.optString("resultcode");
                    if (resultcode.startsWith("200")) {
                        String resultmessage = obj.optString("resultmessage");
                        JSONArray array = obj.getJSONArray("genres");

                        for (int i = 0; i < array.length(); i++) {
                            MyGenres setData = new MyGenres();
                            setData.setGenres(array.get(i).toString());
                            list.add(setData);
                        }
                        //  adapter.notifyDataSetChanged();
                        //adapter = new GenresAdapter(getApplicationContext(), list, clipID);
                        // Upload_clips_GenresGridView.setAdapter(adapter);
                        //Upload_clips_GenresGridView.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE_MODAL);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        });
    }

    private void onClickEvent() {
        Upload_clips_BackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        record_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // recordVideo();
                Bundle bundle = new Bundle();
                bundle.putString("clipPath", clipPath);
                bundle.putString("title", title);
                Log.i("clipId", clipPath);
                Intent i = new Intent(getApplicationContext(), TransprentCameraActivity.class);
                i.putExtras(bundle);
                startActivity(i);
            }
        });
        clip_title_Name_Edittext.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    updateClips();
                    return true;

                }
                return false;
            }


        });
        makeLongplayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commonVollyMethod(judgeMeUrls.MAKELONGPLAY + "&token=" + token + "&clipID=" + clipID, 3);
            }
        });

    }

    private void updateClips() {
        String text = clip_title_Name_Edittext.getText().toString();
        text = text.replace(" ", "%20");
        commonVollyMethod(judgeMeUrls.UPDATECLIPS + "&token=" + token + "&clipID=" + clipID + "&title=" + text, 2);
    }

    private void previewMedia(String filePath) {
        holder = circleSurfaceView.getHolder();
        holder.addCallback(this);
        holder.setFixedSize(145, 130);
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setDisplay(holder);
        try {
            // mMediaPlayer.reset();
            mMediaPlayer.setDataSource(getApplicationContext(), Uri.parse(filePath));
            mMediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mMediaPlayer.start();
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.stop();
                mp.reset();

            }
        });
        circleSurfaceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMediaPlayer.isPlaying()) {
                    mMediaPlayer.pause();
                    pausebutton.setVisibility(View.VISIBLE);
                    playbutton.setVisibility(View.GONE);
                    //mSeekArcProgress.setVisibility(View.VISIBLE);
                    //mMediaPlayer.seekTo(0);
                } else {
                    playbutton.setVisibility(View.VISIBLE);
                    playbutton.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            playbutton.setVisibility(View.INVISIBLE);
                            // mSeekArcProgress.setVisibility(View.INVISIBLE);
                        }
                    }, 2000);
                    pausebutton.setVisibility(View.GONE);
                    mMediaPlayer.start();
                }
            }
        });

    }

    private void findViewById() {
        UploadclipArtistName = (TextView) findViewById(R.id.UploadclipArtistName);
        //Upload_clips_Progressbar = (ProgressBar) findViewById(R.id.Upload_clips_Progressbar);
        //vidPreview = (VideoView) findViewById(R.id.Upload_clips_Videoview);
        record_Button = (Button) findViewById(R.id.record_Button);
        clip_title_Name_Edittext = (EditText) findViewById(R.id.clip_title_Name_Edittext);
        clip_title_Name_Edittext.setInputType(InputType.TYPE_CLASS_TEXT);
        Upload_clips_GenresGridView = (GridView) findViewById(R.id.Upload_clips_GenresGridView);
        Upload_clips_BackButton = (Button) findViewById(R.id.Upload_clips_BackButton);
        playbutton = (TextView) findViewById(R.id.playbutton_upload);
        pausebutton = (TextView) findViewById(R.id.pausebutton_upload);
        circleSurfaceView = (Upload_CircleSurfaceView) findViewById(R.id.circleVideoView_upload);
        makeLongplayButton = (Button) findViewById(R.id.makeLongplayButton);
        LongplayerTextView = (TextView) findViewById(R.id.LongplayerTextView);
        LongplayerTextView.setText("Make Long \nPlay");
        Longplayer = (Button) findViewById(R.id.Longplayer);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // updateClips();
    }


    private void commonVollyMethod(String url, final int type) {
        Log.d("response", url + "");
        VolleyCommonClass.getDataFromServer(url, new interfaceSelector() {
            @Override
            public void volleyError(String error) {
            }

            @Override
            public void volleyResponse(JSONObject response) {
                String streamURL;
                if (type == 1) {
                    try {
                        JSONObject obj = response.getJSONObject("data");
                        String resultcode = obj.optString("resultcode");
                        if (resultcode.startsWith("200")) {
                            JSONObject clipResponse = obj.getJSONObject("clip");
                            clipPath = clipResponse.optString("clipID");
                            String artistname1 = clipResponse.optString("artistname");
                            String streamURL1 = clipResponse.optString("streamURL");
                            streamURL = clipResponse.optString("fileURL");
                            previewMedia(streamURL);
                            String islongplayerYN = clipResponse.optString("islongplayerYN");
                            if (islongplayerYN.startsWith("Y")) {
                                makeLongplayButton.setVisibility(View.GONE);
                                Longplayer.setVisibility(View.VISIBLE);
                                LongplayerTextView.setText("Long \nPlayer");
                            }
                            title = clipResponse.optString("title");
                            UploadclipArtistName.setText(artistname1);
                            clip_title_Name_Edittext.setText(title);

                            JSONArray genresArray = clipResponse.getJSONArray("genres");
                            for (int i = 0; i < genresArray.length(); i++) {
                                GenresSetting setGenres = new GenresSetting();
                                setGenres.setUpdateClipGenres(genresArray.get(i).toString());
                                updateGenreslist.add(setGenres);

                            }
                            adapter = new GenresAdapter(getApplicationContext(), updateGenreslist, true);
                            adapter = new GenresAdapter(getApplicationContext(), list, clipID);
                            Upload_clips_GenresGridView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (type == 3) {
                    JSONObject obj = null;
                    String resultmessage = null;
                    Log.d("makeplayresponse", response + "");
                    try {
                        obj = response.getJSONObject("data");
                        String resultcode = obj.optString("resultcode");
                        JSONObject clipobject = response.getJSONObject("clip");
                        String islongplayerYN = clipobject.getString("islongplayerYN");
                        if (islongplayerYN.startsWith("Y")) {
                            makeLongplayButton.setVisibility(View.GONE);
                            Longplayer.setVisibility(View.VISIBLE);
                            LongplayerTextView.setText("Long \nPlayer");
                        } else {

                        }
                        if (resultcode.startsWith("200")) {
                            resultmessage = obj.getString("resultmessage");
                        } else if (resultcode.startsWith("500")) {
                            resultmessage = obj.getString("resultmessage");
                        }
                        AlertDialog.Builder alertBox = new AlertDialog.Builder(UploadActivity.this);
                        alertBox.setMessage(resultmessage);
                        alertBox.setPositiveButton("Ok",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                }).create().show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
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
}
