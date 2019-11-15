package com.uni.lu.eventmanager.activities.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.request.RequestOptions;
import com.uni.lu.eventmanager.R;
import com.uni.lu.eventmanager.controller.FirebaseController;
import com.uni.lu.eventmanager.media.GlideApp;
import com.uni.lu.eventmanager.view.ProfileView;

public class ProfileFragment extends Fragment {

	private ProfileView profileView;
	private UserModel userModel;

	public View onCreateView(@NonNull LayoutInflater inflater,
	                         ViewGroup container, Bundle savedInstanceState) {

		View root = inflater.inflate(R.layout.fragment_profile, container, false);

		ImageView profile =root.findViewById(R.id.pictureProfile);
		GlideApp.with(this)
				.setDefaultRequestOptions(new RequestOptions()
						.override(450,450)
						.fitCenter()
						.circleCrop()
						.error(R.drawable.ic_user)
						.placeholder(R.drawable.ic_user))
				.load(FirebaseController.getInstance().getUserImageUrl())
				.into(profile);

		TextView name = root.findViewById(R.id.userNameProfile);
		name.setText(FirebaseController.getInstance().getUserName());

		TextView email = root.findViewById(R.id.emailProfile);
		email.setText(FirebaseController.getInstance().getUserEmail());

		return root;
	}
}