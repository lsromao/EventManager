package com.uni.lu.eventmanager.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.uni.lu.eventmanager.R;
import com.uni.lu.eventmanager.authentication.EmailLogin;
import com.uni.lu.eventmanager.authentication.GoogleLogin;
import com.uni.lu.eventmanager.controller.FirebaseController;
import com.uni.lu.eventmanager.controller.LoginCodes;


public class LoginActivity extends AppCompatActivity {

	private ImageView    mBtnGoogleLogin;
	private Button mBtnLogin;
	private TextView  mBtnRegister;
	private TextView  email;
	private TextView  password;

	private GoogleLogin  googleLogin;
	private EmailLogin   emailLogin;


	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		FirebaseController.getInstance().setmAuth(FirebaseAuth.getInstance());

		mBtnGoogleLogin = findViewById(R.id.googleLogin);
		mBtnRegister = findViewById(R.id.registerUser);
		mBtnLogin = findViewById(R.id.btnLogin);
		email = findViewById(R.id.email);
		password = findViewById(R.id.password);

		mBtnRegister.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
				overridePendingTransition(R.anim.slide_in_right,R.anim.stay);
			}
		});

		mBtnGoogleLogin.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				googleLogin = new GoogleLogin(LoginActivity.this, getString(R.string.default_web_client_id));
				googleLogin.signInGoogle();
			}
		});
		mBtnLogin.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				emailLogin = new EmailLogin(LoginActivity.this);
				emailLogin.signInEmail();
			}
		});
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		//TODO Add loading bar, once design be ready
		if (requestCode == LoginCodes.RC_SIGN_IN_GOOGLE) {
			googleLogin.loginGoogle(data);
		} else if (requestCode == LoginCodes.RC_SIGN_IN_EMAIL || requestCode == LoginCodes.RC_SIGN_IN_CREATE) {
			emailLogin.login(email.getText().toString(), password.getText().toString(), requestCode);
		}
	}

	@Override
	public void onStart() {
		super.onStart();
		// Check if user is signed in (non-null) and update UI accordingly.
		FirebaseUser currentUser = FirebaseController.getInstance().getmAuth().getCurrentUser();
		if (currentUser != null) {
			goToListPage();
		}

	}

	private void goToListPage() {
		Intent home = new Intent(LoginActivity.this, ProfileActivity.class);
		home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
		startActivity(home);
	}

	public void onClick(View v) {
		startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
		overridePendingTransition(R.anim.slide_in_right,R.anim.stay);
	}
}

