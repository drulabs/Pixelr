package org.drulabs.pixelr.screens.singlepic;

import android.content.Intent;
import android.widget.ImageView;

import org.drulabs.pixelr.dto.LikeDTO;
import org.drulabs.pixelr.dto.PictureDTO;
import org.drulabs.pixelr.screens.BasePresenter;
import org.drulabs.pixelr.screens.BaseView;

/**
 * Created by kaushald on 25/02/17.
 */

public interface SinglePicContract {

    interface View extends BaseView<Presenter> {
        void showLoading();

        void hideLoading();

        void onLikeFetched(LikeDTO like);

        void onNoMoreLikes();

        void loadDynamicContent(String picKey, PictureDTO picture);
    }

    interface Presenter extends BasePresenter {
        void fetchLikes();

        void loadPic(PictureDTO picture, ImageView imageView);

        void shareDynamicLink(String artifactId);

        void pushNotifyAll(String artifactId);

        void loadDynamicLinkContent(Intent intent);
    }

}
