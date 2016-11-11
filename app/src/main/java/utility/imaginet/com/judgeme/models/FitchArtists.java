package utility.imaginet.com.judgeme.models;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by IM0033 on 11/7/2015.
 */
public class FitchArtists {

    private String resultcode;
    private String resultmessage;
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
    private String profileURL;
    private String numberoflikes;
    private String zonesetting;
    private String genderprefsetting;
    private String dateprefsetting;
    private String agesetting;
    private String ratingsetting;
    private String currentclipID;
    private String worldiconURLlowres;
    private String countryiconURLlowres;
    private String stateiconURLlowres;
    private String cityiconURLlowres;
    private String rank;
    private String zone;
    private String longplaycredits;

    private String accounttypeID;
    private String accounttype;
    private String starlevel;
    private String minimumfollowers;
    private String activeclips;
    private String advertisingfrequency;
    private String allowedcliplength;
    private String upgradecost;
    private String upgradeto;

    public String getUpgradeaccountactiveclips() {
        return upgradeaccountactiveclips;
    }

    private String upgradeaccountactiveclips;

    public String getAccounttypeID() {
        return accounttypeID;
    }

    public String getAccounttype() {
        return accounttype;
    }

    public String getStarlevel() {
        return starlevel;
    }

    public String getMinimumfollowers() {
        return minimumfollowers;
    }

    public String getActiveclips() {
        return activeclips;
    }

    public String getAdvertisingfrequency() {
        return advertisingfrequency;
    }

    public String getAllowedcliplength() {
        return allowedcliplength;
    }

    public String getUpgradecost() {
        return upgradecost;
    }

    public String getUpgradeto() {
        return upgradeto;
    }

    public String getResultcode() {
        return resultcode;
    }

    public String getResultmessage() {
        return resultmessage;
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

    public String getProfileURL() {
        return profileURL;
    }

    public String getNumberoflikes() {
        return numberoflikes;
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

    public String getWorldiconURLlowres() {
        return worldiconURLlowres;
    }

    public String getCountryiconURLlowres() {
        return countryiconURLlowres;
    }

    public String getStateiconURLlowres() {
        return stateiconURLlowres;
    }

    public String getCityiconURLlowres() {
        return cityiconURLlowres;
    }

    public String getRank() {
        return rank;
    }

    public String getZone() {
        return zone;
    }




    public void setArtistsResponseValue(JSONObject response) {
        try {
            JSONObject object=response.getJSONObject("data");
            this.resultcode=object.optString("resultcode");
            this.resultmessage=object.optString("resultmessage");
            //this.zone=object.getString("zone");
            //this.rank=object.getString("rank");
            JSONObject userObject=object.getJSONObject("user");
            this.uid=userObject.optString("uid");
            this.emailorphone=userObject.optString("emailorphone");
            this.pass=userObject.optString("pass");
            this.hasiOSapp=userObject.optString("hasiOSapp");
            this.firstname=userObject.optString("firstname");
            this.lastname=userObject.optString("lastname");
            this.lastlogin=userObject.optString("lastlogin");
            this.isfirstlogin=userObject.optString("isfirstlogin");
            this.token=userObject.optString("token");
            this.tokenvaliduntil=userObject.optString("tokenvaliduntil");
            this.gender=userObject.optString("gender");
            this.birthdate=userObject.optString("birthdate");
            this.city=userObject.optString("city");
            this.state=userObject.optString("state");
            this.country=userObject.optString("country");
            this.photoURL=userObject.optString("photoURL");
            this.profileURL=userObject.optString("profileURL");
            this.numberoflikes=userObject.optString("numberoflikes");
            this.zonesetting=userObject.optString("zonesetting");

            this.worldiconURLlowres=userObject.optString("worldiconURLlowres");
            this.countryiconURLlowres=userObject.optString("countryiconURLlowres");
            this.stateiconURLlowres=userObject.optString("stateiconURLlowres");
            this.cityiconURLlowres=userObject.optString("cityiconURLlowres");
            this.genderprefsetting=userObject.optString("genderprefsetting");
            this.dateprefsetting=userObject.optString("dateprefsetting");
            this.agesetting=userObject.optString("agesetting");
            this.ratingsetting=userObject.optString("ratingsetting");
            this.longplaycredits=userObject.optString("longplaycredits");
            this.currentclipID=userObject.optString("currentclipID");

            JSONObject accounttypeObject=userObject.getJSONObject("accounttype");

            this.accounttypeID=accounttypeObject.optString("accounttypeID");
            this.accounttype=accounttypeObject.optString("accounttype");
            this.starlevel=accounttypeObject.optString("starlevel");
            this.minimumfollowers=accounttypeObject.optString("minimumfollowers");
            this.activeclips=accounttypeObject.optString("activeclips");
            this.advertisingfrequency=accounttypeObject.optString("advertisingfrequency");
            this.allowedcliplength=accounttypeObject.optString("allowedcliplength");
            this.longplaycredits=accounttypeObject.optString("longplaycredits");
            this.upgradecost=accounttypeObject.optString("upgradecost");
            this.upgradeto=accounttypeObject.optString("upgradeto");

            JSONObject upgradeaccounttypeObject=accounttypeObject.getJSONObject("upgradeaccounttype");
            this.upgradeaccountactiveclips=upgradeaccounttypeObject.optString("activeclips");



            JSONArray genresettingsArray=userObject.getJSONArray("genresettings");

            this.rank=object.optString("rank");
            this.zone=object.optString("zone");
        }catch (Exception e){

        }
    }
}
