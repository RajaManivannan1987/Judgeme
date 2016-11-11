package utility.imaginet.com.judgeme.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import utility.imaginet.com.judgeme.R;
import utility.imaginet.com.judgeme.helper.PrefManager;
import utility.imaginet.com.judgeme.helper.Upload;

/**
 * Created by IM0033 on 12/14/2015.
 */
public class TransprentCameraActivity extends AppCompatActivity {
    private PrefManager prefManager;
    private ImageView camera_closeButtom;
    private LinearLayout openCameraLayout, openGalleryLayout;
    private static final int SELECT_PICTURE = 1;
    private static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;
    public static final int MEDIA_TYPE_VIDEO = 2;
    //private int serverResponseCode = 0;
    private Uri fileUri;
    private String token, title, clipid;
    private String selectedImageUri1 = null;
    private Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.camera_choose_activity);
        mContext = this;
        LoadData();
    }

    private void LoadData() {
        findViewById();
        prefManager = new PrefManager(getApplicationContext());
        token = prefManager.getObjectId();
        onClick();
    }

    private void onClick() {
        camera_closeButtom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.animation_enter, R.anim.animation_leave);
            }
        });
        openCameraLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recordVideo();

            }
        });
        openGalleryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File mediaStorageDir = new File(
                        Environment
                                .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                        "JudgeMe_video");
                Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickIntent.setType("video/*");
                startActivityForResult(pickIntent, SELECT_PICTURE);
                //finish();
             /*   Intent i=new Intent(Intent.ACTION_GET_CONTENT);
                Uri uri = Uri.parse(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getPath()
                        + "/JudgeMe_video_Files/");
                i.setDataAndType(uri, "video*//**//*");
                startActivity(Intent.createChooser(i, "Open folder"));
*/
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("TransprentCameraActivity", "onActivityResult");
        if (requestCode == CAMERA_CAPTURE_VIDEO_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                selectedImageUri1 = fileUri.getPath();
                uploadVideo();
                //finish();
            }
        } else if (requestCode == SELECT_PICTURE) {
            if (resultCode == RESULT_OK) {
                Uri selectedUri = data.getData();
                if (data != null) {
                    String[] columns = {MediaStore.Images.Media.DATA,
                            MediaStore.Images.Media.MIME_TYPE};
                    Cursor cursor = getContentResolver().query(selectedUri, columns, null, null, null);
                    cursor.moveToFirst();
                    int pathColumnIndex = cursor.getColumnIndex(columns[0]);
                    int mimeTypeColumnIndex = cursor.getColumnIndex(columns[1]);
                    selectedImageUri1 = cursor.getString(pathColumnIndex);
                    String mimeType = cursor.getString(mimeTypeColumnIndex);
                    cursor.close();
                    if (mimeType.startsWith("image")) {
                    } else if (mimeType.startsWith("video")) {
                        uploadVideo();
                    }
                } else {
                    Log.d("uriPath", "Your path is null");
                }
                //finish();
            }
        }

    }

    private void uploadVideo() {
        class UploadVideo extends AsyncTask<Void, Void, String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
                alertDialog.setTitle("Uploading...");
                alertDialog.setMessage("Your video is upload to judgeme server.");
                alertDialog.setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                finish();
                            }
                        });

                AlertDialog alertBox = alertDialog.create();
                alertBox.show();
            }

            @Override
            protected String doInBackground(Void... params) {
                String C_id = null;
                String msg;
                Upload u = new Upload();
                Bundle bundle = getIntent().getExtras();
                if (bundle != null) {
                    C_id = bundle.getString("clipPath");
                    title = bundle.getString("title");
                    title = title.replace(" ", "%20");
                    msg = u.uploadVideo(selectedImageUri1, token, C_id, title, TransprentCameraActivity.this);
                } else {
                    msg = u.uploadVideo(selectedImageUri1, token, C_id, title, TransprentCameraActivity.this);

                }

                return msg;
            }

            @Override
            protected void onPostExecute(final String s) {
                super.onPostExecute(s);
                // dialog.dismiss();
                if (s != null) {
                    try {
                        JSONObject jsonobject = new JSONObject(s);
                        JSONObject obj = jsonobject.getJSONObject("data");
                        String resultcode = obj.optString("resultcode");
                        if (resultcode.startsWith("200")) {
                            JSONObject clip = obj.getJSONObject("clip");
                            clipid = clip.getString("clipID");
                            Bundle bundle = new Bundle();
                            bundle.putString("clipID", clipid);
                            //Toast.makeText(getApplicationContext(), clipid, Toast.LENGTH_LONG).show();

                            LayoutInflater inflater = getLayoutInflater();
                            View toastRoot = inflater.inflate(R.layout.activity_custom_toast, null);
                            final Toast toast = new Toast(mContext);
                            toast.setView(toastRoot);
                            toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL,
                                    0, 0);
                            toast.setDuration(Toast.LENGTH_LONG);
                            toast.show();

                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    toast.cancel();
                                }
                            }, 80000);
                           // Log.d("ToastFromOnPost", clipid);
                            /*Intent i = new Intent(getApplicationContext(), UploadActivity.class);
                            i.putExtras(bundle);
                            startActivity(i);*/
                        }
                        // finish();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    /*AlertDialog.Builder alertDialog = new AlertDialog.Builder(getApplicationContext());
                    alertDialog.setTitle("Upload Clip");
                    alertDialog.setMessage("Your video is ready.");
                    alertDialog.setPositiveButton("Edit",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    try {
                                        JSONObject jsonobject = new JSONObject(s);
                                        JSONObject obj = jsonobject.getJSONObject("data");
                                        String resultcode = obj.optString("resultcode");
                                        if (resultcode.startsWith("200")) {
                                            JSONObject clip = obj.getJSONObject("clip");
                                            clipid = clip.getString("clipID");
                                            Bundle bundle = new Bundle();
                                            bundle.putString("clipID", clipid);
                                            Intent i = new Intent(getApplicationContext(), UploadActivity.class);
                                            i.putExtras(bundle);
                                            startActivity(i);
                                        }
                                        finish();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                    alertDialog
                            .setNegativeButton("Continue",
                                    new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                    AlertDialog alertBox = alertDialog.create();
                    alertBox.show();*/
                }
            }
        }
        UploadVideo uv = new UploadVideo();
        uv.execute();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("file_uri", fileUri);
    }

    private void recordVideo() {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        fileUri = getOutputMediaFileUri(MEDIA_TYPE_VIDEO);
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file
        startActivityForResult(intent, CAMERA_CAPTURE_VIDEO_REQUEST_CODE);
        overridePendingTransition(R.anim.activity_enter, R.anim.activity_leave);
    }

    private Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    private File getOutputMediaFile(int type) {
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "JudgeMe_video");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("TrasprentClass", "Oops! Failed create "
                        + "JudgeMe_video_Files" + " directory");
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        String Filevalue = UUID.randomUUID().toString();
        String vvv = Filevalue.replace("-", "_");
        File mediaFile;
        if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "VID" + "_" + "Judgeme" + "_" + vvv + ".mp4");
            /*mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + vvv + "_" + "VID_" + timeStamp + ".mp4");*/
        } else {
            return null;
        }

        return mediaFile;
    }

    private void findViewById() {
        camera_closeButtom = (ImageView) findViewById(R.id.camera_closeButtom);
        openCameraLayout = (LinearLayout) findViewById(R.id.openCameraLayout);
        openGalleryLayout = (LinearLayout) findViewById(R.id.openGalleryLayout);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}
