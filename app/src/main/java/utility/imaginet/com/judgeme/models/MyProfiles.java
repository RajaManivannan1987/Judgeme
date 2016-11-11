package utility.imaginet.com.judgeme.models;

import org.json.JSONObject;

/**
 * Created by IM0033 on 11/27/2015.
 */
public class MyProfiles {
    private String zonesetting;
    private String firstname;
    private String lastname;
    private String photoURL;
    private String numberoflikes;
    private String country;
    private String state;
    private String city;
    private String worldiconURLlowres;
    private String countryiconURLlowres;
    private String stateiconURLlowres;
    private String cityiconURLlowres;
    private String accountType;
    private String clipID;
    private String clip_uid;
    private String clip_title;
    private String clip_likescount;
    private String clip_dislikescount;
    private String clip_netlikes;
    private String setClip_streamURL;
    private String activeClip;
    private String rank;
    private String zone;
    private String youtubeOk;
    private static String count;
    int array;

    public String getYoutubeOk() {
        return youtubeOk;
    }


    public String getRank() {
        return rank;
    }

    public String getZone() {
        return zone;
    }

    public String getActiveClip() {
        return activeClip;
    }

    public static String getCount() {
        return count;
    }

    public static void setCount(String count) {
        MyProfiles.count = count;
    }

    public int getArray() {
        return array;
    }

    public void setArray(int array) {
        this.array = array;
    }

    public void setClipID(String clipID) {
        this.clipID = clipID;
    }

    public void setClip_uid(String clip_uid) {
        this.clip_uid = clip_uid;
    }

    public void setClip_title(String clip_title) {
        this.clip_title = clip_title;
    }

    public void setClip_likescount(String clip_likescount) {
        this.clip_likescount = clip_likescount;
    }

    public void setClip_dislikescount(String clip_dislikescount) {
        this.clip_dislikescount = clip_dislikescount;
    }

    public void setClip_netlikes(String clip_netlikes) {
        this.clip_netlikes = clip_netlikes;
    }


    public String getSetClip_streamURL() {
        return setClip_streamURL;
    }

    public void setSetClip_streamURL(String setClip_streamURL) {
        this.setClip_streamURL = setClip_streamURL;
    }


    public String getZonesetting() {
        return zonesetting;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public String getNumberoflikes() {
        return numberoflikes;
    }

    public String getCountry() {
        return country;
    }

    public String getState() {
        return state;
    }

    public String getCity() {
        return city;
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

    public String getAccountType() {
        return accountType;
    }

    public String getClipID() {
        return clipID;
    }

    public String getClip_uid() {
        return clip_uid;
    }

    public String getClip_title() {
        return clip_title;
    }

    public String getClip_likescount() {
        return clip_likescount;
    }

    public String getClip_dislikescount() {
        return clip_dislikescount;
    }

    public String getClip_netlikes() {
        return clip_netlikes;
    }


    public void setMyProfileDetails(JSONObject json) {
        try {
            JSONObject object = json.getJSONObject("data");
            this.rank = object.optString("rank");
            this.zone = object.optString("zone");
            JSONObject userDetail = object.getJSONObject("user");

            this.firstname = userDetail.optString("firstname");
            this.lastname = userDetail.optString("lastname");
            this.photoURL = userDetail.optString("photoURL");
            this.zonesetting = userDetail.optString("zonesetting");
            this.city = userDetail.optString("city");
            this.numberoflikes = userDetail.optString("numberoflikes");
            this.worldiconURLlowres = userDetail.optString("worldiconURLlowres");
            this.countryiconURLlowres = userDetail.optString("countryiconURLlowres");
            this.stateiconURLlowres = userDetail.optString("stateiconURLlowres");
            this.cityiconURLlowres = userDetail.optString("cityiconURLlowres");
            this.country = userDetail.optString("country");
            this.state = userDetail.optString("state");
            this.youtubeOk=userDetail.optString("youtubeOK");
            JSONObject account = userDetail.getJSONObject("accounttype");
            this.accountType = account.optString("accounttype");
            this.activeClip = account.optString("activeclips");
            //this.setCount(account.optString("activeclips"));
        } catch (Exception e) {

        }

    }


}
