package com.uni.lu.eventmanager.authentication;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.uni.lu.eventmanager.activities.ProfileActivity;
import com.uni.lu.eventmanager.controller.FirebaseController;
import com.uni.lu.eventmanager.controller.LoginCodes;

public class GoogleLogin {

	private static final String TAG = "Google Login";

	private GoogleSignInOptions gso;
	private Activity            activity;

	public GoogleLogin(Activity activity, String token) {
		this.activity = activity;
		this.gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
				.requestIdToken(token)
				.requestEmail()
				.build();

		FirebaseController.getInstance().setmGoogleSignInClient(GoogleSignIn.getClient(this.activity, gso));
	}

	public void loginGoogle(Intent data) {
		Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
		try {
			// Google Sign In was successful, authenticate with Firebase
			GoogleSignInAccount account = task.getResult(ApiException.class);
			firebaseAuthWithGoogle(account);
		} catch (ApiException e) {
			// Google Sign In failed, update UI appropriately
			Log.w(TAG, "Google sign in failed", e);
			Toast.makeText(activity, "Google sign in failed!", Toast.LENGTH_SHORT).show();
		}
	}

	private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
		Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

		AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
		FirebaseController.getInstance().getmAuth().signInWithCredential(credential)
				.addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
					@Override
					public void onComplete(@NonNull Task<AuthResult> task) {
						if (task.isSuccessful()) {
							Log.d(TAG, "signInWithCredential:success");
							FirebaseController.getInstance().setmAuth(FirebaseAuth.getInstance());
							goToListPage();
						} else {
							Log.w(TAG, "signInWithCredential:failure", task.getException());
							Toast.makeText(activity, "Authentication failed!", Toast.LENGTH_SHORT).show();
						}
					}
				});
	}

	private void goToListPage() {
		Intent goToListPage = new Intent(activity, ProfileActivity.class);
		activity.startActivity(goToListPage);
		activity.finish();
	}

	public void signInGoogle() {
		Intent signInIntent = FirebaseController.getInstance().getmGoogleSignInClient().getSignInIntent();
		activity.startActivityForResult(signInIntent, LoginCodes.RC_SIGN_IN_GOOGLE);
	}

}
