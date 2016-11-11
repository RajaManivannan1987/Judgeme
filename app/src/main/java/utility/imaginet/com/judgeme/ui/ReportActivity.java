package utility.imaginet.com.judgeme.ui;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import utility.imaginet.com.judgeme.R;
import utility.imaginet.com.judgeme.helper.PrefManager;
import utility.imaginet.com.judgeme.helper.VolleyCommonClass;
import utility.imaginet.com.judgeme.helper.judgeMeUrls;
import utility.imaginet.com.judgeme.interfaceClass.interfaceSelector;

/**
 * Created by IM0033 on 2/18/2016.
 */
public class ReportActivity extends Activity {
    private TextView clipTitleTextview;
    private EditText reportTextEditText, typeEditText;
    private Button BackButton, reportSubmitButton;
    private String title, token, clipId;
    private PrefManager pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_problem_activity);
        pref = new PrefManager(getApplicationContext());
        title = getIntent().getExtras().getString("clipTitle");
        clipId = getIntent().getExtras().getString("clipId");
        token = pref.getObjectId();
        LoadData();
    }

    private void LoadData() {
        findViewById();
        onClickL();
        clipTitleTextview.setText(title);
    }

    private void onClickL() {
        BackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        reportSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String type = typeEditText.getText().toString();
                String text = reportTextEditText.getText().toString();
                if (type.matches("")) {
                    typeEditText.setError("Enter report type");
                } else if (text.matches("")) {
                    reportTextEditText.setError("Enter report text");
                } else if (!type.matches("") && (!text.matches(""))) {
                    commonVollyMethod(judgeMeUrls.ReportAPROBLEM + "&token=" + token + "&clipID=" + clipId + "&text=" + text + "&subtype=" + type);
                }
            }
        });
    }

    private void commonVollyMethod(String url) {
        final Dialog nDialog = new Dialog(ReportActivity.this);
        nDialog.setContentView(R.layout.dialog_progress);
        nDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        nDialog.setCancelable(true);
        nDialog.show();
        VolleyCommonClass.getDataFromServer(url, new interfaceSelector() {
            @Override
            public void volleyError(String error) {
            }

            @Override
            public void volleyResponse(JSONObject response) throws JSONException {
                nDialog.dismiss();
                JSONObject object = response.getJSONObject("data");
                String resultcode = object.getString("resultcode");
                if (resultcode.startsWith("200")) {
                    reportTextEditText.setText("");
                    typeEditText.setText("");
                    finish();
                }
            }
        });
    }

    private void findViewById() {
        clipTitleTextview = (TextView) findViewById(R.id.clipTitleTextview);
        BackButton = (Button) findViewById(R.id.reportProblemBackButton);
        reportTextEditText = (EditText) findViewById(R.id.reportTextEditText);
        typeEditText = (EditText) findViewById(R.id.typeEditText);
        reportSubmitButton = (Button) findViewById(R.id.reportSubmitButton);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.animation_enter, R.anim.animation_leave);
    }
}
