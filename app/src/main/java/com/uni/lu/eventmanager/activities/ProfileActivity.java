package com.uni.lu.eventmanager.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;
import com.uni.lu.eventmanager.R;
import com.uni.lu.eventmanager.controller.FirebaseController;
import com.uni.lu.eventmanager.media.CircleTransform;
import com.uni.lu.eventmanager.model.UserModel;

public class ProfileActivity extends AppCompatActivity {

	private AppBarConfiguration mAppBarConfiguration;
	private UserModel           userModel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);

		userModel = new UserModel();

		Toolbar toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		DrawerLayout   drawer         = findViewById(R.id.drawer_layout);
		NavigationView navigationView = findViewById(R.id.nav_view);
		// Passing each menu ID as a set of Ids because each
		// menu should be considered as top level destinations.
		mAppBarConfiguration = new AppBarConfiguration.Builder(
				R.id.nav_add_event, R.id.nav_profile,
				R.id.nav_events, R.id.sign_out)
				.setDrawerLayout(drawer)
				.build();
		NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
		NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
		NavigationUI.setupWithNavController(navigationView, navController);

		View header = navigationView.getHeaderView(0);
		TextView userEmail = header.findViewById(R.id.userEmail);
		userEmail.setText(userModel.getEmail());
		TextView userName = header.findViewById(R.id.userName);
		userName.setText(userModel.getUserName());

		ImageView ivBasicImage = header.findViewById(R.id.imageView);
		Picasso.get()
				.load(userModel.getPhotoUrl())
				.resize(108, 108)
				.centerCrop()
				.transform(new CircleTransform())
				.into(ivBasicImage);

		navigationView.getMenu().findItem(R.id.sign_out).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				signOut();
				return true;
			}
		});
	}

	@Override
	public boolean onSupportNavigateUp() {
		NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
		return NavigationUI.navigateUp(navController, mAppBarConfiguration)
		       || super.onSupportNavigateUp();
	}

	private void signOut() {
		// Firebase sign out
		FirebaseController.getInstance().getmAuth().signOut();

		// Google sign out
		if (FirebaseController.getInstance().getmGoogleSignInClient() != null) {
			FirebaseController.getInstance().getmGoogleSignInClient().signOut();
		}

		Intent home = new Intent(ProfileActivity.this, LoginActivity.class);
		home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
		home.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		startActivity(home);
		finish();
	}
}
