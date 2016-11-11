package utility.imaginet.com.judgeme.models;

import org.json.JSONObject;

import utility.imaginet.com.judgeme.ui.LoginActivity;

/**
 * Created by IM0033 on 11/4/2015.
 */
public class UserLoginDetails {
    //PrefManager pref;

    private String resultcode;
    private String resultmessage;
    private String login_token;
    private String uid;
    private String emailorphone;
    private String pass;
    private String hasiOSapp;
    private String firstname;
    private String lastname;
    private String lastlogin;
    private String isfirstlogin;
    private String token;
    private String tokenvaliduntil;
    private String gender;
    private String birthdate;
    private String city;
    private String state;
    private String country;
    private String photoURL;
    private String profileUR;
    private String numberoflikes;
    private String expirydate;
    private String activeYN;
    private String zonesetting;
    private String genderprefsetting;
    private String dateprefsetting;
    private String agesetting;
    private String ratingsetting;
    private String longplaycredits;
    private String currentclipID;

    public String getExpirydate() {
        return expirydate;
    }

    public String getActiveYN() {
        return activeYN;
    }

    public String getZonesetting() {
        return zonesetting;
    }

    public String getGenderprefsetting() {
        return genderprefsetting;
    }

    public String getDateprefsetting() {
        return dateprefsetting;
    }

    public String getAgesetting() {
        return agesetting;
    }

    public String getRatingsetting() {
        return ratingsetting;
    }

    public String getLongplaycredits() {
        return longplaycredits;
    }

    public String getCurrentclipID() {
        return currentclipID;
    }


    public String getResultcode() {
        return resultcode;
    }

    public String getResultmessage() {
        return resultmessage;
    }

    public String getLogin_token() {
        return login_token;
    }

    public String getUid() {
        return uid;
    }

    public String getEmailorphone() {
        return emailorphone;
    }

    public String getPass() {
        return pass;
    }

    public String getHasiOSapp() {
        return hasiOSapp;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getLastlogin() {
        return lastlogin;
    }

    public String getIsfirstlogin() {
        return isfirstlogin;
    }

    public String getToken() {
        return token;
    }

    public String getTokenvaliduntil() {
        return tokenvaliduntil;
    }

    public String getGender() {
        return gender;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getCountry() {
        return country;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public String getProfileUR() {
        return profileUR;
    }

    public String getNumberoflikes() {
        return numberoflikes;
    }


    public void setLoginResponseValue(JSONObject json,  LoginActivity loginActivity) {

        try {
            JSONObject response = json.getJSONObject("data");
            this.resultcode = response.getString("resultcode");
            this.resultmessage = response.getString("resultmessage");
            this.login_token = response.getString("token");
            JSONObject userDetail = response.getJSONObject("user");
            this.uid = userDetail.getString("uid");
            this.emailorphone = userDetail.getString("emailorphone");
            this.pass = userDetail.getString("pass");
            this.hasiOSapp = userDetail.getString("hasiOSapp");
            this.firstname = userDetail.getString("firstname");
            this.lastname = userDetail.getString("lastname");
            this.lastlogin = userDetail.getString("lastlogin");
            this.isfirstlogin = userDetail.getString("isfirstlogin");
            this.tokenvaliduntil = userDetail.getString("tokenvaliduntil");
            this.gender = userDetail.getString("gender");
            this.birthdate = userDetail.getString("birthdate");
            this.city = userDetail.getString("city");
            this.state = userDetail.getString("state");
            this.country = userDetail.getString("country");
            this.photoURL = userDetail.getString("photoURL");
            this.profileUR = userDetail.getString("profileUR");
            this.numberoflikes = userDetail.getString("numberoflikes");
            JSONObject accounttype = userDetail.getJSONObject("accounttype");
            this.expirydate=accounttype.getString("expirydate");
            this.activeYN=accounttype.getString("activeYN");
            this.zonesetting=accounttype.getString("zonesetting");
            this.genderprefsetting=accounttype.getString("genderprefsetting");
            this.dateprefsetting=accounttype.getString("dateprefsetting");
            this.agesetting=accounttype.getString("agesetting");
            this.ratingsetting=accounttype.getString("ratingsetting");
            this.longplaycredits=accounttype.getString("longplaycredits");
            this.currentclipID=accounttype.getString("currentclipID");


           // pref.createLoginSession(emailorphone,login_token,firstname);
            //Log.d("Pref_Value", emailorphone+login_token+firstname);
        } catch (Exception e) {
            e.printStackTrace();

        }
    }
}
