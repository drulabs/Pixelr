package org.drulabs.pixelr.screens.like;

import org.drulabs.pixelr.dto.LikeDTO;
import org.drulabs.pixelr.screens.BasePresenter;
import org.drulabs.pixelr.screens.BaseView;

import java.util.List;

/**
 * Created by kaushald on 25/02/17.
 */

public interface LikesContract {

    interface View extends BaseView<Presenter> {
        void showLoading();
        void hideLoading();
        void onLikeFetched(LikeDTO like);
        void onNoMoreLikes();
    }

    interface Presenter extends BasePresenter {
        void fetchLikes();
    }

}
