package org.drulabs.pixelr.screens.landing;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;

import org.drulabs.pixelr.R;
import org.drulabs.pixelr.screens.add.AddPicture;
import org.drulabs.pixelr.screens.login.LoginActivity;
import org.drulabs.pixelr.ui.NotificationToast;
import org.drulabs.pixelr.utils.Store;
import org.drulabs.pixelr.utils.Utility;

public class LandingPage extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private static final long SWIPE_REFRESH_DELAY_MILLIS = 2500;
    private static final String TAG_PICS = "pics";
    PicsFragment picsFragment;

    private SwipeRefreshLayout swipeRefreshLayout;
    private Bundle savedInstanceState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);

        this.savedInstanceState = savedInstanceState;

        Bundle extras = getIntent().getExtras();

        swipeRefreshLayout = findViewById(R.id.pics_swipe_layout);
        swipeRefreshLayout.setOnRefreshListener(this);

        picsFragment = (PicsFragment) getSupportFragmentManager().findFragmentByTag(TAG_PICS);
        if (picsFragment == null) {
            picsFragment = PicsFragment.newInstance("Pics", "none", extras);
            picsFragment.setRetainInstance(true);
        }

        loadFragment(picsFragment, TAG_PICS);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    private void loadFragment(Fragment fragment, String tag) {
        if (findViewById(R.id.fragment_holder) != null) {
            // replace the 'fragment_container' FrameLayout with the fragment
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_holder,
                    fragment, tag).commit();
        }
    }

    private void removeFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().remove(fragment).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_landing, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.only_videos:
            case R.id.only_audio:
            case R.id.only_picture:
                NotificationToast.showToast(this, getString(R.string.feature_coming_soon));
                return true;
            case R.id.bug_report_menu:

                try {
                    PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                    String version = pInfo.versionName;

                    Utility.composeEmail(LandingPage.this, new String[]{"kaushal.devil009@gmail" +
                            ".com"}, "Vivid Vidhi (v" + version + ") Bug report from "
                            + Store.getInstance(this).getMyName() + "(" + Store.getInstance(this)
                            .getMyKey() + ")");
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
                return true;
            case R.id.menu_logout:
                //Logout the user and navigate to Login screen
                FirebaseAuth.getInstance().signOut();
                Intent loginPageIntent = new Intent(LandingPage.this, LoginActivity.class);
                startActivity(loginPageIntent);
                LandingPage.this.finish();
                return true;
            case R.id.add_stuff:
                navigateToAddPicActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void navigateToAddPicActivity() {
        Intent addPicIntent = new Intent(this, AddPicture.class);
        startActivity(addPicIntent);
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(false);
            }
        }, SWIPE_REFRESH_DELAY_MILLIS);

        if (picsFragment != null && !isDestroyed()) {
            picsFragment.reset();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
