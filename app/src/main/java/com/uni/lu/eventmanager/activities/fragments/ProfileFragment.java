package com.uni.lu.eventmanager.activities.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.uni.lu.eventmanager.R;
import com.uni.lu.eventmanager.controller.FirebaseController;
import com.uni.lu.eventmanager.media.GlideApp;
import com.uni.lu.eventmanager.util.Gallery;

public class ProfileFragment extends Fragment implements View.OnClickListener  {


	private Gallery gallery = new Gallery();

	private ImageView profilePic;
	private ImageView iconChangePic;
	private ImageView iconSave;
	private ImageView iconCalcel;
	private TextView userName;
	private TextView userEmail;
	private Button editProfile;

	public View onCreateView(@NonNull LayoutInflater inflater,
	                         ViewGroup container, Bundle savedInstanceState) {


		View root = inflater.inflate(R.layout.fragment_profile, container, false);

		profilePic = root.findViewById(R.id.pictureProfile);

		userName = root.findViewById(R.id.userNameProfile);
		userEmail = root.findViewById(R.id.emailProfile);
		editProfile = root.findViewById(R.id.editProfileButton);
		iconSave = root.findViewById(R.id.profileIconAccept);
		iconCalcel = root.findViewById(R.id.profileIconCancel);
		iconChangePic = root.findViewById(R.id.iconEditProfilePic);

		loadProfile();

		editProfile.setOnClickListener(this);
		iconChangePic.setOnClickListener(this);
		iconSave.setOnClickListener(this);
		iconCalcel.setOnClickListener(this);


		return root;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {


	}

//		//Image to FireStore
//		StorageReference storageRef    = FirebaseStorage.getInstance().getReference();
//		StorageReference userCoverRef = storageRef.child("profile_pictures/" + title.replaceAll("\\s+", "")
//				+ startTime.hashCode());
//		bar.setVisibility(View.VISIBLE);
//		userCoverRef.putFile(gallery.getUrlTemp())
//				.addOnFailureListener(new OnFailureListener() {
//					@Override
//					public void onFailure(@NonNull Exception exception) {
//						Log.w(TAG, "profileImage:failure", exception);
//
//					}
//				}).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//			@Override
//			public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//				String tent = taskSnapshot.getMetadata().getReference().toString();
//			}
//		});

	private void editOn(){
		userName.setEnabled(true);
		userName.setFocusableInTouchMode(true);

		userEmail.setEnabled(true);
		userEmail.setFocusableInTouchMode(true);

		iconChangePic.setVisibility(View.VISIBLE);
		iconSave.setVisibility(View.VISIBLE);
		iconCalcel.setVisibility(View.VISIBLE);

	}

	private void editOff(){
		userName.setEnabled(false);
		userName.setFocusableInTouchMode(false);

		userEmail.setEnabled(false);
		userEmail.setFocusableInTouchMode(false);

		iconChangePic.setVisibility(View.GONE);
		iconSave.setVisibility(View.GONE);
		iconCalcel.setVisibility(View.GONE);

	}

	private void loadProfile(){

		String picture = FirebaseController.getInstance().getUserImageUrl();

		userName.setText(FirebaseController.getInstance().getUserName());
		userEmail.setText(FirebaseController.getInstance().getUserEmail());
		if (picture.equals("")){
			GlideApp.with(this)
					.setDefaultRequestOptions(new RequestOptions()
							.override(450,450)
							.fitCenter()
							.circleCrop()
							.error(R.drawable.ic_user)
							.placeholder(R.drawable.ic_user))
					.load(R.drawable.ic_user)
					.into(profilePic);
		}else{
			StorageReference gsReference = FirebaseStorage.getInstance().getReferenceFromUrl(picture);
			GlideApp.with(this)
					.setDefaultRequestOptions(new RequestOptions()
							.override(450,450)
							.fitCenter()
							.circleCrop()
							.error(R.drawable.ic_user)
							.placeholder(R.drawable.ic_user))
					.load(gsReference)
					.into(profilePic);
		}

	}

	/*private void uploadImage() {

		if(filePath != null)
		{

			final FirebaseDatabase database = FirebaseDatabase.getInstance();
			final DatabaseReference ref = database.getReference("users/"+ FirebaseController.getInstance().getUserId());

			storage = FirebaseStorage.getInstance("gs://misc-eventmanager.appspot.com");
			storageReference = storage.getReference();

			final ProgressDialog progressDialog = new ProgressDialog(getContext());
			progressDialog.setTitle("Uploading...");
			progressDialog.show();

			final StorageReference sRef = storageReference.child("profile_pictures/"+ FirebaseController.getInstance().getUserId());
			sRef.putFile(filePath)
					.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
						@Override
						public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
							sRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
								@Override
								public void onSuccess(Uri uri) {

//									Toast.makeText(getContext(), "Profile picture is uploaded.",
//															Toast.LENGTH_SHORT).show();

								//	FirebaseController.getInstance()
									ref.child("profile_picture").setValue(uri.toString())
											.addOnSuccessListener(new OnSuccessListener<Void>() {
												@Override
												public void onSuccess(Void aVoid) {
													Toast.makeText(getContext(), "Profile picture is uploaded.",
															Toast.LENGTH_SHORT).show();


												}
											})
											.addOnFailureListener(new OnFailureListener() {
												@Override
												public void onFailure(@NonNull Exception e) {
													Toast.makeText(getContext(), "Error: " + e.getMessage(),
															Toast.LENGTH_SHORT).show();
												}
											});
								}
							});

							progressDialog.dismiss();
						}
					})
					.addOnFailureListener(new OnFailureListener() {
						@Override
						public void onFailure(@NonNull Exception e) {
							progressDialog.dismiss();
							Toast.makeText(getContext(), "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
						}
					})
					.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
						@Override
						public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
							double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
									.getTotalByteCount());
							progressDialog.setMessage("Uploaded "+(int)progress+"%");
						}
					});
		}
	}*/


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
				break;
			case R.id.iconEditProfilePic:
				startActivityForResult(Intent.createChooser(gallery.pickFromGallery(), "Select picture"), Gallery.GALLERY_REQUEST_CODE);
				break;
		}
	}
}