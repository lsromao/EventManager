package com.uni.lu.eventmanager.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.uni.lu.eventmanager.R;
import com.uni.lu.eventmanager.authentication.EmailLogin;
import com.uni.lu.eventmanager.controller.LoginCodes;

public class RegisterActivity extends AppCompatActivity {

	private TextView login;

	private EditText  userName;
	private EditText  email;
	private EditText  mobile;
	private TextView  password;
	private Button    btnRegister;

	private EmailLogin  emailLogin;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);

		login = findViewById(R.id.login);

		userName = findViewById(R.id.editTextName);
		email = findViewById(R.id.editTextEmail);
		mobile = findViewById(R.id.editTextMobile);
		password = findViewById(R.id.editTextPassword);
		btnRegister = findViewById(R.id.cirRegisterButton);

		login.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent register = new Intent(RegisterActivity.this, LoginActivity.class);
				register.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
				startActivity(register);
				overridePendingTransition(R.anim.slide_in_left, android.R.anim.slide_out_right);
			}
		});

		btnRegister.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				emailLogin = new EmailLogin(RegisterActivity.this);
				emailLogin.register(userName.getText().toString());
			}
		});

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == LoginCodes.RC_SIGN_IN_CREATE) {
			emailLogin.login(email.getText().toString(), password.getText().toString(), requestCode);
		}
	}
}
