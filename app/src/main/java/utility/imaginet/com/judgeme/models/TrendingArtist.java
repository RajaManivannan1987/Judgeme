package utility.imaginet.com.judgeme.models;

/**
 * Created by IM0033 on 12/1/2015.
 */
public class TrendingArtist {

    private String firstname;
    private String lastname;
    private String city;
    private String state;
    private String country;
    private String photoURL;
    private String netlikes;
    private String numberoflikes;
    private String uid;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }



    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    private String index;

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    public String getNetlikes() {
        return netlikes;
    }

    public void setNetlikes(String netlikes) {
        this.netlikes = netlikes;
    }

    public String getNumberoflikes() {
        return numberoflikes;
    }

    public void setNumberoflikes(String numberoflikes) {
        this.numberoflikes = numberoflikes;
    }



}
