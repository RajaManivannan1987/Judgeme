package utility.imaginet.com.judgeme.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONObject;

import utility.imaginet.com.judgeme.R;
import utility.imaginet.com.judgeme.helper.VolleyCommonClass;
import utility.imaginet.com.judgeme.helper.judgeMeUrls;
import utility.imaginet.com.judgeme.interfaceClass.interfaceSelector;

/**
 * Created by IM0033 on 12/10/2015.
 */
public class TermActivity extends AppCompatActivity {
    private TextView tremTextView;
    private String url;
    private Button terms_backButton;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.terms_activity);
        LoadData();
    }

    private void LoadData() {
        tremTextView= (TextView) findViewById(R.id.tremTextView);
        terms_backButton= (Button) findViewById(R.id.terms_backButton);
        url= judgeMeUrls.HELP+"tnc";
        VolleyCommonClass.getDataFromServer(url,  new interfaceSelector() {
            @Override
            public void volleyError(String error) {

            }
            @Override
            public void volleyResponse(JSONObject response) {
                try {
                    JSONObject obj = response.getJSONObject("data");
                    JSONObject Object = obj.getJSONObject("support");
                    String supporttext = Object.optString("supporttext");
                    tremTextView.setText(Html.fromHtml(supporttext));
                    tremTextView.setMovementMethod(LinkMovementMethod.getInstance());
                    tremTextView.setLinksClickable(true);
                } catch (Exception e) {

                }


            }
        });
        terms_backButton.setOnClickListener(new View.OnClickListener() {
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
