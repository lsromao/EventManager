package com.uni.lu.eventmanager.controller;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.uni.lu.eventmanager.model.EventModel;
import com.uni.lu.eventmanager.util.Gallery;

import java.util.Random;

public class FireStorageController {

	private static final String TAG = "StorageReference Event";

	private StorageReference storageRef;

	public FireStorageController() {
		this.storageRef = FirebaseStorage.getInstance().getReference();
	}

	public EventModel saveCoverPicture(EventModel event, Gallery gallery) {
		StorageReference eventCoverRef = storageRef.child(
				"event/" + event.getTitle().replaceAll("\\s+", "") + event.getStartDate().hashCode());

		event.setUriCover(eventCoverRef.toString());

		eventCoverRef.putFile(gallery.getUrlTemp())
				.addOnFailureListener(new OnFailureListener() {
					@Override
					public void onFailure(@NonNull Exception exception) {
						Log.w(TAG, "saveCoverPicture:failure", exception);

					}
				}).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
			@Override
			public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
				Log.w(TAG, "saveCoverPicture:success");
			}
		});



		return event;
	}

	public String saveProfilePic(Gallery gallery) {
		StorageReference profileCoverRef = storageRef.child(
				"profile_pictures/" + FirebaseController.getInstance().getUserId() + new Random().nextInt(6000));

		//event.setUriCover();

		profileCoverRef.putFile(gallery.getUrlTemp())
				.addOnFailureListener(new OnFailureListener() {
					@Override
					public void onFailure(@NonNull Exception exception) {
						Log.w(TAG, "saveProfilePic:failure", exception);

					}
				}).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
			@Override
			public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
				Log.w(TAG, "saveProfilePic:success");
			}
		});

		return profileCoverRef.toString();
	}
}
