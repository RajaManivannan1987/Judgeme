package utility.imaginet.com.judgeme.interfaceClass;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by IM0033 on 11/5/2015.
 */
public interface interfaceSelector {
    void volleyError(String error);
    void volleyResponse(JSONObject response) throws JSONException;
}
