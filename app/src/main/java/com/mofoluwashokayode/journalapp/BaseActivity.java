package com.mofoluwashokayode.journalapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class BaseActivity extends AppCompatActivity {

    private static String TAG = "BaseActivity";
    // Google Sign In button .
    public com.google.android.gms.common.SignInButton signInButton;
    private GoogleApiClient googleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(BaseActivity.this)
                .enableAutoManage(BaseActivity.this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {


                    }
                }).addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .build();


    }


    public void UserSignOutFunction() {

        Log.d(TAG, "About to sign out");

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        // Sing Out the User.
        Log.d(TAG, "after getting firebase instance");

        //firebaseAuth.signOut();
        FirebaseAuth.getInstance().signOut();

        final FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        Log.d(TAG, "after firebase auth sign out");


        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(
                new ResultCallback() {
                    @Override
                    public void onResult(@NonNull Result result) {


                        // Printing Logout toast message on screen.
                        Toast.makeText(BaseActivity.this, "Signout Successful", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);// New activity
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                        Auth.GoogleSignInApi.signOut(googleApiClient);


                    }

                     /*@Override
                    public void onResult(@NonNull Status status) {

                    // Write down your any code here which you want to execute After Sign Out.

                    // Printing Logout toast message on screen.
                    Toast.makeText(BaseActivity.this, "Signout Successful", Toast.LENGTH_LONG).show();
                         Intent intent = new Intent(getApplicationContext(), MainActivity.class);// New activity
                         intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                         startActivity(intent);
                         finish();
                    }*/
                });


    }
}
