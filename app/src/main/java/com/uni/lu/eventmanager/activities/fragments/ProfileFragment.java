package com.uni.lu.eventmanager.activities.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.uni.lu.eventmanager.R;
import com.uni.lu.eventmanager.controller.FirebaseController;
import com.uni.lu.eventmanager.util.Gallery;

import java.io.IOException;

public class ProfileFragment extends Fragment {


	ImageView profile;
	Uri filePath;
	private Gallery gallery = new Gallery();
	private static final String TAG = "DB Event";
	private static final int GALLERY_REQUEST_CODE = 2;
	private static final int ADDRESS_PICKER_REQUEST = 1020;
	TextView location;
	FirebaseStorage storage;
	StorageReference storageReference;

	public View onCreateView(@NonNull LayoutInflater inflater,
							 ViewGroup container, Bundle savedInstanceState) {


		View root = inflater.inflate(R.layout.fragment_profile, container, false);

		profile = root.findViewById(R.id.pictureProfile);

		profile.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				chooseImage();
			}
		});

//		GlideApp.with(this)
//				.setDefaultRequestOptions(new RequestOptions()
//						.override(450,450)
//						.fitCenter()
//						.circleCrop()
//						.error(R.drawable.ic_user)
//						.placeholder(R.drawable.ic_user))
//				.load(FirebaseController.getInstance().getUserImageUrl())
//				.into(profile);

		TextView name = root.findViewById(R.id.userNameProfile);
		name.setText(FirebaseController.getInstance().getUserName());

		TextView email = root.findViewById(R.id.emailProfile);
		email.setText(FirebaseController.getInstance().getUserEmail());

		return root;
	}

	private void chooseImage() {
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(Intent.createChooser(intent, "Select Picture"), 71);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == 71 && resultCode == Activity.RESULT_OK
				&& data != null && data.getData() != null) {
			filePath = data.getData();
			try {
				Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), filePath);
				profile.setImageBitmap(bitmap);
				//uploadImage();
			} catch (IOException e) {
				e.printStackTrace();
			}
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


	private void pickFromGallery() {
		//Create an Intent with action as ACTION_PICK
		Intent intent = new Intent(Intent.ACTION_PICK);
		// Sets the type as image/*. This ensures only components of type image are selected
		intent.setType("image/*");
		//We pass an extra array with the accepted mime types. This will ensure only components with these MIME types as targeted.
		String[] mimeTypes = {"image/jpeg", "image/png"};
		intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
		// Launching the Intent
		startActivityForResult(intent, GALLERY_REQUEST_CODE);
	}
}