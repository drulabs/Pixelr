package org.drulabs.pixelr.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.firebase.iid.FirebaseInstanceId;

import org.drulabs.pixelr.dto.UserDTO;

/**
 * Created by kaushald on 25/01/17.
 */

public class Store {

    private static final String APP_STORE_LOCAL = "app_prefs";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_NAME = "name";
    private static final String KEY_USERKEY = "mykey";
    private static final String KEY_PIC_URL = "pic_url";
    private static final String KEY_ADMIN = "isadmin";
    private static final String KEY_PIC_NAME = "pic_name";
    private static final String KEY_FCM_TOKEN = "fcm_token";
    private static final String KEY_FIREBASE_UID = "firebase_uid";
    private static final String KEY_FIREBASE_PIC = "firebase_pic_url";
    private static final String KEY_USER_EMAIL = "user_email";
    private static final String KEY_USERINFO_SYNCED = "is_user_info_synced";
    private static final String KEY_IS_ACCOUNT_SUSPENDED = "is_user_account_suspended";
    private static Store instance = null;
    private SharedPreferences prefs;
    private SharedPreferences.Editor prefsEditor;
    private String password;
    private String myName;
    private String myKey;
    private String picUrl;
    private boolean isAdmin;
    private String userPicUrl;
    private String fcmToken;
    private String firebaseUID;
    private String firebasePicUrl;
    private String userEmail;
    private boolean isUserInfoSynced;
    private boolean isUserAccountSuspended;

    private Store(Context context) {
        prefs = context.getSharedPreferences(APP_STORE_LOCAL, Context.MODE_PRIVATE);
        prefsEditor = prefs.edit();
    }

    public static Store getInstance(Context context) {

        if (instance == null) {
            synchronized (Store.class) {
                if (instance == null) {
                    instance = new Store(context);
                }
            }
        }
        return instance;
    }

    public void clearData() {
        prefsEditor.clear().apply();
        password = null;
        myName = null;
        myKey = null;
        picUrl = null;
        isAdmin = false;
        userPicUrl = null;
        fcmToken = null;
        firebaseUID = null;
        firebasePicUrl = null;
        userEmail = null;
        isUserInfoSynced = false;
        isUserAccountSuspended = false;
    }

    public void setUser(UserDTO currentUser) {
        if (currentUser == null) {
            return;
        }
        setMyKey(currentUser.getUid());
        setMyName(currentUser.getDisplayName());
        setUserPicUrl(currentUser.getPicUrl());
        setUserEmail(currentUser.getEmail());
        setAdmin(true);
        setFcmToken(FirebaseInstanceId.getInstance().getToken());
        setFirebasePicUrl(currentUser.getPicUrl());
        setFirebaseUID(currentUser.getUid());
    }

    public void setIsUserInfoSynced(boolean isSynced) {
        this.isUserInfoSynced = isSynced;
        prefsEditor.putBoolean(KEY_USERINFO_SYNCED, isSynced);
        prefsEditor.apply();
    }

    public boolean isUserAccountSuspended() {
        if (isUserAccountSuspended == false) {
            isUserAccountSuspended = prefs.getBoolean(KEY_IS_ACCOUNT_SUSPENDED, false);
        }
        return isUserAccountSuspended;
    }

    public void setUserAccountSuspended(boolean userAccountSuspended) {
        this.isUserAccountSuspended = userAccountSuspended;
        prefsEditor.putBoolean(KEY_IS_ACCOUNT_SUSPENDED, isUserAccountSuspended);
        prefsEditor.apply();
    }

    public boolean isUserInfoSynced() {
        if (isUserInfoSynced == false) {
            isUserInfoSynced = prefs.getBoolean(KEY_USERINFO_SYNCED, false);
        }
        return isUserInfoSynced;
    }

    public String getUserEmail() {
        if (userEmail == null) {
            userEmail = prefs.getString(KEY_USER_EMAIL, null);
        }
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
        prefsEditor.putString(KEY_USER_EMAIL, userEmail);
        prefsEditor.apply();
    }

    public String getFirebasePicUrl() {
        if (firebasePicUrl == null) {
            firebasePicUrl = prefs.getString(KEY_FIREBASE_PIC, null);
        }
        return firebasePicUrl;
    }

    public void setFirebasePicUrl(String firebasePicUrl) {
        this.firebasePicUrl = firebasePicUrl;
        prefsEditor.putString(KEY_FIREBASE_PIC, firebasePicUrl);
        prefsEditor.apply();
    }

    public String getFirebaseUID() {
        if (firebaseUID == null) {
            firebaseUID = prefs.getString(KEY_FIREBASE_UID, null);
        }
        return firebaseUID;
    }

    public void setFirebaseUID(String firebaseUID) {
        this.firebaseUID = firebaseUID;
        prefsEditor.putString(KEY_FIREBASE_UID, firebaseUID);
        prefsEditor.apply();
    }

    public String getFcmToken() {
        if (fcmToken == null) {
            fcmToken = prefs.getString(KEY_FCM_TOKEN, null);
        }
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
        prefsEditor.putString(KEY_FCM_TOKEN, fcmToken);
        prefsEditor.apply();
    }

    public String getMyKey() {
        if (myKey == null) {
            myKey = prefs.getString(KEY_USERKEY, null);
        }
        return myKey;
    }

    public void setMyKey(String key) {
        this.myKey = key;
        prefsEditor.putString(KEY_USERKEY, key);
        prefsEditor.apply();
    }

    public String getPassword() {
        if (password == null) {
            password = prefs.getString(KEY_PASSWORD, null);
        }
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        prefsEditor.putString(KEY_PASSWORD, password);
        prefsEditor.apply();
    }

    public String getMyName() {
        if (myName == null) {
            myName = prefs.getString(KEY_NAME, null);
        }
        return myName;
    }

    public void setMyName(String myName) {
        this.myName = myName;
        prefsEditor.putString(KEY_NAME, myName);
        prefsEditor.apply();
    }

    public String getPicUrl() {
        if (picUrl == null) {
            picUrl = prefs.getString(KEY_PIC_URL, null);
        }
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
        prefsEditor.putString(KEY_PIC_URL, picUrl);
        prefsEditor.apply();
    }

    public String getUserPicUrl() {
        if (userPicUrl == null) {
            userPicUrl = prefs.getString(KEY_PIC_NAME, null);
        }
        return userPicUrl;
    }

    public void setUserPicUrl(String picUrl) {
        this.userPicUrl = picUrl;
        prefsEditor.putString(KEY_PIC_NAME, picUrl);
        prefsEditor.apply();
    }

    public boolean isAdmin() {
        isAdmin = prefs.getBoolean(KEY_ADMIN, false);
        return isAdmin;
    }

    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
        prefsEditor.putBoolean(KEY_ADMIN, isAdmin);
        prefsEditor.apply();
    }


}
