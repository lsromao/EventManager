package com.uni.lu.micseventmanager.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.uni.lu.micseventmanager.R;
import com.uni.lu.micseventmanager.authentication.EmailLogin;
import com.uni.lu.micseventmanager.authentication.GoogleLogin;
import com.uni.lu.micseventmanager.controller.FirebaseController;
import com.uni.lu.micseventmanager.controller.LoginCodes;


public class LoginActivity extends AppCompatActivity {

	private Button    mBtnLogin;
	private TextView  mBtnRegister;
	public ImageView mGoogleLogin;
	private TextView  email;
	private TextView  password;

	private EmailLogin  emailLogin;
	private GoogleLogin googleLogin;

	FirebaseAuth firebaseAuth;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		mBtnRegister = findViewById(R.id.registerUser);
		mBtnLogin = findViewById(R.id.btnLogin);
		mGoogleLogin = findViewById(R.id.googleLogin);
		email = findViewById(R.id.email);
		password = findViewById(R.id.password);

		FirebaseController.getInstance().setmAuth(FirebaseAuth.getInstance());
		firebaseAuth = FirebaseAuth.getInstance();

		mBtnLogin.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				emailLogin = new EmailLogin(LoginActivity.this);
				emailLogin.signInEmail();
			}
		});
		mBtnRegister.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
				overridePendingTransition(R.anim.slide_in_right, R.anim.stay);
			}
		});

		mGoogleLogin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				googleLogin = new GoogleLogin( LoginActivity.this, getString(R.string.default_web_client_id));
				googleLogin.signInGoogle();
			}
		});
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		//TODO Add loading bar, once design be ready
		if (requestCode == LoginCodes.RC_SIGN_IN_EMAIL || requestCode == LoginCodes.RC_SIGN_IN_CREATE) {
			emailLogin.login(email.getText().toString(), password.getText().toString(), null, requestCode);
		}
		if (requestCode == LoginCodes.RC_SIGN_IN_GOOGLE ) {
			googleLogin.loginGoogle(data);
		}

	}

	@Override
	public void onStart() {
		super.onStart();
		FirebaseUser currentUser = FirebaseController.getInstance().getmAuth().getCurrentUser();
		if (currentUser != null) {
			goToListPage();
		}
	}

	private void goToListPage() {
		Intent home = new Intent(LoginActivity.this, ProfileActivity.class);
		home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
		startActivity(home);
		finish();
	}
}