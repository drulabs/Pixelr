package org.drulabs.pixelr.screens.add;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import org.drulabs.pixelr.R;
import org.drulabs.pixelr.dto.PictureDTO;
import org.drulabs.pixelr.screens.PresenterCreator;
import org.drulabs.pixelr.ui.NotificationToast;
import org.drulabs.pixelr.utils.Store;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AddPicture extends AppCompatActivity implements AddPicContract.View {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("MMM-dd, yyyy " +
            "HH:mm");

    ProgressDialog progressDialog;

    // UI references
    private ImageView imgAddPic;
    private EditText timestamp, photographer, comment;
    long epochTimestamp = System.currentTimeMillis();

    private AddPicContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_picture);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.loading_please_wait));
        progressDialog.setCanceledOnTouchOutside(false);

        initializeUI();

        PresenterCreator.createAddPicPresenter(this, this);
        mPresenter.start();
    }

    private void initializeUI() {
        imgAddPic = findViewById(R.id.img_add_pic);
        timestamp = findViewById(R.id.add_pic_timestamp);
        photographer = findViewById(R.id.add_pic_photographer);
        comment = findViewById(R.id.add_pic_comment);

        timestamp.setEnabled(false);
        photographer.setEnabled(false);

        imgAddPic.setOnClickListener(view -> mPresenter.launchCameraOrGallery());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_add_pic, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        //timestamp.setText(dateFormat.format(new Date(System.currentTimeMillis())));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_picture:

                String myName = Store.getInstance(this).getMyName();

                PictureDTO newPic = new PictureDTO();
                newPic.setDateTaken(epochTimestamp == 0 ? System.currentTimeMillis() :
                        epochTimestamp);
                newPic.setCommentsCount(1);
                newPic.setLikesCount(0);
                newPic.setPhotoCredit(myName);

                // Some random pic naming logic
                newPic.setPicName("JPEG_" + myName + "_" + epochTimestamp);

                mPresenter.saveData(newPic, comment.getText().toString());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mPresenter.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        mPresenter.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void setPresenter(AddPicContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void showLoading() {
        if (progressDialog != null && !progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

    @Override
    public void hideLoading() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.cancel();
        }
    }

    @Override
    public void onAlreadyExists() {
        NotificationToast.showToast(this, getString(R.string.pic_already_exists));
    }

    @Override
    public void onSaveSuccessful() {
        NotificationToast.showToast(this, getString(R.string.image_upload_successful));
    }

    @Override
    public void onColumnEmpty() {
        NotificationToast.showToast(AddPicture.this, getString(R.string.fill_all_columns));
    }

    @Override
    public void resetFields() {
        comment.setText("");
        imgAddPic.setImageResource(R.mipmap.ic_camera_plus);
    }

    @Override
    public void populateFields(long timestamp, String photographer) {
        this.timestamp.setText(dateFormat.format(new Date(timestamp)));
        this.photographer.setText(photographer);
        this.epochTimestamp = timestamp;
    }

    @Override
    public void onImageAvailable(Bitmap image, long timestamp) {
        imgAddPic.setImageBitmap(image);
        if (timestamp > 0) {
            this.timestamp.setText(dateFormat.format(new Date(timestamp)));
            this.epochTimestamp = timestamp;
        }
    }

    @Override
    public void onError() {
        NotificationToast.showToast(AddPicture.this, getString(R.string.error_uploading_pic));
    }
}
