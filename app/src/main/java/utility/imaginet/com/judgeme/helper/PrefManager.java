package utility.imaginet.com.judgeme.helper;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by IM028 on 6/25/15.
 */
public class PrefManager {
    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Shared pref file name
    private static final String PREF_NAME = "SanjayM";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";

    // Email address
    private static final String KEY_USER_NAME = "user";

    // Password
    private static final String KEY_PASSWORD = "password";

    // Object Id
    private static final String KEY_TOKEN = "objectId";

    // Full Name
    public static final String KEY_FULL_NAME = "fullName";

    public static final String FBTOKEN = "facebookToken";

    public static final String UID = "uid";

    // Profile Pic
    // private static final String KEY_PROFILR_PIC = "profilePic";

    // Constructor
    public PrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * Create login session
     */
    public void createLoginSession(String user, String objectId,
                                   String fullName, String fbToken, String uid) {
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);

        // Storing email in pref
        editor.putString(KEY_USER_NAME, user);

        editor.putString(KEY_TOKEN, objectId);

        editor.putString(KEY_FULL_NAME, fullName);

        editor.putString(FBTOKEN, fbToken);

        editor.putString(UID, uid);

        // commit changes
        editor.commit();
    }

	/*
     * public void createProfilePic(String profile_pic) {
	 * 
	 * editor.putString(KEY_PROFILR_PIC, profile_pic); editor.commit(); }
	 */

    public String getUserName() {
        return pref.getString(KEY_USER_NAME, null);
    }

    public String getObjectId() {
        return pref.getString(KEY_TOKEN, null);
    }

    public String getUserFullName() {
        return pref.getString(KEY_FULL_NAME, null);
    }

    public String getfbToken() {
        return pref.getString(FBTOKEN, null);
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }

    public  String getUID() {
        return pref.getString(UID, null);
    }

    public void logout() {
        editor.clear();
        editor.commit();
    }
}