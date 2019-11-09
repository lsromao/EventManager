package com.uni.lu.eventmanager.authentication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.uni.lu.eventmanager.R;
import com.uni.lu.eventmanager.activities.ProfileActivity;
import com.uni.lu.eventmanager.controller.FirebaseController;
import com.uni.lu.eventmanager.controller.LoginCodes;

import java.util.regex.Pattern;

public class EmailLogin {

	private static final String TAG = "Email Login";

	private Activity activity;
	private String userName;

	public EmailLogin(Activity activity) {
		this.activity = activity;
	}

	public void signInEmail() {
		Intent signInEmail = new Intent(activity.getIntent());
		activity.startActivityForResult(signInEmail, LoginCodes.RC_SIGN_IN_EMAIL);
	}

	public void register(String s) {
		Intent signInEmail = new Intent(activity.getIntent());
		activity.startActivityForResult(signInEmail, LoginCodes.RC_SIGN_IN_CREATE);
	}

	public void login(String email, String password, int code) {
		if (code == LoginCodes.RC_SIGN_IN_EMAIL) {
			loginWithEmail(email, password);
		} else if (code == LoginCodes.RC_SIGN_IN_CREATE) {
			createUser(email, password);
		}

	}


	private void loginWithEmail(String email, String password) {
		if (validatePassword(password)){
			if (validateEmail(email)) {
				FirebaseController.getInstance().getmAuth().signInWithEmailAndPassword(email, password)
						.addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
							@Override
							public void onComplete(@NonNull Task<AuthResult> task) {
								if (task.isSuccessful()) {
									// Sign in success, update UI with the signed-in user's information
									Log.d(TAG, "signInWithEmail:success");
									FirebaseController.getInstance().setmAuth(FirebaseAuth.getInstance());
									goToListPage();
								} else {
									// If sign in fails, display a message to the user.
									Log.w(TAG, "signInWithEmail:failure", task.getException());
									Toast.makeText(activity, "Authentication failed.",
											Toast.LENGTH_SHORT).show();
								}
							}
						});

			} else {
				Toast.makeText(activity.getApplicationContext(), "Invalid email address", Toast.LENGTH_SHORT).show();
			}
		}else {
			Toast.makeText(activity.getApplicationContext(), "Password need to have 6 characters", Toast.LENGTH_SHORT).show();
		}
	}


	private void createUser(String email, String password) {
		if (validatePassword(password)){
			if (validateEmail(email)) {
				FirebaseController.getInstance().getmAuth().createUserWithEmailAndPassword(email, password)
						.addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
							@Override
							public void onComplete(@NonNull Task<AuthResult> task) {
								if (task.isSuccessful()) {
									// If Sign is success
									Log.d(TAG, "signInWithEmail:success");
									FirebaseUser user = FirebaseController.getInstance().getmAuth().getCurrentUser();
									UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
											.setDisplayName(userName).build();

									user.updateProfile(profileUpdates)
											.addOnCompleteListener(new OnCompleteListener<Void>() {
												@Override
												public void onComplete(@NonNull Task<Void> task) {
													if (task.isSuccessful()) {
														Log.d(TAG, "User profile updated");
													}
												}
											});

//									FirebaseController.getInstance().setmAuth(FirebaseAuth.getInstance());
									goToListPage();
								} else {
									// If sign in fails, display a message to the user.
									Log.w(TAG, "signInWithEmail:failure", task.getException());
									Toast.makeText(activity, "Authentication failed.",
											Toast.LENGTH_SHORT).show();
								}
							}
						});
			} else {
				Toast.makeText(activity.getApplicationContext(), "Invalid email address", Toast.LENGTH_SHORT).show();
			}
		}else {
			Toast.makeText(activity.getApplicationContext(), "Password need to have 6 characters", Toast.LENGTH_SHORT).show();
		}
	}

	private boolean validateEmail(String email) {
		Pattern pattern = Patterns.EMAIL_ADDRESS;
		return pattern.matcher(email).matches();
	}

	private boolean validatePassword(String pass) {
		return pass.length() >= 6;
	}

	private void goToListPage() {
		Intent goToListPage = new Intent(activity, ProfileActivity.class);
		goToListPage.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
		activity.startActivity(goToListPage);
		activity.finish();
	}
}
