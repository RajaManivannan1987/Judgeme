package utility.imaginet.com.judgeme.models;

import org.json.JSONObject;

/**
 * Created by IM0033 on 12/10/2015.
 */
public class MyComments {
    private String clipID;
    private String uid;
    private String artistname;
    private String title;
    private String unreadcomments;


    public void setClipID(String clipID) {
        this.clipID = clipID;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setArtistname(String artistname) {
        this.artistname = artistname;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUnreadcomments(String unreadcomments) {
        this.unreadcomments = unreadcomments;
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

    public String getTitle() {
        return title;
    }

    public String getUnreadcomments() {
        return unreadcomments;
    }


}
