package com.uni.lu.micseventmanager.controller;

import android.net.Uri;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

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

    private boolean isProfileChanged;

    public boolean isProfileChanged() {
        return isProfileChanged;
    }

    public void setProfileChanged(boolean profileChanged) {
        isProfileChanged = profileChanged;
    }

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

    public FirebaseFirestore getFirestoreInstance(){
       return FirebaseFirestore.getInstance();

    }

    public String getUserName(){
       return getmAuth().getCurrentUser().getDisplayName();
    }

    public String getUserImageUrl(){
        Uri uri =  getmAuth().getCurrentUser().getPhotoUrl();

        if (uri != null){
            return uri.toString();
        }

        return "";
    }

    public String getUserEmail(){
        return getmAuth().getCurrentUser().getEmail();
    }

    public String getUserId(){
        return getmAuth().getCurrentUser().getUid();
    }

    public CollectionReference getCommentsCollectionReference(){
        return getFirestoreInstance().collection("comments");
    }

    public CollectionReference getEventsCollectionReference(){
        return getFirestoreInstance().collection("events");
    }

    public CollectionReference getLikesCollectionReference(){
        return getFirestoreInstance().collection("likes");
    }
}
