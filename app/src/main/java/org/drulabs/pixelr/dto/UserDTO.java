package org.drulabs.pixelr.dto;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;

/**
 * Created by kaushald on 28/10/17.
 */

public class UserDTO {

    private String uid;
    private String provider;
    private String displayName;
    private String picUrl;
    private String fcmToken = FirebaseInstanceId.getInstance().getToken();
    private String email;

    public static UserDTO from(FirebaseUser user) {
        if (user == null) {
            return null;
        }

        UserDTO currentUser = new UserDTO();
        currentUser.setDisplayName(user.getDisplayName());
        currentUser.setProvider(user.getProviders().get(0));
        currentUser.setUid(user.getUid());

        String photoURL = user.getPhotoUrl() == null ? "https://learningdru" +
                ".com/images/portfolio/img8.jpg" : user.getPhotoUrl().toString();

        currentUser.setPicUrl(photoURL);
        currentUser.setEmail(user.getEmail());

        return currentUser;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
