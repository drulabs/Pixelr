package org.drulabs.pixelr.screens.singlepic;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import org.drulabs.pixelr.BuildConfig;
import org.drulabs.pixelr.R;
import org.drulabs.pixelr.dto.LikeDTO;
import org.drulabs.pixelr.dto.PictureDTO;
import org.drulabs.pixelr.screens.PresenterCreator;
import org.drulabs.pixelr.screens.landing.LandingPage;
import org.drulabs.pixelr.screens.login.LoginActivity;
import org.drulabs.pixelr.ui.NotificationToast;
import org.drulabs.pixelr.utils.Store;

public class SinglePicPage extends AppCompatActivity implements SinglePicContract.View {

    public static final String KEY_PIC_DTO = "pic_dto";
    public static final java.lang.String KEY_PIC_KEY = "pic_key";
    //Firebase
    FirebaseRemoteConfig mRemoteConfig;
    private SinglePicContract.Presenter mPresenter;

    // UI elements
    private RecyclerView recyclerView;
    private View loaderView;
    private SinglePicLikesAdapter singlePicLikesAdapter;
    private ImageView imgSinglePic;
    private LinearLayout parentView;
    private RelativeLayout likeListHolder;

    // local vars
    private String artifactId;
    private int likesCount;
    private String title;
    private boolean isLaunchedByDynamicLink = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.single_pic_layout);

        if (Store.getInstance(this).getMyKey() == null) {
            NotificationToast.showToast(this, "Please sign in before using the app");
            Intent loginPageIntent = new Intent(this, LoginActivity.class);
            startActivity(loginPageIntent);
            finish();
            return;
        }

        initializeUI();

        Bundle extras = getIntent().getExtras();
        if (extras != null && !extras.containsKey(KEY_PIC_KEY)) {
//            NotificationToast.showToast(this, getString(R.string.invalid_args_msg));
//            this.finish();
            PresenterCreator.createLikesPresenter(this, this, null);
            isLaunchedByDynamicLink = true;
            mPresenter.loadDynamicLinkContent(getIntent());
        } else if (extras != null && extras.containsKey(KEY_PIC_KEY)) {
            isLaunchedByDynamicLink = false;
            PictureDTO picture = (PictureDTO) extras.getSerializable(KEY_PIC_DTO);
            String picKey = extras.getString(KEY_PIC_KEY);
            PresenterCreator.createLikesPresenter(this, this, picKey);
            loadDynamicContent(picKey, picture);
        } else {
            finish();
            NotificationToast.showToast(this, getString(R.string.something_went_wrong));
        }

        fetchRemoteConfigData();
    }

    private void fetchRemoteConfigData() {
        mRemoteConfig = FirebaseRemoteConfig.getInstance();
        // Create a Remote Config Setting to enable developer mode, which you can use to increase
        // the number of fetches available per hour during development.
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build();
        mRemoteConfig.setConfigSettings(configSettings);
        mRemoteConfig.setDefaults(R.xml.remote_config_defaults);
        mRemoteConfig.fetch(100).addOnSuccessListener(aVoid -> mRemoteConfig.activateFetched());

        parentView.setBackgroundColor(Color.parseColor(mRemoteConfig.getString("bg_color")));
        setToolBarTitle(mRemoteConfig.getString("like_title"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_single_pic, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.share_dynamic_link:
                mPresenter.shareDynamicLink(artifactId);
                return true;
            case R.id.share_push_notification:
                mPresenter.pushNotifyAll(artifactId);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initializeUI() {

        loaderView = findViewById(R.id.list_holder_progressbar);
        recyclerView = findViewById(R.id.content_holder);
        recyclerView.setBackgroundColor(ContextCompat.getColor(this, R.color.transparent));
        imgSinglePic = findViewById(R.id.img_single_pic);
        parentView = findViewById(R.id.single_pic_holder);
        likeListHolder = findViewById(R.id.activity_landing_page);
        likeListHolder.setBackgroundColor(ContextCompat.getColor(this, R.color.transparent));

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        singlePicLikesAdapter = new SinglePicLikesAdapter(SinglePicPage.this);
        recyclerView.setAdapter(singlePicLikesAdapter);

        loaderView.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }

    @Override
    protected void onDestroy() {
        if (mPresenter != null) {
            mPresenter.destroy();
            mPresenter = null;
        }
        super.onDestroy();
    }

    @Override
    public void setPresenter(SinglePicContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void showLoading() {
        if (loaderView != null) {
            loaderView.setVisibility(View.VISIBLE);
            recyclerView.setAlpha(0.3f);
        }
    }

    @Override
    public void hideLoading() {
        if (loaderView != null) {
            recyclerView.setAlpha(1.0f);
            loaderView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onLikeFetched(LikeDTO like) {
        singlePicLikesAdapter.append(like);
        if (recyclerView.getVisibility() != View.VISIBLE) {
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onNoMoreLikes() {
        NotificationToast.showToast(this, getString(R.string.no_likes_found));
    }

    @Override
    public void loadDynamicContent(String picKey, PictureDTO picture) {
        artifactId = picKey;
        title = picture.getPicName();
        likesCount = picture.getLikesCount();

        setToolBarTitle(likesCount + " likes for " + title);

        if (isLaunchedByDynamicLink) {
            PresenterCreator.createLikesPresenter(this, this, picKey);
        }

        mPresenter.start();
        mPresenter.loadPic(picture, imgSinglePic);
    }

    @Override
    public void onBackPressed() {
        if (!isLaunchedByDynamicLink) {
            super.onBackPressed();
        } else {
            Intent landingPageIntent = new Intent(this, LandingPage.class);
            startActivity(landingPageIntent);
            super.onBackPressed();
        }
    }

    void setToolBarTitle(String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }
}
