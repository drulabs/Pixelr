package org.drulabs.pixelr.screens.add;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import org.drulabs.pixelr.dto.PictureDTO;
import org.drulabs.pixelr.screens.BasePresenter;
import org.drulabs.pixelr.screens.BaseView;

/**
 * Created by kaushald on 07/02/17.
 */

public interface AddPicContract {

    public interface View extends BaseView<Presenter> {
        void showLoading();

        void hideLoading();

        void onAlreadyExists();

        void onSaveSuccessful();

        void onColumnEmpty();

        void resetFields();

        void populateFields(long timestamp, String photographer);

        void onImageAvailable(Bitmap image, long timestamp);

        void onError();
    }

    public interface Presenter extends BasePresenter {
        void saveData(@NonNull PictureDTO picture, String comment);

        void launchCameraOrGallery();

        void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull
                int[] grantResults);

        void onActivityResult(int requestCode, int resultCode, Intent data);
    }

}
