package utility.imaginet.com.judgeme.models;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by IM0033 on 11/17/2015.
 */
public class NextUpClipDetails {
    JSONArray likesanddislikes;
    private String resultcode;
    private String resultmessage;
    private String clipID;
    private String uid;
    private String artistname;
    private String artistcity;
    private String artiststate;
    private String artistcountry;
    private String artistphotourl;
    private String cliptype;
    private String fileURL;
    private String streamURL;
    private String youtubeID;
    private String datecreated;
    private String datemodified;
    private String title;
    private String tagline;
    private String activeYN;
    private String netlikes;
    private String likescount;
    private String dislikescount;
    private String usersortorder;
    private String islongplayerYN;
    private String goldstars;
    private String unreadcomments;
    private String genres;

    private String comments;

    public String getResultcode() {
        return resultcode;
    }

    public String getResultmessage() {
        return resultmessage;
    }

    public String getClipID() {
        return clipID;
    }

    public String getUid() {
        return uid;
    }

    public String getArtistname() {
        return artistname;
    }

    public String getArtistcity() {
        return artistcity;
    }

    public String getArtiststate() {
        return artiststate;
    }

    public String getArtistcountry() {
        return artistcountry;
    }

    public String getArtistphotourl() {
        return artistphotourl;
    }

    public String getCliptype() {
        return cliptype;
    }

    public String getFileURL() {
        return fileURL;
    }

    public String getStreamURL() {
        return streamURL;
    }

    public String getYoutubeID() {
        return youtubeID;
    }

    public String getDatecreated() {
        return datecreated;
    }

    public String getDatemodified() {
        return datemodified;
    }

    public String getTitle() {
        return title;
    }

    public String getTagline() {
        return tagline;
    }

    public String getActiveYN() {
        return activeYN;
    }

    public String getNetlikes() {
        return netlikes;
    }

    public String getLikescount() {
        return likescount;
    }

    public String getDislikescount() {
        return dislikescount;
    }

    public String getUsersortorder() {
        return usersortorder;
    }

    public String getIslongplayerYN() {
        return islongplayerYN;
    }

    public String getGoldstars() {
        return goldstars;
    }

    public String getUnreadcomments() {
        return unreadcomments;
    }

    public String getGenres() {
        return genres;
    }

    public JSONArray getLikesanddislikes() {
        return likesanddislikes;
    }

    public String getComments() {
        return comments;
    }


    public void getClipDetails(JSONObject json) {
        try {
            JSONObject response = json.getJSONObject("data");
            this.resultcode = response.optString("resultcode");
            this.resultmessage = response.optString("resultmessage");
            JSONObject ClipDetail = response.getJSONObject("clip");
            this.clipID = ClipDetail.optString("clipID");
            this.uid = ClipDetail.optString("uid");
            this.artistname = ClipDetail.optString("artistname");
            this.artistcity = ClipDetail.optString("artistcity");
            this.artiststate = ClipDetail.optString("artiststate");
            this.artistcountry = ClipDetail.optString("artistcountry");
            this.artistphotourl = ClipDetail.optString("artistphotourl");
            this.cliptype = ClipDetail.optString("cliptype");
            this.fileURL = ClipDetail.optString("fileURL");
            this.streamURL = ClipDetail.optString("streamURL");
            this.youtubeID = ClipDetail.optString("youtubeID");
            this.datecreated = ClipDetail.optString("datecreated");
            this.datemodified = ClipDetail.optString("datemodified");
            this.title = ClipDetail.optString("title");
            this.tagline = ClipDetail.optString("tagline");
            this.activeYN = ClipDetail.optString("activeYN");
            this.netlikes = ClipDetail.optString("netlikes");
            this.likescount = ClipDetail.optString("likescount");
            this.dislikescount = ClipDetail.optString("dislikescount");
            this.usersortorder = ClipDetail.optString("usersortorder");
            this.islongplayerYN = ClipDetail.optString("islongplayerYN");
            this.goldstars = ClipDetail.optString("goldstars");
            this.unreadcomments = ClipDetail.optString("unreadcomments");
            this.likesanddislikes = ClipDetail.getJSONArray("likesanddislikes");


        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

