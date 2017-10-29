package org.drulabs.pixelr.screens.landing;

import android.widget.ImageView;

import org.drulabs.pixelr.dto.PictureDTO;
import org.drulabs.pixelr.screens.BasePresenter;
import org.drulabs.pixelr.screens.BaseView;

import java.util.HashMap;

/**
 * Created by kaushald on 05/02/17.
 */

public interface PicsContract {

    interface View extends BaseView<Presenter> {
        void showLoading();

        void hideLoading();

        void reset();

        void loadPics(HashMap<String, PictureDTO> photos);

        void onLoadError(String msg);
    }

    interface Presenter extends BasePresenter {
        void loadImageIn(ImageView img);

        void reset();

        void loadNextBatch();

        void onPicClicked(String key, PictureDTO pic);

        void onLikeClicked(String key, PictureDTO pic, boolean liked);

        void onCommentsClicked(String key, PictureDTO pic);

        void onShareClicked(String key, PictureDTO pic);

        void onDownloadClicked(String key, PictureDTO pic);
    }

}
