package model;

import android.graphics.Bitmap;
import android.net.Uri;

import java.util.Date;
import java.util.HashMap;

/**
 * Friend class storing all its variables and a HashMap containing Friend objects with their associated id
 * <p>
 * Created by Ka Kyen Lam on 3/08/2017.
 */

public class Friend {

//    private static HashMap<String, Friend> friendMap = new HashMap<>();
    private String id;
    private String name;
    private String email;
    private Date birthday;
    private Uri photoUri;


    public Friend(String id, String name, String email, Date birthday, Uri photoUri) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.birthday = birthday;
        this.photoUri = photoUri;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Uri getPhotoUri() {
        return photoUri;
    }

    public void setPhotoUri(Uri photoUri) {
        this.photoUri = photoUri;
    }

//    public static HashMap<String, Friend> getFriendMap() {
//        return friendMap;
//    }

}
