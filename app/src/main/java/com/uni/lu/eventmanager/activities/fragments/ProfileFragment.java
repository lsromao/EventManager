package com.uni.lu.eventmanager.activities.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.squareup.picasso.Picasso;
import com.uni.lu.eventmanager.R;
import com.uni.lu.eventmanager.media.CircleTransform;
import com.uni.lu.eventmanager.model.UserModel;
import com.uni.lu.eventmanager.view.ProfileView;

public class ProfileFragment extends Fragment {

	private ProfileView profileView;
	private UserModel userModel;

	//TODO Add user logic in a single class of view and add a better layout
	public View onCreateView(@NonNull LayoutInflater inflater,
	                         ViewGroup container, Bundle savedInstanceState) {

		View root = inflater.inflate(R.layout.fragment_profile, container, false);
		 userModel = new UserModel();

		ImageView profile =root.findViewById(R.id.profilePicture);
		Picasso.get()
				.load(userModel.getPhotoUrl())
				.resize(450, 450)
				.centerCrop()
				.transform(new CircleTransform())
				.into(profile);

		TextView name = root.findViewById(R.id.profileUserName);
		name.setText(userModel.getUserName());

		TextView mobile = root.findViewById(R.id.profileMobile);
		mobile.setText("3333");

		TextView email = root.findViewById(R.id.profileEmail);
		email.setText(userModel.getEmail());

		TextView address = root.findViewById(R.id.profileAddress);
		address.setText("Uni LU");

		TextView date = root.findViewById(R.id.profileDate);
		date.setText("27/12/98");

		return root;
	}
}