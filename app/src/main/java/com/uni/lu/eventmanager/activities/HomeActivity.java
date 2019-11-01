package com.uni.lu.eventmanager.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.uni.lu.eventmanager.R;
import com.uni.lu.eventmanager.controller.FirebaseController;

public class HomeActivity extends AppCompatActivity {

	private Button mSignOut;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		mSignOut = findViewById(R.id.btnSignOut);

		mSignOut.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				signOut();
			}
		});

	}

	private void signOut() {
		// Firebase sign out
		FirebaseController.getInstance().getmAuth().signOut();

		// Google sign out
		if (FirebaseController.getInstance().getmGoogleSignInClient() != null) {
			FirebaseController.getInstance().getmGoogleSignInClient().signOut();
		}

		Intent home = new Intent(HomeActivity.this, LoginActivity.class);
		startActivity(home);
	}
}
