package utility.imaginet.com.judgeme.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONObject;

import utility.imaginet.com.judgeme.R;
import utility.imaginet.com.judgeme.helper.PrefManager;
import utility.imaginet.com.judgeme.helper.VolleyCommonClass;
import utility.imaginet.com.judgeme.helper.judgeMeUrls;
import utility.imaginet.com.judgeme.interfaceClass.interfaceSelector;

/**
 * Created by IM0033 on 12/23/2015.
 */
public class ReplyComments extends Activity {
    private Button cancelButton, ReplyButton;
    private PrefManager prefManager;
    private String token, text, clipID, commentID;
    private EditText reply_Edittext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.replycomment_activity);
        LoadData();
    }

    private void LoadData() {
        findViewById();
        prefManager = new PrefManager(getApplicationContext());
        token = prefManager.getObjectId();
        onClickAction();
    }

    private void onClickAction() {

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_up_leave, R.anim.slide_down_leave);
            }
        });
        ReplyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = getIntent().getExtras();
                if (bundle != null) {
                    clipID = bundle.getString("clipID");
                    commentID = bundle.getString("commentID");
                }
                text = reply_Edittext.getText().toString();
                String message = text.replace(" ", "%20");
                if (!text.matches("")) {
                    submitComments(judgeMeUrls.SUBMIT_COMMENT + "&token=" + token + "&clipID=" + clipID + "&text=" + message + "&commentID=" + commentID);
                } else {
                    reply_Edittext.setError("You did not enter a comments");
                }
            }
        });

    }

    private void submitComments(String url) {

        VolleyCommonClass.getDataFromServer(url, new interfaceSelector() {
            @Override
            public void volleyError(String error) {

            }

            @Override
            public void volleyResponse(JSONObject response) {
                Bundle bundle = new Bundle();
                bundle.putString("clipID", clipID);
                Intent i = new Intent(getApplicationContext(), CommandActivity.class);
                i.putExtras(bundle);
                startActivity(i);
                finish();
            }
        });
    }

    private void findViewById() {
        reply_Edittext = (EditText) findViewById(R.id.reply_Edittext);
        cancelButton = (Button) findViewById(R.id.cancelButton);
        ReplyButton = (Button) findViewById(R.id.rePlyButton);
    }
}
