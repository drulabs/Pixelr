package org.drulabs.pixelr;

import android.app.Application;

import com.twitter.sdk.android.core.Twitter;

/**
 * Created by kaushald on 28/10/17.
 */

public class AppClass extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Twitter.initialize(this);
    }
}
