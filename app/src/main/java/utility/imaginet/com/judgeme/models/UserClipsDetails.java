package utility.imaginet.com.judgeme.models;

import org.json.JSONObject;

/**
 * Created by IM0033 on 11/20/2015.
 */
public class UserClipsDetails {


    private String resultcode;
    private String resultmessage;
    private String uid;
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

    private String clipID;
    private String clip_uid;
    private String clip_title;
    private String clip_likescount;
    private String clip_dislikescount;
    private String clip_netlikes;
    private String clip_streamURL;
    private String rank;
    private String zone;


    public String getRank() {
        return rank;
    }

    public String getZone() {
        return zone;
    }
    public String getClip_streamURL() {
        return clip_streamURL;
    }

    public void setClip_streamURL(String clip_streamURL) {
        this.clip_streamURL = clip_streamURL;
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


    public String getWorldiconURLlowres() {
        return worldiconURLlowres;
    }

    public String getCountry() {
        return country;
    }

    public String getState() {
        return state;
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


    public String getCity() {
        return city;
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


    public void setUserClipsDetails(JSONObject json) {
        try {
            JSONObject object = json.getJSONObject("data");
            JSONObject userDetail = object.getJSONObject("user");
            this.rank = object.optString("rank");
            this.zone = object.optString("zone");

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
            //JSONArray clipsDetailsArray = userDetail.getJSONArray("clips");


        } catch (Exception e) {

        }

    }
}
