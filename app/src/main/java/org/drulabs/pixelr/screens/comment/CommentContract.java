package org.drulabs.pixelr.screens.comment;

import org.drulabs.pixelr.dto.CommentDTO;
import org.drulabs.pixelr.screens.BasePresenter;
import org.drulabs.pixelr.screens.BaseView;

import java.util.List;

/**
 * Created by kaushald on 10/02/17.
 */

public interface CommentContract {

    public interface View extends BaseView<Presenter> {
        void showLoading();

        void hideLoading();

        void loadComments(List<CommentDTO> comments);

        void loadComment(CommentDTO comment);

        void onLoadError(String message);

        void onCommentSaved();
    }

    public interface Presenter extends BasePresenter {
        void loadNextBatch();

        void addComment(CommentDTO comment);
    }

}
