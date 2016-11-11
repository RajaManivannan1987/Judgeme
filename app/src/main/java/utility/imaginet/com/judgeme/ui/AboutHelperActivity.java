package utility.imaginet.com.judgeme.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;

import org.json.JSONObject;

import utility.imaginet.com.judgeme.R;
import utility.imaginet.com.judgeme.helper.VolleyCommonClass;
import utility.imaginet.com.judgeme.helper.judgeMeUrls;
import utility.imaginet.com.judgeme.interfaceClass.interfaceSelector;

/**
 * Created by IM0033 on 3/18/2016.
 */
public class AboutHelperActivity extends AppCompatActivity {
    private TextView aboutheplTextView, abouthelp_backButton, helperTitleTextview;
    private String url, helpVal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_help_activity);
        aboutheplTextView = (TextView) findViewById(R.id.aboutheplTextView);
        abouthelp_backButton = (TextView) findViewById(R.id.abouthelp_backButton);
        helperTitleTextview= (TextView) findViewById(R.id.helperTitleTextview);
        helpVal = getIntent().getExtras().getString("help");
        if (helpVal.startsWith("Terms & Condition")) {
            url = judgeMeUrls.HELP + "tnc";
            helperTitleTextview.setText(helpVal);
        } else if (helpVal.startsWith("Help")) {
            url = judgeMeUrls.HELP + "help";
            helperTitleTextview.setText(helpVal);
        } else if (helpVal.startsWith("Privacy Policy")) {
            helperTitleTextview.setText(helpVal);
            url = judgeMeUrls.HELP + "privacy";
        }

        VolleyCommonClass.getDataFromServer(url, new interfaceSelector() {
            @Override
            public void volleyError(String error) {

            }

            @Override
            public void volleyResponse(JSONObject response) {
                try {
                    JSONObject obj = response.getJSONObject("data");
                    JSONObject Object = obj.getJSONObject("support");
                    String supporttext = Object.optString("supporttext");
                    aboutheplTextView.setText(Html.fromHtml(supporttext));
                    aboutheplTextView.setMovementMethod(LinkMovementMethod.getInstance());
                    aboutheplTextView.setLinksClickable(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });
        abouthelp_backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.animation_enter, R.anim.animation_leave);
    }
}
