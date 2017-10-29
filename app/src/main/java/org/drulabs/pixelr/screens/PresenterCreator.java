package org.drulabs.pixelr.screens;

import android.app.Activity;
import android.support.annotation.NonNull;

import org.drulabs.pixelr.screens.add.AddPicContract;
import org.drulabs.pixelr.screens.add.AddPicPresenter;
import org.drulabs.pixelr.screens.comment.CommentContract;
import org.drulabs.pixelr.screens.comment.CommentsPresenter;
import org.drulabs.pixelr.screens.landing.PicsContract;
import org.drulabs.pixelr.screens.landing.PicsPresenter;
import org.drulabs.pixelr.screens.like.LikesContract;
import org.drulabs.pixelr.screens.like.LikesPresenter;

/**
 * Created by kaushald on 25/01/17.
 */

public class PresenterCreator {

    @NonNull
    public static PicsContract.Presenter createPicsPresenter(Activity activity, PicsContract.View
            view) {
        return new PicsPresenter(activity, view);
    }

    @NonNull
    public static AddPicContract.Presenter createAddPicPresenter(Activity activity,
                                                                 AddPicContract.View view) {
        return new AddPicPresenter(activity, view);
    }

    @NonNull
    public static CommentContract.Presenter createCommentsPresenter(Activity activity, CommentContract
            .View view, String artifactId, String artifactType) {
        return new CommentsPresenter(activity, view, artifactId, artifactType);
    }

    @NonNull
    public static LikesContract.Presenter createLikesPresenter(Activity activity, LikesContract
            .View view, String artifactId, String artifactType) {
        return new LikesPresenter(activity, view, artifactId, artifactType);
    }
}
