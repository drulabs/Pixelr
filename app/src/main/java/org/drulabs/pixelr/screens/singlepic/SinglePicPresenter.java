package org.drulabs.pixelr.screens.singlepic;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;

import org.drulabs.pixelr.R;
import org.drulabs.pixelr.dto.LikeDTO;
import org.drulabs.pixelr.dto.PictureDTO;
import org.drulabs.pixelr.firebase.FirebaseImageHelper;
import org.drulabs.pixelr.firebase.LikesHandler;
import org.drulabs.pixelr.ui.NotificationToast;

/**
 * Created by kaushald on 25/02/17.
 */

public class SinglePicPresenter implements SinglePicContract.Presenter, LikesHandler.Callback {

    private Context mContext;
    private SinglePicContract.View view;
    private String artifactId;

    private LikesHandler likesHandler;

    public SinglePicPresenter(Context context, SinglePicContract.View view, String artifactId) {
        this.mContext = context;
        this.view = view;
        this.artifactId = artifactId;

        this.view.setPresenter(this);
        this.likesHandler = new LikesHandler(mContext, this, this.artifactId);
    }

    @Override
    public void fetchLikes() {
        view.showLoading();
        likesHandler.fetchLikes();
    }

    @Override
    public void loadPic(PictureDTO picture, ImageView imageView) {
        FirebaseImageHelper.loadFBStorageImageIn(mContext, picture.getPicURL(), imageView);
        if (picture.getLikesCount() == 0) {
            view.hideLoading();
            view.onNoMoreLikes();
            FirebaseCrash.report(new Exception("Pixelr test non-fatal error-" + System
                    .currentTimeMillis()));
        }
    }

    @Override
    public void start() {
        fetchLikes();
    }

    @Override
    public void destroy() {
        this.view = null;
        this.likesHandler = null;
    }

    @Override
    public void onLikeUpdated(boolean isLiked, boolean isSuccess) {

    }

    @Override
    public void onNoMoreLikes() {
        if (view != null) {
            view.onNoMoreLikes();
            view.hideLoading();
        }
    }

    @Override
    public void onPicFetched(String key, PictureDTO picture) {
        view.loadDynamicContent(key, picture);
    }

    @Override
    public void onError() {
        NotificationToast.showToast(mContext, mContext.getString(R.string
                .dynamic_link_process_error));
    }

    @Override
    public void onLikeFetched(LikeDTO like) {
        if (view != null) {
            view.hideLoading();
            view.onLikeFetched(like);
        }
    }

    @Override
    public void shareDynamicLink(String artifactId) {
        FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(Uri.parse("https://drulabs.org?artifact=" + artifactId))
                .setDynamicLinkDomain("h3x4k.app.goo.gl")
                .setAndroidParameters(
                        new DynamicLink.AndroidParameters.Builder("org.drulabs.pixelr")
                                .setMinimumVersion(1)
                                .build())
                .setIosParameters(
                        new DynamicLink.IosParameters.Builder("org.drulabs.pixelr.ios")
                                .setAppStoreId("123456789")
                                .setMinimumVersion("1.0.1")
                                .build())
                .buildShortDynamicLink()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String shortLink = task.getResult().getShortLink().toString();
                        Intent sendIntent = new Intent();
                        sendIntent.setAction(Intent.ACTION_SEND);
                        sendIntent.putExtra(Intent.EXTRA_TEXT, "Checkout this pic - " + shortLink);
                        sendIntent.setType("text/plain");
                        mContext.startActivity(sendIntent);
                    } else {
                        NotificationToast.showToast(mContext, "Error creating deep link");
                        Log.w("ShareDynamicLink", "DynamicLink Error");
                    }
                })
                .addOnFailureListener(ex -> {
                    NotificationToast.showToast(mContext, "Error creating deep link");
                    Log.w("ShareDynamicLink", "DynamicLink Error", ex);
                });
    }

    @Override
    public void pushNotifyAll(String artifactId) {

    }

    @Override
    public void loadDynamicLinkContent(Intent intent) {

        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(intent)
                .addOnCompleteListener(task -> {
                    // Get deep link from result (may be null if no link is found)
                    Uri deepLink = null;
                    if (task != null && task.isSuccessful()) {
                        deepLink = task.getResult().getLink();
                        String dynamicContentId = deepLink.getQueryParameter("artifact");
                        likesHandler.fetchPic(dynamicContentId);
                    } else {
                        Log.w("DynamicLinkError", "getDynamicLink:onFailure");
                        NotificationToast.showToast(mContext, mContext.getString(R.string
                                .dynamic_link_process_error));
                    }
                }).addOnFailureListener(ex -> {
            Log.w("DynamicLinkError", "getDynamicLink:onFailure", ex);
            NotificationToast.showToast(mContext, mContext.getString(R.string
                    .dynamic_link_process_error));
        });

    }
}
