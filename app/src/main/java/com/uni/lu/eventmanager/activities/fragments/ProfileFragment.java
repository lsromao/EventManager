package com.uni.lu.eventmanager.activities.fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.uni.lu.eventmanager.R;
import com.uni.lu.eventmanager.controller.FireStorageController;
import com.uni.lu.eventmanager.controller.FirebaseController;
import com.uni.lu.eventmanager.media.GlideApp;
import com.uni.lu.eventmanager.util.Gallery;

public class ProfileFragment extends Fragment implements View.OnClickListener {


	private Gallery gallery = new Gallery();

	private ImageView   profilePic;
	private ImageView   iconChangePic;
	private ImageView   iconSave;
	private ImageView   iconCalcel;
	private TextView    userName;
	private TextView    userEmail;
	private Button      editProfile;
	private ProgressBar progressBar;

	private FireStorageController storageController;

	public View onCreateView(@NonNull LayoutInflater inflater,
	                         ViewGroup container, Bundle savedInstanceState) {


		View root = inflater.inflate(R.layout.fragment_profile, container, false);

		storageController = new FireStorageController();

		profilePic = root.findViewById(R.id.pictureProfile);

		userName = root.findViewById(R.id.userNameProfile);
		userEmail = root.findViewById(R.id.emailProfile);
		editProfile = root.findViewById(R.id.editProfileButton);
		iconSave = root.findViewById(R.id.profileIconAccept);
		iconCalcel = root.findViewById(R.id.profileIconCancel);
		iconChangePic = root.findViewById(R.id.iconEditProfilePic);
		progressBar = root.findViewById(R.id.progressBarProfile);

		loadProfile();

		editProfile.setOnClickListener(this);
		iconChangePic.setOnClickListener(this);
		iconSave.setOnClickListener(this);
		iconCalcel.setOnClickListener(this);

		editOff();
		return root;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK)
			switch (requestCode) {
				case Gallery.GALLERY_REQUEST_CODE:
					Uri selectedImage = data.getData();
					gallery.setUrlTemp(selectedImage);
					GlideApp.with(this)
							.setDefaultRequestOptions(new RequestOptions()
									.override(450, 450)
									.fitCenter()
									.circleCrop()
									.error(R.drawable.ic_user)
									.placeholder(R.drawable.ic_user))
							.load(selectedImage)
							.into(profilePic);
					break;
			}

	}

	private void editOn() {
		userName.setEnabled(true);
		userName.setFocusableInTouchMode(true);

		iconChangePic.setVisibility(View.VISIBLE);
		iconSave.setVisibility(View.VISIBLE);
		iconCalcel.setVisibility(View.VISIBLE);

	}

	private void editOff() {
		userName.setEnabled(false);
		userName.setFocusableInTouchMode(false);

		userEmail.setEnabled(false);
		userEmail.setFocusableInTouchMode(false);

		iconChangePic.setVisibility(View.GONE);
		iconSave.setVisibility(View.GONE);
		iconCalcel.setVisibility(View.GONE);

	}

	private void loadProfile() {

		String picture = FirebaseController.getInstance().getUserImageUrl();

		userName.setText(FirebaseController.getInstance().getUserName());
		userEmail.setText(FirebaseController.getInstance().getUserEmail());
		if (picture.equals("")) {
			GlideApp.with(this)
					.setDefaultRequestOptions(new RequestOptions()
							.override(450, 450)
							.fitCenter()
							.circleCrop()
							.error(R.drawable.ic_user)
							.placeholder(R.drawable.ic_user))
					.load(R.drawable.ic_user)
					.into(profilePic);
		} else {
			StorageReference gsReference = FirebaseStorage.getInstance().getReferenceFromUrl(picture);
			GlideApp.with(this)
					.setDefaultRequestOptions(new RequestOptions()
							.override(450, 450)
							.fitCenter()
							.circleCrop()
							.error(R.drawable.ic_user)
							.placeholder(R.drawable.ic_user))
					.load(gsReference)
					.into(profilePic);
		}

	}

	private void saveChanges() {

		String uri = "";

		if (gallery.getUrlTemp() != null) {
			uri = storageController.saveProfilePic(gallery);
		}

		if (userName.getText().toString().length() < 1) {
			Toast.makeText(getContext(), "User name cannot be empty!", Toast.LENGTH_SHORT).show();
		} else {
			progressBar.setVisibility(View.VISIBLE);

			UserProfileChangeRequest profileUpdates = null;

			if (!uri.isEmpty()) {
				profileUpdates = new UserProfileChangeRequest.Builder()
						.setPhotoUri(Uri.parse(uri))
						.setDisplayName(userName.getText().toString())
						.build();
			} else {
				profileUpdates = new UserProfileChangeRequest.Builder()
						.setDisplayName(userName.getText().toString())
						.build();
			}

			FirebaseController.getInstance().getmAuth().getCurrentUser().updateProfile(profileUpdates)
					.addOnSuccessListener(new OnSuccessListener<Void>() {
						@Override
						public void onSuccess(Void aVoid) {
							progressBar.setVisibility(View.GONE);
							editOff();
							FirebaseController.getInstance().setProfileChanged(true);
						}
					}).addOnFailureListener(new OnFailureListener() {
						@Override
						public void onFailure(@NonNull Exception e) {
							Toast.makeText(getContext(), "Error!", Toast.LENGTH_SHORT).show();
						}
					});

		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.editProfileButton:
				editOn();
				break;
			case R.id.profileIconCancel:
				loadProfile();
				editOff();
				break;
			case R.id.profileIconAccept:
				saveChanges();
				break;
			case R.id.iconEditProfilePic:
				startActivityForResult(Intent.createChooser(gallery.pickFromGallery(), "Select picture"), Gallery.GALLERY_REQUEST_CODE);
				break;
		}
	}
}