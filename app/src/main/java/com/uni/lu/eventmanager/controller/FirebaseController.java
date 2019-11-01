package com.uni.lu.eventmanager.controller;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.FirebaseAuth;

public class FirebaseController {

    private static FirebaseController single_instance = null;

    public static FirebaseController getInstance()
    {
        if (single_instance == null)
            single_instance = new FirebaseController();

        return single_instance;
    }

    private FirebaseAuth mAuth;

    private GoogleSignInClient mGoogleSignInClient;

    public FirebaseAuth getmAuth() {
        return mAuth;
    }

    public void setmAuth(FirebaseAuth mAuth) {
        this.mAuth = mAuth;
    }

    public GoogleSignInClient getmGoogleSignInClient() {
        return mGoogleSignInClient;
    }

    public void setmGoogleSignInClient(GoogleSignInClient mGoogleSignInClient) {
        this.mGoogleSignInClient = mGoogleSignInClient;
    }
}
