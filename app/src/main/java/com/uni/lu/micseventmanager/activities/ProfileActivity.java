package com.uni.lu.micseventmanager.activities;

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

import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.uni.lu.micseventmanager.R;
import com.uni.lu.micseventmanager.controller.FirebaseController;
import com.uni.lu.micseventmanager.media.GlideApp;

public class ProfileActivity extends AppCompatActivity {

	private AppBarConfiguration mAppBarConfiguration;

	View header;
	ImageView ivBasicImage;
	TextView userEmail;
	TextView userName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);

		Toolbar toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		DrawerLayout   drawer         = findViewById(R.id.drawer_layout);
		NavigationView navigationView = findViewById(R.id.nav_view);
		mAppBarConfiguration = new AppBarConfiguration.Builder(
				R.id.nav_add_event, R.id.nav_profile,
				R.id.nav_events, R.id.sign_out)
				.setDrawerLayout(drawer)
				.build();
		NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
		NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
		NavigationUI.setupWithNavController(navigationView, navController);

		header = navigationView.getHeaderView(0);

		//TODO: Abstract to a method to populate menu profile
		userEmail = header.findViewById(R.id.userEmail);
		userEmail.setText(FirebaseController.getInstance().getmAuth().getCurrentUser().getEmail());
		userName = header.findViewById(R.id.userName);
		userName.setText(FirebaseController.getInstance().getmAuth().getCurrentUser().getDisplayName());

		ivBasicImage = header.findViewById(R.id.imageView);
		setProfilePic();

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

		if(FirebaseController.getInstance().isProfileChanged()){
			userName.setText(FirebaseController.getInstance().getmAuth().getCurrentUser().getDisplayName());
			setProfilePic();
		}

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

	private void setProfilePic()
	{
		String pic = FirebaseController.getInstance().getUserImageUrl();

		if(pic.isEmpty()){
			GlideApp.with(this)
					.setDefaultRequestOptions(new RequestOptions()
							.fitCenter()
							.circleCrop()
							.error(R.drawable.ic_user)
							.placeholder(R.drawable.ic_user))
					.load(R.drawable.ic_user)
					.into(ivBasicImage);
		}else{
			if (pic.contains("gs://eventmanager-misc.appspot.com")){
				StorageReference gsReference = FirebaseStorage.getInstance().getReferenceFromUrl(pic);
				GlideApp.with(this)
						.setDefaultRequestOptions(new RequestOptions()
								.fitCenter()
								.circleCrop()
								.error(R.drawable.ic_user)
								.placeholder(R.drawable.ic_user))
						.load(gsReference)
						.into(ivBasicImage);
			}else{
				GlideApp.with(this)
						.setDefaultRequestOptions(new RequestOptions()
								.fitCenter()
								.circleCrop()
								.error(R.drawable.ic_user)
								.placeholder(R.drawable.ic_user))
						.load(pic)
						.into(ivBasicImage);
			}

		}
	}
}
