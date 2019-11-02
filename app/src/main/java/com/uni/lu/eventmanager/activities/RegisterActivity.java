package com.uni.lu.eventmanager.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.uni.lu.eventmanager.R;

public class RegisterActivity extends AppCompatActivity {

	private TextView login;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);


		login.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
				overridePendingTransition(R.anim.slide_in_left,android.R.anim.slide_out_right);
			}
		});
	}
}
