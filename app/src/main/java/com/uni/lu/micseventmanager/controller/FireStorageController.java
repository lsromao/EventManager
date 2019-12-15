package com.uni.lu.micseventmanager.controller;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.uni.lu.micseventmanager.model.EventModel;
import com.uni.lu.micseventmanager.util.Gallery;

import java.util.Random;

public class FireStorageController {

	private StorageReference storageRef;

	public FireStorageController() {
		this.storageRef = FirebaseStorage.getInstance().getReference();
	}

	public EventModel saveCoverPicture(EventModel event, Gallery gallery) {
		StorageReference eventCoverRef = storageRef.child(
				"event/" + event.getTitle().replaceAll("\\s+", "") + event.getStartDate().hashCode());

		event.setUriCover(eventCoverRef.toString());

		UploadTask task = eventCoverRef.putFile(gallery.getUrlTemp());

		while (!task.isSuccessful()){

		}

		return event;
	}

	public String saveProfilePic(Gallery gallery) {
		StorageReference profileCoverRef = storageRef.child(
				"profile_pictures/" + FirebaseController.getInstance().getUserId() + new Random().nextInt(6000));

		UploadTask task = profileCoverRef.putFile(gallery.getUrlTemp());

		while(!task.isSuccessful()){


		}

		return profileCoverRef.toString();
	}
}
