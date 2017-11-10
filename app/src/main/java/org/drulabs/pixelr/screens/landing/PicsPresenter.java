package org.drulabs.pixelr.screens.landing;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.widget.ImageView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.perf.FirebasePerformance;
import com.google.firebase.perf.metrics.Trace;

import org.drulabs.pixelr.R;
import org.drulabs.pixelr.config.Constants;
import org.drulabs.pixelr.dto.PictureDTO;
import org.drulabs.pixelr.firebase.FirebaseImageHelper;
import org.drulabs.pixelr.firebase.PicsHandler;
import org.drulabs.pixelr.screens.comment.CommentsActivity;
import org.drulabs.pixelr.screens.singlepic.SinglePicPage;
import org.drulabs.pixelr.service.Downloader;
import org.drulabs.pixelr.ui.NotificationToast;

import java.util.HashMap;

/**
 * Created by kaushald on 05/02/17.
 */

public class PicsPresenter implements PicsContract.Presenter, PicsHandler.Callback {

    // Tracer
    Trace landingTrace = FirebasePerformance.getInstance().newTrace("LandingPage");
    // Analytics
    FirebaseAnalytics mFirebaseAnalytics;
    private PicsContract.View view;
    private Activity activity;
    private PicsHandler picsHandler;

    public PicsPresenter(Activity activity, PicsContract.View view) {
        this.view = view;
        this.activity = activity;
        picsHandler = new PicsHandler(this.activity, this);
        view.setPresenter(this);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(activity);
    }

    @Override
    public void loadImageIn(ImageView img) {
        // bloody useless method
    }

    @Override
    public void loadNextBatch() {
        if (picsHandler.hasMorePics()) {
            picsHandler.fetchPics();
            if (view != null) {
                view.showLoading();
            }
        }
    }

    @Override
    public void start() {
        loadNextBatch();
        landingTrace.start();
    }

    @Override
    public void destroy() {
        this.view = null;
        this.picsHandler = null;
        landingTrace.stop();
    }

    @Override
    public void onPhotosFetched(HashMap<String, PictureDTO> photos) {
        if (view != null) {
            view.loadPics(photos);
            view.hideLoading();
        }
    }

    @Override
    public void onAllPicsFetched() {
        view.hideLoading();
    }

    @Override
    public void onError(String message) {
        if (view != null) {
            view.hideLoading();
            view.onLoadError(message);
        }
    }

    @Override
    public void reset() {
        picsHandler.resetLastTimestamp();
        loadNextBatch();
    }

    @Override
    public void onPicClicked(String key, PictureDTO pic) {
        Intent likesIntent = new Intent(activity, SinglePicPage.class);
        likesIntent.putExtra(SinglePicPage.KEY_PIC_DTO, pic);
        likesIntent.putExtra(SinglePicPage.KEY_PIC_KEY, key);
        activity.startActivity(likesIntent);
        // Count number of picture clicks
        landingTrace.incrementCounter("pictureClicked");
    }

    @Override
    public void onLikeClicked(String key, PictureDTO pic, boolean liked) {
        picsHandler.updateLikesCount(key, liked);
        // Count number of picture like clicks
        if (liked) {
            landingTrace.incrementCounter("likeClicked");
        }
    }

    @Override
    public void onCommentsClicked(String key, PictureDTO pic) {
        Intent commentIntent = new Intent(activity, CommentsActivity.class);
        commentIntent.putExtra(CommentsActivity.KEY_TITLE, pic.getPicName());
        commentIntent.putExtra(CommentsActivity.KEY_ARTIFACT_ID, key);
        commentIntent.putExtra(CommentsActivity.KEY_ARTIFACT_TYPE, Constants.PICS_DB);
        activity.startActivity(commentIntent);
        // Count number of picture comment clicks
        landingTrace.incrementCounter("pictureClicked");
    }

    @Override
    public void onShareClicked(String key, final PictureDTO pic) {
//        NotificationToast.showToast(activity, activity.getString(R.string.feature_coming_soon));

        FirebaseImageHelper.getDownloadURI(pic, new FirebaseImageHelper.DownloadURIListener() {
            @Override
            public void onDownloadURIFetched(Uri downloadUri) {
                NotificationToast.showToast(activity, activity.getString(R.string
                        .text_downloading));

                String outputFilePath = Environment.getExternalStorageDirectory() + "/vivi/" +
                        pic.getPicName() + ".jpg";

                Downloader.downloadAndShare(activity, downloadUri, outputFilePath, false);
            }

            @Override
            public void onError(Exception e) {
                NotificationToast.showToast(activity, activity.getString(R.string
                        .something_went_wrong));
            }
        });

        // Analytics share clicked
        Bundle params = new Bundle();
        params.putString("image_name", pic.getPicName());
        params.putString("pic_url", pic.getPicURL());
        params.putString("pic_credit", pic.getPhotoCredit());
        mFirebaseAnalytics.logEvent("share_image", params);
    }

    @Override
    public void onDownloadClicked(String key, final PictureDTO pic) {

        FirebaseImageHelper.getDownloadURI(pic, new FirebaseImageHelper.DownloadURIListener() {
            @Override
            public void onDownloadURIFetched(Uri downloadUri) {
                NotificationToast.showToast(activity, activity.getString(R.string
                        .text_downloading));

                String outputFilePath = Environment.getExternalStorageDirectory() + "/vivi/" +
                        pic.getPicName() + ".jpg";

                Downloader.startDownload(activity, downloadUri, outputFilePath, true);
            }

            @Override
            public void onError(Exception e) {
                NotificationToast.showToast(activity, activity.getString(R.string
                        .something_went_wrong));
            }
        });
    }
}
