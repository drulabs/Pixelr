package org.drulabs.pixelr.screens.login;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.TwitterAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import org.drulabs.pixelr.R;
import org.drulabs.pixelr.dto.UserDTO;
import org.drulabs.pixelr.screens.landing.LandingPage;
import org.drulabs.pixelr.utils.Store;
import org.drulabs.pixelr.utils.Utility;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private static final int GOOGLE_SIGN_IN = 9001;
    private static final int CAMERA_CODE = 7001;
    private final String GoogleOAuthClientId = "835832928476-p9cdfigna9johuush2pohb29q2d89nol" +
            ".apps.googleusercontent.com";
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference usersRef = database.getReference("users");
    private FirebaseAuth mAuth;
    private GoogleApiClient mGoogleApiClient;
    private View btnGoogleLogin;
    private TwitterLoginButton btnTwitterLogin;
    private Bundle extras;

    private ProgressBar loginProgress;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            navigateToLandingPage();
        } else {
            boolean writePermissionGranted = Utility.checkPermission(Manifest.permission
                    .WRITE_EXTERNAL_STORAGE, this);
            if (!writePermissionGranted) {
                Utility.requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        CAMERA_CODE, this);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Configure Google Sign In getString(R.string.default_web_client_id)
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(GoogleOAuthClientId)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        mAuth = FirebaseAuth.getInstance();

        btnGoogleLogin = findViewById(R.id.sign_in_button);
        btnGoogleLogin.setOnClickListener(v -> {
            Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
            startActivityForResult(signInIntent, GOOGLE_SIGN_IN);
        });

        btnTwitterLogin = findViewById(R.id.login_button);
        btnTwitterLogin.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                Log.d("Login", "twitterLogin:success" + result);
                handleTwitterSession(result.data);
            }

            @Override
            public void failure(TwitterException exception) {
                Log.w("Login", "twitterLogin:failure", exception);
                processLogin(null);
            }
        });

        extras = getIntent().getExtras();

        loginProgress = findViewById(R.id.login_progress);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        extras = intent.getExtras();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (Manifest.permission.WRITE_EXTERNAL_STORAGE.equalsIgnoreCase(permissions[0]) &&
                grantResults[0] == PackageManager.PERMISSION_DENIED) {
            finish();
        }
    }

    private void handleTwitterSession(TwitterSession session) {

        loginProgress.setVisibility(View.VISIBLE);

        Log.d("Login", "handleTwitterSession:" + session);

        AuthCredential credential = TwitterAuthProvider.getCredential(
                session.getAuthToken().token,
                session.getAuthToken().secret);

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Login", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            processLogin(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("Login", "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            processLogin(null);
                        }

                        // ...
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == GOOGLE_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                // Google Sign In failed, update UI appropriately
                processLogin(null);
            }
        }

        // Pass the activity result to the Twitter login button.
        try {
            btnTwitterLogin.onActivityResult(requestCode, resultCode, data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {

        loginProgress.setVisibility(View.VISIBLE);

        Log.d("Login", "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Login", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            processLogin(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("Login", "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            processLogin(null);
                        }

                        // ...
                    }
                });
    }

    private void processLogin(FirebaseUser user) {
        if (user != null) {
            UserDTO currentUser = UserDTO.from(user);
            usersRef.child(currentUser.getUid()).setValue(currentUser).addOnCompleteListener(t -> {
                // User logged in. Saving data locally
                Store.getInstance(LoginActivity.this).setUser(currentUser);

                //navigate to landing page
                navigateToLandingPage();


                //dismiss progress bar
                loginProgress.setVisibility(View.GONE);

            }).addOnFailureListener(ex -> {
                Log.e("Login", "Error", ex);
                Toast.makeText(LoginActivity.this, getString(R.string.something_went_wrong),
                        Toast.LENGTH_SHORT).show();
                loginProgress.setVisibility(View.GONE);
            });
        }
    }

    private void navigateToLandingPage() {
        Intent landingPageIntent = new Intent(LoginActivity.this, LandingPage.class);
        if (extras != null) {
            landingPageIntent.putExtras(extras);
        }
        startActivity(landingPageIntent);

        // Finish Login page
        if (!isDestroyed()) {
            LoginActivity.this.finish();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d("Login", "onConnectionFailed:" + connectionResult);
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();

    }
}
